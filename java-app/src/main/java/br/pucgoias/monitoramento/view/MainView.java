package br.pucgoias.monitoramento.view;

import br.pucgoias.monitoramento.model.SensorData;
import br.pucgoias.monitoramento.service.SerialService;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * MainView — interface gráfica principal (Swing + FlatLaf Dark).
 *
 * Exibe em tempo real:
 *   - Temperatura (°C)
 *   - Umidade (%)
 *   - Luminosidade (valor 0–1023 + descrição)
 *   - Status de conexão serial
 *   - Seleção de porta COM
 *
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 */
public class MainView extends JFrame {

    // ─── Cores do tema ───────────────────────────────────────────────
    private static final Color COR_FUNDO       = new Color(30, 30, 46);
    private static final Color COR_PAINEL      = new Color(49, 50, 68);
    private static final Color COR_TEMP        = new Color(243, 139, 168);  // rosa
    private static final Color COR_UMID        = new Color(137, 220, 235);  // azul claro
    private static final Color COR_LUX         = new Color(249, 226, 175);  // amarelo
    private static final Color COR_STATUS_OK   = new Color(166, 227, 161);  // verde
    private static final Color COR_STATUS_ERR  = new Color(243, 139, 168);  // vermelho
    private static final Font  FONTE_VALOR     = new Font("Segoe UI", Font.BOLD, 48);
    private static final Font  FONTE_UNIDADE   = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font  FONTE_LABEL     = new Font("Segoe UI", Font.BOLD, 12);

    // ─── Componentes de dados ────────────────────────────────────────
    private final JLabel lblTempValor    = criarLabelValor("--", COR_TEMP);
    private final JLabel lblUmidValor    = criarLabelValor("--", COR_UMID);
    private final JLabel lblLuxValor     = criarLabelValor("--", COR_LUX);
    private final JLabel lblLuxDesc      = new JLabel("Aguardando...");
    private final JLabel lblStatus       = new JLabel("● Desconectado");
    private final JLabel lblUltimaLeitura = new JLabel("Última leitura: —");

    // ─── Controles de conexão ────────────────────────────────────────
    private final JComboBox<String> cmbPortas   = new JComboBox<>();
    private final JButton           btnConectar = new JButton("Conectar");
    private final JButton           btnAtualizar = new JButton("↻");

    // ─── Callback externo ────────────────────────────────────────────
    private Consumer<String>  onConectar;
    private Consumer<String>  onDesconectar;
    private Runnable          onAtualizarPortas;

    // ─────────────────────────────────────────────────────────────────
    public MainView() {
        super("Monitoramento de Ambientes — PI V-B | PUC Goiás");
        configurarLookAndFeel();
        configurarJanela();
        construirUI();
    }

    // ─── Setup ───────────────────────────────────────────────────────
    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            // fallback para look padrão
        }
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(750, 500));
        setPreferredSize(new Dimension(900, 580));
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (onDesconectar != null) onDesconectar.accept("");
                dispose();
                System.exit(0);
            }
        });
    }

    // ─── Construção da UI ────────────────────────────────────────────
    private void construirUI() {
        add(criarCabecalho(),   BorderLayout.NORTH);
        add(criarPainelDados(), BorderLayout.CENTER);
        add(criarRodape(),      BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout(8, 0));
        painel.setBackground(COR_PAINEL);
        painel.setBorder(new EmptyBorder(12, 16, 12, 16));

        // Título
        JLabel titulo = new JLabel("🏠  Monitoramento de Ambientes Inteligentes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(Color.WHITE);

        // Status
        lblStatus.setFont(FONTE_LABEL);
        lblStatus.setForeground(COR_STATUS_ERR);

        painel.add(titulo,     BorderLayout.WEST);
        painel.add(lblStatus,  BorderLayout.EAST);
        return painel;
    }

    private JPanel criarPainelDados() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 12, 0));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(16, 16, 8, 16));

        painel.add(criarCardSensor("🌡️  Temperatura", lblTempValor, "°C", COR_TEMP));
        painel.add(criarCardSensor("💧  Umidade",     lblUmidValor, "%",  COR_UMID));
        painel.add(criarCardLux());

        return painel;
    }

    private JPanel criarCardSensor(String titulo, JLabel lblValor, String unidade, Color cor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COR_PAINEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitulo  = new JLabel(titulo);
        lblTitulo.setFont(FONTE_LABEL);
        lblTitulo.setForeground(cor);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUn = new JLabel(unidade);
        lblUn.setFont(FONTE_UNIDADE);
        lblUn.setForeground(Color.LIGHT_GRAY);
        lblUn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(12));
        card.add(lblValor);
        card.add(lblUn);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel criarCardLux() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COR_PAINEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_LUX.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitulo = new JLabel("☀️  Luminosidade");
        lblTitulo.setFont(FONTE_LABEL);
        lblTitulo.setForeground(COR_LUX);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLuxValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLuxDesc.setFont(FONTE_UNIDADE);
        lblLuxDesc.setForeground(Color.LIGHT_GRAY);
        lblLuxDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(12));
        card.add(lblLuxValor);
        card.add(lblLuxDesc);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel criarRodape() {
        JPanel painel = new JPanel(new BorderLayout(8, 0));
        painel.setBackground(COR_PAINEL);
        painel.setBorder(new EmptyBorder(8, 16, 12, 16));

        // Painel esquerdo — seleção de porta
        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        esquerda.setBackground(COR_PAINEL);

        JLabel lblPorta = new JLabel("Porta:");
        lblPorta.setForeground(Color.LIGHT_GRAY);
        lblPorta.setFont(FONTE_LABEL);

        cmbPortas.setPreferredSize(new Dimension(120, 28));
        btnConectar.setPreferredSize(new Dimension(110, 28));
        btnAtualizar.setPreferredSize(new Dimension(36, 28));
        btnAtualizar.setToolTipText("Atualizar lista de portas");

        btnConectar.addActionListener(e -> aoClicarConectar());
        btnAtualizar.addActionListener(e -> { if (onAtualizarPortas != null) onAtualizarPortas.run(); });

        esquerda.add(lblPorta);
        esquerda.add(cmbPortas);
        esquerda.add(btnAtualizar);
        esquerda.add(btnConectar);

        // Painel direito — última leitura
        lblUltimaLeitura.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUltimaLeitura.setForeground(Color.GRAY);

        painel.add(esquerda,          BorderLayout.WEST);
        painel.add(lblUltimaLeitura,  BorderLayout.EAST);
        return painel;
    }

    // ─── Ações ───────────────────────────────────────────────────────
    private void aoClicarConectar() {
        String txt = btnConectar.getText();
        String porta = (String) cmbPortas.getSelectedItem();
        if (porta == null) return;

        if ("Conectar".equals(txt)) {
            if (onConectar != null) onConectar.accept(porta);
        } else {
            if (onDesconectar != null) onDesconectar.accept(porta);
        }
    }

    // ─── Atualização de dados (deve ser chamada na EDT) ──────────────
    public void atualizarDados(SensorData dados) {
        lblTempValor.setText(String.valueOf(dados.getTemperatura()));
        lblUmidValor.setText(String.valueOf(dados.getUmidade()));
        lblLuxValor.setText(String.valueOf(dados.getLuminosidade()));
        lblLuxDesc.setText(dados.getLuminosidadeDescricao());
        lblUltimaLeitura.setText("Última leitura: " +
                new java.text.SimpleDateFormat("HH:mm:ss").format(
                        new java.util.Date(dados.getTimestamp())));
    }

    public void setStatus(String msg) {
        lblStatus.setText("● " + msg);
        boolean ok = msg.startsWith("CONECTADO");
        lblStatus.setForeground(ok ? COR_STATUS_OK : COR_STATUS_ERR);
        btnConectar.setText(ok ? "Desconectar" : "Conectar");
        cmbPortas.setEnabled(!ok);
        btnAtualizar.setEnabled(!ok);
    }

    public void setPortas(List<String> portas) {
        cmbPortas.removeAllItems();
        if (portas.isEmpty()) {
            cmbPortas.addItem("(Nenhuma porta)");
        } else {
            portas.forEach(cmbPortas::addItem);
        }
    }

    // ─── Callbacks externos (definidos pelo Controller) ──────────────
    public void setOnConectar(Consumer<String> cb)    { this.onConectar = cb; }
    public void setOnDesconectar(Consumer<String> cb) { this.onDesconectar = cb; }
    public void setOnAtualizarPortas(Runnable cb)     { this.onAtualizarPortas = cb; }

    // ─── Utilitários ─────────────────────────────────────────────────
    private JLabel criarLabelValor(String texto, Color cor) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(FONTE_VALOR);
        l.setForeground(cor);
        return l;
    }
}
