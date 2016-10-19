package com.betha.projetocursobetha.models;

/**
 *
 * @author lucionei.chequeto
 */
public enum TipoChamado {

    INTERNO("I"), EXTERNO("E");

    private final String tipo;

    TipoChamado(String valorTipo) {
        tipo = valorTipo;
    }

    public String getValor() {
        return tipo;
    }

}
