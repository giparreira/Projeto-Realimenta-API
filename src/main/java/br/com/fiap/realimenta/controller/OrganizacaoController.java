package br.com.fiap.realimenta.controller;

import br.com.fiap.realimenta.bo.OrganizacaoBO;
import br.com.fiap.realimenta.model.Organizacao;
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
@RequestMapping("/api/organizacoes")
public class OrganizacaoController {

    private final OrganizacaoBO organizacaoBO;

    public OrganizacaoController(OrganizacaoBO organizacaoBO) {
        this.organizacaoBO = organizacaoBO;
    }

    @GetMapping
    public ResponseEntity<List<Organizacao>> listar() {
        return ResponseEntity.ok(organizacaoBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(organizacaoBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Organizacao> cadastrar(@Valid @RequestBody Organizacao organizacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoBO.cadastrar(organizacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organizacao> atualizar(@PathVariable Long id, @Valid @RequestBody Organizacao organizacao) {
        return ResponseEntity.ok(organizacaoBO.atualizar(id, organizacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        organizacaoBO.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
