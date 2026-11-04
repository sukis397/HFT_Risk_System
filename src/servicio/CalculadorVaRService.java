package servicio;

import modelo.PosicionRiesgo;
import patrones.observer.AlertaRiesgo;

public class CalculadorVaRService {
    private AlertaRiesgo gestorAlertas;

    public CalculadorVaRService(AlertaRiesgo gestorAlertas) {
        this.gestorAlertas = gestorAlertas;
    }

    public double ejecutarSimulacion(PosicionRiesgo posicion, int numTrayectorias, int numPasos) {
        // Evaluamos primero la volatilidad del activo usando el patrón Observer
        gestorAlertas.evaluarVolatilidad(posicion.activo());
        
        System.out.println("[INFO] Ejecutando simulación para " + posicion.activo().getTicker() + 
                           " con " + numTrayectorias + " trayectorias...");
        
        // Simulación base orientada al cálculo del riesgo (preparado para integrarse con C++)
        double varEstimado = posicion.valorEnRiesgo() * Math.sqrt(numPasos) * 1.645; // Intervalo de confianza 95%
        
        return varEstimado;
    }
}