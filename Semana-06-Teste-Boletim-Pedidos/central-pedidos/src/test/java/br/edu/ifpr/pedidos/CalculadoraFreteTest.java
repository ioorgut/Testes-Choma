package br.edu.ifpr.pedidos;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraFreteTest {

    @Test
    void deveCobrarFreteComDoisQuilos() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 2000, false)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 29999L);

        assertEquals(1200L, resultado);
    }

    @Test
    void deveCobrarFracaoDeQuiloExcedente() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 2001, false)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 0L);

        assertEquals(1500L, resultado);
    }

    @Test
    void deveCobrarUmQuiloExcedente() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 3000, false)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 0L);

        assertEquals(1500L, resultado);
    }

    @Test
    void deveCobrarSegundaFracaoExcedente() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 3001, false)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 0L);

        assertEquals(1800L, resultado);
    }

    @Test
    void deveZerarFreteNoLimiteDeGratuidade() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 5001, false)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 30000L);

        assertEquals(0L, resultado);
    }

    @Test
    void deveCobrarFragilidadeMesmoComFreteGratis() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 5001, true)),"PR", false, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(true, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 30001L);

        assertEquals(500L, resultado);
    }

    @Test
    void deveCobrarExpressoMesmoNoLimiteDeGratuidade() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 2000, false)),"PR", true, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(false, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 30000L);

        assertEquals(2700L, resultado);
    }

    @Test
    void deveAplicarVipAntesDosAdicionais() {
        Pedido pedido = new Pedido(List.of(new ItemPedido("A", 1, 1, 1, 2001, true)),"PR", true, null);
        CalculadoraFrete calculadora = new CalculadoraFrete();
        Cliente cliente = new Cliente(true, false, 1);

        long resultado = calculadora.calcular(pedido, cliente, 30000L);

        assertEquals(2750L, resultado);
    }

    @Test
    void deveCobrarFragilidadeUmaVez() {
        ItemPedido item = new ItemPedido("A", 1, 1, 1, 1, true);
        Pedido pedido = new Pedido(List.of(item, item),"PR", false, null);
        assertEquals(1700, new CalculadoraFrete().calcular(pedido, new Cliente(false, false, 1), 0));
    }

}
