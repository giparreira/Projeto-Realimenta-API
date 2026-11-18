package br.com.fiap.realimenta.controller;

import br.com.fiap.realimenta.bo.MatchBO;
import br.com.fiap.realimenta.model.Necessidade;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchBO matchBO;

    public MatchController(MatchBO matchBO) {
        this.matchBO = matchBO;
    }

    @GetMapping("/{loteId}")
    public ResponseEntity<List<Necessidade>> sugerir(@PathVariable Long loteId) {
        return ResponseEntity.ok(matchBO.sugerirInstituicoes(loteId));
    }
}
