package br.com.fiap.realimenta.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErroResposta {

    private final int status;
    private final String mensagem;
    private final List<String> detalhes;
    private final LocalDateTime dataHora;

    public ErroResposta(int status, String mensagem, List<String> detalhes) {
        this.status = status;
        this.mensagem = mensagem;
        this.detalhes = detalhes;
        this.dataHora = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
