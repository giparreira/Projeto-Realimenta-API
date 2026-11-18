package br.com.fiap.realimenta.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class Lote {

    private Long id;

    @NotNull(message = "O doador é obrigatório")
    private Long organizacaoId;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 40, message = "A categoria deve ter no máximo 40 caracteres")
    private String categoria;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotNull(message = "A quantidade em kg é obrigatória")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
    private Double quantidadeKg;

    @NotNull(message = "A data de validade é obrigatória")
    private LocalDate dataValidade;

    @NotBlank(message = "A condição de conservação é obrigatória")
    @Size(max = 120, message = "A conservação deve ter no máximo 120 caracteres")
    private String conservacao;

    @NotNull(message = "A data de retirada é obrigatória")
    private LocalDate dataRetirada;

    private StatusLote status;

    public Lote() {
    }

    public Lote(Long id, Long organizacaoId, String categoria, String descricao, Double quantidadeKg,
                LocalDate dataValidade, String conservacao, LocalDate dataRetirada, StatusLote status) {
        this.id = id;
        this.organizacaoId = organizacaoId;
        this.categoria = categoria;
        this.descricao = descricao;
        this.quantidadeKg = quantidadeKg;
        this.dataValidade = dataValidade;
        this.conservacao = conservacao;
        this.dataRetirada = dataRetirada;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizacaoId() {
        return organizacaoId;
    }

    public void setOrganizacaoId(Long organizacaoId) {
        this.organizacaoId = organizacaoId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getQuantidadeKg() {
        return quantidadeKg;
    }

    public void setQuantidadeKg(Double quantidadeKg) {
        this.quantidadeKg = quantidadeKg;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getConservacao() {
        return conservacao;
    }

    public void setConservacao(String conservacao) {
        this.conservacao = conservacao;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public StatusLote getStatus() {
        return status;
    }

    public void setStatus(StatusLote status) {
        this.status = status;
    }
}
