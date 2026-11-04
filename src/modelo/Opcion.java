package modelo;

public class Opcion extends ActivoFinanciero {
    private double precioEjercicio;
    private double tiempoExpiracion;

    public Opcion(String ticker, double precioInicial, double drift, double volatilidad, double precioEjercicio, double tiempoExpiracion) {
        super(ticker, precioInicial, drift, volatilidad);
        this.precioEjercicio = precioEjercicio;
        this.tiempoExpiracion = tiempoExpiracion;
    }

    public double getPrecioEjercicio() {
        return precioEjercicio;
    }

    public double getTiempoExpiracion() {
        return tiempoExpiracion;
    }

    @Override
    public double calcularRiesgoTeorico() {
        // Cálculo de riesgo teórico específico para una opción financiera
        return this.precioInicial * this.volatilidad * Math.exp(-this.tiempoExpiracion);
    }
}