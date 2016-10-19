package com.betha.projetocursobetha.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public final class Utils {
    
    public final static String INSERT = "INSERT";
    public final static String UPDATE = "UPDATE";
    public final static String DELETE = "DELETE";
    
    public static final Long parseLong(String value) {
        return isEmpty(value) ? null : Long.parseLong(value);
    }
    
    public static final int parseInt(String value) {
        return isEmpty(value) ? null : Integer.parseInt(value);
    }
    
    public static final boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    public static final boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
    
    public static BigDecimal parseDecimal(String value) {
        return isEmpty(value) ? BigDecimal.valueOf(0.0) : new BigDecimal(value);
    }
    
    public static final Map<String, String> parseMap(HttpServletRequest req) {
        Map<String, String> dados = new HashMap<>();
        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            dados.put(key, req.getParameter(key));
        }
        return dados;
    }

    public static LocalDateTime parseDate(String value, String pattern) {
        return isEmpty(value) ? LocalDateTime.MIN : LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
    }
    
    public static boolean isNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNull(Object... values) {
        return !isNull(values);
    }
    
    public static String nullString(Object value) {
        return isNull(value) ? "" : value.toString();
    }
    
    public static String getNumeros(String value) {
        String ret = value.replaceAll("\\D*", "" );
        return ret;
    }
    
}
