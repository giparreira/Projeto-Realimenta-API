package br.com.fiap.realimenta.bo;

import br.com.fiap.realimenta.dao.NecessidadeDAO;
import br.com.fiap.realimenta.exception.RegraNegocioException;
import br.com.fiap.realimenta.model.Lote;
import br.com.fiap.realimenta.model.Necessidade;
import br.com.fiap.realimenta.model.StatusLote;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MatchBO {

    private final LoteBO loteBO;
    private final NecessidadeDAO necessidadeDAO;

    public MatchBO(LoteBO loteBO, NecessidadeDAO necessidadeDAO) {
        this.loteBO = loteBO;
        this.necessidadeDAO = necessidadeDAO;
    }

    public List<Necessidade> sugerirInstituicoes(Long loteId) {
        Lote lote = loteBO.buscarPorId(loteId);

        if (lote.getStatus() != StatusLote.DISPONIVEL) {
            throw new RegraNegocioException("Somente lotes DISPONIVEIS podem ser recomendados");
        }
        if (lote.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Lote vencido não pode ser recomendado");
        }

        List<Necessidade> candidatas = necessidadeDAO.listarAtivasPorCategoria(lote.getCategoria());
        List<Necessidade> compativeis = new ArrayList<>();
        for (Necessidade necessidade : candidatas) {
            if (necessidade.getQuantidadeKg() >= lote.getQuantidadeKg()) {
                compativeis.add(necessidade);
            }
        }
        compativeis.sort(Comparator.comparingDouble(n -> n.getQuantidadeKg() - lote.getQuantidadeKg()));
        return compativeis;
    }
}
