package br.com.fiap.realimenta.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException e) {
        ErroResposta erro = new ErroResposta(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException e) {
        ErroResposta erro = new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException e) {
        List<String> detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .toList();
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarJsonInvalido(HttpMessageNotReadableException e) {
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "JSON inválido ou campo com valor incorreto", List.of());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tratarTipoParametro(MethodArgumentTypeMismatchException e) {
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Parâmetro inválido: " + e.getName(), List.of());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ErroResposta> tratarDao(DaoException e) {
        ErroResposta erro = new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro ao acessar o banco de dados", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarGenerico(Exception e) {
        ErroResposta erro = new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
