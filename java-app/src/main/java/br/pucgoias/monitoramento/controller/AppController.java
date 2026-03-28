package br.pucgoias.monitoramento.controller;

import br.pucgoias.monitoramento.model.SensorData;
import br.pucgoias.monitoramento.service.SerialService;
import br.pucgoias.monitoramento.view.MainView;

import javax.swing.*;

/**
 * AppController — ponto de entrada e orquestrador da aplicação.
 *
 * Responsabilidades:
 * - Inicializar SerialService e MainView
 * - Conectar callbacks entre SerialService → MainView
 * - Manter a UI responsiva usando SwingUtilities.invokeLater()
 *
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 */
public class AppController {

    private final SerialService serialService = new SerialService();
    private final MainView view;

    // ─────────────────────────────────────────────────────────────────
    public AppController() {
        view = new MainView();
        configurarCallbacks();
        atualizarListaPortas();
    }

    // ─── Configuração dos callbacks ───────────────────────────────────
    private void configurarCallbacks() {

        // SerialService → View: novos dados chegaram
        serialService.addDadosListener(dados -> SwingUtilities.invokeLater(() -> view.atualizarDados(dados)));

        // SerialService → View: mudança de status
        serialService.addStatusListener(msg -> SwingUtilities.invokeLater(() -> view.setStatus(formatarStatus(msg))));

        // View → Controller: usuário clicou "Conectar"
        view.setOnConectar(porta -> {
            boolean ok = serialService.conectar(porta);
            if (!ok) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(view,
                        "Não foi possível conectar à porta " + porta + ".\n" +
                                "Verifique se o Arduino está conectado e a porta correta.",
                        "Erro de conexão",
                        JOptionPane.ERROR_MESSAGE));
            }
        });

        // View → Controller: usuário clicou "Desconectar"
        view.setOnDesconectar(porta -> serialService.desconectar());

        // View → Controller: usuário clicou "↻" (atualizar portas)
        view.setOnAtualizarPortas(this::atualizarListaPortas);
    }

    // ─── Utilitários ─────────────────────────────────────────────────
    private void atualizarListaPortas() {
        SwingUtilities.invokeLater(() -> view.setPortas(SerialService.listarPortas()));
    }

    private String formatarStatus(String msg) {
        // Transforma "CONECTADO: COM3" → "CONECTADO: COM3"
        // Transforma "ERRO: ..." → "ERRO: ..."
        return msg;
    }

    // ─── Ponto de entrada ────────────────────────────────────────────
    public void iniciar() {
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    // ─── main() ──────────────────────────────────────────────────────
    public static void main(String[] args) {
        // Habilita renderização de alta qualidade no Windows
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        new AppController().iniciar();
    }
}
