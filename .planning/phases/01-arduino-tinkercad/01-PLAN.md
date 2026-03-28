---
wave: 1
depends_on: []
files_modified:
  - arduino/sketch.ino
  - arduino/README.md
requirements_addressed: [ARD-01, ARD-02, ARD-03]
autonomous: true
---

# PLAN-1.1 — Código Arduino (sketch.ino)

## Objective
Criar o código Arduino completo que lê temperatura/umidade (DHT11), luminosidade (LDR), e transmite via Serial em formato CSV a cada 2 segundos. Este código será colado no editor Tinkercad.

## Context

<read_first>
- .planning/phases/01-arduino-tinkercad/01-RESEARCH.md
- .planning/research/ARCHITECTURE.md
- .planning/REQUIREMENTS.md
</read_first>

## Tasks

### Task 1.1.1 — Criar diretório e arquivo do sketch Arduino

<action>
Criar a pasta `arduino/` na raiz do projeto e o arquivo `arduino/sketch.ino` com o código completo abaixo.

O arquivo deve conter:

```cpp
/*
 * Sistema de Monitoramento de Ambientes Inteligentes
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 *
 * Hardware:
 *   - Arduino UNO R3
 *   - DHT11 (temperatura e umidade) — Pino Digital 2
 *   - LDR / fotoresistor — Pino Analógico A0 (divisor com 10kΩ)
 *
 * Protocolo serial: 9600 bps, CSV com newline
 *   Formato: temperatura,umidade,luminosidade\n
 *   Exemplo: 25,60,512
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
  Serial.println("SISTEMA,INICIADO,0");   // heartbeat de boot
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
      // Leitura OK — emite CSV
      Serial.print(temperatura);
      Serial.print(",");
      Serial.print(umidade);
      Serial.print(",");
      Serial.println(luminosidade);
    } else {
      // Erro na leitura — emite indicador de erro
      Serial.print("ERR,");
      Serial.print(resultado);
      Serial.print(",");
      Serial.println(luminosidade);
    }
  }
}
```

Criar também `arduino/README.md` com instruções de uso no Tinkercad (ver Task 1.1.2).
</action>

<acceptance_criteria>
- Arquivo `arduino/sketch.ino` existe na raiz do projeto
- Arquivo contém `#include <DHT11.h>`
- Arquivo contém `Serial.begin(9600)`
- Arquivo contém `Serial.println(luminosidade)` (saída CSV via println)
- Arquivo contém `INTERVALO = 2000UL` (leitura a cada 2s)
- Arquivo NÃO usa `delay()` — usa `millis()` para temporização
- Arquivo contém comentários de cabeçalho com nome do projeto e pinos
</acceptance_criteria>

---

### Task 1.1.2 — Criar README de instruções Tinkercad

<action>
Criar `arduino/README.md` com as instruções para montar o circuito no Tinkercad:

```markdown
# Arduino — Sketch de Monitoramento

## Como usar no Tinkercad

### 1. Criar o circuito
1. Acesse https://www.tinkercad.com e faça login
2. Clique em **Circuits → Create new Circuit**
3. Adicione os componentes abaixo ao breadboard:

| Componente | Buscar por | Quantidade |
|-----------|-----------|-----------|
| Arduino UNO R3 | "Arduino UNO" | 1 |
| DHT11 | "Temperature Sensor" ou "DHT11" | 1 |
| Fotoresistor | "Photoresistor" | 1 |
| Resistor | "Resistor" → 10kΩ | 1 |
| Breadboard | "Breadboard" | 1 |

### 2. Conexões

#### DHT11
| Pino DHT11 | Destino Arduino |
|-----------|----------------|
| VCC (+) | 5V |
| GND (-) | GND |
| Data (S) | Digital Pin 2 |

#### LDR (divisor de tensão)
| Conexão | Descrição |
|---------|-----------|
| LDR leg 1 | 5V |
| LDR leg 2 | A0 + 10kΩ |
| 10kΩ outro lado | GND |

### 3. Inserir o código
1. Clique em **Code → Text**
2. Apague todo o código existente
3. Cole o conteúdo do arquivo `sketch.ino`
4. Clique em **Start Simulation**

### 4. Testar
- Abra o **Serial Monitor** (botão na parte inferior da tela)
- Aguarde ~2 segundos para a primeira leitura
- Valores aparecem no formato: `temperatura,umidade,luminosidade`
- Clique no LDR/DHT11 durante a simulação para ajustar os sliders

### 5. Salvar e compartilhar
- Dê um nome ao projeto (ex: "PI-V-B Monitoramento")
- Copie o link público da simulação para incluir no relatório
- Tire um screenshot do circuito montado e da Serial Monitor com dados

## Pinagem

```
Arduino UNO
├── Pin 2    → DHT11 Data
├── Pin A0   → LDR (divisor de tensão com 10kΩ toward GND)
├── 5V       → DHT11 VCC + LDR leg1
└── GND      → DHT11 GND + resistor 10kΩ
```
```
</action>

<acceptance_criteria>
- Arquivo `arduino/README.md` existe
- Contém tabela de conexões do DHT11 (VCC, GND, Data → Pin 2)
- Contém descrição do divisor de tensão do LDR (LDR + 10kΩ → A0)
- Contém instruções de como abrir o Serial Monitor no Tinkercad
- Contém instrução para copiar o link público da simulação
</acceptance_criteria>

## Verification

**must_haves:**
- [ ] `arduino/sketch.ino` existe e compila sem erros no Tinkercad
- [ ] Serial Monitor Tinkercad exibe linhas no formato `NN,NN,NNN` a cada 2 segundos
- [ ] Ajustar slider do LDR muda o valor de luminosidade na saída serial
- [ ] `arduino/README.md` documenta conexões e como usar no Tinkercad
