package br.edu.ifpr.pedidos;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    @Test
    void deveFecharPedidoDeClienteComumComFreteDoParanaEPagamentoAprovado() {
        Cliente cliente = new Cliente(false, false, 1);
        ItemPedido item = new ItemPedido("LIVRO-JAVA", 10_000, 1, 5, 1_000, false);
        Pedido pedido = new Pedido(List.of(item), "PR", false, null);

        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            return true;
        });

        ResultadoPedido resultado = service.fechar(pedido, cliente);

        assertAll(
        () -> assertEquals("PAGO", resultado.status()),
        () -> assertEquals(10_000L, resultado.subtotalCentavos()),
        () -> assertEquals(0L, resultado.descontoCentavos()),
        () -> assertEquals(1_200L, resultado.freteCentavos()),
        () -> assertEquals(11_200L, resultado.totalCentavos()),

        () -> assertEquals(List.of(11_200L), cobrancas)
        );
    }

    private Pedido pedido(long preco, int quantidade, int estoque, boolean expresso, String cupom) {
        return new Pedido(List.of(new ItemPedido("A", preco, quantidade, estoque, 1000, false)),
        "PR", expresso, cupom);
    }

    private PedidoService semPagamento() {
        return new PedidoService(total -> {
            fail("O processador nao deve ser chamado antes da aprovacao");
            return true;
        });
    }

    @Test
    void deveRejeitarReferenciasNulas() {
        assertThrows(NullPointerException.class, () -> new PedidoService(null));
        assertThrows(NullPointerException.class,
        () -> semPagamento().fechar(null, new Cliente(false, true, 0)));
        assertThrows(NullPointerException.class,
        () -> semPagamento().fechar(pedido(100, 1, 1, false, null), null));
    }

    @Test
    void deveRejeitarPedidoVazio() {
        Cliente cliente = new Cliente(false, false, 1);
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
        () -> semPagamento().fechar(new Pedido(List.of(), "PR", false, null), cliente));
        assertEquals("Pedido sem itens ativos", erro.getMessage());
    }

    @Test
    void devePreservarValoresEmRevisaoSemCobrar() {
        assertEquals(new ResultadoPedido("REVISAO", 10000, 0, 2700, 12700),
        semPagamento().fechar(pedido(10000, 1, 1, true, null), new Cliente(false, false, 0)));
        assertEquals(new ResultadoPedido("REVISAO", 110000, 5500, 0, 104500),
        semPagamento().fechar(pedido(110000, 1, 1, false, null), new Cliente(false, false, 0)));
        assertEquals(new ResultadoPedido("REVISAO", 600000, 30000, 0, 570000),
        semPagamento().fechar(pedido(600000, 1, 1, false, null), new Cliente(false, false, 1)));
    }

    @Test
    void deveConsiderarDescontoAntesDeConcederFreteGratis() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> { cobrancas.add(total); return true; });
        assertEquals(new ResultadoPedido("PAGO", 30000, 6000, 600, 24600),
        service.fechar(pedido(30000, 1, 1, false, "EXTRA10"), new Cliente(true, false, 1)));
        assertEquals(List.of(24600L), cobrancas);
    }

    @Test
    void deveManterValoresEInterromperTentativasNaRecusaDefinitiva() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> { cobrancas.add(total); return false; });
        assertEquals(new ResultadoPedido("PAGAMENTO_RECUSADO", 10000, 0, 1200, 11200),
        service.fechar(pedido(10000, 1, 1, false, null), new Cliente(false, false, 1)));
        assertEquals(List.of(11200L), cobrancas);
    }

    @Test
    void deveAprovarNaTerceiraTentativa() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            if (cobrancas.size() < 3) throw new IllegalStateException("temporario");
            return true;
        });
        assertEquals(new ResultadoPedido("PAGO", 10000, 0, 1200, 11200),
        service.fechar(pedido(10000, 1, 1, false, null), new Cliente(false, false, 1)));
        assertEquals(List.of(11200L, 11200L, 11200L), cobrancas);
    }

    @Test
    void deveEsgotarTresTentativasQuandoIndisponivel() {
        List<Long> cobrancas = new ArrayList<>();
        PedidoService service = new PedidoService(total -> {
            cobrancas.add(total);
            throw new IllegalStateException("temporario");
        });
        assertEquals(new ResultadoPedido("PAGAMENTO_RECUSADO", 10000, 0, 1200, 11200),
        service.fechar(pedido(10000, 1, 1, false, null), new Cliente(false, false, 1)));
        assertEquals(List.of(11200L, 11200L, 11200L), cobrancas);
    }

    @Test
    void devePropagarErroInesperadoDoProcessador() {
        List<Long> cobrancas = new ArrayList<>();
        RuntimeException erro = new UnsupportedOperationException("falha");
        PedidoService service = new PedidoService(total -> { cobrancas.add(total); throw erro; });
        assertSame(erro, assertThrows(UnsupportedOperationException.class,
        () -> service.fechar(pedido(10000, 1, 1, false, null), new Cliente(false, false, 1))));
        assertEquals(List.of(11200L), cobrancas);
    }
}
