package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void deveRejeitarPrecoZero() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 0L, 1, 1, 1, false));
    }

    @Test
    void deveRejeitarPrecoAcimaDoMaximo() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1000001L, 1, 1, 1, false));
    }

    @Test
    void deveRejeitarQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1L, -1, 1, 1, false));
    }

    @Test
    void deveRejeitarQuantidadeAcimaDoMaximo() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1L, 101, 1, 1, false));
    }

    @Test
    void deveRejeitarPesoZero() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1L, 1, 1, 0, false));
    }

    @Test
    void deveRejeitarPesoAcimaDoMaximo() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedido("SKU", 1L, 1, 1, 100001, false));
    }

    @Test
    void deveAceitarItemInativoSemEstoque() {
        long preco = 1L;
        int quantidade = 0;
        int estoque = 0;
        int peso = 1;
        boolean disponivel = true;

        ItemPedido item = new ItemPedido("SKU", preco, quantidade, estoque, peso, true);
        assertAll(() -> assertEquals(0L, item.totalCentavos()), () -> assertEquals(disponivel, item.disponivel()),
        () -> assertEquals("SKU", item.sku()), () -> assertEquals(preco, item.precoCentavos()),
        () -> assertEquals(quantidade, item.quantidade()), () -> assertEquals(estoque, item.estoque()),
        () -> assertEquals(peso, item.pesoGramas()), () -> assertTrue(item.fragil()));
    }

    @Test
    void deveAceitarValoresMinimos() {
        long preco = 1L;
        int quantidade = 1;
        int estoque = 1;
        int peso = 1;
        boolean disponivel = true;

        ItemPedido item = new ItemPedido("SKU", preco, quantidade, estoque, peso, true);
        assertAll(() -> assertEquals(1L, item.totalCentavos()), () -> assertEquals(disponivel, item.disponivel()),
        () -> assertEquals("SKU", item.sku()), () -> assertEquals(preco, item.precoCentavos()),
        () -> assertEquals(quantidade, item.quantidade()), () -> assertEquals(estoque, item.estoque()),
        () -> assertEquals(peso, item.pesoGramas()), () -> assertTrue(item.fragil()));
    }

    @Test
    void deveDetectarQuantidadeMaiorQueEstoque() {
        long preco = 123L;
        int quantidade = 3;
        int estoque = 2;
        int peso = 100;
        boolean disponivel = false;

        ItemPedido item = new ItemPedido("SKU", preco, quantidade, estoque, peso, true);
        assertAll(() -> assertEquals(369L, item.totalCentavos()), () -> assertEquals(disponivel, item.disponivel()),
        () -> assertEquals("SKU", item.sku()), () -> assertEquals(preco, item.precoCentavos()),
        () -> assertEquals(quantidade, item.quantidade()), () -> assertEquals(estoque, item.estoque()),
        () -> assertEquals(peso, item.pesoGramas()), () -> assertTrue(item.fragil()));
    }

    @Test
    void deveAceitarValoresMaximos() {
        long preco = 1000000L;
        int quantidade = 100;
        int estoque = 100;
        int peso = 100000;
        boolean disponivel = true;

        ItemPedido item = new ItemPedido("SKU", preco, quantidade, estoque, peso, true);
        assertAll(() -> assertEquals(100000000L, item.totalCentavos()), () -> assertEquals(disponivel, item.disponivel()),
        () -> assertEquals("SKU", item.sku()), () -> assertEquals(preco, item.precoCentavos()),
        () -> assertEquals(quantidade, item.quantidade()), () -> assertEquals(estoque, item.estoque()),
        () -> assertEquals(peso, item.pesoGramas()), () -> assertTrue(item.fragil()));
    }
}
