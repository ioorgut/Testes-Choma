package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipacaoTest {
    // TODO: criar o objeto, chamar calcularPontos e verificar o resultado.

    @Test
    void deveCalcular3Pontos(){
        Participacao participacao = new Participacao();

        int resultado = participacao.calcularPontos(true,true);

        assertEquals(3,resultado);
    }

    @Test
    void deveCalcular2Pontos(){
        Participacao participacao = new Participacao();

        int resultado = participacao.calcularPontos(true,false );

        assertEquals(2,resultado);
    }

    @Test
    void deveCalcular1Pontos(){
        Participacao participacao = new Participacao();

        int resultado = participacao.calcularPontos(false,true);

        assertEquals(1,resultado);
    }
}
