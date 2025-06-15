/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package br.com.bean;

import br.com.DAO.UsuarioDAO;
import br.com.entidade.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thalles
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("logout".equals(acao)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("login.jsp?mensagem=Logout realizado com sucesso!");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        
        if (email == null || email.trim().isEmpty() || 
            senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("login.jsp?erro=Por favor, preencha todos os campos!");
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticarUsuario(email.trim(), senha);
            
            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);
                session.setAttribute("nomeUsuario", usuario.getNomeUsuario());
                session.setAttribute("tipoUsuario", usuario.getTipoUsuario());
                
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("login.jsp?erro=Email ou senha incorretos!");
            }
            
        } catch (Exception e) {
            System.err.println("Erro no login: " + e.getMessage());
            response.sendRedirect("login.jsp?erro=Erro interno do sistema. Tente novamente.");
        }
    }
}