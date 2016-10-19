package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.Cliente;
import com.betha.projetocursobetha.utils.Utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao extends GenericDao {

    public Cliente insert(Cliente cliente) throws Exception {
        String insert = "insert into clientes (id_cliente, nome, telefone, documento, email) values (?, ?, ?, ?, ?)";
        String sqlSequencia = "select nextval('seq_clientes')";

        List<Object> parametros = new ArrayList<>();
        parametros = setParametros(cliente, Utils.INSERT);
        Long id = super.insertGenerico(insert, sqlSequencia, parametros);

        return findById(id);
    }

    public Cliente update(Cliente cliente) throws Exception {
        String update = "update clientes set nome=?, telefone=?, documento=?, email=? where id_cliente = ?";

        List<Object> parametros = new ArrayList<>();
        parametros = setParametros(cliente, Utils.UPDATE);
        super.updateDeleteGenerico(update, parametros);

        return findById(cliente.getId());
    }

    public void delete(Long id) throws Exception {
        String sql = "delete from clientes where id_cliente = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        super.updateDeleteGenerico(sql, parametros);
    }

    public Cliente findById(Long id) throws Exception {
        String sql = "select * from clientes where id_cliente = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        return (Cliente) super.findById(sql, parametros, new Parse<Cliente>() {
            @Override
            public Cliente mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Cliente> findAll() throws Exception {
        String sql = "select * from clientes order by id_cliente";
        List<Object> parametros = new ArrayList<>();
        return super.findAll(sql, parametros, new Parse<Cliente>() {
            @Override
            public Cliente mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Cliente> findAll(Long pagina, int limitePagina) throws Exception {
        String sql = "select * from clientes order by id_cliente limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Cliente>() {
            @Override
            public Cliente mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<Cliente> findAll(Long pagina, int limitePagina, String pesquisa) throws Exception {
        String sql = "select * from clientes where upper(nome) like ? order by id_cliente limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + pesquisa.toUpperCase() + "%");
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<Cliente>() {
            @Override
            public Cliente mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    private Cliente parse(ResultSet resultSet) throws SQLException {
        Cliente cliente = new Cliente();
        if (resultSet != null) {
            cliente.setId(resultSet.getLong("id_cliente"));
            cliente.setNome(resultSet.getString("nome"));
            cliente.setTelefone(resultSet.getString("telefone"));
            cliente.setDocumento(resultSet.getString("documento"));
            cliente.setEmail(resultSet.getString("email"));
        }
        return cliente;
    }

    private List<Object> setParametros(Cliente cliente, String operacao) {
        List<Object> parametros = new ArrayList<>();
        parametros.add(cliente.getNome());
        parametros.add(cliente.getTelefone());
        parametros.add(cliente.getDocumento());
        parametros.add(cliente.getEmail());
        if (operacao.equals(Utils.UPDATE)) {
            parametros.add(cliente.getId());
        }
        return parametros;
    }

}
