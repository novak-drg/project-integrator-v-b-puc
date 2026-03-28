/*
 * Sistema de Monitoramento de Ambientes Inteligentes
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 * Professor: Thalles Bruno G. N. dos Santos
 *
 * Hardware (Tinkercad-compatível):
 *   - Arduino UNO R3
 *   - TMP36 (temperatura) → Pino Analógico A1
 *   - LDR / fotoresistor (luminosidade) → Pino Analógico A0 (divisor com resistor 10kΩ)
 *   - Potenciômetro (simula umidade) → Pino Analógico A2
 *
 * Protocolo serial: 9600 bps, CSV com newline
 *   Formato: temperatura,umidade,luminosidade
 *   Exemplo: 25,60,512
 *
 * Pinagem:
 *   A0 → LDR (divisor de tensão: LDR → A0 + 10kΩ → GND)
 *   A1 → TMP36 Vout (perna central)
 *   A2 → Potenciômetro pino central
 *   5V → TMP36 +V + LDR leg1 + Potenciômetro 5V
 *   GND → TMP36 GND + Resistor 10kΩ + Potenciômetro GND
 */

// ─── Configuração de pinos ───────────────────────────────────────────
const int TEMP_PIN = A1;   // TMP36 — saída de tensão analógica
const int LDR_PIN  = A0;   // Fotorresistor (LDR) — divisor de tensão
const int UMID_PIN = A2;   // Potenciômetro — simula umidade relativa

// ─── Intervalo de leitura (ms) ───────────────────────────────────────
const unsigned long INTERVALO = 2000UL;   // 2 segundos

// ─── Variáveis de estado ─────────────────────────────────────────────
unsigned long ultimaLeitura = 0;

// ─────────────────────────────────────────────────────────────────────
void setup() {
  Serial.begin(9600);
  // Heartbeat de inicialização (ignorado pelo Java com .trim())
  Serial.println("SISTEMA,INICIADO,0");
}

// ─────────────────────────────────────────────────────────────────────
void loop() {
  unsigned long agora = millis();

  if (agora - ultimaLeitura >= INTERVALO) {
    ultimaLeitura = agora;

    // ── Leitura TMP36 ────────────────────────────────────────────────
    // TMP36: 10mV por grau Celsius, 500mV = 0°C
    // Fórmula: Temperatura (°C) = (Tensão - 0.5V) × 100
    int leituraTemp  = analogRead(TEMP_PIN);
    float tensao     = leituraTemp * (5.0 / 1023.0);
    int temperatura  = (int)((tensao - 0.5) * 100.0);

    // ── Leitura Potenciômetro (umidade simulada) ─────────────────────
    // Mapeia 0-1023 para 0-100% de umidade relativa
    int leituraUmid = analogRead(UMID_PIN);
    int umidade     = map(leituraUmid, 0, 1023, 0, 100);

    // ── Leitura LDR ──────────────────────────────────────────────────
    int luminosidade = analogRead(LDR_PIN);  // 0 (escuro) – 1023 (claro)

    // ── Saída Serial ─────────────────────────────────────────────────
    // Emite CSV: temperatura,umidade,luminosidade
    Serial.print(temperatura);
    Serial.print(",");
    Serial.print(umidade);
    Serial.print(",");
    Serial.println(luminosidade);
  }
}
