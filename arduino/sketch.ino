/*
 * Sistema de Monitoramento de Ambientes Inteligentes
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 * Professor: Thalles Bruno G. N. dos Santos
 *
 * Hardware:
 *   - Arduino UNO R3
 *   - DHT11 (temperatura e umidade) — Pino Digital 2
 *   - LDR / fotoresistor — Pino Analógico A0 (divisor com resistor 10kΩ)
 *
 * Protocolo serial: 9600 bps, CSV com newline
 *   Formato: temperatura,umidade,luminosidade
 *   Exemplo: 25,60,512
 *
 * Pinagem:
 *   Pin 2  → DHT11 Data
 *   A0     → LDR (divisor de tensão: LDR → A0 + 10kΩ → GND)
 *   5V     → DHT11 VCC + LDR leg1
 *   GND    → DHT11 GND + resistor 10kΩ
 */

#include <DHT11.h>

// ─── Configuração de pinos ───────────────────────────────────────────
const int  DHT_PIN   = 2;    // Pino digital — DHT11
const int  LDR_PIN   = A0;   // Pino analógico — LDR

// ─── Intervalo de leitura (ms) ───────────────────────────────────────
const unsigned long INTERVALO = 2000UL;   // 2 segundos

// ─── Variáveis de estado ─────────────────────────────────────────────
DHT11          dht11(DHT_PIN);
unsigned long  ultimaLeitura = 0;

// ─────────────────────────────────────────────────────────────────────
void setup() {
  Serial.begin(9600);
  // Aguarda Serial estar pronta (necessário para algumas placas)
  while (!Serial) { ; }
  // Heartbeat de inicialização (ignorado pelo Java com .trim())
  Serial.println("SISTEMA,INICIADO,0");
}

// ─────────────────────────────────────────────────────────────────────
void loop() {
  unsigned long agora = millis();

  if (agora - ultimaLeitura >= INTERVALO) {
    ultimaLeitura = agora;

    // ── Leitura DHT11 ────────────────────────────────────────────────
    int temperatura = 0;
    int umidade     = 0;
    int resultado   = dht11.readTemperatureHumidity(temperatura, umidade);

    // ── Leitura LDR ──────────────────────────────────────────────────
    int luminosidade = analogRead(LDR_PIN);  // 0 (escuro) – 1023 (claro)

    // ── Saída Serial ─────────────────────────────────────────────────
    if (resultado == 0) {
      // Leitura OK — emite CSV: temperatura,umidade,luminosidade
      Serial.print(temperatura);
      Serial.print(",");
      Serial.print(umidade);
      Serial.print(",");
      Serial.println(luminosidade);
    } else {
      // Erro na leitura do DHT11 — emite indicador de erro
      // O módulo Java ignora linhas que começam com "ERR"
      Serial.print("ERR,");
      Serial.print(resultado);
      Serial.print(",");
      Serial.println(luminosidade);
    }
  }
}
