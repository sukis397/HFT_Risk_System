package modelo;

public class Accion extends ActivoFinanciero {
    private double betaMercado;

    public Accion(String ticker, double precioInicial, double drift, double volatilidad, double betaMercado) {
        super(ticker, precioInicial, drift, volatilidad);
        this.betaMercado = betaMercado;
    }

    public double getBetaMercado() {
        return betaMercado;
    }

    @Override
    public double calcularRiesgoTeorico() {
        // Cálculo base de riesgo teórico para una acción usando su volatilidad y beta
        return this.precioInicial * this.volatilidad * Math.sqrt(this.betaMercado);
    }
}