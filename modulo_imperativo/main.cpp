#include <iostream>
#include <vector>
#include <cmath>
#include <random>
#include <chrono>
#include <omp.h>

// Cada trayectoria i usa su propia semilla (1337 + i), tanto en la versión
// secuencial como en la paralela. Así ambas producen exactamente los mismos
// resultados y se pueden comparar.
void simularGBMSecuencial(int M, int T, double S0, double mu, double sigma, double dt, std::vector<double>& preciosFinales) {
    for (int i = 0; i < M; ++i) {
        std::mt19937 generator(1337 + i);
        std::normal_distribution<double> distribution(0.0, 1.0);
        double S = S0;
        for (int t = 0; t < T; ++t) {
            double Z = distribution(generator);
            S = S * std::exp((mu - 0.5 * sigma * sigma) * dt + sigma * std::sqrt(dt) * Z);
        }
        preciosFinales[i] = S;
    }
}

void simularGBMParalelo(int M, int T, double S0, double mu, double sigma, double dt, std::vector<double>& preciosFinales) {
    #pragma omp parallel for schedule(static)
    for (int i = 0; i < M; ++i) {
        std::mt19937 generator(1337 + i);
        std::normal_distribution<double> distribution(0.0, 1.0);
        double S = S0;
        for (int t = 0; t < T; ++t) {
            double Z = distribution(generator);
            S = S * std::exp((mu - 0.5 * sigma * sigma) * dt + sigma * std::sqrt(dt) * Z);
        }
        preciosFinales[i] = S;
    }
}

int main() {
    int M = 1000000;
    int T = 252;
    double S0 = 100.0;
    double mu = 0.05;
    double sigma = 0.2;
    double dt = 1.0 / T;

    std::vector<double> preciosSecuencial(M);
    std::vector<double> preciosParalelo(M);

    // Todo lo informativo va a stderr para no contaminar stdout
    std::cerr << "[INFO] Iniciando benchmarks de simulaciones Monte Carlo HFT...\n";

    auto startSeq = std::chrono::high_resolution_clock::now();
    simularGBMSecuencial(M, T, S0, mu, sigma, dt, preciosSecuencial);
    auto endSeq = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> durationSeq = endSeq - startSeq;

    auto startPar = std::chrono::high_resolution_clock::now();
    simularGBMParalelo(M, T, S0, mu, sigma, dt, preciosParalelo);
    auto endPar = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> durationPar = endPar - startPar;

    std::cerr << "----------------------------------------\n";
    std::cerr << "Tiempo Secuencial: " << durationSeq.count() << " ms\n";
    std::cerr << "Tiempo Paralelo (OpenMP): " << durationPar.count() << " ms\n";
    std::cerr << "Speedup obtenido: " << durationSeq.count() / durationPar.count() << "x\n";
    std::cerr << "----------------------------------------\n";

    // Verificación: con semillas iguales, ambas versiones deben coincidir exactamente
    bool iguales = true;
    for (int i = 0; i < M; ++i) {
        if (preciosSecuencial[i] != preciosParalelo[i]) { iguales = false; break; }
    }
    std::cerr << "Verificación secuencial == paralelo: " << (iguales ? "OK" : "FALLA") << "\n";

    // stdout: solo los precios finales, uno por línea, para Python y Java
    for (double p : preciosParalelo) std::cout << p << '\n';
    return 0;
}