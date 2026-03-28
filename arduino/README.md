# Arduino — Sketch de Monitoramento Ambiental

## Como usar no Tinkercad

### 1. Criar o circuito

1. Acesse [https://www.tinkercad.com](https://www.tinkercad.com) e faça login
2. Clique em **Circuits → Create new Circuit**
3. Adicione os componentes da tabela abaixo ao breadboard:

| Componente | Buscar por no Tinkercad | Qtd |
|-----------|------------------------|-----|
| Arduino UNO R3 | "Arduino UNO" | 1 |
| Sensor de temp/umidade | "Temperature Sensor" ou "DHT11" | 1 |
| Fotoresistor | "Photoresistor" | 1 |
| Resistor 10kΩ | "Resistor" (ajustar para 10kΩ) | 1 |
| Breadboard | "Breadboard Small" | 1 |

---

### 2. Conexões

#### Sensor DHT11 / Temperature Sensor
| Pino no sensor | Conectar em |
|---------------|-------------|
| VCC (+) | Arduino 5V |
| GND (–) | Arduino GND |
| Data (S) | Arduino Digital Pin **2** |

#### LDR (divisor de tensão)
```
5V ──── [LDR] ──── A0 ──── [Resistor 10kΩ] ──── GND
```
| Conexão | Destino |
|---------|---------|
| LDR leg 1 | Arduino 5V |
| LDR leg 2 | Arduino A0 **e** um lado do resistor 10kΩ |
| Resistor 10kΩ outro lado | Arduino GND |

---

### 3. Inserir o código

1. Clique em **Code** (botão no topo)
2. Selecione modo **Text** (não Blocks)
3. Apague todo o código existente
4. Cole o conteúdo completo do arquivo `sketch.ino`
5. Clique em **Start Simulation**

---

### 4. Testar a simulação

- Clique em **Serial Monitor** (aparece na parte inferior)
- Aguarde ~2 segundos para a primeira leitura aparecer
- Valores no formato: `temperatura,umidade,luminosidade`

**Interagir com os sensores durante a simulação:**
- Clique no **fotoresistor** → surge um slider de luz → ajuste e veja o 3º valor mudar
- Clique no **sensor DHT11** → sliders de temperatura e umidade → ajuste e veja os 2 primeiros valores mudarem

---

### 5. Salvar e compartilhar

1. Dê nome ao projeto: **"PI-V-B — Monitoramento Ambiental"**
2. Clique em **Share** → **Copy Link**
3. Cole o link no arquivo `arduino/TINKERCAD_LINK.md`
4. Tire screenshots:
   - **circuito.png** — tela mostrando o circuito montado completo
   - **serial-monitor.png** — Serial Monitor com pelo menos 5 leituras visíveis

Salve os screenshots em: `arduino/screenshots/`

---

## Pinagem resumida

```
Arduino UNO
├── Pin 2    → DHT11 / Temperature Sensor (Data)
├── Pin A0   → LDR (leg2) + Resistor 10kΩ (para GND)
├── 5V       → DHT11 VCC + LDR (leg1)
└── GND      → DHT11 GND + Resistor 10kΩ (outro lado)
```

## Saída esperada no Serial Monitor

```
SISTEMA,INICIADO,0
25,60,512
25,60,510
26,61,498
...
```

> Linha `SISTEMA,INICIADO,0` é o heartbeat de boot — o módulo Java ignora linhas que não têm 3 campos numéricos.
