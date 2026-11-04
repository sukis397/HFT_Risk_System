package patrones.observer;

public class GestorNotificaciones implements ObserverVolatilidad {

    @Override
    public void notificarExcesoVolatilidad(String ticker, double volatilidadActual, double umbral) {
        System.out.println("[ALERTA DE RIESGO] El activo " + ticker + 
                           " superó el umbral permitido. Volatilidad actual: " + 
                           volatilidadActual + " (Umbral: " + umbral + ")");
    }
}