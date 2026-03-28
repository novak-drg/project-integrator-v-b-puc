# ROADMAP — Sistema de Monitoramento de Ambientes Inteligentes
# Milestone 1 — Entrega Acadêmica PI V-B (prazo: 07/04/2026)

## Overview

**3 fases** | **13 requirements mapeados** | Todos os requirements v1 cobertos ✓

| # | Fase | Goal | Requirements | Prazo Sugerido |
|---|------|------|-------------|----------------|
| 1 | Arduino + Tinkercad | Circuito simulado funcional com sensores e serial output | ARD-01, ARD-02, ARD-03 | 30/03 |
| 2 | Módulo Java + UML | App Java lendo dados e exibindo em tempo real + diagrama UML | JAVA-01, JAVA-02, JAVA-03, JAVA-04, UML-01, UML-02 | 04/04 |
| 3 | UI Prototype + Documentação | Protótipo visual + relatório final + referencial + GitHub | UI-01, UI-02, DOC-01, DOC-02, DOC-03 | 07/04 |

---

## Phase 1: Arduino + Tinkercad

**Goal:** Circuito Arduino UNO funcional no Tinkercad com DHT11 (temperatura/umidade) e LDR (luminosidade), transmitindo dados em formato CSV via Serial Monitor.

**Requirements:**
- ARD-01: Esquema do circuito no Tinkercad (screenshot + link)
- ARD-02: Código Arduino transmitindo `temperatura,umidade,luminosidade\n` via Serial a cada 2 segundos
- ARD-03: Simulação interativa — sliders de temperatura, umidade e luminosidade funcionando

**Success Criteria:**
1. Circuito no Tinkercad com Arduino UNO, DHT11, LDR e resistor 10kΩ é visível e salvo com link público
2. Ao iniciar a simulação, o Serial Monitor exibe linhas no formato `25.00,60.00,512` a cada ~2 segundos
3. Alterando o slider do LDR/DHT11 no Tinkercad, os valores no Serial Monitor mudam correspondentemente

**Plans:**
- PLAN-1.1: Montar circuito no Tinkercad (Arduino + DHT11 + LDR + resistor)
- PLAN-1.2: Escrever e testar código Arduino (leitura dos 3 sensores + output serial CSV)

**UI hint:** no

---

## Phase 2: Módulo Java + UML

**Goal:** Aplicação Java que recebe os dados seriais do Arduino (ou simula recepção), exibe temperatura, umidade e luminosidade em interface gráfica Swing em tempo real, com código organizado em padrão MVC e diagrama UML fiel à implementação.

**Requirements:**
- JAVA-01: Leitura serial com jSerialComm
- JAVA-02: Exibição em tempo real na UI Swing
- JAVA-03: Status de conexão + reconexão sem reiniciar
- JAVA-04: JAR standalone executável
- UML-01: Diagrama de classes com 4+ classes e relacionamentos
- UML-02: Diagrama disponível como imagem + link LucidChart/yED

**Success Criteria:**
1. `mvn clean package` (ou build equivalente) gera um JAR executável que abre a janela Swing
2. Com Arduino/Tinkercad conectado (ou modo simulação), os 3 valores atualizam a cada ~2 segundos na tela
3. Painel de status indica "Conectado" / "Desconectado" e botão de reconexão funciona sem reiniciar o app
4. Diagrama de Classes UML mostra ao menos `SensorData`, `SerialService`, `MainView`, `AppController` com atributos e métodos

**Plans:**
- PLAN-2.1: Setup do projeto Java (estrutura Maven, dependências: jSerialComm + FlatLaf)
- PLAN-2.2: Implementar `SensorData` (model) e `SerialService` (leitura serial + parser CSV)
- PLAN-2.3: Implementar `MainView` (Swing UI com JLabels para cada sensor + status bar)
- PLAN-2.4: Implementar `AppController` + modo de simulação (para demo sem hardware)
- PLAN-2.5: Gerar diagrama UML de classes (LucidChart ou yED) + exportar imagem

**UI hint:** yes

---

## Phase 3: UI Prototype + Documentação

**Goal:** Protótipo visual da interface no Figma/QuantUX representando o painel de monitoramento, mais todo o conjunto documental: README no GitHub, relatório final formatado e referencial teórico.

**Requirements:**
- UI-01: Protótipo de UI no Figma/QuantUX
- UI-02: Screenshot + link público do protótipo
- DOC-01: Repositório GitHub com código + README
- DOC-02: Relatório final ABNT-like
- DOC-03: Referencial teórico (≥3 referências)

**Success Criteria:**
1. Link do protótipo no Figma/QuantUX está público e mostra painel com temperatura, umidade e luminosidade
2. Repositório GitHub está público, contém todo o código Java e README com instruções de execução
3. Relatório final (PDF) tem capa, introdução, motivação, objetivos, metodologia, resultados, conclusão e referências — formatado em Arial 12pt, espaço simples, margens 2,5cm/3cm
4. Referencial teórico tem ao menos 3 referências em formato ABNT

**Plans:**
- PLAN-3.1: Criar protótipo no Figma/QuantUX (painel de monitoramento) + capturar screenshot + obter link público
- PLAN-3.2: Publicar repositório GitHub + escrever README completo
- PLAN-3.3: Escrever relatório final (todas as seções) + referencial teórico

**UI hint:** yes

---

## Milestone Summary

**Entrega:** 07/04/2026 às 23:59
**Critérios por pontuação:**
- Arduino Prototype Schema: 1,5 pts → Fase 1
- Java Module: 1,5 pts → Fase 2
- UI Prototype: 1,0 pt → Fase 3
- Apresentação: 3,0 pts → Preparar ao longo das 3 fases
- Documentação/Relatório: 1,0 pt → Fase 3
- Referencial Teórico: 1,0 pt → Fase 3

---

*Gerado em: 2026-03-28 | Milestone 1*
