<!-- GSD:project-start source:PROJECT.md -->
## Project

**Sistema de Monitoramento de Ambientes Inteligentes**

Sistema de monitoramento de ambientes inteligentes desenvolvido como Projeto Integrador V-B (PUC Goiás — ADS). O projeto simula o cenário de Marcos, estudante que transformou sua residência em um ambiente inteligente monitorando temperatura, umidade e luminosidade usando Arduino UNO e Java.

**Core Value:** **O sistema deve coletar dados de sensores (DHT11/LDR/Arduino) e exibi-los em tempo real numa interface Java intuitiva**, com diagrama UML e protótipo visual de UI — pronto para apresentação acadêmica até 07/04/2026.

### Constraints

- **Prazo:** 07/04/2026 (≈ 10 dias a partir de hoje)
- **Formato:** Arial 12pt, espaço simples, margens 2,5cm superior/inferior, 3cm laterais
- **Ferramentas:** Tinkercad (Arduino), LucidChart/yED (UML), QuantUX/Figma (UI), Java (backend)
<!-- GSD:project-end -->

<!-- GSD:stack-start source:research/STACK.md -->
## Technology Stack

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
## Ferramentas de Suporte (indicadas pelo professor)
| Ferramenta | Uso |
|-----------|-----|
| Tinkercad | Simulação do circuito Arduino |
| LucidChart / yED Live | Diagrama de Classes UML |
| Figma / QuantUX | Protótipo de UI |
| GitHub | Repositório do código-fonte Java |
## O que NÃO usar
- ❌ **RXTX** — deprecated, requer instalação nativa manual
- ❌ **JSSC** — pouco mantido em 2025
- ❌ **Banco de dados** — fora do escopo acadêmico
- ❌ **Spring Boot / frameworks pesados** — overkill para este projeto
<!-- GSD:stack-end -->

<!-- GSD:conventions-start source:CONVENTIONS.md -->
## Conventions

Conventions not yet established. Will populate as patterns emerge during development.
<!-- GSD:conventions-end -->

<!-- GSD:architecture-start source:ARCHITECTURE.md -->
## Architecture

Architecture not yet mapped. Follow existing patterns found in the codebase.
<!-- GSD:architecture-end -->

<!-- GSD:workflow-start source:GSD defaults -->
## GSD Workflow Enforcement

Before using Edit, Write, or other file-changing tools, start work through a GSD command so planning artifacts and execution context stay in sync.

Use these entry points:
- `/gsd-quick` for small fixes, doc updates, and ad-hoc tasks
- `/gsd-debug` for investigation and bug fixing
- `/gsd-execute-phase` for planned phase work

Do not make direct repo edits outside a GSD workflow unless the user explicitly asks to bypass it.
<!-- GSD:workflow-end -->



<!-- GSD:profile-start -->
## Developer Profile

> Profile not yet configured. Run `/gsd-profile-user` to generate your developer profile.
> This section is managed by `generate-claude-profile` -- do not edit manually.
<!-- GSD:profile-end -->
