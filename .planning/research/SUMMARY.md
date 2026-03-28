# Research Summary — Sistema de Monitoramento de Ambientes Inteligentes

## Stack

**Arduino:** UNO R3 + DHT11 (temp/umidade) + LDR + resistor 10kΩ, simulado no **Tinkercad**  
**Java:** JDK 17/21 LTS + **jSerialComm** (serial) + **Swing com FlatLaf** (UI) — padrão MVC  
**UML:** LucidChart ou yED Live  
**UI Prototype:** QuantUX ou Figma  
**Repo:** GitHub

## Table Stakes (devem estar no v1)

1. Circuito Arduino funcional no Tinkercad (DHT11 + LDR, output serial CSV)
2. Código Arduino produzindo `temperatura,umidade,luminosidade\n` via Serial
3. Módulo Java lendo serial com jSerialComm e exibindo valores em tempo real
4. Diagrama de Classes UML refletindo as classes implementadas
5. Protótipo de UI (screenshot + link)
6. Repositório GitHub com código Java
7. Relatório e referencial teórico

## Top 3 Pitfalls

1. **DHT11 no Tinkercad** — pode precisar de workaround (buscar "Temperature Sensor" ou simular valores)
2. **`\r\n` no parse Java** — usar `.trim()` em todos os campos parseados
3. **Atualização da UI fora da EDT** — sempre `SwingUtilities.invokeLater()` para Swing

## Ordem de Build Recomendada

```
Fase 1: Tinkercad (circuito + código Arduino + link)
Fase 2: Módulo Java (serial + UI + UML)  
Fase 3: Protótipo UI formal + Documentação
```

## Decisões Chave

| Decisão | Escolha | Motivo |
|---------|---------|--------|
| UI Java | Swing + FlatLaf | Mais simples, academicamente suficiente |
| Serial lib | jSerialComm | Moderna, cross-platform, sem instalação nativa |
| Protocolo serial | CSV com `\n` | Simples de parsear com `.split(",")` e `.trim()` |
| UML | Após código | Garantir que o diagrama reflete a implementação real |
