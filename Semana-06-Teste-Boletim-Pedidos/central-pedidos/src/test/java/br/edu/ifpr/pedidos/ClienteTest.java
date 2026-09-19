package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveValidarHistoricoEPreservarDados() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente(false, false, -1));
        Cliente novo = new Cliente(false, false, 0);
        Cliente antigo = new Cliente(true, true, Integer.MAX_VALUE);
        assertAll(() -> assertFalse(novo.vip()), () -> assertFalse(novo.bloqueado()),
        () -> assertEquals(0, novo.comprasAnteriores()), () -> assertTrue(antigo.vip()),
        () -> assertTrue(antigo.bloqueado()), () -> assertEquals(Integer.MAX_VALUE, antigo.comprasAnteriores()));
    }
}
