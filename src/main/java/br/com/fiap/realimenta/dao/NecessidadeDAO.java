package br.com.fiap.realimenta.dao;

import br.com.fiap.realimenta.config.ConnectionFactory;
import br.com.fiap.realimenta.exception.DaoException;
import br.com.fiap.realimenta.model.Necessidade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NecessidadeDAO {

    private static final String COLUNAS = "ID_NECESSIDADE, ID_ORGANIZACAO, DS_CATEGORIA, NR_QUANTIDADE_KG, ST_ATIVA";

    public Necessidade inserir(Necessidade necessidade) {
        String sql = "INSERT INTO TB_RA_NECESSIDADE (ID_ORGANIZACAO, DS_CATEGORIA, NR_QUANTIDADE_KG, ST_ATIVA) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"ID_NECESSIDADE"})) {
            ps.setLong(1, necessidade.getOrganizacaoId());
            ps.setString(2, necessidade.getCategoria());
            ps.setDouble(3, necessidade.getQuantidadeKg());
            ps.setInt(4, necessidade.getAtiva() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    necessidade.setId(chaves.getLong(1));
                }
            }
            return necessidade;
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir necessidade", e);
        }
    }

    public List<Necessidade> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_NECESSIDADE ORDER BY ID_NECESSIDADE";
        List<Necessidade> lista = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar necessidades", e);
        }
    }

    public List<Necessidade> listarAtivasPorCategoria(String categoria) {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_NECESSIDADE WHERE ST_ATIVA = 1 AND UPPER(DS_CATEGORIA) = UPPER(?) ORDER BY ID_NECESSIDADE";
        List<Necessidade> lista = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, categoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar necessidades ativas", e);
        }
    }

    public Necessidade buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_NECESSIDADE WHERE ID_NECESSIDADE = ?";
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
            throw new DaoException("Erro ao buscar necessidade", e);
        }
    }

    public boolean atualizar(Necessidade necessidade) {
        String sql = "UPDATE TB_RA_NECESSIDADE SET ID_ORGANIZACAO = ?, DS_CATEGORIA = ?, NR_QUANTIDADE_KG = ?, ST_ATIVA = ? WHERE ID_NECESSIDADE = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, necessidade.getOrganizacaoId());
            ps.setString(2, necessidade.getCategoria());
            ps.setDouble(3, necessidade.getQuantidadeKg());
            ps.setInt(4, necessidade.getAtiva() ? 1 : 0);
            ps.setLong(5, necessidade.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar necessidade", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM TB_RA_NECESSIDADE WHERE ID_NECESSIDADE = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao deletar necessidade", e);
        }
    }

    private Necessidade montar(ResultSet rs) throws SQLException {
        return new Necessidade(
                rs.getLong("ID_NECESSIDADE"),
                rs.getLong("ID_ORGANIZACAO"),
                rs.getString("DS_CATEGORIA"),
                rs.getDouble("NR_QUANTIDADE_KG"),
                rs.getInt("ST_ATIVA") == 1);
    }
}
