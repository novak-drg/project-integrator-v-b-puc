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
 * SerialService — gerencia a comunicação serial com o Arduino.
 *
 * Responsabilidades:
 *   - Listar portas disponíveis
 *   - Conectar / desconectar da porta serial
 *   - Ler dados em background thread (nunca bloqueando a EDT)
 *   - Notificar listeners quando um SensorData válido chega
 *   - Notificar listeners de status de conexão
 *
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 */
public class SerialService {

    // ─── Configuração serial ─────────────────────────────────────────
    private static final int BAUD_RATE  = 9600;
    private static final int DATA_BITS  = 8;
    private static final int STOP_BITS  = SerialPort.ONE_STOP_BIT;
    private static final int PARITY     = SerialPort.NO_PARITY;
    private static final int TIMEOUT_MS = 3000;

    // ─── Estado interno ──────────────────────────────────────────────
    private SerialPort   porta;
    private Thread       threadLeitura;
    private volatile boolean rodando = false;

    // ─── Listeners ───────────────────────────────────────────────────
    private final List<Consumer<SensorData>>  dadosListeners  = new CopyOnWriteArrayList<>();
    private final List<Consumer<String>>      statusListeners = new CopyOnWriteArrayList<>();

    // ─── Registro de listeners ───────────────────────────────────────
    public void addDadosListener(Consumer<SensorData> listener) {
        dadosListeners.add(listener);
    }

    public void addStatusListener(Consumer<String> listener) {
        statusListeners.add(listener);
    }

    // ─── Listar portas disponíveis ───────────────────────────────────
    public static List<String> listarPortas() {
        List<String> nomes = new ArrayList<>();
        for (SerialPort p : SerialPort.getCommPorts()) {
            nomes.add(p.getSystemPortName());
        }
        return nomes;
    }

    // ─── Conectar ────────────────────────────────────────────────────
    public boolean conectar(String nomePorta) {
        desconectar();  // garante estado limpo

        porta = SerialPort.getCommPort(nomePorta);
        porta.setBaudRate(BAUD_RATE);
        porta.setNumDataBits(DATA_BITS);
        porta.setNumStopBits(STOP_BITS);
        porta.setParity(PARITY);
        porta.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING, TIMEOUT_MS, 0);

        if (!porta.openPort()) {
            notificarStatus("ERRO: Não foi possível abrir " + nomePorta);
            return false;
        }

        // Aguarda Arduino reiniciar após abertura da porta (auto-reset)
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        rodando = true;
        threadLeitura = new Thread(this::loopLeitura, "serial-reader");
        threadLeitura.setDaemon(true);
        threadLeitura.start();

        notificarStatus("CONECTADO: " + nomePorta);
        return true;
    }

    // ─── Desconectar ─────────────────────────────────────────────────
    public void desconectar() {
        rodando = false;
        if (threadLeitura != null) {
            threadLeitura.interrupt();
            threadLeitura = null;
        }
        if (porta != null && porta.isOpen()) {
            porta.closePort();
        }
        porta = null;
        notificarStatus("DESCONECTADO");
    }

    public boolean isConectado() {
        return porta != null && porta.isOpen() && rodando;
    }

    // ─── Loop de leitura (thread background) ─────────────────────────
    private void loopLeitura() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(porta.getInputStream()))) {

            String linha;
            while (rodando && (linha = reader.readLine()) != null) {
                SensorData dados = SensorData.fromCsv(linha);
                if (dados != null) {
                    notificarDados(dados);
                }
            }
        } catch (Exception e) {
            if (rodando) {
                notificarStatus("ERRO: " + e.getMessage());
            }
        } finally {
            if (rodando) {
                rodando = false;
                notificarStatus("DESCONECTADO: conexão perdida");
            }
        }
    }

    // ─── Notificações ────────────────────────────────────────────────
    private void notificarDados(SensorData dados) {
        for (Consumer<SensorData> l : dadosListeners) {
            l.accept(dados);
        }
    }

    private void notificarStatus(String msg) {
        for (Consumer<String> l : statusListeners) {
            l.accept(msg);
        }
    }
}
