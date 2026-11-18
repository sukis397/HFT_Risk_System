package modelo;

import java.util.Objects;

public record PosicionRiesgo(ActivoFinanciero activo, int cantidadContratos, double valorEnRiesgo) {

    // Constructor compacto: guarda una copia del activo
    public PosicionRiesgo {
        Objects.requireNonNull(activo, "El activo no puede ser nulo");
        activo = activo.copiar();
    }

    // Al leer, también se devuelve una copia: nadie obtiene la referencia interna
    @Override
    public ActivoFinanciero activo() {
        return activo.copiar();
    }
}