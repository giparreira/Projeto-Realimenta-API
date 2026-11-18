package br.com.fiap.realimenta.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Necessidade {

    private Long id;

    @NotNull(message = "A instituição é obrigatória")
    private Long organizacaoId;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 40, message = "A categoria deve ter no máximo 40 caracteres")
    private String categoria;

    @NotNull(message = "A quantidade em kg é obrigatória")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero")
    private Double quantidadeKg;

    private Boolean ativa;

    public Necessidade() {
    }

    public Necessidade(Long id, Long organizacaoId, String categoria, Double quantidadeKg, Boolean ativa) {
        this.id = id;
        this.organizacaoId = organizacaoId;
        this.categoria = categoria;
        this.quantidadeKg = quantidadeKg;
        this.ativa = ativa;
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

    public Double getQuantidadeKg() {
        return quantidadeKg;
    }

    public void setQuantidadeKg(Double quantidadeKg) {
        this.quantidadeKg = quantidadeKg;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}
