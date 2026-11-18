package br.com.fiap.realimenta.exception;

public class DaoException extends RuntimeException {

    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
