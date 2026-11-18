# HFT_Risk_System
## Ejecución en GitHub Codespace llamado ubiquitous space meme

Este proyecto combina tres módulos: un kernel en C++ (Monte Carlo con OpenMP), un pipeline en Python (currificación y evaluación perezosa) y una capa de integración en Java (POO, patrón Observer y cálculo del VaR).

### Requisitos
- g++ con soporte OpenMP (`-fopenmp`)
- Make
- Python 3 (`python3`)
- Java 17 o superior (`javac` y `java`)

El Codespace por defecto ya trae estas herramientas.

### Pasos

Desde la terminal, en la carpeta raíz del proyecto (`/workspaces/HFT_Risk_System`):

**1. Verifica la rama**

```bash
git branch --show-current
```

Debe mostrar `version-final`.

**2. Compila el kernel C++**

```bash
make
```

Genera `kernel_montecarlo.exe` en la raíz. Este archivo no está en el repositorio (está en `.gitignore`), por eso hay que compilarlo en cada Codespace nuevo.

**3. Compila el código Java**

```bash
rm -rf target/classes
javac --release 17 -d target/classes $(find src -name "*.java")
```

**4. Ejecuta el flujo completo**

```bash
java -cp target/classes servicio.MainPrueba
```

### Resultado esperado

```
[ALERTA DE RIESGO] El activo AAPL superó el umbral permitido. Volatilidad actual: 0.3 (Umbral: 0.25)
[INFO] Simulando AAPL con kernel C++ y pipeline Python...
[INFO] Iniciando benchmarks de simulaciones Monte Carlo HFT...
----------------------------------------
Tiempo Secuencial: ... ms
Tiempo Paralelo (OpenMP): ... ms
Speedup obtenido: ...x
----------------------------------------
Verificación secuencial == paralelo: OK
[INFO] muestras=1000000 promedio_movil_media=105.1586
VaR Calculado: 2582.38...
```

### Por qué pasa cada cosa

- **Alerta de AAPL:** la volatilidad del activo (0.30) supera el umbral configurado (0.25). El Observer (`AlertaRiesgo`) notifica al `GestorNotificaciones`.
- **Tiempos y speedup:** dependen del hardware. El Codespace tiene pocos núcleos, así que el speedup es bajo (≈1.5x). En una PC con más núcleos se han medido alrededor de 6.5x. El resultado correcto no depende de esto.
- **`Verificación secuencial == paralelo: OK`:** ambas versiones usan la misma semilla por trayectoria (`1337 + i`), así que deben producir exactamente los mismos precios. Si aparece `FALLA`, hay un error de paralelización.
- **`muestras=1000000`:** el kernel genera un millón de trayectorias y Python las recibe por tubería (stdin). El promedio móvil de ventana 5 se imprime en stderr.
- **`VaR Calculado: 2582.38`:** Python devuelve el percentil 5% de los precios finales (≈74.18). Java calcula `(100 − 74.18) × 100 contratos`. El 100 es `S0_KERNEL`, el precio inicial con el que simula el kernel, no el precio de AAPL (150).

### Por qué `make` es necesario

`kernel_montecarlo.exe` no se sube al repositorio. Si no se compila antes, Java no encuentra el ejecutable y falla con `Cannot run program "kernel_montecarlo.exe"`.

### Limitaciones conocidas

- `S0`, `mu` y `sigma` están fijos en `modulo_imperativo/main.cpp`. El VaR **no depende** de la volatilidad ni del precio de AAPL; solo de los parámetros del kernel.
- `generar_precios_simulados` en `Pipeline/pipeline_riesgo.py` es solo una demostración del generador perezoso; la integración usa los precios del kernel.

### Solución de problemas

| Error | Causa | Solución |
|---|---|---|
| `make: *** No targets specified and no makefile found` | Estás en una rama sin `Makefile` | `git checkout version-final` |
| `Cannot run program "kernel_montecarlo.exe"` | No se compiló el kernel | Ejecutar `make` |
| `missing separator` | Las recetas del Makefile perdieron el tabulador | Revisar la sangría del Makefile |
| Sale `10000 trayectorias` en lugar de `Simulando AAPL...` | Estás en una versión vieja del código | Revisar la rama y hacer pull |
