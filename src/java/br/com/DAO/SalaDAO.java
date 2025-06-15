/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.DAO;

import br.com.entidade.Sala;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author Thalles
 */
public class SalaDAO {
    
    public boolean inserirSala(Sala sala) {
        String sql = "INSERT INTO salas (nome_sala, capacidade, descricao, disponivel, equipamentos) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, sala.getNomeSala());
            stmt.setInt(2, sala.getCapacidade());
            stmt.setString(3, sala.getDescricao());
            stmt.setBoolean(4, sala.isDisponivel());
            stmt.setString(5, sala.getEquipamentos());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir sala: " + e.getMessage());
            return false;
        }
    }
    
    public List<Sala> listarTodasSalas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM salas ORDER BY nome_sala";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                salas.add(criarSalaDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar salas: " + e.getMessage());
        }
        
        return salas;
    }
    
    public List<Sala> listarSalasDisponiveis() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM salas WHERE disponivel = true ORDER BY nome_sala";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                salas.add(criarSalaDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar salas disponíveis: " + e.getMessage());
        }
        
        return salas;
    }
    
    public Sala buscarSalaPorId(int idSala) {
        String sql = "SELECT * FROM salas WHERE id_sala = ?";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idSala);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarSalaDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar sala por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean atualizarSala(Sala sala) {
        String sql = "UPDATE salas SET nome_sala = ?, capacidade = ?, descricao = ?, disponivel = ?, equipamentos = ? WHERE id_sala = ?";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, sala.getNomeSala());
            stmt.setInt(2, sala.getCapacidade());
            stmt.setString(3, sala.getDescricao());
            stmt.setBoolean(4, sala.isDisponivel());
            stmt.setString(5, sala.getEquipamentos());
            stmt.setInt(6, sala.getIdSala());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar sala: " + e.getMessage());
            return false;
        }
    }
    
    public boolean excluirSala(int idSala) {
        String sql = "DELETE FROM salas WHERE id_sala = ?";
        
        try (Connection conexao = DAO.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, idSala);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir sala: " + e.getMessage());
            return false;
        }
    }
    
    private Sala criarSalaDoResultSet(ResultSet rs) throws SQLException {
        Sala sala = new Sala();
        sala.setIdSala(rs.getInt("id_sala"));
        sala.setNomeSala(rs.getString("nome_sala"));
        sala.setCapacidade(rs.getInt("capacidade"));
        sala.setDescricao(rs.getString("descricao"));
        sala.setDisponivel(rs.getBoolean("disponivel"));
        sala.setEquipamentos(rs.getString("equipamentos"));
        
        Timestamp timestamp = rs.getTimestamp("data_criacao");
        if (timestamp != null) {
            sala.setDataCriacao(timestamp.toLocalDateTime());
        }
        
        return sala;
    }
}