# Features Research — Sistema de Monitoramento de Ambientes

## Table Stakes (obrigatório — sem isso o projeto falha)

### Coleta de Dados
- [ ] Leitura de temperatura (DHT11)
- [ ] Leitura de umidade relativa do ar (DHT11)
- [ ] Leitura de luminosidade (LDR / fotoresistor)
- [ ] Transmissão serial Arduino → PC (formato CSV)
- [ ] Simulação funcional no Tinkercad com Serial Monitor

### Exibição Java
- [ ] Recebimento e parse dos dados seriais
- [ ] Exibição dos 3 valores em tempo real (atualização periódica)
- [ ] Interface gráfica com labels/campos para cada sensor
- [ ] Tratamento de erros de conexão (porta não encontrada)

### Documentação Acadêmica
- [ ] Diagrama de Classes UML (mínimo: SensorService, DataModel, MainView)
- [ ] Protótipo de UI (Figma ou QuantUX — screenshot + link)
- [ ] Esquema do circuito Tinkercad (screenshot + link)
- [ ] Repositório GitHub com código Java
- [ ] Relatório final (formatação ABNT-like)
- [ ] Referencial teórico (≥3 referências)

---

## Diferenciadores (agrega valor, não obrigatório)

- Gráfico histórico de leituras (LineChart)
- Indicador visual de alerta (ex: temperatura acima de 30°C → ícone vermelho)
- Seleção de porta COM na UI
- Exportação de log para CSV
- Timestamps nas leituras

---

## Anti-Features (não construir)

- ❌ Banco de dados — fora do escopo
- ❌ Integração com IoT real / MQTT / cloud — fora do escopo
- ❌ App mobile — fora do escopo
- ❌ Múltiplos ambientes / cômodos — fora do escopo (apenas protótipo)
- ❌ Autenticação/login — não necessário

---

## Complexidade por feature

| Feature | Complexidade | Prioridade |
|---------|-------------|-----------|
| Circuito Tinkercad | Baixa | P0 |
| Código Arduino (leitura + serial) | Baixa | P0 |
| Módulo Java (parse + exibição) | Média | P0 |
| Diagrama UML | Baixa | P0 |
| Protótipo UI | Baixa | P0 |
| Gráfico histórico | Média | P1 |
| Alerta visual | Baixa | P1 |
