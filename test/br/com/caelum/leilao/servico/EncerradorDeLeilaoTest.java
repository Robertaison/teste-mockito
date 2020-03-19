package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void setup(){
        dao.cleanDataBase();
        this.daoFalso = mock(RepositorioDeLeiloes.class);
        this.enviadorDeEmail = mock(EnviadorDeEmail.class);
        this.encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
    }

    @Test
    public void deveEncerrrarLeiloesQueComecaramUmaSemanaAntes() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("Tv de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();


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
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("Tv de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
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

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

        EnviadorDeEmail enviadorDeEmail = mock(EnviadorDeEmail.class);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
        encerrador.encerra();

        verify(daoFalso, never()).atualiza(leilao1);
        verify(daoFalso, never()).atualiza(leilao2);

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesCasoNaoHajaNenhum() {

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
    }

    @Test
    public void deveAtualizarLeiloesEncerrados() {
        Calendar data = Calendar.getInstance();
        data.set(1999, 5, 20);

        Leilao leilao = new CriadorDeLeilao().para("Celular")
                .naData(data).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao));

        EncerradorDeLeilao encerradorDeLeilao = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
        encerradorDeLeilao.encerra();

        verify(daoFalso, times(1)).atualiza(leilao);
    }
}
