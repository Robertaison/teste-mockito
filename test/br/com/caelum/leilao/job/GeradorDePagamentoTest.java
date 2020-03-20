package br.com.caelum.leilao.job;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.dao.RepositorioDePagamentos;
import br.com.caelum.leilao.servico.Avaliador;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GeradorDePagamentoTest {

    private RepositorioDePagamentos pagamentosDao;
    private RepositorioDeLeiloes leilaoDao;
    private Avaliador avaliador;

    @Before
    public void setup(){
        this.avaliador = new Avaliador();
        this.leilaoDao = mock(RepositorioDeLeiloes.class);
        this.pagamentosDao = mock(RepositorioDePagamentos.class);
    }

    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation")
                .lance(new Usuario("Jose"), 2000.0)
                .lance(new Usuario("Alan"), 2500.0)
                .constroi();

        when(leilaoDao.encerrados()).thenReturn(Arrays.asList(leilao));

        GeradorDePagamento gerador = new GeradorDePagamento(leilaoDao, pagamentosDao, avaliador);
        gerador.gera();

        ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentosDao).salva(argumento.capture());
        Pagamento pagamentoGerado = argumento.getValue();
        assertEquals(2500.0, pagamentoGerado.getValor(), 0.00001);
    }
}
