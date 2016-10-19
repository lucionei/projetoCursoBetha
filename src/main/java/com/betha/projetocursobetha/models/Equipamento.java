package com.betha.projetocursobetha.models;

import com.betha.projetocursobetha.utils.Utils;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author lucionei.chequeto
 */
public class Equipamento implements Parseable {

    private Long id;
    private String descricao;

    public Equipamento() {
    }

    public Equipamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String validaDados() {
        StringBuilder sb = new StringBuilder();
        if (descricao.length() > 150) {
            sb.append("Campo descrição deve possuir no máximo 150 caracteres\n");
        }
        if (Utils.isEmpty(descricao)) {
            sb.append("Campo descrição é obrigatório\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"descricao\":\"%s\"}", id, descricao);
    }

    @Override
    public void parse(Map<String, String> dados) {
        id = dados.get("id") == null || dados.get("id").isEmpty() ? null : Long.parseLong(dados.get("id"));
        descricao = dados.get("descricao");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.descricao);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Equipamento other = (Equipamento) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
