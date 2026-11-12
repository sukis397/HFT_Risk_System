from functools import reduce
from typing import Iterator, List

# ---------------------------------------------------------
# 1. GENERADOR INFINITO Y EVALUACIÓN PEREZOSA (Lazy Evaluation)
# ---------------------------------------------------------
def generar_precios_simulados(precio_base: int = 100) -> Iterator[float]:
    """Generador perezoso que simula un flujo continuo de precios de mercado."""
    precio = float(precio_base)
    while True:
        # Simulación simple de cambio estocástico por iteración perezosa
        import random
        precio += random.gauss(0.05, 1.2)
        yield max(precio, 1.0) # Evita precios negativos

# ---------------------------------------------------------
# 2. FUNCIONES PURAS PARA EL PIPELINE FUNCIONAL
# ---------------------------------------------------------
def filtrar_anomalias(umbral_min: float) -> callable:
    """Función de orden superior (currificada) para filtrar valores."""
    return lambda precios: filter(lambda p: p > umbral_min, precios)

def calcular_rendimientos(precios: Iterator[float]) -> Iterator[float]:
    """Transforma precios en variaciones porcentuales mediante map puro."""
    precios_lista = list(precios)
    return map(lambda i: (precios_lista[i] - precios_lista[i-1]) / precios_lista[i-1], range(1, len(precios_lista)))

# ---------------------------------------------------------
# 3. EJECUCIÓN DEL PIPELINE Y REDUCCIÓN (Reduce)
# ---------------------------------------------------------
if __name__ == "__main__":
    print("[INFO] Iniciando Pipeline Funcional (Módulo 3) - Análisis HFT...")

    # Tomamos un lote perezoso de 1000 datos del mercado
    stream_mercado = generar_precios_simulados(150)
    
    # Aplicando Pipeline Funcional puro (Evaluación perezosa + Map + Filter)
    datos_filtrados = list(filtrar_anomalias(50.0)(list(stream_mercado)[:1000]))
    
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