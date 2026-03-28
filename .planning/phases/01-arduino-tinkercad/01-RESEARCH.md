# Phase 1 Research — Arduino + Tinkercad

## Objective
Como implementar o circuito Arduino UNO com DHT11 e LDR no Tinkercad + código de leitura serial?

## Findings

### Tinkercad — Componentes e Conexões

**DHT11 no Tinkercad:**
- Buscar por "TMP36" *não* é correto — buscar "Temperature Sensor" ou diretamente "DHT11"
- Tinkercad 2024/2025 suporta DHT11 nativo na biblioteca de componentes
- Pinos: VCC → 5V, GND → GND, Data → Digital Pin 2
- A biblioteca DHT.h pode não estar disponível no ambiente Tinkercad — usar a versão simplificada `dht11.h` **ou** simular usando a função `dhtRead()` do Tinkercad Code Blocks
- **Alternativa confiável:** Usar `Serial.print()` com valores lidos de `analogRead()` para simular variações (Tinkercad permite ajuste de sliders nos sensores durante simulação)

**LDR (fotoresistor) no Tinkercad:**
- Componente disponível como "Photoresistor" na biblioteca
- Conexão divisor de tensão: LDR leg1 → 5V; LDR leg2 → A0 + resistor 10kΩ → GND
- Leitura: `analogRead(A0)` retorna 0–1023

### Código Arduino — Protocolo de Saída

**Formato determinado (já definido no projeto):**
```
temperatura,umidade,luminosidade\n
```
Exemplo: `25.30,60.00,512\n`

**Baud rate:** 9600 (compatível com Tinkercad Serial Monitor)

**Frequência:** leitura a cada 2000ms com `millis()` (não `delay()`) para não bloquear

### Bibliotecas Disponíveis no Tinkercad
- `DHT11` lib integrada ao ambiente (usar `#include <DHT11.h>` ou versão simplificada)
- Alternativa: Simular DHT11 com variação via `random()` para fins de demo
- **Recomendação final:** Usar `#include <DHT11.h>` que está disponível no editor de código Tinkercad

### Estrutura do Sketch

```cpp
#include <DHT11.h>

DHT11 dht11(2);          // Data pin 2
const int LDR_PIN = A0;
const long INTERVAL = 2000;
unsigned long previousMillis = 0;

void setup() {
  Serial.begin(9600);
}

void loop() {
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= INTERVAL) {
    previousMillis = currentMillis;
    int temp, hum;
    dht11.readTemperatureHumidity(temp, hum);
    int lux = analogRead(LDR_PIN);
    Serial.print(temp);
    Serial.print(",");
    Serial.print(hum);
    Serial.print(",");
    Serial.println(lux);
  }
}
```

## Validation Architecture
- Serial Monitor do Tinkercad deve mostrar linhas no formato `NN,NN,NNN`
- Sliders do DHT11 e do LDR devem alterar os valores em tempo real
- Nenhum erro de compilação no editor Tinkercad

## Pitfalls identificados
1. DHT11 pode não compilar com `DHT.h` (Adafruit) — usar `DHT11.h` nativo do Tinkercad
2. `delay(2000)` bloqueia loop inteiro — usar `millis()` 
3. Tinkercad não salva automaticamente — copiar link após cada sessão
