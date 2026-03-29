# Roteiro de Apresentação — PI-V-B
## Sistema de Monitoramento de Ambientes Inteligentes

**Tempo total estimado:** 8–12 minutos  
**Data da entrega:** 07/04/2026  
**Disciplina:** Projeto Integrador V – B | Prof. Thalles Bruno G. N. dos Santos

---

## 🎬 ATO 1 — Contexto e Problema (≈ 1–2 min)

> "Boa tarde, professor. Meu projeto é o Sistema de Monitoramento de Ambientes Inteligentes."

**O que falar:**
- Apresentar o cenário do Marcos: estudante de ADS que herdou uma casa antiga e quer torná-la inteligente
- Problema: como monitorar temperatura, umidade e luminosidade de forma automática e de baixo custo?
- Solução escolhida: **Arduino UNO + Java**, integrados via comunicação serial

---

## 🔌 ATO 2 — Circuito Arduino no Tinkercad (≈ 2 min)

> "Vou mostrar o circuito simulado no Tinkercad."

**Abrir:** https://www.tinkercad.com/things/eALbQIdph4d-pi-v-b-monitoramento-ambiental

**O que demonstrar e falar:**
1. Mostrar o circuito na tela e apontar os componentes:
   - **TMP36** → lê temperatura em graus Celsius via pino analógico A1
   - **LDR + Resistor 10kΩ** → divisor de tensão, lê luminosidade (0–1023) via A0
   - **Potenciômetro** → simula umidade relativa (0–100%) via A2
2. Clicar em **"Iniciar Simulação"**
3. Abrir o **Monitor Serial** e mostrar os dados aparecendo:
   ```
   24,38,81
   25,40,512
   ```
4. Girar o potenciômetro e mostrar que o valor de umidade muda na saída serial
5. Falar: _"O protocolo é simples: CSV com newline, a cada 2 segundos, a 9600 bps"_

---

## ☕ ATO 3 — Aplicação Java + Demo ao Vivo (≈ 3 min)

> "Essa é a aplicação Java que recebe esses dados e exibe em tempo real."

**Abrir o terminal e rodar:**
```powershell
Set-Location -LiteralPath "C:\Users\novak\Desktop\[Faculty]\PI-V-B"
java -jar java-app/target/monitoramento-ambiental.jar
```

**O que demonstrar e falar:**
1. A janela abre com tema escuro (FlatLaf Dark) — 3 cards: Temperatura, Umidade, Luminosidade
2. Explicar o modo DEMO (dados simulados automáticos):
   - _"Como não tenho Arduino físico, implementei um modo demo que gera dados aleatórios realistas"_
3. Selecionar uma porta COM (se disponível) ou mostrar o modo demo funcionando
4. Apontar os cards mudando em tempo real
5. Mencionar: _"Toda a atualização de tela usa `SwingUtilities.invokeLater()` — thread safety correto para Swing"_

---

## 📐 ATO 4 — Arquitetura e UML (≈ 2 min)

> "O sistema segue o padrão MVC. Posso mostrar o diagrama de classes."

**Abrir:** `uml/diagrama-classes.html` no navegador

**O que falar:**
- **SensorData** (Model): POJO com temperatura, umidade, luminosidade + método `fromCsv()` para parsing
- **SerialService** (Service): thread em background, padrão Observer com `Consumer<SensorData>`
- **MainView** (View): interface Swing, apenas exibe — não tem lógica de negócio
- **AppController** (Controller): `main()`, conecta tudo via callbacks
- Mostrar a seta de associação (AppController → SerialService e MainView) vs dependência pontilhada (→ SensorData)

---

## 🏁 ATO 5 — Repositório e Conclusão (≈ 1 min)

> "O código está no GitHub e o relatório está disponível."

**Mostrar:**
- GitHub: https://github.com/novak-drg/project-integrator-v-b-puc
- Commits organizados por fase (fase 1, fase 2, fase 3, final)

**Conclusão:**
> _"O projeto cumpre todos os requisitos: circuito Arduino simulado, módulo Java funcional, diagrama UML, protótipo de interface e repositório GitHub. A solução é de baixo custo, utilizando apenas hardware acessível e tecnologias Java amplamente adotadas na indústria."_

---

## ❓ Perguntas Frequentes — Respostas Prontas

| Pergunta provável | Resposta |
|-------------------|----------|
| _"Por que usou TMP36 em vez do DHT11?"_ | Tinkercad não suporta DHT11 nativamente; TMP36 é suportado e cobre o requisito de temperatura |
| _"Por que jSerialComm e não RXTX?"_ | RXTX é deprecated e requer instalação nativa; jSerialComm é cross-platform e sem dependências |
| _"O que é FlatLaf?"_ | Look & Feel moderno para Swing — mantido ativamente, substitui o Nimbus |
| _"O modo demo não é trapaca?"_ | Não — é uma prática padrão. O código funciona com hardware real na mesma porta COM |
| _"Por que Maven?"_ | Gerenciamento de dependências automático; o JAR final inclui todas as libs |
| _"O que é padrão Observer?"_ | SerialService notifica MainView via `Consumer<SensorData>` sem acoplamento direto |

---

## 📋 Checklist pré-apresentação

- [ ] Abrir o Tinkercad com antecedência (pode demorar para carregar)
- [ ] Testar o JAR: `java -jar java-app/target/monitoramento-ambiental.jar`
- [ ] Abrir `uml/diagrama-classes.html` no Chrome/Edge
- [ ] Abrir GitHub no navegador
- [ ] Ter o relatório PDF pronto para mostrar se pedido
- [ ] Preencher seu **nome** e **matrícula** na capa do relatório antes de imprimir

---

## 🎯 Pontuação alvo

| Critério | Pts | Status |
|---------|-----|--------|
| Esquema Arduino (Tinkercad) | 1,5 | ✅ Link + screenshot |
| Módulo Java + UML + repo | 1,5 | ✅ JAR + GitHub + diagrama |
| Protótipo UI | 1,0 | ✅ Imagem + descrição |
| **Apresentação** | **3,0** | 🎯 Este roteiro |
| Documentação/Relatório | 1,0 | ✅ RELATORIO.html pronto |
| Referencial Teórico | 1,0 | ✅ 6 referências |
| **Total** | **9,0/10** | |
