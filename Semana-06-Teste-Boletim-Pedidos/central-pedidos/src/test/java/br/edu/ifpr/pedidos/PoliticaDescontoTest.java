package br.edu.ifpr.pedidos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescontoTest {

    @Test
    void deveNaoDescontarAbaixoDeCinquentaMil() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 49999L, null);

        assertEquals(0L, resultado);
    }

    @Test
    void deveDescontarCincoPorCentoNoLimite() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 50000L, null);

        assertEquals(2500L, resultado);
    }

    @Test
    void deveTruncarDescontoVip() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(true, false, 1);

        long resultado = politica.calcular(cliente, 19L, null);

        assertEquals(1L, resultado);
    }

    @Test
    void deveManterDescontoComCupomVazio() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(true, false, 1);

        long resultado = politica.calcular(cliente, 50000, "");

        assertEquals(5000, resultado);
    }

    @Test
    void deveNaoAplicarBemvindoAbaixoDoLimite() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 0);

        long resultado = politica.calcular(cliente, 9999L, "BEMVINDO");

        assertEquals(0L, resultado);
    }

    @Test
    void deveAplicarBemvindoNoLimite() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 0);

        long resultado = politica.calcular(cliente, 10000L, "BEMVINDO");

        assertEquals(2000L, resultado);
    }

    @Test
    void deveNaoAplicarBemvindoParaClienteAntigo() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 10000L, "BEMVINDO");

        assertEquals(0L, resultado);
    }

    @Test
    void deveLimitarDescontoVipComBemvindo() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(true, false, 0);

        long resultado = politica.calcular(cliente, 10000L, "BEMVINDO");

        assertEquals(2000L, resultado);
    }

    @Test
    void deveNaoAplicarExtra10AbaixoDoLimite() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 0);

        long resultado = politica.calcular(cliente, 19999L, "EXTRA10");

        assertEquals(0L, resultado);
    }

    @Test
    void deveAplicarExtra10NoLimite() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 20000L, "EXTRA10");

        assertEquals(2000L, resultado);
    }

    @Test
    void deveTruncarExtra10() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 20019L, "EXTRA10");

        assertEquals(2001L, resultado);
    }

    @Test
    void deveSomarExtra10AoDescontoVip() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(true, false, 1);

        long resultado = politica.calcular(cliente, 20019L, "EXTRA10");

        assertEquals(4002L, resultado);
    }

    @Test
    void deveSomarExtra10AoDescontoComum() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = politica.calcular(cliente, 50000L, "EXTRA10");

        assertEquals(7500L, resultado);
    }

    @Test
    void deveNormalizarCupomBemvindo() {
        PoliticaDesconto politica = new PoliticaDesconto();
        Cliente cliente = new Cliente(false, false, 0);

        long resultado = politica.calcular(cliente, 10000L, "  bemvindo  ");

        assertEquals(2000L, resultado);
    }

}
