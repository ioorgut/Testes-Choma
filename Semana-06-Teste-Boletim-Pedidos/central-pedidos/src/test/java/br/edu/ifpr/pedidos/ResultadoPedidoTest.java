package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultadoPedidoTest {

    @Test
    void devePreservarValoresDoFechamento() {
        ResultadoPedido resultado = new ResultadoPedido("PAGO", 10000, 2000, 1200, 9200);
        assertAll(
        () -> assertEquals("PAGO", resultado.status()),
        () -> assertEquals(10000, resultado.subtotalCentavos()),
        () -> assertEquals(2000, resultado.descontoCentavos()),
        () -> assertEquals(1200, resultado.freteCentavos()),
        () -> assertEquals(9200, resultado.totalCentavos()));
    }
}
