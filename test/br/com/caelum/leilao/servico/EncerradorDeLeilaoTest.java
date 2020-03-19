package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EncerradorDeLeilaoTest {

    private LeilaoDao dao = new LeilaoDao();
    private EnviadorDeEmail enviadorDeEmail;
    private RepositorioDeLeiloes daoFalso;
    private EncerradorDeLeilao encerrador;
    private Leilao leilao1;
    private Leilao leilao2;
    private Calendar antiga = Calendar.getInstance();

    @Before
    public void setup(){
        dao.cleanDataBase();
        this.daoFalso = mock(RepositorioDeLeiloes.class);
        this.enviadorDeEmail = mock(EnviadorDeEmail.class);
        this.encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);

        antiga.set(1999, 1, 20);
        this.leilao1 = new CriadorDeLeilao().para("Tv de plasma").naData(antiga).constroi();
        this.leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
    }

    @Test
    public void deveEncerrrarLeiloesQueComecaramUmaSemanaAntes() {

        dao.salva(leilao1);
        dao.salva(leilao2);

        EncerradorDeLeilao ecerrador = new EncerradorDeLeilao();
        ecerrador.encerra(true);

        List<Leilao> encerrados = dao.encerrados();

        assertEquals(2, encerrados.size());
        assertTrue(encerrados.get(0).isEncerrado());
        assertTrue(encerrados.get(1).isEncerrado());
    }

    @Test
    public void deveEncerrrarLeiloesQueComecaramUmaSemanaAntesMockito() {

        Leilao leilao1 = new CriadorDeLeilao().para("Tv de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);


        when(daoFalso.correntes()).thenReturn(leiloesAntigos);
        encerrador.encerra();

        List<Leilao> encerrados = dao.encerrados();

        assertEquals(2, encerrador.getTotalEncerrados());
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {

        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
                .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira")
                .naData(ontem).constroi();

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        encerrador.encerra();

        verify(daoFalso, never()).atualiza(leilao1);
        verify(daoFalso, never()).atualiza(leilao2);

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesCasoNaoHajaNenhum() {

        when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
    }

    @Test
    public void deveAtualizarLeiloesEncerrados() {

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));
        encerrador.encerra();

        verify(daoFalso, times(1)).atualiza(leilao1);
    }

    @Test
    public void deveEnviarEmailAoAtualizar() {

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));
        encerrador.encerra();

        InOrder inOrder = inOrder(daoFalso, enviadorDeEmail);
        inOrder.verify(daoFalso).atualiza(leilao1);
        inOrder.verify(enviadorDeEmail).envia(leilao1);
    }
}
