package br.pucgoias.monitoramento.model;

/**
 * Represents a single sensor reading received from the Arduino via serial.
 *
 * <p>Data arrives in CSV format: {@code temperature,humidity,luminosity}
 *
 * @see #fromCsv(String)
 */
public class SensorData {

    private final int  temperature;
    private final int  humidity;
    private final int  luminosity;
    private final long timestamp;

    /**
     * @param temperature temperature in degrees Celsius
     * @param humidity    relative humidity percentage (0–100)
     * @param luminosity  raw LDR analog value (0–1023)
     */
    public SensorData(int temperature, int humidity, int luminosity) {
        this.temperature = temperature;
        this.humidity    = humidity;
        this.luminosity  = luminosity;
        this.timestamp   = System.currentTimeMillis();
    }

    /** @return temperature in °C */
    public int  getTemperature() { return temperature; }

    /** @return relative humidity (0–100 %) */
    public int  getHumidity()    { return humidity;    }

    /** @return raw LDR value (0–1023) */
    public int  getLuminosity()  { return luminosity;  }

    /** @return reading timestamp (epoch ms) */
    public long getTimestamp()   { return timestamp;   }

    /**
     * Returns a human-readable luminosity label based on the raw LDR value.
     *
     * @return descriptive string (e.g. {@code "Bright"})
     */
    public String getLuminosityDescription() {
        if (luminosity < 200) return "Very dark";
        if (luminosity < 500) return "Dim";
        if (luminosity < 800) return "Bright";
        return "Very bright";
    }

    /**
     * Parses a raw CSV line from the Arduino serial stream.
     *
     * @param line raw serial line (e.g. {@code "25,60,512"})
     * @return parsed {@link SensorData}, or {@code null} if the line is invalid
     */
    public static SensorData fromCsv(String line) {
        if (line == null) return null;
        String raw = line.trim();
        if (raw.isEmpty() || raw.startsWith("SISTEMA") || raw.startsWith("ERR")) return null;

        String[] parts = raw.split(",");
        if (parts.length < 3) return null;

        try {
            int temp  = Integer.parseInt(parts[0].trim());
            int humid = Integer.parseInt(parts[1].trim());
            int lux   = Integer.parseInt(parts[2].trim());
            return new SensorData(temp, humid, lux);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("SensorData{temp=%d°C, humid=%d%%, lux=%d}",
                temperature, humidity, luminosity);
    }
}
