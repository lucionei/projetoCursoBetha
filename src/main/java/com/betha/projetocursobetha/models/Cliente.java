package com.betha.projetocursobetha.models;

import com.betha.projetocursobetha.utils.Utils;
import java.util.Map;
import java.util.Objects;

public class Cliente implements Parseable {

    private static final int[] PESOCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private Long id;
    private String nome;
    private String telefone;
    private String documento;
    private String email;

    public Cliente() {
    }

    public Cliente(Long id) {
        this.id = id;
    }

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

    public String validaDados() {
        StringBuilder sb = new StringBuilder();
        if (nome.length() > 200) {
            sb.append("Campo nome deve possuir no máximo 200 caracteres\n");
        }
        if (telefone.length() > 20) {
            sb.append("Campo telefone deve possuir no máximo 20 caracteres\n");
        }
        if (documento.length() > 20) {
            sb.append("Campo documento deve possuir no máximo 20 caracteres\n");
        }
        if (email.length() > 120) {
            sb.append("Campo e-mail deve possuir no máximo 120 caracteres\n");
        }
        if (Utils.isEmpty(nome)) {
            sb.append("Campo nome é obrigatório\n");
        }
        if (Utils.isEmpty(telefone)) {
            sb.append("Campo telefone é obrigatório\n");
        }
        if (Utils.isEmpty(documento)) {
            sb.append("Campo documento é obrigatório\n");
        }
        if (!Utils.isEmpty(email)) {
            if (!email.matches("\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b")) {
                sb.append("Informe um e-mail válido\n");
            }
        }
        if (!isValidCPF(documento)) {
            if (!isValidCNPJ(documento)) {
                sb.append("Informe um documento válido\n");
            }
        }
        return sb.toString();
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    private static boolean isValidCPF(String cpf) {
        if ((cpf == null) || (cpf.length() != 11)) {
            return false;
        }

        Integer digito1 = calcularDigito(cpf.substring(0, 9), PESOCPF);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, PESOCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    private static boolean isValidCNPJ(String cnpj) {
        if ((cnpj == null) || (cnpj.length() != 14)) {
            return false;
        }

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), PESOCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, PESOCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"nome\":\"%s\",\"telefone\":\"%s\",\"documento\":\"%s\",\"email\":\"%s\"}", id, nome, telefone, documento, email);
    }

    @Override
    public void parse(Map<String, String> dados) {
        id = dados.get("id") == null || dados.get("id").isEmpty() ? null : Long.parseLong(dados.get("id"));
        nome = dados.get("nome");
        telefone = Utils.getNumeros(dados.get("telefone"));
        documento = Utils.getNumeros(dados.get("documento"));
        email = dados.get("email");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.nome);
        hash = 17 * hash + Objects.hashCode(this.telefone);
        hash = 17 * hash + Objects.hashCode(this.documento);
        hash = 17 * hash + Objects.hashCode(this.email);
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
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.telefone, other.telefone)) {
            return false;
        }
        if (!Objects.equals(this.documento, other.documento)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
