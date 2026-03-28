---
wave: 2
depends_on: [01-PLAN.md]
files_modified:
  - arduino/sketch.ino
  - .planning/STATE.md
requirements_addressed: [ARD-01, ARD-02, ARD-03]
autonomous: false
---

# PLAN-1.2 — Circuito Tinkercad (Tarefa Manual)

## Objective
Montar o circuito Arduino UNO + DHT11 + LDR no Tinkercad, executar a simulação e capturar os artefatos necessários para a entrega (screenshot + link público).

> ⚠️ **Esta fase requer ação manual do usuário no Tinkercad (navegador).**
> O agente pode guiar, mas não pode acessar o Tinkercad diretamente.

## Context

<read_first>
- arduino/README.md
- arduino/sketch.ino
- .planning/phases/01-arduino-tinkercad/01-RESEARCH.md
</read_first>

## Tasks

### Task 1.2.1 — Montar circuito no Tinkercad

<action>
Seguir as instruções do arquivo `arduino/README.md` para montar o circuito. Resumo:

1. Acessar https://www.tinkercad.com → Login → Circuits → Create new Circuit
2. Adicionar componentes: Arduino UNO, DHT11 (ou Temperature Sensor), Photoresistor, Resistor 10kΩ, Breadboard
3. Fazer as conexões: DHT11 (VCC→5V, GND→GND, Data→Pin2), LDR (leg1→5V, leg2→A0 + 10kΩ→GND)
4. Clicar em Code → Text → Colar conteúdo de `arduino/sketch.ino`
5. Clicar em Start Simulation
6. Abrir Serial Monitor — verificar saída CSV
</action>

<acceptance_criteria>
- Simulação inicia sem erros de compilação no Tinkercad
- Serial Monitor exibe pelo menos 3 linhas no formato `NN,NN,NNN`
- Ajustar slider do LDR muda o terceiro valor
- Ajustar slider de temperatura do DHT11 muda o primeiro valor
</acceptance_criteria>

---

### Task 1.2.2 — Capturar artefatos de entrega

<action>
Com a simulação rodando:

1. **Screenshot do circuito:** Tire print da tela mostrando o circuito montado (Arduino + sensores + breadboard)
2. **Screenshot do Serial Monitor:** Tire print com pelo menos 5 leituras visíveis no monitor serial
3. **Nome do projeto:** Renomear o projeto no Tinkercad para "PI-V-B — Monitoramento Ambiental"
4. **Link público:** Compartilhar → copiar URL pública da simulação
5. **Salvar os screenshots** na pasta `arduino/screenshots/` do projeto

Salvar o link no arquivo `arduino/TINKERCAD_LINK.md`:
```markdown
# Link Tinkercad

**Projeto:** PI-V-B — Monitoramento Ambiental
**Link público:** [URL_AQUI]
**Data:** 2026-03-XX

## Screenshots
- `screenshots/circuito.png` — Circuito montado
- `screenshots/serial-monitor.png` — Serial Monitor com leituras
```
</action>

<acceptance_criteria>
- Arquivo `arduino/TINKERCAD_LINK.md` existe e contém URL do Tinkercad
- Pasta `arduino/screenshots/` existe com ao menos 2 imagens
- Screenshots mostram circuito completo e Serial Monitor com dados
</acceptance_criteria>

## Verification

**must_haves:**
- [ ] Circuito no Tinkercad está funcional (simulação roda sem erro)
- [ ] Serial Monitor exibe dados no formato `temperatura,umidade,luminosidade`
- [ ] Screenshot do circuito capturado
- [ ] Screenshot do Serial Monitor capturado
- [ ] Link público do Tinkercad disponível em `arduino/TINKERCAD_LINK.md`
