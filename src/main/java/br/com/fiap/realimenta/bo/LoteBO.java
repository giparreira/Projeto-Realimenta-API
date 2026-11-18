package br.com.fiap.realimenta.bo;

import br.com.fiap.realimenta.dao.LoteDAO;
import br.com.fiap.realimenta.dao.ReservaDAO;
import br.com.fiap.realimenta.exception.RecursoNaoEncontradoException;
import br.com.fiap.realimenta.exception.RegraNegocioException;
import br.com.fiap.realimenta.model.Lote;
import br.com.fiap.realimenta.model.Organizacao;
import br.com.fiap.realimenta.model.StatusLote;
import br.com.fiap.realimenta.model.TipoOrganizacao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoteBO {

    private final LoteDAO loteDAO;
    private final ReservaDAO reservaDAO;
    private final OrganizacaoBO organizacaoBO;

    public LoteBO(LoteDAO loteDAO, ReservaDAO reservaDAO, OrganizacaoBO organizacaoBO) {
        this.loteDAO = loteDAO;
        this.reservaDAO = reservaDAO;
        this.organizacaoBO = organizacaoBO;
    }

    public Lote cadastrar(Lote lote) {
        validarRegras(lote);
        lote.setId(null);
        lote.setStatus(StatusLote.DISPONIVEL);
        return loteDAO.inserir(lote);
    }

    public List<Lote> listar(StatusLote status) {
        if (status == null) {
            return loteDAO.listar();
        }
        return loteDAO.listarPorStatus(status);
    }

    public Lote buscarPorId(Long id) {
        Lote lote = loteDAO.buscarPorId(id);
        if (lote == null) {
            throw new RecursoNaoEncontradoException("Lote " + id + " não encontrado");
        }
        return lote;
    }

    public Lote atualizar(Long id, Lote lote) {
        Lote existente = buscarPorId(id);
        if (existente.getStatus() != StatusLote.DISPONIVEL) {
            throw new RegraNegocioException("Só é possível alterar lotes com status DISPONIVEL");
        }
        validarRegras(lote);
        lote.setId(id);
        lote.setStatus(StatusLote.DISPONIVEL);
        loteDAO.atualizar(lote);
        return lote;
    }

    public void deletar(Long id) {
        buscarPorId(id);
        if (reservaDAO.existeReservaParaLote(id)) {
            throw new RegraNegocioException("Não é possível excluir um lote que possui reservas");
        }
        loteDAO.deletar(id);
    }

    private void validarRegras(Lote lote) {
        Organizacao doador = organizacaoBO.buscarPorId(lote.getOrganizacaoId());
        if (doador.getTipo() != TipoOrganizacao.DOADOR) {
            throw new RegraNegocioException("Somente organizações do tipo DOADOR podem cadastrar lotes");
        }
        LocalDate hoje = LocalDate.now();
        if (lote.getDataValidade().isBefore(hoje)) {
            throw new RegraNegocioException("Lote com validade vencida não pode ser cadastrado");
        }
        if (lote.getDataRetirada().isBefore(hoje)) {
            throw new RegraNegocioException("A data de retirada não pode estar no passado");
        }
        if (lote.getDataRetirada().isAfter(lote.getDataValidade())) {
            throw new RegraNegocioException("A data de retirada não pode ser depois da validade do lote");
        }
    }
}
