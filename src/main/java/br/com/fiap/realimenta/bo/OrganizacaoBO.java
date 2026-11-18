package br.com.fiap.realimenta.bo;

import br.com.fiap.realimenta.dao.OrganizacaoDAO;
import br.com.fiap.realimenta.exception.RecursoNaoEncontradoException;
import br.com.fiap.realimenta.exception.RegraNegocioException;
import br.com.fiap.realimenta.model.Organizacao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrganizacaoBO {

    private final OrganizacaoDAO organizacaoDAO;

    public OrganizacaoBO(OrganizacaoDAO organizacaoDAO) {
        this.organizacaoDAO = organizacaoDAO;
    }

    public Organizacao cadastrar(Organizacao organizacao) {
        organizacao.setId(null);
        return organizacaoDAO.inserir(organizacao);
    }

    public List<Organizacao> listar() {
        return organizacaoDAO.listar();
    }

    public Organizacao buscarPorId(Long id) {
        Organizacao organizacao = organizacaoDAO.buscarPorId(id);
        if (organizacao == null) {
            throw new RecursoNaoEncontradoException("Organização " + id + " não encontrada");
        }
        return organizacao;
    }

    public Organizacao atualizar(Long id, Organizacao organizacao) {
        Organizacao existente = buscarPorId(id);
        if (existente.getTipo() != organizacao.getTipo() && organizacaoDAO.possuiLotesOuNecessidades(id)) {
            throw new RegraNegocioException("Não é possível mudar o tipo de uma organização que já possui lotes ou necessidades");
        }
        organizacao.setId(id);
        organizacaoDAO.atualizar(organizacao);
        return organizacao;
    }

    public void deletar(Long id) {
        buscarPorId(id);
        if (organizacaoDAO.possuiLotesOuNecessidades(id)) {
            throw new RegraNegocioException("Não é possível excluir uma organização que possui lotes ou necessidades");
        }
        organizacaoDAO.deletar(id);
    }
}
