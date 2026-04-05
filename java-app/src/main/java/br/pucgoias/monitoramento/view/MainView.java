package br.pucgoias.monitoramento.view;

import br.pucgoias.monitoramento.model.SensorData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class MainView extends JFrame {

    private static final Color BG_BODY    = c("#020617");
    private static final Color BG_SURFACE = c("#0B1326");
    private static final Color BG_CTRL    = c("#171F33");
    private static final Color C_TEMP     = c("#FFB783");
    private static final Color C_HUMID    = c("#7BD0FF");
    private static final Color C_LUX      = c("#EFC200");
    private static final Color C_OK       = c("#34D399");
    private static final Color C_ERR      = c("#F87171");
    private static final Color C_PRI      = c("#F1F5F9");
    private static final Color C_SEC      = c("#C6C6CD");
    private static final Color C_MUT      = new Color(198, 198, 205, 90);
    private static final Color C_HEADING  = c("#DAE2FD");
    private static final Color C_NAV_ON   = c("#7BD0FF");
    private static final Color C_NAV_OFF  = c("#64748B");
    private static final Color C_BADGE    = c("#94A3B8");

    private final JLabel lblTempVal   = new JLabel("—");
    private final JLabel lblHumidVal  = new JLabel("—");
    private final JLabel lblLuxLabel  = new JLabel("—");
    private final JLabel lblLuxRaw    = new JLabel("");
    private final JLabel lblEnvStatus = new JLabel("Aguardando...");
    private final JLabel lblLastRead  = new JLabel("ÚLTIMA LEITURA: —");
    private final JLabel lblDot       = new JLabel("●");
    private final JLabel lblBadgeTxt  = new JLabel("DESCONECTADO");

    private final JComboBox<String> cmbPortas    = new JComboBox<>();
    private final JButton           btnConectar  = new JButton("Conectar");
    private final JButton           btnAtualizar = new JButton("↻");

    private Consumer<String> onConnect;
    private Consumer<String> onDisconnect;
    private Runnable         onRefreshPorts;

    public MainView() {
        super("Monitor Ambiental — PI V-B | PUC Goiás");
        setupWindow();
        buildUI();
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(480, 750));
        setMinimumSize(new Dimension(420, 650));
        setResizable(true);
        getContentPane().setBackground(BG_BODY);
        setLayout(new BorderLayout());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                if (onDisconnect != null) onDisconnect.accept("");
                dispose();
                System.exit(0);
            }
        });
    }

    private void buildUI() {
        add(buildHeader(), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildContent());
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        add(buildBottomNav(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel p = solid(BG_SURFACE, new BorderLayout());
        p.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel left = transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.add(makeWifiIcon());
        left.add(lbl("<html>Monitor<br>Ambiental</html>", font("Manrope", Font.BOLD, 18), C_PRI));

        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 3)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(BG_CTRL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 999, 999);
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(0, 4, 0, 4));
        lblDot.setFont(font("Dialog", Font.PLAIN, 8));
        lblDot.setForeground(C_ERR);
        lblBadgeTxt.setFont(font("Inter", Font.BOLD, 9));
        lblBadgeTxt.setForeground(C_BADGE);
        badge.add(lblDot);
        badge.add(lblBadgeTxt);

        p.add(left,  BorderLayout.WEST);
        p.add(badge, BorderLayout.EAST);
        return p;
    }

    private JPanel buildContent() {
        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(BG_SURFACE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        canvas.setLayout(new GridBagLayout());
        canvas.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx   = 0;
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0;

        gc.gridy  = 0;
        gc.insets = new Insets(24, 20, 0, 20);
        canvas.add(buildEnvSection(), gc);

        gc.insets = new Insets(16, 20, 0, 20);

        gc.gridy = 1;
        canvas.add(buildCard("Temperatura",  C_TEMP,  new Color(217, 119, 34, 45), lblTempVal,  "°C", null,      IconType.TEMP),  gc);
        gc.gridy = 2;
        canvas.add(buildCard("Umidade",      C_HUMID, new Color(0,   80, 120, 60), lblHumidVal, "%",  null,      IconType.HUMID), gc);
        gc.gridy = 3;
        canvas.add(buildCard("Luminosidade", C_LUX,   new Color(206, 167, 0,  45), lblLuxLabel, null, lblLuxRaw, IconType.LUX),   gc);

        gc.gridy  = 4;
        gc.insets = new Insets(14, 20, 0, 20);
        canvas.add(buildConnectionPanel(), gc);

        gc.gridy  = 5;
        gc.insets = new Insets(10, 20, 16, 20);
        JPanel footer = transparent(new FlowLayout(FlowLayout.CENTER));
        lblLastRead.setFont(font("Inter", Font.PLAIN, 9));
        lblLastRead.setForeground(C_MUT);
        footer.add(lblLastRead);
        canvas.add(footer, gc);

        gc.gridy   = 6;
        gc.weighty = 1.0;
        canvas.add(Box.createVerticalGlue(), gc);

        return canvas;
    }

    private JPanel buildEnvSection() {
        JPanel p = transparent(new GridLayout(2, 1, 0, 4));
        JLabel tag = lbl("STATUS DO AMBIENTE", font("Inter", Font.PLAIN, 10), new Color(198, 198, 205, 130));
        lblEnvStatus.setFont(font("Manrope", Font.BOLD, 28));
        lblEnvStatus.setForeground(C_HEADING);
        p.add(tag);
        p.add(lblEnvStatus);
        return p;
    }

    private JPanel buildCard(String name, Color accent, Color iconBg,
                              JLabel mainLbl, String unit, JLabel subLbl, IconType icon) {
        JPanel card = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(new Color(45, 52, 73, 95));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                int r = 90;
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 22));
                g2.fillOval(getWidth() - r, -r / 2, r * 2, r * 2);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel topRow  = transparent(new BorderLayout());
        JPanel iconName = transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        iconName.add(makeSensorBadge(icon, accent, iconBg));
        iconName.add(lbl(name, font("Inter", Font.BOLD, 13), C_SEC));
        topRow.add(iconName, BorderLayout.WEST);
        topRow.add(lbl("· · ·", font("Dialog", Font.PLAIN, 9), new Color(198, 198, 205, 80)), BorderLayout.EAST);

        mainLbl.setFont(font("Manrope", Font.BOLD, unit != null ? 52 : 36));
        mainLbl.setForeground(accent);

        JPanel valueRow = transparent(new FlowLayout(FlowLayout.LEFT, 3, 0));
        valueRow.add(mainLbl);
        if (unit != null) valueRow.add(lbl(unit, font("Manrope", Font.BOLD, 26), accent));
        if (subLbl != null) {
            subLbl.setFont(font("Inter", Font.PLAIN, 13));
            subLbl.setForeground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 170));
            valueRow.add(subLbl);
        }

        card.add(topRow,   BorderLayout.NORTH);
        card.add(valueRow, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildConnectionPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(new Color(23, 31, 51, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        p.setOpaque(false);

        cmbPortas.setPreferredSize(new Dimension(120, 28));
        cmbPortas.setFont(font("Inter", Font.PLAIN, 11));
        cmbPortas.setBackground(BG_CTRL);
        cmbPortas.setForeground(C_PRI);

        styleBtn(btnAtualizar, BG_CTRL, C_SEC);
        btnAtualizar.setPreferredSize(new Dimension(32, 28));
        btnAtualizar.setFont(font("Dialog", Font.PLAIN, 14));
        btnAtualizar.setToolTipText("Atualizar portas");

        styleBtn(btnConectar, C_HUMID, BG_SURFACE);
        btnConectar.setPreferredSize(new Dimension(110, 28));
        btnConectar.setFont(font("Inter", Font.BOLD, 11));

        btnConectar.addActionListener(e -> onConnectClicked());
        btnAtualizar.addActionListener(e -> { if (onRefreshPorts != null) onRefreshPorts.run(); });

        p.add(lbl("Porta:", font("Inter", Font.PLAIN, 11), C_SEC));
        p.add(cmbPortas);
        p.add(btnAtualizar);
        p.add(btnConectar);
        return p;
    }

    private JPanel buildBottomNav() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(new Color(11, 19, 38, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(4, 20, 8, 20));
        nav.add(navTab("DASHBOARD", true));
        nav.add(navTab("HISTÓRICO", false));
        return nav;
    }

    private JPanel navTab(String text, boolean active) {
        Color c = active ? C_NAV_ON : C_NAV_OFF;
        JPanel tab = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0)) {
            @Override protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = aa(g);
                    g2.setColor(BG_CTRL);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 999, 999);
                    g2.dispose();
                }
            }
        };
        tab.setOpaque(false);
        tab.setBorder(new EmptyBorder(8, 14, 8, 14));
        tab.add(makeNavIcon(text.equals("DASHBOARD"), c));
        tab.add(lbl(text, font("Inter", Font.BOLD, 9), c));
        return tab;
    }

    public void updateData(SensorData data) {
        int t = data.getTemperature(), h = data.getHumidity(), l = data.getLuminosity();
        lblTempVal.setText(String.valueOf(t));
        lblHumidVal.setText(String.valueOf(h));
        lblLuxLabel.setText(luxDesc(l));
        lblLuxRaw.setText("(" + l + " lux)");
        lblEnvStatus.setText(envStatus(t, h, l));
        lblLastRead.setText("ÚLTIMA LEITURA: " +
                new SimpleDateFormat("HH:mm:ss").format(new Date(data.getTimestamp())));
    }

    public void setStatus(String msg) {
        boolean ok = msg.startsWith("CONNECTED");
        lblDot.setForeground(ok ? C_OK : C_ERR);
        lblBadgeTxt.setText(ok ? "CONECTADO" : "DESCONECTADO");
        if (!ok) lblEnvStatus.setText("Aguardando...");
        btnConectar.setText(ok ? "Desconectar" : "Conectar");
        cmbPortas.setEnabled(!ok);
        btnAtualizar.setEnabled(!ok);
        styleBtn(btnConectar, ok ? C_ERR : C_HUMID, ok ? Color.WHITE : BG_SURFACE);
    }

    public void setPorts(List<String> ports) {
        cmbPortas.removeAllItems();
        if (ports.isEmpty()) cmbPortas.addItem("(Nenhuma porta)");
        else ports.forEach(cmbPortas::addItem);
    }

    public void setOnConnect(Consumer<String> cb)    { this.onConnect = cb; }
    public void setOnDisconnect(Consumer<String> cb) { this.onDisconnect = cb; }
    public void setOnRefreshPorts(Runnable cb)       { this.onRefreshPorts = cb; }

    private void onConnectClicked() {
        String p = (String) cmbPortas.getSelectedItem();
        if (p == null) return;
        if ("Conectar".equals(btnConectar.getText())) { if (onConnect    != null) onConnect.accept(p); }
        else                                          { if (onDisconnect != null) onDisconnect.accept(p); }
    }

    private String envStatus(int t, int h, int l) {
        int s = 0;
        if (t >= 18 && t <= 28) s++;
        if (h >= 40 && h <= 70) s++;
        if (l >= 200 && l <= 800) s++;
        return s == 3 ? "Equilibrado" : s == 2 ? "Razoável" : s == 1 ? "Atenção" : "Crítico";
    }

    private String luxDesc(int l) {
        if (l < 200) return "Escuro";
        if (l < 500) return "Meia-luz";
        if (l < 800) return "Médio";
        return "Claro";
    }

    private enum IconType { TEMP, HUMID, LUX }

    private JComponent makeSensorBadge(IconType type, Color accent, Color bg) {
        int S = 40;
        return new JComponent() {
            { setPreferredSize(new Dimension(S, S)); setMaximumSize(new Dimension(S, S)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(bg);
                g2.fillOval(0, 0, S, S);
                g2.setColor(accent);
                int cx = S / 2, cy = S / 2;
                if (type == IconType.TEMP) {
                    g2.fillRoundRect(cx - 3, 7, 6, 17, 4, 4);
                    g2.fillOval(cx - 7, cy + 4, 14, 14);
                } else if (type == IconType.HUMID) {
                    Path2D.Double d = new Path2D.Double();
                    d.moveTo(cx, 7);
                    d.curveTo(cx + 9, 17, cx + 9, 29, cx, 33);
                    d.curveTo(cx - 9, 29, cx - 9, 17, cx, 7);
                    g2.fill(d);
                } else {
                    g2.fillOval(cx - 6, cy - 6, 12, 12);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 8; i++) {
                        double a = Math.toRadians(i * 45.0);
                        g2.drawLine((int) (cx + 9  * Math.cos(a)), (int) (cy + 9  * Math.sin(a)),
                                    (int) (cx + 14 * Math.cos(a)), (int) (cy + 14 * Math.sin(a)));
                    }
                }
                g2.dispose();
            }
        };
    }

    private JComponent makeWifiIcon() {
        return new JComponent() {
            { Dimension d = new Dimension(22, 16); setPreferredSize(d); setMaximumSize(d); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(C_HUMID);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(1, -2, 20, 20, 30, 120);
                g2.drawArc(4,  2, 14, 14, 30, 120);
                g2.drawArc(7,  6,  8,  8, 30, 120);
                g2.fillOval(9, 13, 4, 4);
                g2.dispose();
            }
        };
    }

    private JComponent makeNavIcon(boolean dash, Color c) {
        int S = 18;
        return new JComponent() {
            { Dimension d = new Dimension(S, S); setPreferredSize(d); setMaximumSize(d); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = aa(g);
                g2.setColor(c);
                if (dash) {
                    g2.fillRoundRect(0,  0,  7, 7, 2, 2);
                    g2.fillRoundRect(11, 0,  7, 7, 2, 2);
                    g2.fillRoundRect(0,  11, 7, 7, 2, 2);
                    g2.fillRoundRect(11, 11, 7, 7, 2, 2);
                } else {
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(1, 1, S - 2, S - 2);
                    g2.drawLine(S / 2, S / 2, S / 2, 4);
                    g2.drawLine(S / 2, S / 2, S - 4, S / 2);
                }
                g2.dispose();
            }
        };
    }

    private static Color  c(String hex)                { return Color.decode(hex); }
    private static Font   font(String f, int s, int sz) { return new Font(f, s, sz); }
    private static JLabel lbl(String t, Font f, Color fg) {
        JLabel l = new JLabel(t); l.setFont(f); l.setForeground(fg); return l;
    }
    private static JPanel solid(Color bg, LayoutManager lm) {
        JPanel p = new JPanel(lm) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(getBackground()); g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setBackground(bg); p.setOpaque(false); return p;
    }
    private static JPanel transparent(LayoutManager lm) {
        JPanel p = lm != null ? new JPanel(lm) : new JPanel();
        p.setOpaque(false); return p;
    }
    private static Graphics2D aa(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }
    private static void styleBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg);
        b.setOpaque(true); b.setBorderPainted(false); b.setFocusPainted(false);
    }
}
