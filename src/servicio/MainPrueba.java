package servicio;

import modelo.Accion;
import modelo.PosicionRiesgo;
import patrones.observer.AlertaRiesgo;
import patrones.observer.GestorNotificaciones;

public class MainPrueba {
    public static void main(String[] args) {
        // 1. Configuramos el sistema de alertas (Umbral de volatilidad: 0.25)
        AlertaRiesgo alertaRiesgo = new AlertaRiesgo(0.25);
        alertaRiesgo.registrarObservador(new GestorNotificaciones());

        // 2. Creamos un activo financiero (Acción con alta volatilidad para disparar la alerta)
        Accion apple = new Accion("AAPL", 150.0, 0.05, 0.30, 1.2);

        // 3. Creamos una posición de riesgo inmutable (record)
        PosicionRiesgo posicion = new PosicionRiesgo(apple, 100, 5000.0);

        // 4. Instanciamos el servicio y ejecutamos
        CalculadorVaRService servicio = new CalculadorVaRService(alertaRiesgo);
        double resultadoVaR = servicio.ejecutarSimulacion(posicion, 10000, 252);

        System.out.println("VaR Calculado: " + resultadoVaR);
    }
}