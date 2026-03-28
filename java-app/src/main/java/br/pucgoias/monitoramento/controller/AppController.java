package br.pucgoias.monitoramento.controller;

import br.pucgoias.monitoramento.model.SensorData;
import br.pucgoias.monitoramento.service.SerialService;
import br.pucgoias.monitoramento.view.MainView;

import javax.swing.*;

/**
 * Application entry point and MVC orchestrator.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Initialise {@link SerialService} and {@link MainView}</li>
 *   <li>Wire callbacks between service and view</li>
 *   <li>Ensure all UI updates run on the Event Dispatch Thread</li>
 * </ul>
 */
public class AppController {

    private final SerialService serialService = new SerialService();
    private final MainView view;

    public AppController() {
        view = new MainView();
        setupCallbacks();
        refreshPortList();
    }

    private void setupCallbacks() {
        serialService.addDataListener(data ->
            SwingUtilities.invokeLater(() -> view.updateData(data)));

        serialService.addStatusListener(msg ->
            SwingUtilities.invokeLater(() -> view.setStatus(formatStatus(msg))));

        view.setOnConnect(port -> {
            boolean ok = serialService.connect(port);
            if (!ok) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view,
                        "Could not connect to port " + port + ".\n" +
                        "Ensure the Arduino is connected and the correct port is selected.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE));
            }
        });

        view.setOnDisconnect(port -> serialService.disconnect());

        view.setOnRefreshPorts(this::refreshPortList);
    }

    private void refreshPortList() {
        SwingUtilities.invokeLater(() -> view.setPorts(SerialService.listPorts()));
    }

    private String formatStatus(String msg) {
        return msg;
    }

    /**
     * Makes the main window visible on the Event Dispatch Thread.
     */
    public void start() {
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        new AppController().start();
    }
}
