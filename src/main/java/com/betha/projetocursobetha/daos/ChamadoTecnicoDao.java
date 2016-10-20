package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.models.ChamadoTecnico;
import com.betha.projetocursobetha.models.Cliente;
import com.betha.projetocursobetha.models.Equipamento;
import com.betha.projetocursobetha.models.Gerente;
import com.betha.projetocursobetha.models.StatusChamado;
import com.betha.projetocursobetha.models.Tecnico;
import com.betha.projetocursobetha.models.TipoChamado;
import com.betha.projetocursobetha.utils.Utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucionei.chequeto
 */
public class ChamadoTecnicoDao extends GenericDao {

    public ChamadoTecnico insert(ChamadoTecnico chamado) throws Exception {
        String insert = "insert into chamados_tecnicos (id_chamado_tecnico, descricao_problema, descricao_solucao, emissao, aprovacao, status, tipo, valor_total, id_cliente, id_tecnico, id_gerente, id_equipamento) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlSequencia = "select nextval('seq_chamados_tecnicos')";

        List<Object> parametros = new ArrayList<>();
        parametros = setParametros(chamado, Utils.INSERT);
        Long id = super.insertGenerico(insert, sqlSequencia, parametros);

        return findById(id);
    }

    public ChamadoTecnico update(ChamadoTecnico chamado) throws Exception {
        String update = "update chamados_tecnicos set descricao_problema = ?, descricao_solucao = ?, emissao = ?, aprovacao = ?, status = ?, tipo = ?, valor_total = ?, id_cliente = ?, id_tecnico = ?, id_gerente = ?, id_equipamento = ? where id_chamado_tecnico = ?";

        List<Object> parametros = new ArrayList<>();
        parametros = setParametros(chamado, Utils.UPDATE);
        super.updateDeleteGenerico(update, parametros);

        return findById(chamado.getId());
    }

    public void delete(Long id) throws Exception {
        String sql = "delete from chamados_tecnicos where id_chamado_tecnico = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        super.updateDeleteGenerico(sql, parametros);
    }

    public ChamadoTecnico findById(Long id) throws Exception {
        String sql = "select * from chamados_tecnicos where id_chamado_tecnico = ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(id);
        return (ChamadoTecnico) super.findById(sql, parametros, new Parse<ChamadoTecnico>() {
            @Override
            public ChamadoTecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<ChamadoTecnico> findAll() throws Exception {
        String sql = "select * from chamados_tecnicos order by id_chamado_tecnico";
        List<Object> parametros = new ArrayList<>();
        return super.findAll(sql, parametros, new Parse<ChamadoTecnico>() {
            @Override
            public ChamadoTecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<ChamadoTecnico> findAll(Long pagina, int limitePagina) throws Exception {
        String sql = "select * from chamados_tecnicos order by id_chamado_tecnico limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<ChamadoTecnico>() {
            @Override
            public ChamadoTecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    public List<ChamadoTecnico> findAll(Long pagina, int limitePagina, String pesquisa) throws Exception {
        String sql = "select * from chamados_tecnicos where upper(descricao_problema) like ? order by id_chamado_tecnico limit ? offset(?) * ?";
        List<Object> parametros = new ArrayList<>();
        parametros.add("%" + pesquisa.toUpperCase() + "%");
        parametros.add(limitePagina);
        parametros.add(pagina - 1);
        parametros.add(limitePagina);

        return super.findAll(sql, parametros, new Parse<ChamadoTecnico>() {
            @Override
            public ChamadoTecnico mapping(ResultSet resultSet) throws SQLException {
                return parse(resultSet);
            }
        });
    }

    private ChamadoTecnico parse(ResultSet resultSet) throws SQLException {
        ChamadoTecnico chamado = new ChamadoTecnico();
        if (resultSet != null) {
            Cliente cliente = new Cliente(resultSet.getLong("id_cliente"));
            Tecnico tecnico = new Tecnico(resultSet.getLong("id_tecnico"));
            Gerente gerente = new Gerente(resultSet.getLong("id_gerente"));
            Equipamento equipamento = new Equipamento(resultSet.getLong("id_equipamento"));
            chamado.setId(resultSet.getLong("id_chamado_tecnico"));
            chamado.setDescricaoProblema(resultSet.getString("descricao_problema"));
            chamado.setDescricaoSolucao(resultSet.getString("descricao_solucao"));
            chamado.setEmissao(resultSet.getTimestamp("emissao") == null ? LocalDateTime.MIN : resultSet.getTimestamp("emissao").toLocalDateTime());
            chamado.setAprovacao(resultSet.getTimestamp("aprovacao") == null ? LocalDateTime.MIN : resultSet.getTimestamp("aprovacao").toLocalDateTime());
            chamado.setValorTotal(resultSet.getBigDecimal("valor_total"));
            chamado.setCliente(cliente);
            chamado.setTecnico(tecnico);
            chamado.setGerente(gerente);
            chamado.setEquipamento(equipamento);

            switch (resultSet.getString("status")) {
                case "01":
                    chamado.setStatus(StatusChamado.ABERTO);
                    break;
                case "02":
                    chamado.setStatus(StatusChamado.APROVADO);
                    break;
                case "03":
                    chamado.setStatus(StatusChamado.CANCELADO);
                    break;
                case "04":
                    chamado.setStatus(StatusChamado.FINALIZADO);
                    break;
                default:
                    chamado.setStatus(StatusChamado.ABERTO);
                    break;
            }

            switch (resultSet.getString("tipo")) {
                case "I":
                    chamado.setTipo(TipoChamado.INTERNO);
                    break;
                case "E":
                    chamado.setTipo(TipoChamado.EXTERNO);
                    break;
                default:
                    chamado.setTipo(TipoChamado.INTERNO);
                    break;
            }

        }
        return chamado;
    }

    private List<Object> setParametros(ChamadoTecnico chamado, String operacao) {
        List<Object> parametros = new ArrayList<>();
        parametros.add(chamado.getDescricaoProblema());
        parametros.add(chamado.getDescricaoSolucao());
        parametros.add(chamado.getEmissao());
        parametros.add(chamado.getAprovacao());
        parametros.add(chamado.getStatus().getValor());
        parametros.add(chamado.getTipo().getValor());
        parametros.add(chamado.getValorTotal());
        parametros.add(chamado.getCliente().getId());
        parametros.add(chamado.getTecnico().getId());
        parametros.add(chamado.getGerente().getId());
        parametros.add(chamado.getEquipamento().getId());
        if (operacao.equals(Utils.UPDATE)) {
            parametros.add(chamado.getId());
        }
        return parametros;
    }

}
