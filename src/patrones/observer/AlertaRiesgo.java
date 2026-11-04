package patrones.observer;

import java.util.ArrayList;
import java.util.List;
import modelo.ActivoFinanciero;

public class AlertaRiesgo {
    private double umbralMaximo;
    private List<ObserverVolatilidad> observadores;

    public AlertaRiesgo(double umbralMaximo) {
        this.umbralMaximo = umbralMaximo;
        this.observadores = new ArrayList<>();
    }

    public void registrarObservador(ObserverVolatilidad obs) {
        observadores.add(obs);
    }

    public void removerObservador(ObserverVolatilidad obs) {
        observadores.remove(obs);
    }

    public void evaluarVolatilidad(ActivoFinanciero activo) {
        if (activo.getVolatilidad() > umbralMaximo) {
            notificarTodos(activo.getTicker(), activo.getVolatilidad());
        }
    }

    private void notificarTodos(String ticker, double volatilidad) {
        for (ObserverVolatilidad obs : observadores) {
            obs.notificarExcesoVolatilidad(ticker, volatilidad, umbralMaximo);
        }
    }
}