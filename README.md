# Sistema de Monitoramento de Ambientes Inteligentes

> **Projeto Integrador V-B — PUC Goiás — ADS 2026**  
> Disciplina: Análise e Desenvolvimento de Sistemas  
> Professor: Thalles Bruno G. N. dos Santos

---

## 📋 Sobre o Projeto

Sistema de monitoramento que coleta dados de sensores (temperatura, umidade e luminosidade) via Arduino UNO e exibe em tempo real em uma interface Java, conforme proposta do cenário acadêmico de Marcos, estudante que transformou sua residência em um ambiente inteligente.

---

## 🗂️ Estrutura do Repositório

```
PI-V-B/
├── arduino/               ← Código Arduino + instruções Tinkercad
│   ├── sketch.ino         ← Sketch de leitura dos sensores
│   ├── README.md          ← Como montar o circuito no Tinkercad
│   └── TINKERCAD_LINK.md  ← Link da simulação no Tinkercad
│
├── java-app/              ← Módulo Java (Maven)
│   ├── pom.xml
│   ├── README.md          ← Como compilar e executar
│   └── src/main/java/br/pucgoias/monitoramento/
│       ├── model/         ← SensorData.java
│       ├── service/       ← SerialService.java
│       ├── view/          ← MainView.java
│       └── controller/    ← AppController.java (main)
│
├── uml/                   ← Diagrama de Classes UML
│   ├── DIAGRAMA-CLASSES.md ← Especificação + PlantUML
│   └── LINK.md            ← Link do diagrama online
│
├── ui-prototype/          ← Protótipo de interface visual
│   ├── LINK.md            ← Link do protótipo (Figma/QuantUX)
│   └── screenshots/       ← Screenshots do protótipo
│
└── docs/
    └── RELATORIO.md       ← Relatório técnico completo
```

---

## 🔧 Como Executar o Módulo Java

### Pré-requisito
- Java 17 ou superior instalado

### Compilar

```powershell
# A partir da pasta java-app/
$mvn = "$env:USERPROFILE\.local\maven\bin\mvn.cmd"
& $mvn clean package
```

### Executar

```powershell
java -jar java-app/target/monitoramento-ambiental.jar
```

### Usar com Tinkercad
1. Inicie a simulação no Tinkercad (ver link em `arduino/TINKERCAD_LINK.md`)
2. Abra a aplicação Java
3. Selecione a porta COM e clique em **Conectar**

---

## 🔌 Circuito Arduino

| Sensor | Pino | Leitura |
|--------|------|---------|
| DHT11 (temp/umidade) | Digital 2 | `int temperatura`, `int umidade` |
| LDR (luminosidade) | Analógico A0 | `int luminosidade` (0–1023) |

**Protocolo Serial:** `temperatura,umidade,luminosidade\n` @ 9600 bps

**Simulação:** [Tinkercad — PI-V-B Monitoramento Ambiental](arduino/TINKERCAD_LINK.md)

---

## 📐 Arquitetura

```
Arduino (Tinkercad)
    │ Serial CSV 9600bps
    ▼
SerialService.java  ─── background thread ──► notifica Consumer<SensorData>
    │                                               │
    │                                    SwingUtilities.invokeLater()
    │                                               │
    ▼                                               ▼
SensorData.java (POJO)                     MainView.java (Swing UI)
    │
AppController.java (main + orquestrador)
```

---

## 📝 Entregáveis

| Item | Arquivo / Link | Status |
|------|---------------|--------|
| Esquema Arduino + Tinkercad | `arduino/TINKERCAD_LINK.md` | ⏳ Aguardando link |
| Módulo Java (código) | `java-app/src/` | ✅ Implementado |
| Diagrama UML | `uml/LINK.md` | ⏳ Aguardando link |
| Protótipo UI | `ui-prototype/LINK.md` | ⏳ Aguardando link |
| Relatório final | `docs/RELATORIO.md` | ✅ Redigido |

---

## 📚 Referencial Teórico

- BANZI, M.; SHILOH, M. **Getting Started with Arduino**. 3. ed. O'Reilly, 2015.
- BLOCH, J. **Effective Java**. 3. ed. Addison-Wesley, 2018.
- MARGOLIS, M. **Arduino Cookbook**. 2. ed. O'Reilly, 2012.
- Fazecast. **jSerialComm**. https://github.com/Fazecast/jSerialComm
- Oracle. **Java Swing Tutorials**. https://docs.oracle.com/javase/tutorial/uiswing/
