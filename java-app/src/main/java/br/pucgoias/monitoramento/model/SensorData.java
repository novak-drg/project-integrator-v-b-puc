package br.pucgoias.monitoramento.model;

/**
 * Model — dados capturados de uma leitura dos sensores.
 *
 * Representa uma linha CSV recebida do Arduino no formato:
 *   temperatura,umidade,luminosidade
 *
 * Projeto Integrador V-B — PUC Goiás — ADS 2026
 */
public class SensorData {

    // ─── Atributos ───────────────────────────────────────────────────
    private final int    temperatura;   // °C
    private final int    umidade;       // % relativo
    private final int    luminosidade;  // 0–1023 (valor analógico LDR)
    private final long   timestamp;     // System.currentTimeMillis()

    // ─── Construtor ──────────────────────────────────────────────────
    public SensorData(int temperatura, int umidade, int luminosidade) {
        this.temperatura  = temperatura;
        this.umidade      = umidade;
        this.luminosidade = luminosidade;
        this.timestamp    = System.currentTimeMillis();
    }

    // ─── Getters ─────────────────────────────────────────────────────
    public int  getTemperatura()  { return temperatura;  }
    public int  getUmidade()      { return umidade;      }
    public int  getLuminosidade() { return luminosidade; }
    public long getTimestamp()    { return timestamp;    }

    /**
     * Converte luminosidade (0–1023) para percentual descritivo.
     * Usado na UI para exibição amigável.
     */
    public String getLuminosidadeDescricao() {
        if (luminosidade < 200)  return "Muito escuro";
        if (luminosidade < 500)  return "Meia-luz";
        if (luminosidade < 800)  return "Claro";
        return "Muito claro";
    }

    /**
     * Parseia uma linha CSV do Arduino.
     * Retorna null se a linha for inválida (heartbeat, erro, etc.).
     *
     * @param linha  linha bruta do Serial (ex: "25,60,512")
     * @return SensorData ou null
     */
    public static SensorData fromCsv(String linha) {
        if (linha == null) return null;
        String s = linha.trim();
        // Ignorar linhas de heartbeat/erro
        if (s.isEmpty() || s.startsWith("SISTEMA") || s.startsWith("ERR")) return null;

        String[] partes = s.split(",");
        if (partes.length < 3) return null;

        try {
            int temp = Integer.parseInt(partes[0].trim());
            int umid = Integer.parseInt(partes[1].trim());
            int lux  = Integer.parseInt(partes[2].trim());
            return new SensorData(temp, umid, lux);
        } catch (NumberFormatException e) {
            return null;  // linha malformada — ignorar
        }
    }

    @Override
    public String toString() {
        return String.format("SensorData{temp=%d°C, umid=%d%%, lux=%d}",
                temperatura, umidade, luminosidade);
    }
}
