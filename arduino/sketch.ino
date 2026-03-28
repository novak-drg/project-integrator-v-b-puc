/**
 * Smart Environment Monitoring System — Arduino Firmware
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 *
 * Hardware (Tinkercad-compatible):
 *   - Arduino UNO R3
 *   - TMP36  (temperature)  → Analog Pin A1
 *   - LDR    (luminosity)   → Analog Pin A0 (voltage divider with 10kΩ resistor)
 *   - Potentiometer (simulates humidity) → Analog Pin A2
 *
 * Serial protocol: 9600 bps, CSV with newline
 *   Format : temperature,humidity,luminosity
 *   Example: 25,60,512
 *
 * Wiring:
 *   A0  → LDR  (voltage divider: LDR → A0, 10kΩ → GND)
 *   A1  → TMP36 Vout (center pin)
 *   A2  → Potentiometer wiper (center pin)
 *   5V  → TMP36 +V, LDR leg 1, Potentiometer 5V
 *   GND → TMP36 GND, 10kΩ resistor, Potentiometer GND
 */

const int          TEMP_PIN = A1;
const int          LDR_PIN  = A0;
const int          HUMID_PIN = A2;
const unsigned long READ_INTERVAL = 2000UL;

unsigned long lastRead = 0;

void setup() {
  Serial.begin(9600);
  Serial.println("SISTEMA,INICIADO,0");
}

void loop() {
  unsigned long now = millis();

  if (now - lastRead >= READ_INTERVAL) {
    lastRead = now;

    int   rawTemp    = analogRead(TEMP_PIN);
    float voltage    = rawTemp * (5.0 / 1023.0);
    int   temperature = (int)((voltage - 0.5) * 100.0);

    int rawHumid  = analogRead(HUMID_PIN);
    int humidity  = map(rawHumid, 0, 1023, 0, 100);

    int luminosity = analogRead(LDR_PIN);

    Serial.print(temperature);
    Serial.print(",");
    Serial.print(humidity);
    Serial.print(",");
    Serial.println(luminosity);
  }
}
