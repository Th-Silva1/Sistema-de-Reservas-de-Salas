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
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class UsuarioServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        
        if (acao == null) {
            acao = "listar";
        }
        
        switch (acao) {
            case "listar":
                listarUsuarios(request, response);
                break;
            case "editar":
                editarUsuario(request, response);
                break;
            case "excluir":
                excluirUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        
        if ("cadastrar".equals(acao)) {
            cadastrarUsuario(request, response);
        } else if ("atualizar".equals(acao)) {
            atualizarUsuario(request, response);
        }
    }
    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodosUsuarios();
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("abaAtiva", "usuarios");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
            response.sendRedirect("cadastro.jsp?erro=Erro ao carregar usuários.");
        }
    }
    
    private void cadastrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nomeUsuario = request.getParameter("nomeUsuario");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmarSenha = request.getParameter("confirmarSenha");
        String tipoUsuario = request.getParameter("tipoUsuario");
        
        // Validações
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("cadastro.jsp?erro=Por favor, preencha todos os campos obrigatórios!");
            return;
        }
        
        if (!senha.equals(confirmarSenha)) {
            response.sendRedirect("cadastro.jsp?erro=As senhas não coincidem!");
            return;
        }
        
        if (senha.length() < 6) {
            response.sendRedirect("cadastro.jsp?erro=A senha deve ter pelo menos 6 caracteres!");
            return;
        }
        
        try {
            // Verificar se email já existe
            if (usuarioDAO.emailJaExiste(email.trim())) {
                response.sendRedirect("cadastro.jsp?erro=Este email já está cadastrado!");
                return;
            }
            
            Usuario usuario = new Usuario();
            usuario.setNomeUsuario(nomeUsuario.trim());
            usuario.setEmail(email.trim().toLowerCase());
            usuario.setSenha(senha);
            usuario.setTipoUsuario(tipoUsuario != null ? tipoUsuario : "USUARIO");
            
            boolean sucesso = usuarioDAO.inserirUsuario(usuario);
            
            if (sucesso) {
                response.sendRedirect("cadastro.jsp?sucesso=Usuário cadastrado com sucesso!");
            } else {
                response.sendRedirect("cadastro.jsp?erro=Erro ao cadastrar usuário. Tente novamente.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            response.sendRedirect("cadastro.jsp?erro=Erro interno do sistema.");
        }
    }
    
    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            Usuario usuario = usuarioDAO.buscarUsuarioPorId(idUsuario);
            
            if (usuario != null) {
                request.setAttribute("usuarioEdicao", usuario);
                request.setAttribute("abaAtiva", "usuarios");
                request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            } else {
                response.sendRedirect("UsuarioServlet?erro=Usuário não encontrado!");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?erro=ID de usuário inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao editar usuário: " + e.getMessage());
            response.sendRedirect("UsuarioServlet?erro=Erro ao carregar usuário para edição.");
        }
    }
    
    private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nomeUsuario = request.getParameter("nomeUsuario");
            String email = request.getParameter("email");
            String tipoUsuario = request.getParameter("tipoUsuario");
            String ativo = request.getParameter("ativo");
            
            Usuario usuario = usuarioDAO.buscarUsuarioPorId(idUsuario);
            if (usuario == null) {
                response.sendRedirect("UsuarioServlet?erro=Usuário não encontrado!");
                return;
            }
            
            usuario.setNomeUsuario(nomeUsuario.trim());
            usuario.setEmail(email.trim().toLowerCase());
            usuario.setTipoUsuario(tipoUsuario);
            usuario.setAtivo("on".equals(ativo));
            
            boolean sucesso = usuarioDAO.atualizarUsuario(usuario);
            
            if (sucesso) {
                response.sendRedirect("UsuarioServlet?sucesso=Usuário atualizado com sucesso!");
            } else {
                response.sendRedirect("UsuarioServlet?erro=Erro ao atualizar usuário.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?erro=ID de usuário inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            response.sendRedirect("UsuarioServlet?erro=Erro interno do sistema.");
        }
    }
    
    private void excluirUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            
            // Verificar se não é o próprio usuário logado
            HttpSession session = request.getSession();
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            
            if (usuarioLogado != null && usuarioLogado.getIdUsuario() == idUsuario) {
                response.sendRedirect("UsuarioServlet?erro=Você não pode excluir seu próprio usuário!");
                return;
            }
            
            boolean sucesso = usuarioDAO.excluirUsuario(idUsuario);
            
            if (sucesso) {
                response.sendRedirect("UsuarioServlet?sucesso=Usuário excluído com sucesso!");
            } else {
                response.sendRedirect("UsuarioServlet?erro=Erro ao excluir usuário.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("UsuarioServlet?erro=ID de usuário inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
            response.sendRedirect("UsuarioServlet?erro=Erro interno do sistema.");
        }
    }
}
