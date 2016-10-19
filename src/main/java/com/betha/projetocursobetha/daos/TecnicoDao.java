package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Tecnico;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucionei.chequeto
 */
public class TecnicoDao extends GenericDao {

    public Tecnico insert(Tecnico tecnico) throws Exception {
        String insert = "insert into tecnicos (id_tecnico, nome, tipo) values (?,?,'T')";
        String sqlSequencia = "select nextval('seq_tecnicos')";

        List<Object> parametros = new ArrayList<>();
        parametros.add(tecnico.getNome());

        Long id = super.insertGenerico(insert, sqlSequencia, parametros);

        return findById(id);

    }

    public Tecnico update(Tecnico tecnico) throws Exception {
        String update = "update tecnicos set nome = ?, tipo = 'T' where id_tecnico = ?";

        List<Object> parametros = new ArrayList<>();
        parametros.add(tecnico.getNome());
        parametros.add(tecnico.getId());

        super.updateDeleteGenerico(update, parametros);

        return findById(tecnico.getId());
    }

    public void delete(Long id) throws Exception {
        String sql = "delete from tecnicos where id_tecnico = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        super.updateDeleteGenerico(sql, parametros);
    }

    public Tecnico findById(Long id) throws Exception {
        String sql = "select * from tecnicos where id_tecnico = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        return (Tecnico) super.findById(sql, parametros, new Parse<Tecnico>() {
            @Override
            public Tecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Tecnico> findAll() throws Exception {
        String sql = "select * from tecnicos order by id_tecnico";
        List<Object> parametros = new ArrayList<>();
        return super.findAll(sql, parametros, new Parse<Tecnico>() {
            @Override
            public Tecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }
    
     public List<Tecnico> findAll(String tipo) throws Exception {
        String sql = "select * from tecnicos where tipo = ? order by id_tecnico";
        List<Object> parametros = new ArrayList<>();
        parametros.add(tipo);
        return super.findAll(sql, parametros, new Parse<Tecnico>() {
            @Override
            public Tecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Tecnico> findAll(Long pagina, int limitePagina) throws Exception {
        String sql = "select * from tecnicos order by id_tecnico limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Tecnico>() {
            @Override
            public Tecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Tecnico> findAll(Long pagina, int limitePagina, String pesquisa) throws Exception {
        String sql = "select * from tecnicos where upper(nome) like ? order by id_tecnico limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + pesquisa.toUpperCase() + "%");
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Tecnico>() {
            @Override
            public Tecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    private Tecnico parse(ResultSet resultSet) throws SQLException {
        Tecnico tecnico = new Tecnico();
        if (resultSet != null) {
            tecnico.setId(resultSet.getLong("id_tecnico"));
            tecnico.setNome(resultSet.getString("nome"));
            tecnico.setTipo(resultSet.getString("tipo"));
        }
        return tecnico;
    }

}
