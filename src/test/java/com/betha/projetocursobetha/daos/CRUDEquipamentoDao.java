/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Equipamento;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author lucionei.chequeto
 */
public class CRUDEquipamentoDao {

    @Test
    public void testCRUD() throws Exception {
        String descricao = "Teste";
        Equipamento equipamento = new Equipamento();
        equipamento.setDescricao(descricao);
        EquipamentoDao dao = new EquipamentoDao();

        equipamento = dao.insert(equipamento);
        assertNotNull(equipamento.getId());
        assertEquals(equipamento.getDescricao(), descricao);

        descricao = "Outro Teste";
        equipamento.setDescricao(descricao);
        equipamento = dao.update(equipamento);
        assertNotNull(equipamento.getId());
        assertEquals(equipamento.getDescricao(), descricao);

        Long id = equipamento.getId();
        dao.delete(id);
        equipamento = dao.findById(id);
        assertNull(equipamento);

        int limitePagina = 10;
        Long pagina = 1L;
        System.out.println(dao.findAll(pagina, limitePagina).toString());
    }

}
