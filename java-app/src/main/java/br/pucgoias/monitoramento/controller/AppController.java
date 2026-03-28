package br.pucgoias.monitoramento.controller;

import br.pucgoias.monitoramento.model.SensorData;
import br.pucgoias.monitoramento.service.SerialService;
import br.pucgoias.monitoramento.view.MainView;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Application entry point and MVC orchestrator.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Initialise {@link SerialService} and {@link MainView}</li>
 *   <li>Wire callbacks between service and view</li>
 *   <li>Ensure all UI updates run on the Event Dispatch Thread</li>
 *   <li>Provide a built-in DEMO mode for offline demonstration</li>
 * </ul>
 */
public class AppController {

    private static final String DEMO_PORT = "DEMO";

    private final SerialService           serialService = new SerialService();
    private final MainView                view;
    private final ScheduledExecutorService scheduler     = Executors.newSingleThreadScheduledExecutor();
    private       ScheduledFuture<?>      demoTask;
    private final Random                  random        = new Random();

    private int demoTemp = 24;
    private int demoHumid = 55;
    private int demoLux = 512;

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
            if (DEMO_PORT.equals(port)) {
                startDemo();
            } else {
                boolean ok = serialService.connect(port);
                if (!ok) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view,
                            "Could not connect to port " + port + ".\n" +
                            "Ensure the Arduino is connected and the correct port is selected.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE));
                }
            }
        });

        view.setOnDisconnect(port -> {
            stopDemo();
            serialService.disconnect();
        });

        view.setOnRefreshPorts(this::refreshPortList);
    }

    /**
     * Starts the built-in demo mode, generating realistic sensor readings every 2 seconds.
     */
    private void startDemo() {
        demoTemp  = 22 + random.nextInt(6);
        demoHumid = 45 + random.nextInt(30);
        demoLux   = 400 + random.nextInt(400);

        SwingUtilities.invokeLater(() -> view.setStatus("CONNECTED: DEMO"));

        demoTask = scheduler.scheduleAtFixedRate(() -> {
            demoTemp  = clamp(demoTemp  + random.nextInt(3) - 1, 18,  40);
            demoHumid = clamp(demoHumid + random.nextInt(5) - 2,  0, 100);
            demoLux   = clamp(demoLux   + random.nextInt(60) - 30, 0, 1023);

            SensorData data = new SensorData(demoTemp, demoHumid, demoLux);
            SwingUtilities.invokeLater(() -> view.updateData(data));
        }, 2, 2, TimeUnit.SECONDS);
    }

    /**
     * Stops the demo mode simulation.
     */
    private void stopDemo() {
        if (demoTask != null && !demoTask.isCancelled()) {
            demoTask.cancel(false);
            demoTask = null;
            SwingUtilities.invokeLater(() -> view.setStatus("DISCONNECTED"));
        }
    }

    private void refreshPortList() {
        List<String> ports = SerialService.listPorts();
        ports.add(0, DEMO_PORT);
        SwingUtilities.invokeLater(() -> view.setPorts(ports));
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

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
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
