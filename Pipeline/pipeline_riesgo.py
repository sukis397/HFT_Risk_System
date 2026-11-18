import random
import sys
from functools import reduce
from typing import Callable, Iterable, Iterator, Sequence

# ---------------------------------------------------------
# 1. Fuente perezosa (demostración)
# ---------------------------------------------------------
def generar_precios_simulados(precio_base: float = 100.0) -> Iterator[float]:
    """Generador infinito: los precios se calculan solo cuando se piden."""
    precio = float(precio_base)
    while True:
        precio += random.gauss(0.05, 1.2)
        yield max(precio, 1.0)

# ---------------------------------------------------------
# 2. Funciones currificadas: un argumento por nivel
# ---------------------------------------------------------
def filtrar_anomalias(umbral: float) -> Callable[[Iterable[float]], Iterator[float]]:
    """filtrar_anomalias(umbral)(serie). Devuelve un iterador perezoso."""
    def con_serie(serie: Iterable[float]) -> Iterator[float]:
        return filter(lambda p: p > umbral, serie)
    return con_serie

def promedio_movil(ventana: int) -> Callable[[Sequence[float]], tuple]:
    """promedio_movil(k)(serie). Cada promedio se calcula con reduce, sin mutar la entrada."""
    def con_serie(serie: Sequence[float]) -> tuple:
        def media(xs: tuple) -> float:
            return reduce(lambda acc, x: acc + x, xs, 0.0) / len(xs)
        return tuple(
            media(tuple(serie[i:i + ventana]))
            for i in range(len(serie) - ventana + 1)
        )
    return con_serie

# ---------------------------------------------------------
# 3. Función pura de estadística
# ---------------------------------------------------------
def percentil(datos: Sequence[float], q: float) -> float:
    """Trabaja sobre una copia ordenada; no modifica la entrada."""
    ordenados = sorted(datos)
    return ordenados[min(int(q * len(ordenados)), len(ordenados) - 1)]

# ---------------------------------------------------------
# 4. Entrada y salida para la integración con Java
# ---------------------------------------------------------
def leer_stdin() -> Iterator[float]:
    for linea in sys.stdin:
        if linea.strip():
            yield float(linea)

if __name__ == "__main__":
    sys.stdin.reconfigure(encoding="utf-8-sig")   # descarta el BOM que PowerShell agrega al pipe

    precios = tuple(filtrar_anomalias(0.0)(leer_stdin()))
    suavizados = promedio_movil(5)(precios)

    print(f"[INFO] muestras={len(precios)} "
          f"promedio_movil_media={reduce(lambda a, x: a + x, suavizados, 0.0) / len(suavizados):.4f}",
          file=sys.stderr)

    # stdout: solo el número que lee Java (percentil 5% de los precios)
    print(f"{percentil(precios, 0.05):.6f}")