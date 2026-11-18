package br.com.fiap.realimenta.bo;

import br.com.fiap.realimenta.dao.LoteDAO;
import br.com.fiap.realimenta.dao.ReservaDAO;
import br.com.fiap.realimenta.exception.RecursoNaoEncontradoException;
import br.com.fiap.realimenta.exception.RegraNegocioException;
import br.com.fiap.realimenta.model.Lote;
import br.com.fiap.realimenta.model.Necessidade;
import br.com.fiap.realimenta.model.Reserva;
import br.com.fiap.realimenta.model.StatusLote;
import br.com.fiap.realimenta.model.StatusReserva;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaBO {

    private final ReservaDAO reservaDAO;
    private final LoteDAO loteDAO;
    private final LoteBO loteBO;
    private final NecessidadeBO necessidadeBO;

    public ReservaBO(ReservaDAO reservaDAO, LoteDAO loteDAO, LoteBO loteBO, NecessidadeBO necessidadeBO) {
        this.reservaDAO = reservaDAO;
        this.loteDAO = loteDAO;
        this.loteBO = loteBO;
        this.necessidadeBO = necessidadeBO;
    }

    public Reserva reservar(Reserva reserva) {
        Lote lote = loteBO.buscarPorId(reserva.getLoteId());
        Necessidade necessidade = necessidadeBO.buscarPorId(reserva.getNecessidadeId());

        if (lote.getStatus() != StatusLote.DISPONIVEL) {
            throw new RegraNegocioException("O lote não está disponível para reserva");
        }
        if (lote.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Lote vencido não pode ser reservado");
        }
        if (reservaDAO.existeReservaAtivaParaLote(lote.getId())) {
            throw new RegraNegocioException("O lote já possui uma reserva ativa");
        }
        if (!necessidade.getAtiva()) {
            throw new RegraNegocioException("A necessidade informada está inativa");
        }
        if (!lote.getCategoria().equalsIgnoreCase(necessidade.getCategoria())) {
            throw new RegraNegocioException("A categoria do lote é diferente da categoria da necessidade");
        }
        if (lote.getQuantidadeKg() > necessidade.getQuantidadeKg()) {
            throw new RegraNegocioException("A quantidade do lote é maior que a quantidade necessária");
        }

        reserva.setId(null);
        reserva.setStatus(StatusReserva.AGENDADA);
        Reserva salva = reservaDAO.inserir(reserva);
        loteDAO.atualizarStatus(lote.getId(), StatusLote.RESERVADO);
        return reservaDAO.buscarPorId(salva.getId());
    }

    public List<Reserva> listar() {
        return reservaDAO.listar();
    }

    public Reserva buscarPorId(Long id) {
        Reserva reserva = reservaDAO.buscarPorId(id);
        if (reserva == null) {
            throw new RecursoNaoEncontradoException("Reserva " + id + " não encontrada");
        }
        return reserva;
    }

    public Reserva atualizarStatus(Long id, Reserva dados) {
        Reserva reserva = buscarPorId(id);
        StatusReserva novoStatus = dados.getStatus();
        if (novoStatus == null) {
            throw new RegraNegocioException("O novo status da reserva é obrigatório");
        }
        if (!transicaoValida(reserva.getStatus(), novoStatus)) {
            throw new RegraNegocioException("Transição inválida: " + reserva.getStatus() + " para " + novoStatus);
        }

        reservaDAO.atualizarStatus(id, novoStatus);
        if (novoStatus == StatusReserva.ENTREGUE) {
            loteDAO.atualizarStatus(reserva.getLoteId(), StatusLote.ENTREGUE);
        }
        if (novoStatus == StatusReserva.CANCELADA) {
            loteDAO.atualizarStatus(reserva.getLoteId(), StatusLote.DISPONIVEL);
        }
        return reservaDAO.buscarPorId(id);
    }

    public void deletar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getStatus() != StatusReserva.CANCELADA) {
            throw new RegraNegocioException("Só é possível excluir reservas CANCELADAS");
        }
        reservaDAO.deletar(id);
    }

    private boolean transicaoValida(StatusReserva atual, StatusReserva novo) {
        if (atual == StatusReserva.AGENDADA) {
            return novo == StatusReserva.COLETADA || novo == StatusReserva.CANCELADA;
        }
        if (atual == StatusReserva.COLETADA) {
            return novo == StatusReserva.ENTREGUE;
        }
        return false;
    }
}
