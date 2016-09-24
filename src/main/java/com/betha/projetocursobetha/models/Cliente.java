package com.betha.projetocursobetha.models;



import java.util.Map;

public class Cliente implements Parseable {

    private Long id;
    private String nome;
    private String telefone;
    private String documento;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"nome\":\"%s\",\"telefone\":\"%s\",\"documento\":\"%s\",\"email\":\"%s\"}", id, nome, telefone, documento, email);
    }

    @Override
    public void parse(Map<String, String> dados) {
        id = dados.get("id") == null || dados.get("id").isEmpty() ? null : Long.parseLong(dados.get("id"));
        nome = dados.get("nome");
        telefone = dados.get("telefone");
        documento = dados.get("documento");
        email = dados.get("email");
    }

}
