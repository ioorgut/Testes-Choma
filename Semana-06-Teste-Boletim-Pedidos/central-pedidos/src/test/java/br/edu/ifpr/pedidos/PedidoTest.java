package br.edu.ifpr.pedidos;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    private ItemPedido item(int quantidade, int estoque, boolean fragil) {
        return new ItemPedido("A", 123, quantidade, estoque, 10, fragil);
    }

    @Test
    void deveAceitarListaVaziaSemValorPesoOuFragilidade() {
        Pedido pedido = new Pedido(List.of(),"PR", false, null);
        assertAll(() -> assertEquals(0, pedido.subtotalCentavos()), () -> assertEquals(0, pedido.pesoGramas()),
        () -> assertFalse(pedido.temFragil()), () -> assertTrue(pedido.estoqueSuficiente()));
    }

    @Test
    void deveCopiarListaDefensivamenteEPreservarDados() {
        List<ItemPedido> origem = new ArrayList<>(List.of(item(1, 1, false)));
        Pedido pedido = new Pedido(origem,"SP", true,"EXTRA10");
        origem.clear();
        assertEquals(1, pedido.itens().size());
        assertThrows(UnsupportedOperationException.class, () -> pedido.itens().clear());
        assertAll(() -> assertEquals("SP", pedido.uf()), () -> assertTrue(pedido.expresso()),
        () -> assertEquals("EXTRA10", pedido.cupom()));
    }


    @Test
    void deveVerificarEstoquePorLinhaMesmoComSkuRepetido() {
        assertTrue(new Pedido(List.of(item(1, 1, false), item(1, 1, false)),"PR", false, null).estoqueSuficiente());
    }

    @Test
    void deveCalcularSubtotalMaximoSemEstourarInteiro() {
        Pedido pedido = new Pedido(Collections.nCopies(100, new ItemPedido("A", 1000000, 100, 100, 100000, false)),"PR", false, null);
        assertEquals(10000000000L, pedido.subtotalCentavos());
        assertEquals(1000000000, pedido.pesoGramas());
    }
}
