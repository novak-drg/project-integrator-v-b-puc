# Architecture Research — Sistema Arduino + Java

## Visão Geral

```
┌─────────────────────────────┐     Serial (USB/Virtual)      ┌──────────────────────────────────┐
│         ARDUINO UNO         │ ──────────────────────────→  │          JAVA APPLICATION         │
│                             │   CSV: temp,umid,lux\n       │                                   │
│  DHT11 → temperatura        │                               │  SerialService (jSerialComm)      │
│  DHT11 → umidade            │                               │       ↓ Observer/Callback         │
│  LDR   → luminosidade       │                               │  SensorDataModel (POJO)          │
│                             │                               │       ↓ SwingUtilities.invokeLater│
└─────────────────────────────┘                               │  MainView (JFrame/Painel)        │
                                                              └──────────────────────────────────┘
```

## Componentes Principais

### 1. Arduino Sketch (`sketch.ino`)
- **Responsabilidade:** Leitura dos sensores e transmissão serial
- **Fluxo:** `setup()` → inicializa Serial + DHT11 → `loop()` → lê sensores cada 2s → imprime CSV
- **Saída:** `25.3,60.1,512\n` (temperatura °C, umidade %, valor analógico LDR 0-1023)

### 2. `SerialService.java`
- **Responsabilidade:** Conexão/desconexão da porta serial, leitura de dados em background thread
- **Usa:** jSerialComm
- **Padrão:** Observer — notifica listeners quando dado completo chega
- **Thread safety:** leitura em thread separada, notificação via `SwingUtilities.invokeLater()`

### 3. `SensorData.java` (Model)
- **Responsabilidade:** POJO com campos `temperature`, `humidity`, `luminosity`
- **Sem lógica** — apenas dados
```java
public class SensorData {
    private double temperature;
    private double humidity;
    private int luminosity;
}
```

### 4. `MainView.java` (View)
- **Responsabilidade:** JFrame principal com painel de exibição dos 3 sensores
- **Componentes:** JLabels para cada valor, painel de status de conexão
- **Atualização:** recebe callbacks do SerialService, atualiza labels na EDT

### 5. `AppController.java` (Controller)
- **Responsabilidade:** Inicializa componentes, conecta SerialService → MainView

---

## Diagrama de Classes UML (estrutura mínima)

```
┌──────────────────┐        ┌───────────────────┐        ┌──────────────────┐
│   SerialService   │──────▶│    SensorData      │◀──────│    MainView       │
│ +connect(port)   │  notif │ -temperature:double│  usa  │ +update(data)    │
│ +disconnect()    │        │ -humidity:double   │        │ -lblTemp:JLabel  │
│ +addListener()   │        │ -luminosity:int    │        │ -lblUmid:JLabel  │
└──────────────────┘        └───────────────────┘        │ -lblLux:JLabel   │
        ▲                                                 └──────────────────┘
        │                   ┌───────────────────┐
        │                   │   AppController   │
        └───────────────────│ +main()           │
                            │ -service          │
                            │ -view             │
                            └───────────────────┘
```

---

## Ordem de Build

1. **Circuito Tinkercad** — base de tudo (valida sensores)
2. **Código Arduino** — gera o stream de dados
3. **Classes Java** (SensorData → SerialService → AppController → MainView)
4. **UML** — derivado das classes Java implementadas
5. **Protótipo UI** — mockup da MainView no Figma/QuantUX
6. **Documentação** — relatório final + referencial

---

## Considerações de Plataforma
- Windows: porta `COM3`, `COM4`, etc.
- Não hardcodar a porta — listar portas disponíveis ou permitir configuração
- Para o Tinkercad (simulação): comunicação via Serial Monitor interno, não serial real
