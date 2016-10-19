package com.betha.projetocursobetha.models;

/**
 *
 * @author lucionei.chequeto
 */
public enum StatusChamado {
    ABERTO("01"), APROVADO("02"), CANCELADO("03"), FINALIZADO("04");

    private final String status;

    StatusChamado(String valorStatus) {
        status = valorStatus;
    }

    public String getValor() {
        return status;
    }
}
