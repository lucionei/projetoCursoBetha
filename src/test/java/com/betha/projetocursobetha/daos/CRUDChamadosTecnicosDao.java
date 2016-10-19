package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.ChamadoTecnico;
import com.betha.projetocursobetha.models.Cliente;
import com.betha.projetocursobetha.models.Equipamento;
import com.betha.projetocursobetha.models.Gerente;
import com.betha.projetocursobetha.models.StatusChamado;
import com.betha.projetocursobetha.models.Tecnico;
import com.betha.projetocursobetha.models.TipoChamado;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author lucionei.chequeto
 */
public class CRUDChamadosTecnicosDao {

    @Test
    public void testCRUD() throws Exception {
        ClienteDao clienteDao = new ClienteDao();
        Cliente cliente = new Cliente();
        cliente = clienteDao.findById(28L);

        TecnicoDao tecnicoDao = new TecnicoDao();
        Tecnico tecnico = new Tecnico();
        tecnico = tecnicoDao.findById(25L);

        GerenteDao gerenteDao = new GerenteDao();
        Gerente gerente = new Gerente();
        gerente = gerenteDao.findById(24L);

        EquipamentoDao equipamentoDao = new EquipamentoDao();
        Equipamento equipamento = new Equipamento();
        equipamento = equipamentoDao.findById(115L);

        ChamadoTecnicoDao chamadoDao = new ChamadoTecnicoDao();
        ChamadoTecnico chamado = new ChamadoTecnico();
        chamado.setDescricaoProblema("Teste");
        chamado.setAprovacao(LocalDateTime.MIN);
        chamado.setCliente(cliente);
        chamado.setEmissao(LocalDateTime.now());
        chamado.setEquipamento(equipamento);
        chamado.setGerente(gerente);
        chamado.setTecnico(tecnico);
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setTipo(TipoChamado.INTERNO);
        chamado.setValorTotal(BigDecimal.valueOf(10.00));

        chamado = chamadoDao.insert(chamado);
        ChamadoTecnico chamadoInserido = new ChamadoTecnico();
        chamadoInserido = chamadoDao.findById(chamado.getId());
        assertEquals(chamado.getId(), chamadoInserido.getId());

        chamado.setDescricaoProblema("Teste Alterado");
        // setar data para minimo valor quando campo vier nulo
        chamado.setAprovacao(LocalDateTime.MIN);
        chamadoDao.update(chamado);
        ChamadoTecnico chamadoAlterado = new ChamadoTecnico();
        chamadoAlterado = chamadoDao.findById(chamado.getId());
        assertEquals(chamado.getDescricaoProblema(), chamadoAlterado.getDescricaoProblema());

        chamadoDao.delete(chamado.getId());
        ChamadoTecnico chamadoDeletado = new ChamadoTecnico();
        chamadoDeletado = chamadoDao.findById(chamado.getId());
        assertNull(chamadoDeletado);

    }

}
