package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnaliseRiscoTest {

    @Test
    void deveRecusarClienteBloqueado() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, true, 0);

        String resultado = analise.avaliar(cliente, 0L, false);

        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveAprovarNovoClienteNoLimite() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 0);

        String resultado = analise.avaliar(cliente, 100000L, false);

        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRevisarNovoClienteAcimaDoLimite() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 0);

        String resultado = analise.avaliar(cliente, 100001L, false);

        assertEquals("REVISAO", resultado);
    }

    @Test
    void deveRevisarEntregaExpressaDeNovoCliente() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 0);

        String resultado = analise.avaliar(cliente, 1L, true);

        assertEquals("REVISAO", resultado);
    }

    @Test
    void deveAprovarClienteAntigoNoLimite() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 1);

        String resultado = analise.avaliar(cliente, 500000L, true);

        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRevisarClienteAntigoAcimaDoLimite() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(false, false, 1);

        String resultado = analise.avaliar(cliente, 500001L, false);

        assertEquals("REVISAO", resultado);
    }

    @Test
    void deveAprovarVipAntigoAcimaDoLimite() {
        AnaliseRisco analise = new AnaliseRisco();
        Cliente cliente = new Cliente(true, false, 1);

        String resultado = analise.avaliar(cliente, 500001L, true);

        assertEquals("APROVADO", resultado);
    }
}
