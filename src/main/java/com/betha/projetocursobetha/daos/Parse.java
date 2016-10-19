package com.betha.projetocursobetha.daos;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lucionei.chequeto
 */
public interface Parse<T> {

    T mapping(ResultSet resultSet) throws SQLException;
}
