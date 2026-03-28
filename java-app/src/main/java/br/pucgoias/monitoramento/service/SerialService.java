package br.pucgoias.monitoramento.service;

import br.pucgoias.monitoramento.model.SensorData;
import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Manages serial communication with the Arduino.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>List available serial ports</li>
 *   <li>Open / close the serial connection</li>
 *   <li>Read data on a background daemon thread</li>
 *   <li>Notify registered listeners with parsed {@link SensorData}</li>
 *   <li>Notify registered listeners with connection status messages</li>
 * </ul>
 */
public class SerialService {

    private static final int BAUD_RATE  = 9600;
    private static final int DATA_BITS  = 8;
    private static final int STOP_BITS  = SerialPort.ONE_STOP_BIT;
    private static final int PARITY     = SerialPort.NO_PARITY;
    private static final int TIMEOUT_MS = 3000;

    private SerialPort       port;
    private Thread           readThread;
    private volatile boolean running = false;

    private final List<Consumer<SensorData>> dataListeners   = new CopyOnWriteArrayList<>();
    private final List<Consumer<String>>     statusListeners = new CopyOnWriteArrayList<>();

    /**
     * Registers a listener that receives parsed sensor readings.
     *
     * @param listener callback invoked for each valid {@link SensorData}
     */
    public void addDataListener(Consumer<SensorData> listener) {
        dataListeners.add(listener);
    }

    /**
     * Registers a listener that receives connection status messages.
     *
     * @param listener callback invoked with a status string
     */
    public void addStatusListener(Consumer<String> listener) {
        statusListeners.add(listener);
    }

    /**
     * Returns the names of all available serial ports on this machine.
     *
     * @return list of port names (e.g. {@code ["COM3", "COM4"]})
     */
    public static List<String> listPorts() {
        List<String> names = new ArrayList<>();
        try {
            for (SerialPort p : SerialPort.getCommPorts()) {
                names.add(p.getSystemPortName());
            }
        } catch (Throwable ignored) {
            /* Native library unavailable — return empty list so DEMO mode still works */
        }
        return names;
    }

    /**
     * Opens the specified serial port and starts the background read loop.
     *
     * @param portName system port name (e.g. {@code "COM3"})
     * @return {@code true} if the connection was established successfully
     */
    public boolean connect(String portName) {
        disconnect();

        port = SerialPort.getCommPort(portName);
        port.setBaudRate(BAUD_RATE);
        port.setNumDataBits(DATA_BITS);
        port.setNumStopBits(STOP_BITS);
        port.setParity(PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, TIMEOUT_MS, 0);

        if (!port.openPort()) {
            notifyStatus("ERROR: Could not open " + portName);
            return false;
        }

        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        running    = true;
        readThread = new Thread(this::readLoop, "serial-reader");
        readThread.setDaemon(true);
        readThread.start();

        notifyStatus("CONNECTED: " + portName);
        return true;
    }

    /**
     * Closes the serial port and stops the background read thread.
     */
    public void disconnect() {
        running = false;

        if (readThread != null) {
            readThread.interrupt();
            readThread = null;
        }
        if (port != null && port.isOpen()) {
            port.closePort();
        }

        port = null;
        notifyStatus("DISCONNECTED");
    }

    /**
     * @return {@code true} if the port is currently open and the read loop is active
     */
    public boolean isConnected() {
        return port != null && port.isOpen() && running;
    }

    private void readLoop() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(port.getInputStream()))) {

            String line;
            while (running && (line = reader.readLine()) != null) {
                SensorData data = SensorData.fromCsv(line);
                if (data != null) {
                    notifyData(data);
                }
            }
        } catch (Exception e) {
            if (running) {
                notifyStatus("ERROR: " + e.getMessage());
            }
        } finally {
            if (running) {
                running = false;
                notifyStatus("DISCONNECTED: connection lost");
            }
        }
    }

    private void notifyData(SensorData data) {
        for (Consumer<SensorData> l : dataListeners) {
            l.accept(data);
        }
    }

    private void notifyStatus(String msg) {
        for (Consumer<String> l : statusListeners) {
            l.accept(msg);
        }
    }
}
