package br.com.fiap.realimenta.dao;

import br.com.fiap.realimenta.config.ConnectionFactory;
import br.com.fiap.realimenta.exception.DaoException;
import br.com.fiap.realimenta.model.Organizacao;
import br.com.fiap.realimenta.model.TipoOrganizacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizacaoDAO {

    public Organizacao inserir(Organizacao organizacao) {
        String sql = "INSERT INTO TB_RA_ORGANIZACAO (NM_ORGANIZACAO, TP_ORGANIZACAO, DS_ENDERECO, DS_EMAIL) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"ID_ORGANIZACAO"})) {
            ps.setString(1, organizacao.getNome());
            ps.setString(2, organizacao.getTipo().name());
            ps.setString(3, organizacao.getEndereco());
            ps.setString(4, organizacao.getEmail());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    organizacao.setId(chaves.getLong(1));
                }
            }
            return organizacao;
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir organização", e);
        }
    }

    public List<Organizacao> listar() {
        String sql = "SELECT ID_ORGANIZACAO, NM_ORGANIZACAO, TP_ORGANIZACAO, DS_ENDERECO, DS_EMAIL FROM TB_RA_ORGANIZACAO ORDER BY ID_ORGANIZACAO";
        List<Organizacao> lista = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar organizações", e);
        }
    }

    public Organizacao buscarPorId(Long id) {
        String sql = "SELECT ID_ORGANIZACAO, NM_ORGANIZACAO, TP_ORGANIZACAO, DS_ENDERECO, DS_EMAIL FROM TB_RA_ORGANIZACAO WHERE ID_ORGANIZACAO = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar organização", e);
        }
    }

    public boolean atualizar(Organizacao organizacao) {
        String sql = "UPDATE TB_RA_ORGANIZACAO SET NM_ORGANIZACAO = ?, TP_ORGANIZACAO = ?, DS_ENDERECO = ?, DS_EMAIL = ? WHERE ID_ORGANIZACAO = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, organizacao.getNome());
            ps.setString(2, organizacao.getTipo().name());
            ps.setString(3, organizacao.getEndereco());
            ps.setString(4, organizacao.getEmail());
            ps.setLong(5, organizacao.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar organização", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM TB_RA_ORGANIZACAO WHERE ID_ORGANIZACAO = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao deletar organização", e);
        }
    }

    public boolean possuiLotesOuNecessidades(Long id) {
        String sql = "SELECT (SELECT COUNT(*) FROM TB_RA_LOTE WHERE ID_ORGANIZACAO = ?) + (SELECT COUNT(*) FROM TB_RA_NECESSIDADE WHERE ID_ORGANIZACAO = ?) FROM DUAL";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao verificar vínculos da organização", e);
        }
    }

    private Organizacao montar(ResultSet rs) throws SQLException {
        return new Organizacao(
                rs.getLong("ID_ORGANIZACAO"),
                rs.getString("NM_ORGANIZACAO"),
                TipoOrganizacao.valueOf(rs.getString("TP_ORGANIZACAO")),
                rs.getString("DS_ENDERECO"),
                rs.getString("DS_EMAIL"));
    }
}
