# Diagrama de Classes UML — Sistema de Monitoramento de Ambientes

> Projeto Integrador V-B — PUC Goiás — ADS 2026

## Diagrama (PlantUML / Texto)

```plantuml
@startuml MonitoramentoAmbiental

skinparam classBackgroundColor #1e1e2e
skinparam classBorderColor #89b4fa
skinparam classArrowColor #cdd6f4
skinparam classFontColor #cdd6f4
skinparam backgroundColor #181825

package "br.pucgoias.monitoramento" {

  package "model" {
    class SensorData {
      - temperatura : int
      - umidade : int
      - luminosidade : int
      - timestamp : long
      --
      + SensorData(temperatura, umidade, luminosidade)
      + getTemperatura() : int
      + getUmidade() : int
      + getLuminosidade() : int
      + getTimestamp() : long
      + getLuminosidadeDescricao() : String
      + {static} fromCsv(linha : String) : SensorData
      + toString() : String
    }
  }

  package "service" {
    class SerialService {
      - porta : SerialPort
      - threadLeitura : Thread
      - rodando : boolean
      - dadosListeners : List<Consumer<SensorData>>
      - statusListeners : List<Consumer<String>>
      --
      + addDadosListener(listener : Consumer<SensorData>) : void
      + addStatusListener(listener : Consumer<String>) : void
      + {static} listarPortas() : List<String>
      + conectar(nomePorta : String) : boolean
      + desconectar() : void
      + isConectado() : boolean
      - loopLeitura() : void
      - notificarDados(dados : SensorData) : void
      - notificarStatus(msg : String) : void
    }
  }

  package "view" {
    class MainView {
      - lblTempValor : JLabel
      - lblUmidValor : JLabel
      - lblLuxValor : JLabel
      - lblLuxDesc : JLabel
      - lblStatus : JLabel
      - cmbPortas : JComboBox<String>
      - btnConectar : JButton
      - onConectar : Consumer<String>
      - onDesconectar : Consumer<String>
      - onAtualizarPortas : Runnable
      --
      + MainView()
      + atualizarDados(dados : SensorData) : void
      + setStatus(msg : String) : void
      + setPortas(portas : List<String>) : void
      + setOnConectar(cb : Consumer<String>) : void
      + setOnDesconectar(cb : Consumer<String>) : void
      + setOnAtualizarPortas(cb : Runnable) : void
    }
  }

  package "controller" {
    class AppController {
      - serialService : SerialService
      - view : MainView
      --
      + AppController()
      + iniciar() : void
      - configurarCallbacks() : void
      - atualizarListaPortas() : void
      - formatarStatus(msg : String) : String
      + {static} main(args : String[]) : void
    }
  }

  ' Relacionamentos
  AppController --> SerialService : usa
  AppController --> MainView : controla
  SerialService ..> SensorData : cria / notifica
  MainView ..> SensorData : exibe
  AppController ..> SensorData : intermedia

}

note right of SerialService
  Executa leitura serial em
  background thread (daemon).
  Notifica UI via
  SwingUtilities.invokeLater()
end note

note right of SensorData
  fromCsv() parseia a linha
  "temp,umid,lux" do Arduino
  e retorna null se inválida
end note

@enduml
```

## Como gerar a imagem do diagrama

### Opção 1 — LucidChart (recomendado pelo professor)
1. Acesse https://www.lucidchart.com
2. Novo diagrama → **UML Class Diagram**
3. Adicione as 4 classes manualmente com base no diagrama acima
4. Exporte como PNG/SVG e salve em `uml/diagrama-classes.png`

### Opção 2 — yED Live
1. Acesse https://www.yworks.com/yed-live
2. Crie o diagrama de classes manualmente
3. Exporte e salve em `uml/diagrama-classes.png`

### Opção 3 — PlantUML online
1. Acesse https://www.plantuml.com/plantuml/uml/
2. Cole o código PlantUML acima (entre `@startuml` e `@enduml`)
3. Baixe a imagem gerada e salve em `uml/diagrama-classes.png`

## Descrição dos Relacionamentos

| Relação | Tipo | Descrição |
|---------|------|-----------|
| AppController → SerialService | Associação | Controller instancia e usa o SerialService |
| AppController → MainView | Associação | Controller instancia e configura a View |
| SerialService ⇢ SensorData | Dependência | Cria objetos SensorData ao parsear CSV |
| MainView ⇢ SensorData | Dependência | Recebe e exibe dados do SensorData |

## Checklist de entrega UML

- [ ] Diagrama gerado no LucidChart ou yED
- [ ] 4 classes presentes: `SensorData`, `SerialService`, `MainView`, `AppController`
- [ ] Atributos e métodos listados em cada classe
- [ ] Relacionamentos/associações representados com setas
- [ ] Imagem exportada como PNG salva em `uml/diagrama-classes.png`
- [ ] Link do diagrama online salvo em `uml/LINK.md`
