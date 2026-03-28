# Módulo Java — Monitoramento de Ambientes Inteligentes

> Projeto Integrador V-B — PUC Goiás — ADS 2026  
> Professor: Thalles Bruno G. N. dos Santos

## Pré-requisitos

- Java 17+ (testado com Java 26)
- Maven 3.9+ **ou** usar o Maven portátil já configurado:
  `~/.local/maven/bin/mvn.cmd`

## Estrutura do Projeto

```
java-app/
├── pom.xml
└── src/main/java/br/pucgoias/monitoramento/
    ├── model/
    │   └── SensorData.java          ← POJO com dados dos sensores
    ├── service/
    │   └── SerialService.java       ← Comunicação serial (jSerialComm)
    ├── view/
    │   └── MainView.java            ← Interface Swing + FlatLaf Dark
    └── controller/
        └── AppController.java       ← Orquestrador + main()
```

## Como Compilar e Executar

### 1. Compilar (gerar JAR)

```powershell
# A partir da pasta java-app/
$mvn = "$env:USERPROFILE\.local\maven\bin\mvn.cmd"
& $mvn clean package
```

O JAR executável será gerado em:
```
java-app/target/monitoramento-ambiental.jar
```

### 2. Executar

```powershell
java -jar target/monitoramento-ambiental.jar
```

### 3. Usar com Arduino / Tinkercad

1. Com o Arduino conectado via USB (ou Tinkercad + serial virtual):
   - Abra a aplicação Java
   - No rodapé, selecione a porta COM (ex: `COM3`)
   - Clique em **Conectar**
   - Os valores atualizam a cada ~2 segundos

2. Para demo **sem hardware** (modo Tinkercad):
   - Apenas abra a aplicação — ela aguardará conexão
   - Pode conectar ao Tinkercad com extensão de serial virtual se disponível

## Dependências

| Biblioteca | Versão | Uso |
|-----------|--------|-----|
| jSerialComm | 2.11.0 | Comunicação serial cross-platform |
| FlatLaf | 3.4 | Look & Feel dark moderno para Swing |

## Arquitetura MVC

```
AppController (Controller)
    ├── SerialService (Model / Service)
    │       └── SensorData (Model / POJO)
    └── MainView (View / Swing)
```

**Fluxo de dados:**
```
Arduino → USB/Serial → SerialService (thread background)
                             ↓ SwingUtilities.invokeLater()
                         MainView (EDT — Event Dispatch Thread)
```

## Padrões Utilizados

- **MVC** — separação clara de responsabilidades
- **Observer** — `SerialService` notifica `MainView` via `Consumer<T>`
- **Thread Safety** — atualizações de UI sempre via `SwingUtilities.invokeLater()`
- **Daemon Thread** — thread de leitura não impede encerramento da JVM

## Protocolo Serial Esperado

O Arduino deve enviar dados no formato CSV via Serial (9600 bps):
```
temperatura,umidade,luminosidade\n
```
Exemplo: `25,60,512`

Linhas que começam com `SISTEMA`, `ERR` ou não têm 3 campos numéricos são ignoradas.
