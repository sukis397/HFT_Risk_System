package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import modelo.PosicionRiesgo;
import patrones.observer.AlertaRiesgo;

public class CalculadorVaRService {
        // Debe coincidir con S0 en main.cpp: el kernel simula desde este precio
    private static final double S0_KERNEL = 100.0;
    private final AlertaRiesgo gestorAlertas;

    public CalculadorVaRService(AlertaRiesgo gestorAlertas) {
        this.gestorAlertas = gestorAlertas;
    }

    public double ejecutarSimulacion(PosicionRiesgo posicion) throws IOException, InterruptedException {
        // Patrón Observer: alerta si la volatilidad supera el umbral
        gestorAlertas.evaluarVolatilidad(posicion.activo());

        System.out.println("[INFO] Simulando " + posicion.activo().getTicker()
                + " con kernel C++ y pipeline Python...");

        // 1) Módulo imperativo C++: genera los precios finales (una línea por trayectoria)
        List<String> precios = ejecutar(new ProcessBuilder("kernel_montecarlo.exe"), null);

        // 2) Módulo funcional Python: recibe los precios por stdin, devuelve el percentil 5%
        List<String> salidaPy = ejecutar(new ProcessBuilder("python", "Pipeline/pipeline_riesgo.py"), precios);
        double percentil5 = Double.parseDouble(salidaPy.get(0));

        // 3) Módulo POO: VaR 95% = pérdida respecto al precio inicial
                double s0 = S0_KERNEL;
        return (s0 - percentil5) * posicion.cantidadContratos();
    }

    // Ejecuta un proceso externo. Si hay entrada, la envía por stdin y lee stdout.
    private static List<String> ejecutar(ProcessBuilder pb, List<String> entrada)
            throws IOException, InterruptedException {
        pb.redirectError(ProcessBuilder.Redirect.INHERIT); // [INFO] y tiempos a la consola
        Process p = pb.start();

        if (entrada != null) {
            try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
                for (String s : entrada) {
                    w.write(s);
                    w.newLine();
                }
            }
        } else {
            p.getOutputStream().close();
        }

        List<String> salida = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String linea;
            while ((linea = r.readLine()) != null) {
                if (!linea.isBlank()) salida.add(linea.trim());
            }
        }

        if (p.waitFor() != 0) {
            throw new IllegalStateException("Falló el proceso: " + pb.command());
        }
        return salida;
    }
}