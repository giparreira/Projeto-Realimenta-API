package br.com.fiap.realimenta.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class Reserva {

    private Long id;

    @NotNull(message = "O lote é obrigatório")
    private Long loteId;

    @NotNull(message = "A necessidade é obrigatória")
    private Long necessidadeId;

    private StatusReserva status;

    private LocalDate dataCriacao;

    public Reserva() {
    }

    public Reserva(Long id, Long loteId, Long necessidadeId, StatusReserva status, LocalDate dataCriacao) {
        this.id = id;
        this.loteId = loteId;
        this.necessidadeId = necessidadeId;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoteId() {
        return loteId;
    }

    public void setLoteId(Long loteId) {
        this.loteId = loteId;
    }

    public Long getNecessidadeId() {
        return necessidadeId;
    }

    public void setNecessidadeId(Long necessidadeId) {
        this.necessidadeId = necessidadeId;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
