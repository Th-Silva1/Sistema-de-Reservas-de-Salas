/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.DAO;

import br.com.entidade.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thalles
 */
public class ReservaDAO {
    
    public boolean inserirReserva(Reserva reserva) {
        String sql = "INSERT INTO reservas (id_usuario, id_sala, data_hora_inicio, data_hora_fim, finalidade, status_reserva) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, reserva.getIdUsuario());
            stmt.setInt(2, reserva.getIdSala());
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getDataHoraInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getDataHoraFim()));
            stmt.setString(5, reserva.getFinalidade());
            stmt.setString(6, reserva.getStatusReserva());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir reserva: " + e.getMessage());
            return false;
        }
    }
    
    public List<Reserva> listarTodasReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = """
            SELECT r.*, u.nome_usuario, s.nome_sala 
            FROM reservas r 
            JOIN usuarios u ON r.id_usuario = u.id_usuario 
            JOIN salas s ON r.id_sala = s.id_sala 
            ORDER BY r.data_hora_inicio DESC
        """;
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reservas.add(criarReservaDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas: " + e.getMessage());
        }
        
        return reservas;
    }
    
    public List<Reserva> listarReservasPorUsuario(int idUsuario) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = """
            SELECT r.*, u.nome_usuario, s.nome_sala 
            FROM reservas r 
            JOIN usuarios u ON r.id_usuario = u.id_usuario 
            JOIN salas s ON r.id_sala = s.id_sala 
            WHERE r.id_usuario = ? 
            ORDER BY r.data_hora_inicio DESC
        """;
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(criarReservaDoResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas por usuário: " + e.getMessage());
        }
        
        return reservas;
    }
    
    public List<Reserva> listarReservasPorSala(int idSala) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = """
            SELECT r.*, u.nome_usuario, s.nome_sala 
            FROM reservas r 
            JOIN usuarios u ON r.id_usuario = u.id_usuario 
            JOIN salas s ON r.id_sala = s.id_sala 
            WHERE r.id_sala = ? 
            ORDER BY r.data_hora_inicio DESC
        """;
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idSala);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(criarReservaDoResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas por sala: " + e.getMessage());
        }
        
        return reservas;
    }
    
    public Reserva buscarReservaPorId(int idReserva) {
        String sql = """
            SELECT r.*, u.nome_usuario, s.nome_sala 
            FROM reservas r 
            JOIN usuarios u ON r.id_usuario = u.id_usuario 
            JOIN salas s ON r.id_sala = s.id_sala 
            WHERE r.id_reserva = ?
        """;
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idReserva);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarReservaDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reserva por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean atualizarReserva(Reserva reserva) {
        String sql = "UPDATE reservas SET id_sala = ?, data_hora_inicio = ?, data_hora_fim = ?, finalidade = ?, status_reserva = ? WHERE id_reserva = ?";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, reserva.getIdSala());
            stmt.setTimestamp(2, Timestamp.valueOf(reserva.getDataHoraInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getDataHoraFim()));
            stmt.setString(4, reserva.getFinalidade());
            stmt.setString(5, reserva.getStatusReserva());
            stmt.setInt(6, reserva.getIdReserva());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar reserva: " + e.getMessage());
            return false;
        }
    }
    
    public boolean excluirReserva(int idReserva) {
        String sql = "DELETE FROM reservas WHERE id_reserva = ?";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idReserva);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir reserva: " + e.getMessage());
            return false;
        }
    }
    
    public boolean verificarConflito(Reserva reserva) {
        String sql = """
            SELECT COUNT(*) FROM reservas 
            WHERE id_sala = ? 
            AND status_reserva != 'CANCELADA'
            AND ((data_hora_inicio < ? AND data_hora_fim > ?) 
                 OR (data_hora_inicio < ? AND data_hora_fim > ?)
                 OR (data_hora_inicio >= ? AND data_hora_fim <= ?))
        """;
        
        if (reserva.getIdReserva() > 0) {
            sql += " AND id_reserva != ?";
        }
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, reserva.getIdSala());
            stmt.setTimestamp(2, Timestamp.valueOf(reserva.getDataHoraFim()));
            stmt.setTimestamp(3, Timestamp.valueOf(reserva.getDataHoraInicio()));
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getDataHoraFim()));
            stmt.setTimestamp(5, Timestamp.valueOf(reserva.getDataHoraFim()));
            stmt.setTimestamp(6, Timestamp.valueOf(reserva.getDataHoraInicio()));
            stmt.setTimestamp(7, Timestamp.valueOf(reserva.getDataHoraFim()));
            
            if (reserva.getIdReserva() > 0) {
                stmt.setInt(8, reserva.getIdReserva());
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao verificar conflito: " + e.getMessage());
        }
        
        return false;
    }
    
    private Reserva criarReservaDoResultSet(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(rs.getInt("id_reserva"));
        reserva.setIdUsuario(rs.getInt("id_usuario"));
        reserva.setIdSala(rs.getInt("id_sala"));
        
        Timestamp inicioTimestamp = rs.getTimestamp("data_hora_inicio");
        if (inicioTimestamp != null) {
            reserva.setDataHoraInicio(inicioTimestamp.toLocalDateTime());
        }
        
        Timestamp fimTimestamp = rs.getTimestamp("data_hora_fim");
        if (fimTimestamp != null) {
            reserva.setDataHoraFim(fimTimestamp.toLocalDateTime());
        }
        
        reserva.setFinalidade(rs.getString("finalidade"));
        reserva.setStatusReserva(rs.getString("status_reserva"));
        
        Timestamp criacaoTimestamp = rs.getTimestamp("data_criacao");
        if (criacaoTimestamp != null) {
            reserva.setDataCriacao(criacaoTimestamp.toLocalDateTime());
        }
        
        // Propriedades auxiliares
        reserva.setNomeUsuario(rs.getString("nome_usuario"));
        reserva.setNomeSala(rs.getString("nome_sala"));
        
        return reserva;
    }
}