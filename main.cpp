#include <iostream>
#include <vector>
#include <cmath>
#include <random>
#include <chrono>
#include <omp.h>

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
        std::mt19937 generator(54321 + i);
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

    std::cout << "[INFO] Iniciando benchmarks de simulaciones Monte Carlo HFT...\n";

    auto startSeq = std::chrono::high_resolution_clock::now();
    simularGBMSecuencial(M, T, S0, mu, sigma, dt, preciosSecuencial);
    auto endSeq = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> durationSeq = endSeq - startSeq;

    auto startPar = std::chrono::high_resolution_clock::now();
    simularGBMParalelo(M, T, S0, mu, sigma, dt, preciosParalelo);
    auto endPar = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> durationPar = endPar - startPar;

    std::cout << "----------------------------------------\n";
    std::cout << "Tiempo Secuencial: " << durationSeq.count() << " ms\n";
    std::cout << "Tiempo Paralelo (OpenMP): " << durationPar.count() << " ms\n";
    std::cout << "Speedup obtenido: " << durationSeq.count() / durationPar.count() << "x\n";
    std::cout << "----------------------------------------\n";
    return 0;
}
