package br.pucgoias.monitoramento.view;

import br.pucgoias.monitoramento.model.SensorData;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interface gráfica principal — tema VSCode Dark com cards de sensores.
 *
 * <p>Exibe em tempo real: Temperatura, Umidade e Luminosidade,
 * além de controles de conexão serial e indicador de status.
 */
public class MainView extends JFrame {

    // ── Paleta VSCode Dark ────────────────────────────────────────────────
    private static final Color BG         = new Color(30, 30, 30);
    private static final Color BG_SIDEBAR  = new Color(37, 37, 38);
    private static final Color BG_CARD     = new Color(45, 45, 48);
    private static final Color BG_CONTROL  = new Color(58, 58, 62);
    private static final Color BG_CTRL_HOV = new Color(75, 75, 80);
    private static final Color C_TEMP      = new Color(255, 100, 140);
    private static final Color C_HUMID     = new Color(50,  205, 255);
    private static final Color C_LUX       = new Color(255, 195,  50);
    private static final Color C_OK        = new Color(72,  200, 120);
    private static final Color C_ERR       = new Color(240,  80,  80);
    private static final Color C_TEXT      = new Color(204, 204, 204);
    private static final Color C_GRAY      = new Color(130, 130, 145);
    private static final Color C_LINE      = new Color(60,  60,  65);

    // ── Fontes ────────────────────────────────────────────────────────────
    private static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD,  18);
    private static final Font F_SUB    = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font F_CLABEL = new Font("Segoe UI", Font.BOLD,  10);
    private static final Font F_VALUE  = new Font("Segoe UI", Font.BOLD,  50);
    private static final Font F_UNIT   = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font F_DESC   = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font F_STATUS = new Font("Segoe UI", Font.BOLD,  11);
    private static final Font F_CTRL   = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Widgets ───────────────────────────────────────────────────────────
    private final JLabel lblTempVal   = makeValueLabel("—", C_TEMP);
    private final JLabel lblHumidVal  = makeValueLabel("—", C_HUMID);
    private final JLabel lblLuxVal    = makeValueLabel("—", C_LUX);
    private final JLabel lblTempDesc  = makeDescLabel("Aguardando...");
    private final JLabel lblHumidDesc = makeDescLabel("Aguardando...");
    private final JLabel lblLuxDesc   = makeDescLabel("Aguardando...");
    private final JProgressBar pbTemp  = makeBar(C_TEMP,  0,  50);
    private final JProgressBar pbHumid = makeBar(C_HUMID, 0, 100);
    private final JProgressBar pbLux   = makeBar(C_LUX,   0, 1023);

    private final JLabel lblStatus   = new JLabel("● Desconectado");
    private final JLabel lblLastRead = new JLabel("Última leitura: —");

    private final JComboBox<String> cmbPortas   = new JComboBox<>();
    private final JButton           btnConectar  = new JButton("Conectar");
    private final JButton           btnAtualizar = new JButton("↻");

    private Consumer<String> onConnect;
    private Consumer<String> onDisconnect;
    private Runnable         onRefreshPorts;

    // ── Construtor ────────────────────────────────────────────────────────
    public MainView() {
        super("Monitoramento Ambiental — PI V-B | PUC Goiás");
        setupLaf();
        setupWindow();
        buildUI();
    }

    private void setupLaf() {
        try {
            FlatDarkLaf.setup();
            UIManager.put("Button.arc", 6);
            UIManager.put("Component.arc", 6);
            UIManager.put("Button.background",        BG_CONTROL);
            UIManager.put("Button.foreground",        C_TEXT);
            UIManager.put("Button.hoverBackground",   BG_CTRL_HOV);
            UIManager.put("Button.pressedBackground", new Color(45, 45, 50));
            UIManager.put("ComboBox.background",      BG_CONTROL);
            UIManager.put("ComboBox.foreground",      C_TEXT);
            UIManager.put("ComboBox.buttonBackground",BG_CTRL_HOV);
            UIManager.put("ComboBox.selectionBackground", new Color(0, 120, 215));
            UIManager.put("TextField.background",     BG_CONTROL);
            UIManager.put("TextField.foreground",     C_TEXT);
            UIManager.put("ScrollPane.background",    BG_CARD);
            UIManager.put("List.background",          BG_CARD);
            UIManager.put("List.foreground",          C_TEXT);
            UIManager.put("List.selectionBackground", new Color(0, 100, 180));
            UIManager.put("PopupMenu.background",     BG_CARD);
            UIManager.put("MenuItem.background",      BG_CARD);
            UIManager.put("MenuItem.foreground",      C_TEXT);
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ignored) {}
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(860, 570));
        setPreferredSize(new Dimension(1020, 650));
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
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 30, 60), getWidth(), 0, BG_SIDEBAR));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_LINE);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(14, 22, 14, 22));

        JComponent homeIcon = makeHomeIcon();
        homeIcon.setAlignmentY(Component.CENTER_ALIGNMENT);

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

        JPanel left = new JPanel(new BorderLayout(14, 0));
        left.setOpaque(false);
        left.add(homeIcon, BorderLayout.WEST);
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
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 20, 12, 20));

        p.add(buildCard(IconType.TEMPERATURE, "TEMPERATURA", "°C",  lblTempVal,  lblTempDesc,  pbTemp,  C_TEMP));
        p.add(buildCard(IconType.HUMIDITY,    "UMIDADE",     "%",   lblHumidVal, lblHumidDesc, pbHumid, C_HUMID));
        p.add(buildCard(IconType.LUMINOSITY,  "LUMINOSIDADE","lux", lblLuxVal,   lblLuxDesc,   pbLux,   C_LUX));
        return p;
    }

    private JPanel buildCard(IconType type, String title, String unit,
                              JLabel valLbl, JLabel descLbl, JProgressBar pb, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), new Color(35, 35, 38)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(accent);
                g2.fillRect(0, 0, getWidth(), 4);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 50));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComponent icon = makeSensorIcon(type, accent);
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(F_CLABEL);
        titleLbl.setForeground(accent);
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(80, 80, 90));
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(0, 1));

        valLbl.setAlignmentX(CENTER_ALIGNMENT);
        valLbl.setFont(F_VALUE);

        JLabel unitLbl = new JLabel(unit, SwingConstants.CENTER);
        unitLbl.setFont(F_UNIT);
        unitLbl.setForeground(C_GRAY);
        unitLbl.setAlignmentX(CENTER_ALIGNMENT);

        pb.setAlignmentX(CENTER_ALIGNMENT);
        pb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        pb.setPreferredSize(new Dimension(0, 5));

        descLbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(10));
        card.add(sep);
        card.add(Box.createVerticalGlue());
        card.add(valLbl);
        card.add(unitLbl);
        card.add(Box.createVerticalGlue());
        card.add(pb);
        card.add(Box.createVerticalStrut(6));
        card.add(descLbl);
        return card;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel p = new JPanel(new BorderLayout(8, 0)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_SIDEBAR);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(C_LINE);
                ((Graphics2D) g).fillRect(0, 0, getWidth(), 1);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 20, 12, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel lblPorta = new JLabel("Porta:");
        lblPorta.setForeground(C_GRAY);
        lblPorta.setFont(F_CTRL);

        cmbPortas.setPreferredSize(new Dimension(130, 30));
        cmbPortas.setFont(F_CTRL);

        btnAtualizar.setPreferredSize(new Dimension(36, 30));
        btnAtualizar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnAtualizar.setToolTipText("Atualizar portas");

        btnConectar.setPreferredSize(new Dimension(108, 30));
        btnConectar.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btnConectar.addActionListener(e -> onConnectClicked());
        btnAtualizar.addActionListener(e -> { if (onRefreshPorts != null) onRefreshPorts.run(); });

        left.add(lblPorta);
        left.add(cmbPortas);
        left.add(btnAtualizar);
        left.add(btnConectar);

        lblLastRead.setFont(F_CTRL);
        lblLastRead.setForeground(C_GRAY);

        p.add(left, BorderLayout.WEST);
        p.add(lblLastRead, BorderLayout.EAST);
        return p;
    }

    // ── Ação de conexão ───────────────────────────────────────────────────
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
     * Atualiza o indicador de status. Deve ser chamado na EDT.
     *
     * @param msg mensagem de status do SerialService
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

    /** @param cb acionado com a porta ao clicar "Conectar" */
    public void setOnConnect(Consumer<String> cb)    { this.onConnect = cb; }

    /** @param cb acionado ao clicar "Desconectar" */
    public void setOnDisconnect(Consumer<String> cb) { this.onDisconnect = cb; }

    /** @param cb acionado ao clicar no botão de atualizar portas */
    public void setOnRefreshPorts(Runnable cb)       { this.onRefreshPorts = cb; }

    // ── Descrições ────────────────────────────────────────────────────────
    private String descTemp(int t) {
        if (t < 15) return "Muito frio";
        if (t < 22) return "Frio";
        if (t < 28) return "Confortavel";
        if (t < 35) return "Quente";
        return "Muito quente";
    }

    private String descHumid(int h) {
        if (h < 20) return "Muito seco";
        if (h < 40) return "Seco";
        if (h < 70) return "Ideal";
        return "Umido";
    }

    private String descLux(int l) {
        if (l < 200) return "Muito escuro";
        if (l < 500) return "Meia-luz";
        if (l < 800) return "Claro";
        return "Muito claro";
    }

    // ── Tipos de ícone ────────────────────────────────────────────────────
    private enum IconType { TEMPERATURE, HUMIDITY, LUMINOSITY }

    // ── Ícones pintados com Java2D ────────────────────────────────────────
    private JComponent makeSensorIcon(IconType type, Color color) {
        return new JComponent() {
            private static final int S = 48;
            { setPreferredSize(new Dimension(S, S)); setMaximumSize(new Dimension(S, S)); setMinimumSize(new Dimension(S, S)); }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = S / 2, cy = S / 2;

                // Outer glow
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 22));
                g2.fillOval(2, 2, S - 4, S - 4);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 90));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(2, 2, S - 4, S - 4);
                g2.setColor(color);

                if (type == IconType.TEMPERATURE) {
                    // Stem
                    g2.fillRoundRect(cx - 3, 10, 6, 20, 4, 4);
                    // Bulb
                    g2.fillOval(cx - 7, 28, 14, 14);
                    // Inner lighter
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
                    g2.fillRoundRect(cx - 1, 12, 2, 14, 2, 2);

                } else if (type == IconType.HUMIDITY) {
                    // Water drop using Path2D
                    Path2D.Double drop = new Path2D.Double();
                    drop.moveTo(cx, 10);
                    drop.curveTo(cx + 12, 22, cx + 11, 34, cx, 38);
                    drop.curveTo(cx - 11, 34, cx - 12, 22, cx, 10);
                    g2.fill(drop);
                    // Shine
                    g2.setColor(new Color(255, 255, 255, 55));
                    Path2D.Double shine = new Path2D.Double();
                    shine.moveTo(cx - 3, 16);
                    shine.curveTo(cx - 7, 22, cx - 6, 26, cx - 3, 28);
                    shine.curveTo(cx - 1, 26, cx - 1, 22, cx - 3, 16);
                    g2.fill(shine);

                } else {
                    // Sun: center circle
                    g2.fillOval(cx - 7, cy - 7, 14, 14);
                    g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    // 8 rays
                    for (int i = 0; i < 8; i++) {
                        double angle = Math.toRadians(i * 45.0);
                        int x1 = (int) (cx + 11 * Math.cos(angle));
                        int y1 = (int) (cy + 11 * Math.sin(angle));
                        int x2 = (int) (cx + 17 * Math.cos(angle));
                        int y2 = (int) (cy + 17 * Math.sin(angle));
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
                g2.dispose();
            }
        };
    }

    private JComponent makeHomeIcon() {
        return new JComponent() {
            private static final int S = 38;
            { setPreferredSize(new Dimension(S, S)); setMaximumSize(new Dimension(S, S)); setMinimumSize(new Dimension(S, S)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 160, 255));
                // Roof triangle
                int[] rx = {S/2, S-4, 4}, ry = {4, 20, 20};
                g2.fillPolygon(rx, ry, 3);
                // House body
                g2.setColor(new Color(160, 140, 240));
                g2.fillRect(8, 19, S-16, S-20);
                // Door
                g2.setColor(new Color(40, 40, 50, 180));
                g2.fillRect(S/2-4, 26, 8, S-26-1);
                g2.dispose();
            }
        };
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
        pb.setBackground(new Color(55, 55, 60));
        pb.setBorderPainted(false);
        return pb;
    }
}
