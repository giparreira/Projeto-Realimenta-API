package br.com.fiap.realimenta.bo;

import br.com.fiap.realimenta.dao.NecessidadeDAO;
import br.com.fiap.realimenta.dao.ReservaDAO;
import br.com.fiap.realimenta.exception.RecursoNaoEncontradoException;
import br.com.fiap.realimenta.exception.RegraNegocioException;
import br.com.fiap.realimenta.model.Necessidade;
import br.com.fiap.realimenta.model.Organizacao;
import br.com.fiap.realimenta.model.TipoOrganizacao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NecessidadeBO {

    private final NecessidadeDAO necessidadeDAO;
    private final ReservaDAO reservaDAO;
    private final OrganizacaoBO organizacaoBO;

    public NecessidadeBO(NecessidadeDAO necessidadeDAO, ReservaDAO reservaDAO, OrganizacaoBO organizacaoBO) {
        this.necessidadeDAO = necessidadeDAO;
        this.reservaDAO = reservaDAO;
        this.organizacaoBO = organizacaoBO;
    }

    public Necessidade cadastrar(Necessidade necessidade) {
        validarInstituicao(necessidade);
        necessidade.setId(null);
        if (necessidade.getAtiva() == null) {
            necessidade.setAtiva(true);
        }
        return necessidadeDAO.inserir(necessidade);
    }

    public List<Necessidade> listar() {
        return necessidadeDAO.listar();
    }

    public Necessidade buscarPorId(Long id) {
        Necessidade necessidade = necessidadeDAO.buscarPorId(id);
        if (necessidade == null) {
            throw new RecursoNaoEncontradoException("Necessidade " + id + " não encontrada");
        }
        return necessidade;
    }

    public Necessidade atualizar(Long id, Necessidade necessidade) {
        buscarPorId(id);
        validarInstituicao(necessidade);
        necessidade.setId(id);
        if (necessidade.getAtiva() == null) {
            necessidade.setAtiva(true);
        }
        necessidadeDAO.atualizar(necessidade);
        return necessidade;
    }

    public void deletar(Long id) {
        buscarPorId(id);
        if (reservaDAO.existeReservaParaNecessidade(id)) {
            throw new RegraNegocioException("Não é possível excluir uma necessidade que possui reservas");
        }
        necessidadeDAO.deletar(id);
    }

    private void validarInstituicao(Necessidade necessidade) {
        Organizacao instituicao = organizacaoBO.buscarPorId(necessidade.getOrganizacaoId());
        if (instituicao.getTipo() != TipoOrganizacao.INSTITUICAO) {
            throw new RegraNegocioException("Somente organizações do tipo INSTITUICAO podem cadastrar necessidades");
        }
    }
}
