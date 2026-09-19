package br.edu.ifpr.pedidos;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagamentoServiceTest {

    @Test
    void deveExigirProcessadorDePagamento() {
        assertThrows(NullPointerException.class, () -> new PagamentoService(null));
    }

    @Test
    void deveRejeitarTotalZeroSemCobrar() {
        PagamentoService service = new PagamentoService(valor -> { fail("Nao deve cobrar"); return true; });
        assertThrows(IllegalArgumentException.class, () -> service.pagar(0L, 1));
    }

    @Test
    void deveAprovarSemRepetirPagamento() {
        boolean aprovado = true;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> { chamadas.add(valor); return aprovado; });
        assertEquals(aprovado, service.pagar(1, 3));
        assertEquals(List.of(1L), chamadas);
    }

    @Test
    void deveRecusarSemRepetirPagamento() {
        boolean aprovado = false;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> { chamadas.add(valor); return aprovado; });
        assertEquals(aprovado, service.pagar(1, 3));
        assertEquals(List.of(1L), chamadas);
    }

    @Test
    void deveEsgotarTresTentativas() {
        int limite = 3;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> {
            chamadas.add(valor); throw new IllegalStateException("temporario");
        });
        assertFalse(service.pagar(123, limite));
        assertEquals(Collections.nCopies(limite, 123L), chamadas);
    }

    @Test
    void deveAprovarAposUmaFalha() {
        int falhas = 1;
        boolean aprovado = true;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> {
            chamadas.add(valor);
            if (chamadas.size() <= falhas) throw new IllegalStateException("temporario");
            return aprovado;
        });
        assertEquals(aprovado, service.pagar(456, 3));
        assertEquals(Collections.nCopies(falhas+1, 456L), chamadas);
    }

    @Test
    void deveAprovarAposDuasFalhas() {
        int falhas = 2;
        boolean aprovado = true;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> {
            chamadas.add(valor);
            if (chamadas.size() <= falhas) throw new IllegalStateException("temporario");
            return aprovado;
        });
        assertEquals(aprovado, service.pagar(456, 3));
        assertEquals(Collections.nCopies(falhas+1, 456L), chamadas);
    }

    @Test
    void deveRecusarAposUmaFalha() {
        int falhas = 1;
        boolean aprovado = false;

        List<Long> chamadas = new ArrayList<>();
        PagamentoService service = new PagamentoService(valor -> {
            chamadas.add(valor);
            if (chamadas.size() <= falhas) throw new IllegalStateException("temporario");
            return aprovado;
        });
        assertEquals(aprovado, service.pagar(456, 3));
        assertEquals(Collections.nCopies(falhas+1, 456L), chamadas);
    }

    @Test
    void devePropagarOutrasExcecoesSemRepetir() {
        List<Long> chamadas = new ArrayList<>();
        RuntimeException erro = new IllegalArgumentException("definitivo");
        PagamentoService service = new PagamentoService(valor -> { chamadas.add(valor); throw erro; });
        assertSame(erro, assertThrows(IllegalArgumentException.class, () -> service.pagar(100, 3)));
        assertEquals(List.of(100L), chamadas);
    }
}
