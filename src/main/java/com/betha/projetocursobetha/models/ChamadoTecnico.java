package com.betha.projetocursobetha.models;

import com.betha.projetocursobetha.utils.Utils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author lucionei.chequeto
 */
public class ChamadoTecnico implements Parseable {

    private Long id;
    private String descricaoProblema;
    private LocalDateTime emissao;
    private LocalDateTime aprovacao;
    private StatusChamado status;
    private TipoChamado tipo;
    private BigDecimal valorTotal;
    private Cliente cliente;
    private Tecnico tecnico;
    private Gerente gerente;
    private Equipamento equipamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
    }

    public LocalDateTime getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDateTime emissao) {
        this.emissao = emissao;
    }

    public LocalDateTime getAprovacao() {
        return aprovacao;
    }

    public void setAprovacao(LocalDateTime aprovacao) {
        this.aprovacao = aprovacao;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public TipoChamado getTipo() {
        return tipo;
    }

    public void setTipo(TipoChamado tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public Gerente getGerente() {
        return gerente;
    }

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public String validaDados() {
        StringBuilder sb = new StringBuilder();
        if (descricaoProblema.length() > 1000) {
            sb.append("Campo descrição do problema deve possuir no máximo 1000 caracteres\n");
        }

        if (Utils.isEmpty(descricaoProblema)) {
            sb.append("Campo descrição do problema é obrigatório\n");
        }

        if (Utils.isNull(emissao)) {
            sb.append("Campo emissão é obrigatório\n");
        }

        if (emissao.equals(LocalDateTime.MIN)) {
            sb.append("Campo emissão é obrigatório\n");
        }

        if (Utils.isNull(status)) {
            sb.append("Campo status é obrigatório\n");
        }

        if (Utils.isNull(tipo)) {
            sb.append("Campo tipo é obrigatório\n");
        }

        if (Utils.isNull(cliente.getId())) {
            sb.append("Campo cliente é obrigatório\n");
        }

        if (Utils.isNull(tecnico.getId())) {
            sb.append("Campo tecnico é obrigatório\n");
        }

        if (Utils.isNull(gerente.getId())) {
            sb.append("Campo gerente é obrigatório\n");
        }

        if (Utils.isNull(equipamento.getId())) {
            sb.append("Campo equipamento é obrigatório");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("{\"id\":\"%s\",\"descricaoProblema\":\"%s\",\"emissao\":\"%s\",\"aprovacao\":\"%s\",\"status\":\"%s\",\"tipo\":\"%s\",\"valorTotal\":\"%s\",\"cliente\":\"%s\",\"tecnico\":\"%s\",\"gerente\":\"%s\",\"equipamento\":\"%s\"}",
                id, descricaoProblema, Utils.nullString(emissao), Utils.nullString(aprovacao == LocalDateTime.MIN ? null : aprovacao), status.getValor(), tipo.getValor(), valorTotal, cliente.getId(), tecnico.getId(), gerente.getId(), equipamento.getId());
    }

    @Override
    public void parse(Map<String, String> dados) {
        id = dados.get("id") == null || dados.get("id").isEmpty() ? null : Long.parseLong(dados.get("id"));
        descricaoProblema = dados.get("descricaoProblema");
        emissao = Utils.parseDate(dados.get("emissao"), "dd/MM/yyyy HH:mm:ss");
        aprovacao = Utils.parseDate(dados.get("aprovacao"), "dd/MM/yyyy HH:mm:ss");
        valorTotal = Utils.parseDecimal(dados.get("valorTotal"));
        cliente = new Cliente(Utils.parseLong(dados.get("cliente")));
        tecnico = new Tecnico(Utils.parseLong(dados.get("tecnico")));
        gerente = new Gerente(Utils.parseLong(dados.get("gerente")));
        equipamento = new Equipamento(Utils.parseLong(dados.get("equipamento")));

        switch (dados.get("status")) {
            case "01":
                status = StatusChamado.ABERTO;
                break;
            case "02":
                status = StatusChamado.APROVADO;
                break;
            case "03":
                status = StatusChamado.CANCELADO;
                break;
            case "04":
                status = StatusChamado.FINALIZADO;
                break;
            default:
                status = StatusChamado.ABERTO;
                break;
        }

        switch (dados.get("tipo")) {
            case "I":
                tipo = TipoChamado.INTERNO;
                break;
            case "E":
                tipo = TipoChamado.EXTERNO;
                break;
            default:
                tipo = TipoChamado.INTERNO;
                break;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.descricaoProblema);
        hash = 89 * hash + Objects.hashCode(this.emissao);
        hash = 89 * hash + Objects.hashCode(this.aprovacao);
        hash = 89 * hash + Objects.hashCode(this.status);
        hash = 89 * hash + Objects.hashCode(this.tipo);
        hash = 89 * hash + Objects.hashCode(this.valorTotal);
        hash = 89 * hash + Objects.hashCode(this.cliente);
        hash = 89 * hash + Objects.hashCode(this.tecnico);
        hash = 89 * hash + Objects.hashCode(this.gerente);
        hash = 89 * hash + Objects.hashCode(this.equipamento);
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
        final ChamadoTecnico other = (ChamadoTecnico) obj;
        if (!Objects.equals(this.descricaoProblema, other.descricaoProblema)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.emissao, other.emissao)) {
            return false;
        }
        if (!Objects.equals(this.aprovacao, other.aprovacao)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.valorTotal, other.valorTotal)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.tecnico, other.tecnico)) {
            return false;
        }
        if (!Objects.equals(this.gerente, other.gerente)) {
            return false;
        }
        if (!Objects.equals(this.equipamento, other.equipamento)) {
            return false;
        }
        return true;
    }

}
