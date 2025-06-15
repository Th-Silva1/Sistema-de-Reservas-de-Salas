/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template"
 */
package br.com.DAO;
import java.sql.*;

public class DAO {
    private static final String URL = "jdbc:mysql://localhost:3306/projeeweb?autoReconnect=true&useSSL=false";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado", e);
        }
    }
    
    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
    }
    
    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    public static boolean testarConexao() {
        try (Connection conexao = getConexao()) {
            return conexao != null && !conexao.isClosed();
        } catch (Exception e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}
