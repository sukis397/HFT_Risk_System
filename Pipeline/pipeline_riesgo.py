from functools import reduce
from typing import Iterator, List
import random

# ---------------------------------------------------------
# 1. GENERADOR INFINITO Y EVALUACIÓN PEREZOSA (Lazy Evaluation)
# ---------------------------------------------------------
def generar_precios_simulados(precio_base: int = 100) -> Iterator[float]:
    """Generador perezoso que simula un flujo continuo de precios de mercado."""
    precio = float(precio_base)
    while True:
        precio += random.gauss(0.05, 1.2)
        yield max(precio, 1.0) # Evita precios negativos

# ---------------------------------------------------------
# 2. FUNCIONES PURAS PARA EL PIPELINE FUNCIONAL
# ---------------------------------------------------------
def filtrar_anomalias(umbral_min: float):
    """Función de orden superior (currificada) para filtrar valores."""
    return lambda precios: filter(lambda p: p > umbral_min, precios)

# ---------------------------------------------------------
# 3. EJECUCIÓN DEL PIPELINE Y REDUCCIÓN (Reduce)
# ---------------------------------------------------------
if __name__ == "__main__":
    print("[INFO] Iniciando Pipeline Funcional (Módulo 3) - Análisis HFT...")

    stream_mercado = generar_precios_simulados(150)
    
    # SOLUCIÓN: Tomamos exactamente 1000 elementos del generador de forma segura (sin bucle infinito)
    lote_inicial = [next(stream_mercado) for _ in range(1000)]
    
    # Aplicando Pipeline Funcional puro (Filter + Map implícito)
    datos_filtrados = list(filtrar_anomalias(50.0)(lote_inicial))
    
    if datos_filtrados:
        # Uso de reduce para acumular/calcular métricas globales de riesgo de forma funcional pura
        suma_total = reduce(lambda acumulado, val: acumulado + val, datos_filtrados, 0.0)
        promedio_movil = suma_total / len(datos_filtrados)
        
        print(f"----------------------------------------")
        print(f"Total de muestras procesadas: {len(datos_filtrados)}")
        print(f"Precio promedio acumulado (Reduce): {promedio_movil:.4f}")
        print(f"----------------------------------------")
    else:
        print("[AVISO] No se recolectaron datos suficientes en el pipeline.")
