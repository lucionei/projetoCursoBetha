package com.betha.projetocursobetha.models;

import com.betha.projetocursobetha.utils.Utils;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author lucionei.chequeto
 */
public class Tecnico implements Parseable {

    private Long id;
    private String nome;
    private String tipo;

    public Tecnico() {
    }

    public Tecnico(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String validaDados() {
        StringBuilder sb = new StringBuilder();
        if (nome.length() > 150) {
            sb.append("Campo nome deve possuir no máximo 150 caracteres\n");
        }
        if (Utils.isEmpty(nome)) {
            sb.append("Campo nome é obrigatório\n");
        }

        if (Utils.isEmpty(tipo)) {
            sb.append("Campo tipo é obrigatório\n");
        }

        if (!tipo.equals("T") && !tipo.equals("G")) {
            sb.append("Somente é permitido informar tipo T-Técnico ou G-Gerente");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"nome\":\"%s\",\"tipo\":\"%s\"}", id, nome, tipo);
    }

    @Override
    public void parse(Map<String, String> dados) {
        id = dados.get("id") == null || dados.get("id").isEmpty() ? null : Long.parseLong(dados.get("id"));
        nome = dados.get("nome");
        tipo = dados.get("tipo");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.nome);
        hash = 47 * hash + Objects.hashCode(this.tipo);
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
        final Tecnico other = (Tecnico) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
