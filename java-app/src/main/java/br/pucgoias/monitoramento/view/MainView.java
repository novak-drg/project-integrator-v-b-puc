package br.pucgoias.monitoramento.view;

import br.pucgoias.monitoramento.model.SensorData;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interface gráfica principal — tema escuro premium com cards de sensores.
 *
 * <p>Exibe em tempo real: Temperatura, Umidade e Luminosidade,
 * além de controles de conexão serial e indicador de status.
 */
public class MainView extends JFrame {

    // ── Paleta ────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(10, 10, 22);
    private static final Color BG_PANEL  = new Color(17, 18, 35);
    private static final Color BG_CARD   = new Color(22, 24, 45);
    private static final Color C_TEMP    = new Color(255, 100, 140);
    private static final Color C_HUMID   = new Color(50,  205, 255);
    private static final Color C_LUX     = new Color(255, 195,  50);
    private static final Color C_OK      = new Color(72,  220, 128);
    private static final Color C_ERR     = new Color(255,  90,  90);
    private static final Color C_GRAY    = new Color(120, 125, 165);
    private static final Color C_DIVIDE  = new Color(55,  58, 100);

    // ── Fontes ────────────────────────────────────────────────────────────
    private static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  18);
    private static final Font F_SUB     = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font F_CLABEL  = new Font("Segoe UI", Font.BOLD,  10);
    private static final Font F_VALUE   = new Font("Segoe UI", Font.BOLD,  54);
    private static final Font F_UNIT    = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font F_DESC    = new Font("Segoe UI", Font.ITALIC,10);
    private static final Font F_STATUS  = new Font("Segoe UI", Font.BOLD,  11);
    private static final Font F_SMALL   = new Font("Segoe UI", Font.PLAIN, 10);

    // ── Widgets de valor ─────────────────────────────────────────────────
    private final JLabel lblTempVal  = makeValueLabel("--", C_TEMP);
    private final JLabel lblHumidVal = makeValueLabel("--", C_HUMID);
    private final JLabel lblLuxVal   = makeValueLabel("--", C_LUX);
    private final JLabel lblTempDesc = makeDescLabel("Aguardando...");
    private final JLabel lblHumidDesc= makeDescLabel("Aguardando...");
    private final JLabel lblLuxDesc  = makeDescLabel("Aguardando...");
    private final JProgressBar pbTemp  = makeBar(C_TEMP,  0,  50);
    private final JProgressBar pbHumid = makeBar(C_HUMID, 0, 100);
    private final JProgressBar pbLux   = makeBar(C_LUX,   0, 1023);

    // ── Status / rodapé ──────────────────────────────────────────────────
    private final JLabel lblStatus    = new JLabel("● Desconectado");
    private final JLabel lblLastRead  = new JLabel("Última leitura: —");

    // ── Controles ────────────────────────────────────────────────────────
    private final JComboBox<String> cmbPortas  = new JComboBox<>();
    private final JButton           btnConectar = new JButton("Conectar");
    private final JButton           btnAtualizar = new JButton("↻");

    // ── Callbacks ─────────────────────────────────────────────────────────
    private Consumer<String> onConnect;
    private Consumer<String> onDisconnect;
    private Runnable         onRefreshPorts;

    public MainView() {
        super("Monitoramento Ambiental — PI V-B | PUC Goiás");
        setupLaf();
        setupWindow();
        buildUI();
    }

    private void setupLaf() {
        try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ignored) {}
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(840, 560));
        setPreferredSize(new Dimension(1000, 640));
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (onDisconnect != null) onDisconnect.accept("");
                dispose();
                System.exit(0);
            }
        });
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCards(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    // ── Cabeçalho ─────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout(14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_PANEL, getWidth(), 0, new Color(28, 15, 55)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_DIVIDE);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel ico = new JLabel("🏠");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel title = new JLabel("Monitoramento de Ambientes Inteligentes");
        title.setFont(F_TITLE);
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Projeto Integrador V-B  ·  PUC Goiás  ·  ADS 2026");
        sub.setFont(F_SUB);
        sub.setForeground(C_GRAY);

        texts.add(title);
        texts.add(Box.createVerticalStrut(3));
        texts.add(sub);

        JPanel left = new JPanel(new BorderLayout(12, 0));
        left.setOpaque(false);
        left.add(ico, BorderLayout.WEST);
        left.add(texts, BorderLayout.CENTER);

        lblStatus.setFont(F_STATUS);
        lblStatus.setForeground(C_ERR);

        p.add(left, BorderLayout.WEST);
        p.add(lblStatus, BorderLayout.EAST);
        return p;
    }

    // ── Cards ─────────────────────────────────────────────────────────────
    private JPanel buildCards() {
        JPanel p = new JPanel(new GridLayout(1, 3, 16, 0));
        p.setOpaque(true);
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 20, 12, 20));
        p.add(buildCard("🌡", "TEMPERATURA", "°C",  lblTempVal,  lblTempDesc,  pbTemp,  C_TEMP));
        p.add(buildCard("💧", "UMIDADE",     "%",   lblHumidVal, lblHumidDesc, pbHumid, C_HUMID));
        p.add(buildCard("☀", "LUMINOSIDADE","lux",  lblLuxVal,   lblLuxDesc,   pbLux,   C_LUX));
        return p;
    }

    private JPanel buildCard(String emoji, String title, String unit,
                              JLabel valLbl, JLabel descLbl, JProgressBar pb, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), new Color(13, 14, 28)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), 5, 5, 5);
                g2.fillRect(0, 3, getWidth(), 2);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 55));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 18, 18);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(24, 22, 20, 22));

        JLabel emojiLbl = new JLabel(emoji, SwingConstants.CENTER);
        emojiLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        emojiLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(F_CLABEL);
        titleLbl.setForeground(accent);
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(0, 1));

        valLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel unitLbl = new JLabel(unit, SwingConstants.CENTER);
        unitLbl.setFont(F_UNIT);
        unitLbl.setForeground(C_GRAY);
        unitLbl.setAlignmentX(CENTER_ALIGNMENT);

        pb.setAlignmentX(CENTER_ALIGNMENT);
        pb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        pb.setPreferredSize(new Dimension(0, 6));

        descLbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(emojiLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(sep);
        card.add(Box.createVerticalGlue());
        card.add(valLbl);
        card.add(unitLbl);
        card.add(Box.createVerticalGlue());
        card.add(pb);
        card.add(Box.createVerticalStrut(7));
        card.add(descLbl);
        return card;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout(8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BG_PANEL);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_DIVIDE);
                g2.fillRect(0, 0, getWidth(), 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 20, 13, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel lblPorta = new JLabel("Porta:");
        lblPorta.setForeground(C_GRAY);
        lblPorta.setFont(F_SMALL);

        cmbPortas.setPreferredSize(new Dimension(130, 30));
        cmbPortas.setFont(F_SMALL);

        btnAtualizar.setPreferredSize(new Dimension(36, 30));
        btnAtualizar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAtualizar.setToolTipText("Atualizar lista de portas");

        btnConectar.setPreferredSize(new Dimension(108, 30));
        btnConectar.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnConectar.addActionListener(e -> onConnectClicked());
        btnAtualizar.addActionListener(e -> { if (onRefreshPorts != null) onRefreshPorts.run(); });

        left.add(lblPorta);
        left.add(cmbPortas);
        left.add(btnAtualizar);
        left.add(btnConectar);

        lblLastRead.setFont(F_SMALL);
        lblLastRead.setForeground(C_GRAY);

        p.add(left, BorderLayout.WEST);
        p.add(lblLastRead, BorderLayout.EAST);
        return p;
    }

    // ── Ações ─────────────────────────────────────────────────────────────
    private void onConnectClicked() {
        String porta = (String) cmbPortas.getSelectedItem();
        if (porta == null) return;
        if ("Conectar".equals(btnConectar.getText())) {
            if (onConnect != null) onConnect.accept(porta);
        } else {
            if (onDisconnect != null) onDisconnect.accept(porta);
        }
    }

    // ── API pública ───────────────────────────────────────────────────────

    /**
     * Atualiza os cards com a leitura mais recente. Deve ser chamado na EDT.
     *
     * @param data dados do sensor
     */
    public void updateData(SensorData data) {
        lblTempVal.setText(String.valueOf(data.getTemperature()));
        lblHumidVal.setText(String.valueOf(data.getHumidity()));
        lblLuxVal.setText(String.valueOf(data.getLuminosity()));

        lblTempDesc.setText(descTemp(data.getTemperature()));
        lblHumidDesc.setText(descHumid(data.getHumidity()));
        lblLuxDesc.setText(descLux(data.getLuminosity()));

        pbTemp.setValue(Math.min(data.getTemperature(), 50));
        pbHumid.setValue(data.getHumidity());
        pbLux.setValue(data.getLuminosity());

        lblLastRead.setText("Última leitura: " +
                new java.text.SimpleDateFormat("HH:mm:ss")
                        .format(new java.util.Date(data.getTimestamp())));
    }

    /**
     * Atualiza o indicador de status de conexão. Deve ser chamado na EDT.
     *
     * @param msg mensagem de status do {@link br.pucgoias.monitoramento.service.SerialService}
     */
    public void setStatus(String msg) {
        boolean ok = msg.startsWith("CONNECTED");
        String display;
        if (msg.startsWith("CONNECTED")) {
            String porta = msg.contains(": ") ? msg.substring(msg.indexOf(": ") + 2) : "";
            display = "● Conectado" + (porta.isEmpty() ? "" : ": " + porta);
        } else if (msg.startsWith("DISCONNECTED")) {
            display = msg.contains("lost") ? "● Conexão perdida" : "● Desconectado";
        } else if (msg.startsWith("ERROR")) {
            display = "● Erro: " + msg.substring(msg.indexOf(":") + 2);
        } else {
            display = "● " + msg;
        }
        lblStatus.setText(display);
        lblStatus.setForeground(ok ? C_OK : C_ERR);
        btnConectar.setText(ok ? "Desconectar" : "Conectar");
        cmbPortas.setEnabled(!ok);
        btnAtualizar.setEnabled(!ok);
    }

    /**
     * Preenche o combo de portas. Deve ser chamado na EDT.
     *
     * @param ports portas disponíveis
     */
    public void setPorts(List<String> ports) {
        cmbPortas.removeAllItems();
        if (ports.isEmpty()) {
            cmbPortas.addItem("(Nenhuma porta)");
        } else {
            ports.forEach(cmbPortas::addItem);
        }
    }

    /**
     * @param cb callback acionado com a porta ao clicar "Conectar"
     */
    public void setOnConnect(Consumer<String> cb)    { this.onConnect = cb; }

    /**
     * @param cb callback acionado com a porta ao clicar "Desconectar"
     */
    public void setOnDisconnect(Consumer<String> cb) { this.onDisconnect = cb; }

    /**
     * @param cb callback acionado ao clicar no botão de atualizar portas
     */
    public void setOnRefreshPorts(Runnable cb)       { this.onRefreshPorts = cb; }

    // ── Descrições PT-BR dos sensores ─────────────────────────────────────
    private String descTemp(int t) {
        if (t < 15) return "❄ Muito frio";
        if (t < 22) return "🌤 Frio";
        if (t < 28) return "✅ Confortável";
        if (t < 35) return "☀ Quente";
        return "🔥 Muito quente";
    }

    private String descHumid(int h) {
        if (h < 20) return "💨 Muito seco";
        if (h < 40) return "🌬 Seco";
        if (h < 70) return "✅ Ideal";
        return "💦 Úmido";
    }

    private String descLux(int l) {
        if (l < 200) return "🌑 Muito escuro";
        if (l < 500) return "🌒 Meia-luz";
        if (l < 800) return "🌤 Claro";
        return "☀ Muito claro";
    }

    // ── Fábricas de widgets ───────────────────────────────────────────────
    private JLabel makeValueLabel(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(F_VALUE);
        l.setForeground(color);
        return l;
    }

    private JLabel makeDescLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(F_DESC);
        l.setForeground(C_GRAY);
        return l;
    }

    private JProgressBar makeBar(Color color, int min, int max) {
        JProgressBar pb = new JProgressBar(min, max);
        pb.setValue(0);
        pb.setStringPainted(false);
        pb.setForeground(color);
        pb.setBackground(new Color(38, 40, 65));
        pb.setBorderPainted(false);
        return pb;
    }
}
