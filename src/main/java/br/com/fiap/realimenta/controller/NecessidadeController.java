package br.com.fiap.realimenta.controller;

import br.com.fiap.realimenta.bo.NecessidadeBO;
import br.com.fiap.realimenta.model.Necessidade;
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
@RequestMapping("/api/necessidades")
public class NecessidadeController {

    private final NecessidadeBO necessidadeBO;

    public NecessidadeController(NecessidadeBO necessidadeBO) {
        this.necessidadeBO = necessidadeBO;
    }

    @GetMapping
    public ResponseEntity<List<Necessidade>> listar() {
        return ResponseEntity.ok(necessidadeBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Necessidade> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(necessidadeBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Necessidade> cadastrar(@Valid @RequestBody Necessidade necessidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(necessidadeBO.cadastrar(necessidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Necessidade> atualizar(@PathVariable Long id, @Valid @RequestBody Necessidade necessidade) {
        return ResponseEntity.ok(necessidadeBO.atualizar(id, necessidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        necessidadeBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
