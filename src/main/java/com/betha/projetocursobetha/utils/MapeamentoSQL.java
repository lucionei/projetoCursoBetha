package com.betha.projetocursobetha.utils;

/**
 *
 * @author lucionei.chequeto
 */
public final class MapeamentoSQL {

    /*public static Map<String, Object> mapeiaObjeto(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> dados = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            String chave;
            field.setAccessible(true);
            if (field.isAnnotationPresent(Coluna.class)) {
                Coluna col = field.getAnnotation(Coluna.class);
                chave = col.value();
            } else {
                chave = field.getName().toUpperCase();
            }
            dados.put(chave, field.get(obj));
        }

        return dados;
    }

    public static Map<String, Object> mapeiaIdObjeto(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> dados = new HashMap<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            String chave;
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                Id id = field.getAnnotation(Id.class);
                chave = id.value();
            } else {
                chave = field.getName().toUpperCase();
            }
            dados.put(chave, field.get(obj));
        }

        return dados;
    }

    public static String mapeiaComandoInsert(Object obj) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ");

        if (obj.getClass().isAnnotationPresent(Tabela.class)) {
            Tabela t = obj.getClass().getAnnotation(Tabela.class);
            sb.append(t.schema()).append(".").append(t.nome());
        } else {
            throw new Exception(
                    String.format("Você deveria ter anotado"
                            + " a classe %s com @Tabela",
                            obj.getClass().getName()));
        }

        Map<String, Object> colunas = mapeiaObjeto(obj);

        sb.append(" (");
        boolean primeiro = true;
        for (String col : colunas.keySet()) {
            if (!primeiro) {
                sb.append(", ");
            }
            sb.append(col);
            primeiro = false;
        }
        sb.append(") VALUES (");
        primeiro = true;
        for (String col : colunas.keySet()) {
            if (!primeiro) {
                sb.append(", ");
            }
            sb.append("'").append(colunas.get(col)).append("'");
            primeiro = false;
        }

        sb.append(")");

        return sb.toString();
    }

    public static String mapeiaComandoSelect(Object obj) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT ");

        Map<String, Object> colunas = mapeiaObjeto(obj);

        boolean primeiro = true;
        for (String col : colunas.keySet()) {
            if (!primeiro) {
                sb.append(", ");
            }
            sb.append(col);
            primeiro = false;
        }

        sb.append(" FROM ");

        if (obj.getClass().isAnnotationPresent(Tabela.class)) {
            Tabela t = obj.getClass().getAnnotation(Tabela.class);
            sb.append(t.schema()).append(".").append(t.nome());
        } else {
            throw new Exception(
                    String.format("Você deveria ter anotado"
                            + " a classe %s com @Tabela",
                            obj.getClass().getName()));
        }

        sb.append(" WHERE ");

        Map<String, Object> id = mapeiaIdObjeto(obj);
        primeiro = true;
        for (String col : id.keySet()) {
            if (!primeiro) {
                break;
            }
            sb.append(col);
            primeiro = false;
        }
        sb.append(" = ");

        primeiro = true;
        for (String col : id.keySet()) {
            if (!primeiro) {
                break;
            }
            sb.append("'").append(id.get(col)).append("'");
            primeiro = false;
        }

        return sb.toString();
    }*/

}
