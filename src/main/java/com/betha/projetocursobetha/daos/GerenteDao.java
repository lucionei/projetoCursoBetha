package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Gerente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucionei.chequeto
 */
public class GerenteDao extends GenericDao {

    public Gerente insert(Gerente tecnico) throws Exception {
        String insert = "insert into tecnicos (id_tecnico, nome, tipo) values (?,?,'G')";
        String sqlSequencia = "select nextval('seq_tecnicos')";

        List<Object> parametros = new ArrayList<>();
        parametros.add(tecnico.getNome());

        Long id = super.insertGenerico(insert, sqlSequencia, parametros);

        return findById(id);

    }

    public Gerente update(Gerente tecnico) throws Exception {
        String update = "update tecnicos set nome = ?, tipo = 'G' where id_tecnico = ?";

        List<Object> parametros = new ArrayList<>();
        parametros.add(tecnico.getNome());
        parametros.add(tecnico.getId());

        super.updateDeleteGenerico(update, parametros);

        return findById(tecnico.getId());
    }

    public Gerente findById(Long id) throws Exception {
        String sql = "select * from tecnicos where id_tecnico = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        return (Gerente) super.findById(sql, parametros, new Parse<Gerente>() {
            @Override
            public Gerente mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    private Gerente parse(ResultSet resultSet) throws SQLException {
        Gerente tecnico = new Gerente();
        if (resultSet != null) {
            tecnico.setId(resultSet.getLong("id_tecnico"));
            tecnico.setNome(resultSet.getString("nome"));
            tecnico.setTipo(resultSet.getString("tipo"));
        }
        return tecnico;
    }

}
