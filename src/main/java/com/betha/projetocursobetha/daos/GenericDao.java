package com.betha.projetocursobetha.daos;

import com.betha.projetocursobetha.utils.ConnectionUtils;
import com.betha.projetocursobetha.utils.Utils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucionei.chequeto
 */
public abstract class GenericDao {

    public Long insertGenerico(String sql, String sqlSequncia, List<Object> parametros) {
        try (Connection conn = ConnectionUtils.getConnection()) {
            Long id = getSequencia(conn, sqlSequncia);
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setLong(1, id);
                setaParametros(stm, parametros, 2);
                stm.execute();
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateDeleteGenerico(String sql, List<Object> parametros) throws Exception {
        try (Connection conn = ConnectionUtils.getConnection()) {
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                setaParametros(stm, parametros, 1);
                stm.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
    }

    private void setaParametros(PreparedStatement preparedStatement, List<Object> parametros, int posicao) throws SQLException {
        for (Object parametro : parametros) {
            if (parametro == null) {
                preparedStatement.setNull(posicao, 1);
            } else if (parametro instanceof Integer) {
                preparedStatement.setInt(posicao, (Integer) parametro);
            } else if (parametro instanceof Long) {
                preparedStatement.setLong(posicao, (Long) parametro);
            } else if (parametro instanceof Float) {
                preparedStatement.setFloat(posicao, (Float) parametro);
            } else if (parametro instanceof Double) {
                preparedStatement.setDouble(posicao, (Double) parametro);
            } else if (parametro instanceof LocalDateTime) {
                Timestamp aux = Timestamp.valueOf((LocalDateTime) parametro);
                if ((LocalDateTime) parametro == LocalDateTime.MIN) {
                    preparedStatement.setNull(posicao, java.sql.Types.TIMESTAMP);
                } else {
                    preparedStatement.setTimestamp(posicao, aux);
                }
            } else if (parametro instanceof BigDecimal) {
                preparedStatement.setBigDecimal(posicao, Utils.parseDecimal(parametro.toString()));
            } else if (parametro instanceof String) {
                preparedStatement.setString(posicao, parametro.toString());
            }
            posicao++;
        }
    }

    public List findAll(String sql, List<Object> parametros, Parse parse) throws Exception {
        List rows = new ArrayList();
        try (Connection conn = ConnectionUtils.getConnection()) {
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                setaParametros(stm, parametros, 1);
                try (ResultSet rs = stm.executeQuery()) {
                    while (rs.next()) {
                        rows.add(parse.mapping(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
        return rows;
    }

    public Object findById(String sql, List<Object> parametros, Parse parse) throws Exception {
        Object row = null;
        try (Connection conn = ConnectionUtils.getConnection()) {
            try (PreparedStatement stm = conn.prepareStatement(sql)) {
                setaParametros(stm, parametros, 1);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        row = parse.mapping(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }
        return row;
    }

    private Long getSequencia(Connection conn, String sql) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            try (ResultSet rs = stm.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }
}
