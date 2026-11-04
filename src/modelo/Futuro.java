package modelo;

public class Futuro extends ActivoFinanciero {
    private double tasaFinanciamiento;

    public Futuro(String ticker, double precioInicial, double drift, double volatilidad, double tasaFinanciamiento) {
        super(ticker, precioInicial, drift, volatilidad);
        this.tasaFinanciamiento = tasaFinanciamiento;
    }

    public double getTasaFinanciamiento() {
        return tasaFinanciamiento;
    }

    @Override
    public double calcularRiesgoTeorico() {
        // Cálculo de riesgo teórico para un contrato de futuro
        return this.precioInicial * this.volatilidad * (1.0 + this.tasaFinanciamiento);
    }
}