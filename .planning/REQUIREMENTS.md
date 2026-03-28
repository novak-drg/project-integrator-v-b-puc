# REQUIREMENTS — Sistema de Monitoramento de Ambientes Inteligentes

## v1 Requirements

### ARDUINO — Prototipagem e Simulação
- [ ] **ARD-01**: Usuário pode visualizar o esquema do circuito Arduino UNO com DHT11 e LDR funcional no Tinkercad (screenshot + link)
- [ ] **ARD-02**: Código Arduino transmite leituras de temperatura (°C), umidade (%) e luminosidade (0–1023) via Serial em formato CSV a cada 2 segundos
- [ ] **ARD-03**: Simulação no Tinkercad permite ajustar temperatura, umidade e luminosidade interativamente durante a demo

### JAVA — Módulo de Processamento e Exibição
- [ ] **JAVA-01**: Módulo Java conecta-se à porta serial (COM ou virtual) e lê os dados enviados pelo Arduino usando jSerialComm
- [ ] **JAVA-02**: Módulo Java exibe os 3 valores (temperatura, umidade, luminosidade) em tempo real em uma interface gráfica Swing
- [ ] **JAVA-03**: Interface Java mostra status de conexão serial (conectado/desconectado) e permite reconexão sem reiniciar o app
- [ ] **JAVA-04**: Interface Java é executável como arquivo JAR standalone (sem dependências externas além do jSerialComm empacotado)

### UML — Documentação Técnica
- [ ] **UML-01**: Diagrama de Classes UML inclui ao menos 4 classes: `SensorData`, `SerialService`, `MainView`, `AppController` com atributos, métodos e relacionamentos
- [ ] **UML-02**: Diagrama UML está disponível em formato de imagem (PNG/SVG) e via link LucidChart ou yED

### UI — Protótipo Visual
- [ ] **UI-01**: Protótipo de interface visual no Figma ou QuantUX mostra painel de monitoramento com temperatura, umidade e luminosidade
- [ ] **UI-02**: Protótipo disponível via screenshot + link público (Figma/QuantUX)

### DOC — Documentação e Entrega
- [ ] **DOC-01**: Repositório GitHub público contém código-fonte Java organizado em pacotes, com README explicando como executar
- [ ] **DOC-02**: Relatório final no formato ABNT-like (Arial 12pt, espaço simples, margens 2,5cm/3cm) com introdução, motivação, objetivos, metodologia e conclusão
- [ ] **DOC-03**: Referencial teórico contém ao menos 3 referências técnicas relevantes (IoT, Arduino, monitoramento ambiental, Java)

---

## v2 Requirements (deferred)

- Gráfico histórico de leituras (LineChart com JavaFX ou JFreeChart)
- Alertas visuais por threshold (ex: temperatura > 30°C → ícone vermelho)
- Exportação de log para CSV
- Seleção dinâmica de porta COM via JComboBox na UI
- Múltiplos ambientes / cômodos
- Banco de dados persistente

---

## Out of Scope

- Hardware físico real — sem necessidade, Tinkercad é suficiente para avaliação
- Integração IoT (MQTT, cloud, broker) — muito além do escopo do PI V-B
- App mobile — não solicitado
- Autenticação / login — desnecessário para monitoramento local
- Deploy em produção / servidor — entrega é local/acadêmica

---

## Traceability

| Fase | Requirements cobertos |
|------|-----------------------|
| Fase 1: Arduino + Tinkercad | ARD-01, ARD-02, ARD-03 |
| Fase 2: Módulo Java | JAVA-01, JAVA-02, JAVA-03, JAVA-04, UML-01, UML-02 |
| Fase 3: UI Prototype + Docs | UI-01, UI-02, DOC-01, DOC-02, DOC-03 |

---

*Gerado em: 2026-03-28 | v1*
