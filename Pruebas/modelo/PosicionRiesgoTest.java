package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class PosicionRiesgoTest {

    @Test
    void testAccesoresDevuelvenLosValoresCreados() {
        ActivoFinanciero activo = new Accion("AAPL", 150.0, 0.05, 0.3, 1.2);
        PosicionRiesgo pos = new PosicionRiesgo(activo, 10, 5000.0);

        assertEquals(activo, pos.activo());
        assertEquals(10, pos.cantidadContratos());
        assertEquals(5000.0, pos.valorEnRiesgo(), 0.0001);
    }

    @Test
    void testIgualdadPorValor() {
        ActivoFinanciero activo = new Accion("AAPL", 150.0, 0.05, 0.3, 1.2);
        PosicionRiesgo p1 = new PosicionRiesgo(activo, 10, 5000.0);
        PosicionRiesgo p2 = new PosicionRiesgo(activo, 10, 5000.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testCamposSonPrivadosYFinales() {
        for (Field campo : PosicionRiesgo.class.getDeclaredFields()) {
            assertTrue(Modifier.isPrivate(campo.getModifiers()),
                    "Campo no privado: " + campo.getName());
            assertTrue(Modifier.isFinal(campo.getModifiers()),
                    "Campo no final: " + campo.getName());
        }
    }

    @Test
    void testClaseEsFinal() {
        assertTrue(Modifier.isFinal(PosicionRiesgo.class.getModifiers()));
    }

    @Test
    void testActivoSeCompartePorReferencia() {
        ActivoFinanciero activo = new Accion("AAPL", 150.0, 0.05, 0.3, 1.2);
        PosicionRiesgo pos = new PosicionRiesgo(activo, 10, 5000.0);

        assertSame(activo, pos.activo());
    }
}