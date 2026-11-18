package br.com.fiap.realimenta.dao;

import br.com.fiap.realimenta.config.ConnectionFactory;
import br.com.fiap.realimenta.exception.DaoException;
import br.com.fiap.realimenta.model.Reserva;
import br.com.fiap.realimenta.model.StatusReserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaDAO {

    private static final String COLUNAS = "ID_RESERVA, ID_LOTE, ID_NECESSIDADE, ST_RESERVA, DT_CRIACAO";

    public Reserva inserir(Reserva reserva) {
        String sql = "INSERT INTO TB_RA_RESERVA (ID_LOTE, ID_NECESSIDADE, ST_RESERVA) VALUES (?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql, new String[]{"ID_RESERVA"})) {
            ps.setLong(1, reserva.getLoteId());
            ps.setLong(2, reserva.getNecessidadeId());
            ps.setString(3, reserva.getStatus().name());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    reserva.setId(chaves.getLong(1));
                }
            }
            return reserva;
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir reserva", e);
        }
    }

    public List<Reserva> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_RESERVA ORDER BY ID_RESERVA";
        List<Reserva> lista = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montar(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar reservas", e);
        }
    }

    public Reserva buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM TB_RA_RESERVA WHERE ID_RESERVA = ?";
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
            throw new DaoException("Erro ao buscar reserva", e);
        }
    }

    public boolean existeReservaAtivaParaLote(Long loteId) {
        String sql = "SELECT COUNT(*) FROM TB_RA_RESERVA WHERE ID_LOTE = ? AND ST_RESERVA <> 'CANCELADA'";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, loteId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao verificar reserva do lote", e);
        }
    }

    public boolean existeReservaParaLote(Long loteId) {
        return contar("SELECT COUNT(*) FROM TB_RA_RESERVA WHERE ID_LOTE = ?", loteId) > 0;
    }

    public boolean existeReservaParaNecessidade(Long necessidadeId) {
        return contar("SELECT COUNT(*) FROM TB_RA_RESERVA WHERE ID_NECESSIDADE = ?", necessidadeId) > 0;
    }

    private int contar(String sql, Long id) {
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao consultar reservas", e);
        }
    }

    public boolean atualizarStatus(Long id, StatusReserva status) {
        String sql = "UPDATE TB_RA_RESERVA SET ST_RESERVA = ? WHERE ID_RESERVA = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar status da reserva", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM TB_RA_RESERVA WHERE ID_RESERVA = ?";
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao deletar reserva", e);
        }
    }

    private Reserva montar(ResultSet rs) throws SQLException {
        return new Reserva(
                rs.getLong("ID_RESERVA"),
                rs.getLong("ID_LOTE"),
                rs.getLong("ID_NECESSIDADE"),
                StatusReserva.valueOf(rs.getString("ST_RESERVA")),
                rs.getDate("DT_CRIACAO").toLocalDate());
    }
}
