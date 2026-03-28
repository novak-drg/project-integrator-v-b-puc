# Sistema de Monitoramento de Ambientes Inteligentes

## What This Is

Sistema de monitoramento de ambientes inteligentes desenvolvido como Projeto Integrador V-B (PUC Goiás — ADS). O projeto simula o cenário de Marcos, estudante que transformou sua residência em um ambiente inteligente monitorando temperatura, umidade e luminosidade usando Arduino UNO e Java.

## Core Value

**O sistema deve coletar dados de sensores (DHT11/LDR/Arduino) e exibi-los em tempo real numa interface Java intuitiva**, com diagrama UML e protótipo visual de UI — pronto para apresentação acadêmica até 07/04/2026.

## Context

- **Curso:** Análise e Desenvolvimento de Sistemas — PUC Goiás (EaD/CEAD)
- **Disciplina:** Projeto Integrador V – B
- **Professor:** Thalles Bruno G. N. dos Santos
- **Prazo final:** 07/04/2026 às 23:59
- **Apresentação:** 28/03/2026 (aula síncrona — hoje)
- **Nota máxima:** 10 pontos

## Stakeholders

- **Marcos (usuário fictício):** morador que quer monitorar temperatura, umidade e luminosidade de forma intuitiva e de baixo custo
- **Professor/Banca:** avaliará os entregáveis técnicos e a apresentação

## Tech Stack

| Camada | Tecnologia | Justificativa |
|--------|-----------|---------------|
| Hardware | Arduino UNO | Baixo custo, fácil prototipagem |
| Sensores | DHT11 (temp/umidade) + LDR (luminosidade) | Padrão do mercado, fácil integração |
| Simulação | Tinkercad | Plataforma indicada pelo professor |
| Backend / Leitura | Java (módulo de leitura serial) | Linguagem robusta exigida pelo enunciado |
| Diagrama UML | LucidChart ou yED Live | Ferramentas indicadas |
| Protótipo UI | Figma ou QuantUX | Ferramentas indicadas |
| Versionamento | Git/GitHub | Entrega requer link de repositório |

## Requirements

### Validated

(Nenhum ainda — greenfield)

### Active

- [ ] Esquema ilustrativo do protótipo Arduino UNO com DHT11 e LDR no Tinkercad
- [ ] Módulo Java funcional para leitura e exibição dos dados coletados
- [ ] Diagrama de classes UML descrevendo os componentes de software
- [ ] Protótipo de interface visual (UI) — screenshot + link Figma ou QuantUX
- [ ] Repositório Git com o código-fonte Java
- [ ] Relatório/documentação do projeto
- [ ] Referencial teórico

### Out of Scope

- Integração real com hardware físico — apenas simulação no Tinkercad
- Backend com banco de dados persistente — dados em memória/display são suficientes
- Deploy em produção — entrega acadêmica local/simulada

## Key Decisions

| Decisão | Rationale | Resultado |
|---------|-----------|-----------|
| Usar Tinkercad para Arduino | Indicado pelo professor, sem necessidade de hardware real | Pendente |
| Java para leitura de dados | Exigido pelo enunciado | Pendente |
| Interface Java Swing ou JavaFX | Exibição em tempo real dos dados dos sensores | Pendente |
| QuantUX ou Figma para UI prototype | Indicado pelo professor | Pendente |

## Deliverables (Critérios de Avaliação)

| Entregável | Pontos | Formato |
|------------|--------|---------|
| Esquema do protótipo Arduino | 1,5 | Screenshot + link Tinkercad |
| Módulo Java (código + UML + repo) | 1,5 | Código-fonte + diagrama + link GitHub |
| Protótipo de UI | 1,0 | Screenshot + link Figma/QuantUX |
| Apresentação | 3,0 | Aula síncrona |
| Documentação e relatório final | 1,0 | Formatação ABNT-like (Arial 12, espaço simples) |
| Referencial teórico | 1,0 | Mínimo 3 referências técnicas relevantes |

## Constraints

- **Prazo:** 07/04/2026 (≈ 10 dias a partir de hoje)
- **Formato:** Arial 12pt, espaço simples, margens 2,5cm superior/inferior, 3cm laterais
- **Ferramentas:** Tinkercad (Arduino), LucidChart/yED (UML), QuantUX/Figma (UI), Java (backend)

## Evolution

Este documento evolui a cada transição de fase.

**Após cada transição de fase** (via `/gsd-transition`):
1. Requirements invalidados? → Mover para Out of Scope com motivo
2. Requirements validados? → Mover para Validated com referência de fase
3. Novos requirements surgiram? → Adicionar em Active
4. Decisões a registrar? → Adicionar em Key Decisions
5. "What This Is" ainda é preciso? → Atualizar se necessário

**Após cada milestone** (via `/gsd-complete-milestone`):
1. Revisão completa de todas as seções
2. Core Value check — ainda a prioridade certa?
3. Auditar Out of Scope — motivos ainda válidos?
4. Atualizar Context com estado atual

---
*Last updated: 2026-03-28 após inicialização*
