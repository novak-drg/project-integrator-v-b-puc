# Pitfalls Research — Erros Comuns em Projetos Arduino + Java

## Pitfall 1: DHT11 não disponível no Tinkercad como esperado
- **Sintoma:** Não encontra "DHT11" na lista de componentes
- **Causa:** Tinkercad tem limitações de biblioteca; DHT11 pode aparecer como "Temperature Sensor" ou exigir código manual
- **Prevenção:** Buscar por "Temperature Sensor (grove)" ou simular com variáveis aleatórias para demo
- **Fase:** Arduino / Tinkercad (Fase 1)

## Pitfall 2: Serial Monitor do Arduino IDE aberto ao rodar Java
- **Sintoma:** `PortInUseException` ou falha silenciosa ao conectar
- **Causa:** Somente um processo pode usar a porta serial por vez
- **Prevenção:** Fechar IDE/Serial Monitor antes de executar o Java
- **Fase:** Integração Java-Arduino (Fase 2)

## Pitfall 3: Auto-reset do Arduino ao abrir conexão Java
- **Sintoma:** Primeiras leituras chegam corrompidas ou o Arduino reinicia
- **Causa:** Abertura da porta serial dispara reset de hardware no UNO
- **Prevenção:** Aguardar 2-3 segundos após abrir a porta antes de ler; ou usar `Thread.sleep(2000)` após `openPort()`
- **Fase:** Integração Java-Arduino (Fase 2)

## Pitfall 4: `\r\n` vs `\n` no parse Java
- **Sintoma:** Parse retorna strings com `\r` trailing, causando `NumberFormatException`
- **Causa:** `Serial.println()` no Arduino envia `\r\n`; `split(",")` deixa `\r` no último campo
- **Prevenção:** Usar `.trim()` em cada valor antes de `Double.parseDouble()`
- **Fase:** Módulo Java (Fase 2)

## Pitfall 5: Atualização da UI fora da EDT (Event Dispatch Thread)
- **Sintoma:** UI congela, dados aparecem inconsistentes ou `ConcurrentModificationException`
- **Causa:** Atualizar JLabels direto da thread de leitura serial
- **Prevenção:** Sempre usar `SwingUtilities.invokeLater(() -> { ... })` para atualizar componentes Swing
- **Fase:** Módulo Java (Fase 2)

## Pitfall 6: Hardcode da porta serial (`COM3`)
- **Sintoma:** Funciona na máquina do dev, falha na apresentação/entrega
- **Causa:** Porta COM varia entre computadores
- **Prevenção:** Listar portas disponíveis com `SerialPort.getCommPorts()` e exibir dropdown ou `JComboBox`
- **Fase:** Módulo Java (Fase 2)

## Pitfall 7: Usar RXTX em vez de jSerialComm
- **Sintoma:** Erros nativos, necessidade de copiar `.dll` manualmente, não funciona no Java moderno
- **Causa:** RXTX está deprecated e não mantido
- **Prevenção:** Usar exclusivamente **jSerialComm** (fazecast/jSerialComm)
- **Fase:** Setup do projeto Java (Fase 2)

## Pitfall 8: Diagrama UML feito "do zero" sem refletir o código real
- **Sintoma:** Diagrama não corresponde às classes implementadas; banca percebe inconsistência
- **Causa:** Fazer UML antes do código Java
- **Prevenção:** Implementar classes primeiro, depois gerar/confirmar o UML
- **Fase:** Documentação (Fase 3)

## Pitfall 9: Deixar relatório para o último dia
- **Sintoma:** Relatório incompleto, sem referencial teórico, perda de pontos de documentação
- **Causa:** Subestimar o esforço de documentação
- **Prevenção:** Fazer seções do relatório conforme cada deliverable é concluído; não acumular para o final
- **Fase:** Todas as fases

## Pitfall 10: Tinkercad não salva automaticamente
- **Sintoma:** Perde o progresso do circuito ao fechar o navegador
- **Causa:** Sessão expirada / não logado
- **Prevenção:** Criar conta no Tinkercad, copiar o link do projeto após cada sessão
- **Fase:** Fase 1 (Arduino)
