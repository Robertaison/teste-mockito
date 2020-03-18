package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.LeilaoDao;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EncerradorDeLeilaoTest {

    private LeilaoDao dao = new LeilaoDao();

    @Before
    public void setup(){
        dao.cleanDataBase();
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
        ecerrador.encerra();

        List<Leilao> encerrados = dao.encerrados();

        assertEquals(2, encerrados.size());
        assertTrue(encerrados.get(0).isEncerrado());
        assertTrue(encerrados.get(1).isEncerrado());
    }
}
