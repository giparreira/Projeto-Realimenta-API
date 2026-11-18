package br.com.fiap.realimenta.controller;

import br.com.fiap.realimenta.bo.LoteBO;
import br.com.fiap.realimenta.model.Lote;
import br.com.fiap.realimenta.model.StatusLote;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteBO loteBO;

    public LoteController(LoteBO loteBO) {
        this.loteBO = loteBO;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> listar(@RequestParam(required = false) StatusLote status) {
        return ResponseEntity.ok(loteBO.listar(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(loteBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Lote> cadastrar(@Valid @RequestBody Lote lote) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loteBO.cadastrar(lote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> atualizar(@PathVariable Long id, @Valid @RequestBody Lote lote) {
        return ResponseEntity.ok(loteBO.atualizar(id, lote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        loteBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
