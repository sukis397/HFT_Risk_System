package modelo;

public abstract class ActivoFinanciero {
    protected final String ticker;
    protected final double precioInicial;
    protected final double drift;
    protected final double volatilidad;

    public ActivoFinanciero(String ticker, double precioInicial, double drift, double volatilidad) {
        this.ticker = ticker;
        this.precioInicial = precioInicial;
        this.drift = drift;
        this.volatilidad = volatilidad;
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrecioInicial() {
        return precioInicial;
    }

    public double getDrift() {
        return drift;
    }

    public double getVolatilidad() {
        return volatilidad;
    }

    public abstract double calcularRiesgoTeorico();

    // Cada subclase devuelve una copia independiente de sí misma
    public abstract ActivoFinanciero copiar();
}