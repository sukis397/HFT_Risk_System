package modelo;

public abstract class ActivoFinanciero {
    protected String ticker;
    protected double precioInicial;
    protected double drift;
    protected double volatilidad;

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

    // Método abstracto que las subclases (Accion, Opcion, Futuro) obligatoriamente implementarán
    public abstract double calcularRiesgoTeorico();
}