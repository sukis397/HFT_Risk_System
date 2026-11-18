package patrones.observer;

public interface ObserverVolatilidad {
    void notificarExcesoVolatilidad(String ticker, double volatilidadActual, double umbral);
}