# Link da Simulação — Tinkercad

**Projeto:** PI-V-B — Monitoramento Ambiental  
**Plataforma:** Tinkercad (Autodesk)  
**Link público:** https://www.tinkercad.com/things/eALbQIdph4d-pi-v-b-monitoramento-ambiental

## Componentes do Circuito

| Componente | Função | Pino Arduino |
|-----------|--------|-------------|
| TMP36 | Sensor analógico de temperatura (°C) | A1 |
| Fotorresistor (LDR) + Resistor 10kΩ | Divisor de tensão para luminosidade | A0 |
| Potenciômetro | Simula umidade relativa do ar (0–100%) | A2 |
| Arduino UNO R3 | Microcontrolador + comunicação serial | — |

## Protocolo Serial

- **Baud rate:** 9600 bps  
- **Formato:** `temperatura,umidade,luminosidade\n`  
- **Exemplo real:** `24,38,81`

## Output do Serial Monitor (verificado)

```
SISTEMA,INICIADO,0
24,38,969
24,38,81
81,30,81
```

## Screenshots

- `screenshots/circuito.png` — Circuito completo com conexões
- `screenshots/simulacao.png` — Simulação ativa com Serial Monitor

## Instruções para replicar

1. Acesse o link acima
2. Clique em **"Abrir no Tinkercad"** e faça login
3. Clique em **"Iniciar Simulação"**
4. Abra o **Monitor Serial** para ver os dados em tempo real
5. Interaja com os componentes:
   - Gire o **potenciômetro** para variar a umidade
   - Clique no **LDR** e ajuste o slider de luz para variar a luminosidade
   - Clique no **TMP36** e ajuste o slider de temperatura
