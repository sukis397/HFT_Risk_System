package modelo;

public record PosicionRiesgo(ActivoFinanciero activo, int cantidadContratos, double valorEnRiesgo) {
    // Los records en Java 17 generan automáticamente el constructor, 
    // los métodos getter (ej: activo(), cantidadContratos(), valorEnRiesgo()), 
    // equals, hashCode y toString de forma inmutable.
}