package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Equipamento;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucionei.chequeto
 */
public class EquipamentoDao extends GenericDao {

    public Equipamento insert(Equipamento equipamento) throws Exception {
        String insert = "insert into equipamentos (id_equipamento, descricao) values (?,?)";
        String sqlSequencia = "select nextval('seq_equipamentos')";

        List<Object> parametros = new ArrayList<>();
        parametros.add(equipamento.getDescricao());

        Long id = super.insertGenerico(insert, sqlSequencia, parametros);

        return findById(id);

    }

    public Equipamento update(Equipamento equipamento) throws Exception {
        String update = "update equipamentos set descricao = ? where id_equipamento = ?";

        List<Object> parametros = new ArrayList<>();
        parametros.add(equipamento.getDescricao());
        parametros.add(equipamento.getId());

        super.updateDeleteGenerico(update, parametros);

        return findById(equipamento.getId());
    }

    public void delete(Long id) throws Exception {
        String sql = "delete from equipamentos where id_equipamento = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        super.updateDeleteGenerico(sql, parametros);
    }

    public Equipamento findById(Long id) throws Exception {
        String sql = "select * from equipamentos where id_equipamento = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        return (Equipamento) super.findById(sql, parametros, new Parse<Equipamento>() {
            @Override
            public Equipamento mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Equipamento> findAll() throws Exception {
        String sql = "select * from equipamentos order by id_equipamento";
        List<Object> parametros = new ArrayList<>();
        return super.findAll(sql, parametros, new Parse<Equipamento>() {
            @Override
            public Equipamento mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Equipamento> findAll(Long pagina, int limitePagina) throws Exception {
        String sql = "select * from equipamentos order by id_equipamento limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Equipamento>() {
            @Override
            public Equipamento mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Equipamento> findAll(Long pagina, int limitePagina, String pesquisa) throws Exception {
        String sql = "select * from equipamentos where upper(descricao) like ? order by id_equipamento limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + pesquisa.toUpperCase() + "%");
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Equipamento>() {
            @Override
            public Equipamento mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    private Equipamento parse(ResultSet resultSet) throws SQLException {
        Equipamento equipamento = new Equipamento();
        if (resultSet != null) {
            equipamento.setId(resultSet.getLong("id_equipamento"));
            equipamento.setDescricao(resultSet.getString("descricao"));
        }
        return equipamento;
    }

}
