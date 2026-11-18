package servicio;

import modelo.Accion;
import modelo.PosicionRiesgo;
import patrones.observer.AlertaRiesgo;
import patrones.observer.GestorNotificaciones;

public class MainPrueba {
    public static void main(String[] args) throws Exception {
    AlertaRiesgo alertaRiesgo = new AlertaRiesgo(0.25);
    alertaRiesgo.registrarObservador(new GestorNotificaciones());

    Accion apple = new Accion("AAPL", 150.0, 0.05, 0.30, 1.2);
    PosicionRiesgo posicion = new PosicionRiesgo(apple, 100, 5000.0);

    CalculadorVaRService servicio = new CalculadorVaRService(alertaRiesgo);
    double resultadoVaR = servicio.ejecutarSimulacion(posicion);

    System.out.println("VaR Calculado: " + resultadoVaR);
}

}
