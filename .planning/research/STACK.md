# Stack Research — Sistema de Monitoramento Arduino + Java

## Camada Arduino

| Componente | Escolha | Versão | Confiança |
|-----------|---------|--------|-----------|
| Placa | Arduino UNO R3 | — | ✅ Alta (exigido) |
| Sensor Temp/Umidade | DHT11 | — | ✅ Alta (exigido) |
| Sensor Luminosidade | LDR (fotoresistor) + resistor 10kΩ | — | ✅ Alta (exigido) |
| Simulação | Tinkercad | 2025 | ✅ Alta (indicado) |
| Biblioteca | DHT.h (Adafruit) | — | ✅ Alta |

### Pinagem recomendada
- DHT11: Pin Digital 2, VCC→5V, GND→GND
- LDR: Divisor de tensão com 10kΩ → Pin Analógico A0

### Protocolo de dados
- Formato: CSV com newline — `temperatura,umidade,luminosidade\n`
- Baud rate: 9600 bps (compatível com Tinkercad)

---

## Camada Java

| Componente | Escolha | Justificativa |
|-----------|---------|--------------|
| JDK | Java 17 ou 21 LTS | Versão estável recomendada 2025 |
| Comunicação Serial | **jSerialComm** (fazecast) | Substituto moderno do RXTX; cross-platform; sem instalação nativa |
| UI Framework | **JavaFX** (preferência) ou **Swing + FlatLaf** | JavaFX tem charts nativos; Swing é mais simples para projeto acadêmico |
| Build | Maven ou projeto simples com JAR | Maven preferível para gestão do jSerialComm |
| Padrão arquitetural | MVC | Model = dados do sensor; View = painel; Controller = leitura serial |

### Decisão UI
- **JavaFX** → mais moderno, charts nativos (`LineChart`), CSS, melhor para apresentação
- **Swing** → mais simples de configurar, suficiente para o escopo acadêmico
- **Recomendado:** Swing com **FlatLaf** (look moderno sem overhead de JavaFX)

---

## Ferramentas de Suporte (indicadas pelo professor)

| Ferramenta | Uso |
|-----------|-----|
| Tinkercad | Simulação do circuito Arduino |
| LucidChart / yED Live | Diagrama de Classes UML |
| Figma / QuantUX | Protótipo de UI |
| GitHub | Repositório do código-fonte Java |

---

## O que NÃO usar

- ❌ **RXTX** — deprecated, requer instalação nativa manual
- ❌ **JSSC** — pouco mantido em 2025
- ❌ **Banco de dados** — fora do escopo acadêmico
- ❌ **Spring Boot / frameworks pesados** — overkill para este projeto
