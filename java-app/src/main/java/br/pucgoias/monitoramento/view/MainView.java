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
 * Main application window (Swing + FlatLaf Dark theme).
 *
 * <p>Displays in real-time:
 * <ul>
 *   <li>Temperature (°C)</li>
 *   <li>Humidity (%)</li>
 *   <li>Luminosity (raw 0–1023 + label)</li>
 *   <li>Serial connection status</li>
 *   <li>COM port selector</li>
 * </ul>
 */
public class MainView extends JFrame {

    private static final Color COLOR_BG        = new Color(30, 30, 46);
    private static final Color COLOR_PANEL     = new Color(49, 50, 68);
    private static final Color COLOR_TEMP      = new Color(243, 139, 168);
    private static final Color COLOR_HUMID     = new Color(137, 220, 235);
    private static final Color COLOR_LUX       = new Color(249, 226, 175);
    private static final Color COLOR_STATUS_OK = new Color(166, 227, 161);
    private static final Color COLOR_STATUS_ERR= new Color(243, 139, 168);

    private static final Font FONT_VALUE = new Font("Segoe UI", Font.BOLD, 48);
    private static final Font FONT_UNIT  = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);

    private final JLabel lblTempValue   = createValueLabel("--", COLOR_TEMP);
    private final JLabel lblHumidValue  = createValueLabel("--", COLOR_HUMID);
    private final JLabel lblLuxValue    = createValueLabel("--", COLOR_LUX);
    private final JLabel lblLuxDesc     = new JLabel("Waiting...");
    private final JLabel lblStatus      = new JLabel("● Disconnected");
    private final JLabel lblLastReading = new JLabel("Last reading: —");

    private final JComboBox<String> cmbPorts  = new JComboBox<>();
    private final JButton           btnConnect = new JButton("Connect");
    private final JButton           btnRefresh = new JButton("↻");

    private Consumer<String> onConnect;
    private Consumer<String> onDisconnect;
    private Runnable         onRefreshPorts;

    public MainView() {
        super("Environment Monitoring — PI V-B | PUC Goiás");
        setupLookAndFeel();
        setupWindow();
        buildUI();
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ignored) {}
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(750, 500));
        setPreferredSize(new Dimension(900, 580));
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (onDisconnect != null) onDisconnect.accept("");
                dispose();
                System.exit(0);
            }
        });
    }

    private void buildUI() {
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildDataPanel(), BorderLayout.CENTER);
        add(buildFooter(),    BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("🏠  Smart Environment Monitoring");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.WHITE);

        lblStatus.setFont(FONT_LABEL);
        lblStatus.setForeground(COLOR_STATUS_ERR);

        panel.add(title,     BorderLayout.WEST);
        panel.add(lblStatus, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildDataPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 8, 16));

        panel.add(buildSensorCard("🌡️  Temperature", lblTempValue,  "°C", COLOR_TEMP));
        panel.add(buildSensorCard("💧  Humidity",    lblHumidValue, "%",  COLOR_HUMID));
        panel.add(buildLuxCard());
        return panel;
    }

    private JPanel buildSensorCard(String title, JLabel valueLabel, String unit, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_LABEL);
        lblTitle.setForeground(color);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUnit = new JLabel(unit);
        lblUnit.setFont(FONT_UNIT);
        lblUnit.setForeground(Color.LIGHT_GRAY);
        lblUnit.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);
        card.add(lblUnit);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel buildLuxCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_LUX.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("☀️  Luminosity");
        lblTitle.setFont(FONT_LABEL);
        lblTitle.setForeground(COLOR_LUX);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLuxValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLuxDesc.setFont(FONT_UNIT);
        lblLuxDesc.setForeground(Color.LIGHT_GRAY);
        lblLuxDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(lblLuxValue);
        card.add(lblLuxDesc);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(8, 16, 12, 16));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        leftPanel.setBackground(COLOR_PANEL);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setForeground(Color.LIGHT_GRAY);
        lblPort.setFont(FONT_LABEL);

        cmbPorts.setPreferredSize(new Dimension(120, 28));
        btnConnect.setPreferredSize(new Dimension(110, 28));
        btnRefresh.setPreferredSize(new Dimension(36, 28));
        btnRefresh.setToolTipText("Refresh port list");

        btnConnect.addActionListener(e -> onConnectClicked());
        btnRefresh.addActionListener(e -> { if (onRefreshPorts != null) onRefreshPorts.run(); });

        leftPanel.add(lblPort);
        leftPanel.add(cmbPorts);
        leftPanel.add(btnRefresh);
        leftPanel.add(btnConnect);

        lblLastReading.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLastReading.setForeground(Color.GRAY);

        panel.add(leftPanel,      BorderLayout.WEST);
        panel.add(lblLastReading, BorderLayout.EAST);
        return panel;
    }

    private void onConnectClicked() {
        String text = btnConnect.getText();
        String port = (String) cmbPorts.getSelectedItem();
        if (port == null) return;

        if ("Connect".equals(text)) {
            if (onConnect != null) onConnect.accept(port);
        } else {
            if (onDisconnect != null) onDisconnect.accept(port);
        }
    }

    /**
     * Updates all sensor value labels. Must be called on the EDT.
     *
     * @param data the latest sensor reading
     */
    public void updateData(SensorData data) {
        lblTempValue.setText(String.valueOf(data.getTemperature()));
        lblHumidValue.setText(String.valueOf(data.getHumidity()));
        lblLuxValue.setText(String.valueOf(data.getLuminosity()));
        lblLuxDesc.setText(data.getLuminosityDescription());
        lblLastReading.setText("Last reading: " +
                new java.text.SimpleDateFormat("HH:mm:ss").format(
                        new java.util.Date(data.getTimestamp())));
    }

    /**
     * Updates the connection status indicator. Must be called on the EDT.
     *
     * @param msg status message from {@link br.pucgoias.monitoramento.service.SerialService}
     */
    public void setStatus(String msg) {
        lblStatus.setText("● " + msg);
        boolean ok = msg.startsWith("CONNECTED");
        lblStatus.setForeground(ok ? COLOR_STATUS_OK : COLOR_STATUS_ERR);
        btnConnect.setText(ok ? "Disconnect" : "Connect");
        cmbPorts.setEnabled(!ok);
        btnRefresh.setEnabled(!ok);
    }

    /**
     * Populates the port combo box. Must be called on the EDT.
     *
     * @param ports list of available port names
     */
    public void setPorts(List<String> ports) {
        cmbPorts.removeAllItems();
        if (ports.isEmpty()) {
            cmbPorts.addItem("(No ports available)");
        } else {
            ports.forEach(cmbPorts::addItem);
        }
    }

    /**
     * @param cb callback invoked with the selected port name when Connect is clicked
     */
    public void setOnConnect(Consumer<String> cb)    { this.onConnect = cb; }

    /**
     * @param cb callback invoked with the selected port name when Disconnect is clicked
     */
    public void setOnDisconnect(Consumer<String> cb) { this.onDisconnect = cb; }

    /**
     * @param cb callback invoked when the port refresh button is clicked
     */
    public void setOnRefreshPorts(Runnable cb)       { this.onRefreshPorts = cb; }

    private JLabel createValueLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FONT_VALUE);
        label.setForeground(color);
        return label;
    }
}
