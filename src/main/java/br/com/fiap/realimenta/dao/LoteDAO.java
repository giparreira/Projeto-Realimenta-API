package br.com.fiap.realimenta.dao;

import br.com.fiap.realimenta.config.ConnectionFactory;
import br.com.fiap.realimenta.exception.DaoException;
import br.com.fiap.realimenta.model.Lote;
import br.com.fiap.realimenta.model.StatusLote;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LoteDAO {

    private static final String COLUNAS = "ID_LOTE, ID_ORGANIZACAO, DS_CATEGORIA, DS_LOTE, NR_QUANTIDADE_KG, DT_VALIDADE, DS_CONSERVACAO, DT_RETIRADA, ST_LOTE";

    public Lote inserir(Lote lote) {
        String sql = "INSERT INTO TB_RA_LOTE (ID_ORGANIZACAO, DS_CATEGORIA, DS_LOTE, NR_QUANTIDADE_KG, DT_VALIDADE, DS_CONSERVACAO, DT_RETIRADA, ST_LOTE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"ID_LOTE"})) {
            ps.setLong(1, lote.getOrganizacaoId());
            ps.setString(2, lote.getCategoria());
            ps.setString(3, lote.getDescricao());
            ps.setDouble(4, lote.getQuantidadeKg());
            ps.setDate(5, Date.valueOf(lote.getDataValidade()));
            ps.setString(6, lote.getConservacao());
            ps.setDate(7, Date.valueOf(lote.getDataRetirada()));
            ps.setString(8, lote.getStatus().name());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    lote.setId(chaves.getLong(1));
                }
            }
            return lote;
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir lote", e);
        }
    }

    public List<Lote> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_LOTE ORDER BY ID_LOTE";
        return consultarLista(sql, null);
    }

    public List<Lote> listarPorStatus(StatusLote status) {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_LOTE WHERE ST_LOTE = ? ORDER BY ID_LOTE";
        return consultarLista(sql, status.name());
    }

    public Lote buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_LOTE WHERE ID_LOTE = ?";
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
            throw new DaoException("Erro ao buscar lote", e);
        }
    }

    public boolean atualizar(Lote lote) {
        String sql = "UPDATE TB_RA_LOTE SET ID_ORGANIZACAO = ?, DS_CATEGORIA = ?, DS_LOTE = ?, NR_QUANTIDADE_KG = ?, DT_VALIDADE = ?, DS_CONSERVACAO = ?, DT_RETIRADA = ?, ST_LOTE = ? WHERE ID_LOTE = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, lote.getOrganizacaoId());
            ps.setString(2, lote.getCategoria());
            ps.setString(3, lote.getDescricao());
            ps.setDouble(4, lote.getQuantidadeKg());
            ps.setDate(5, Date.valueOf(lote.getDataValidade()));
            ps.setString(6, lote.getConservacao());
            ps.setDate(7, Date.valueOf(lote.getDataRetirada()));
            ps.setString(8, lote.getStatus().name());
            ps.setLong(9, lote.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar lote", e);
        }
    }

    public boolean atualizarStatus(Long id, StatusLote status) {
        String sql = "UPDATE TB_RA_LOTE SET ST_LOTE = ? WHERE ID_LOTE = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar status do lote", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM TB_RA_LOTE WHERE ID_LOTE = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao deletar lote", e);
        }
    }

    private List<Lote> consultarLista(String sql, String parametro) {
        List<Lote> lista = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setString(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar lotes", e);
        }
    }

    private Lote montar(ResultSet rs) throws SQLException {
        return new Lote(
                rs.getLong("ID_LOTE"),
                rs.getLong("ID_ORGANIZACAO"),
                rs.getString("DS_CATEGORIA"),
                rs.getString("DS_LOTE"),
                rs.getDouble("NR_QUANTIDADE_KG"),
                rs.getDate("DT_VALIDADE").toLocalDate(),
                rs.getString("DS_CONSERVACAO"),
                rs.getDate("DT_RETIRADA").toLocalDate(),
                StatusLote.valueOf(rs.getString("ST_LOTE")));
    }
}
