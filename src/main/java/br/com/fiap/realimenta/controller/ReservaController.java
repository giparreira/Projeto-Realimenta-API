package br.com.fiap.realimenta.controller;

import br.com.fiap.realimenta.bo.ReservaBO;
import br.com.fiap.realimenta.model.Reserva;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaBO reservaBO;

    public ReservaController(ReservaBO reservaBO) {
        this.reservaBO = reservaBO;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Reserva> reservar(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaBO.reservar(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> atualizarStatus(@PathVariable Long id, @RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaBO.atualizarStatus(id, reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        reservaBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
