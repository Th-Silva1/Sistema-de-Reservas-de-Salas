/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package br.com.bean;

import br.com.DAO.SalaDAO;
import br.com.entidade.Sala;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thalles
 */
@WebServlet(name = "SalaServlet", urlPatterns = {"/SalaServlet"})
public class SalaServlet extends HttpServlet {
    
    private SalaDAO salaDAO;
    
    @Override
    public void init() throws ServletException {
        salaDAO = new SalaDAO();
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
                listarSalas(request, response);
                break;
            case "editar":
                editarSala(request, response);
                break;
            case "excluir":
                excluirSala(request, response);
                break;
            case "listarDisponiveis":
                listarSalasDisponiveis(request, response);
                break;
            default:
                listarSalas(request, response);
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
            cadastrarSala(request, response);
        } else if ("atualizar".equals(acao)) {
            atualizarSala(request, response);
        }
    }
    
    private void listarSalas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Sala> salas = salaDAO.listarTodasSalas();
            request.setAttribute("salas", salas);
            request.setAttribute("abaAtiva", "salas");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Erro ao listar salas: " + e.getMessage());
            response.sendRedirect("cadastro.jsp?erro=Erro ao carregar salas.");
        }
    }
    
    private void listarSalasDisponiveis(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Sala> salas = salaDAO.listarSalasDisponiveis();
            request.setAttribute("salasDisponiveis", salas);
            request.getRequestDispatcher("novaReserva.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Erro ao listar salas disponíveis: " + e.getMessage());
            response.sendRedirect("novaReserva.jsp?erro=Erro ao carregar salas.");
        }
    }
    
    private void cadastrarSala(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nomeSala = request.getParameter("nomeSala");
        String capacidadeStr = request.getParameter("capacidade");
        String descricao = request.getParameter("descricao");
        String equipamentos = request.getParameter("equipamentos");
        String disponivel = request.getParameter("disponivel");
        
        // Validações
        if (nomeSala == null || nomeSala.trim().isEmpty() ||
            capacidadeStr == null || capacidadeStr.trim().isEmpty()) {
            response.sendRedirect("cadastro.jsp?erro=Por favor, preencha todos os campos obrigatórios!");
            return;
        }
        
        try {
            int capacidade = Integer.parseInt(capacidadeStr);
            
            if (capacidade <= 0) {
                response.sendRedirect("cadastro.jsp?erro=A capacidade deve ser um número positivo!");
                return;
            }
            
            Sala sala = new Sala();
            sala.setNomeSala(nomeSala.trim());
            sala.setCapacidade(capacidade);
            sala.setDescricao(descricao != null ? descricao.trim() : "");
            sala.setEquipamentos(equipamentos != null ? equipamentos.trim() : "");
            sala.setDisponivel("on".equals(disponivel));
            
            boolean sucesso = salaDAO.inserirSala(sala);
            
            if (sucesso) {
                response.sendRedirect("cadastro.jsp?sucesso=Sala cadastrada com sucesso!");
            } else {
                response.sendRedirect("cadastro.jsp?erro=Erro ao cadastrar sala. Tente novamente.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("cadastro.jsp?erro=Capacidade deve ser um número válido!");
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar sala: " + e.getMessage());
            response.sendRedirect("cadastro.jsp?erro=Erro interno do sistema.");
        }
    }
    
    private void editarSala(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idSala = Integer.parseInt(request.getParameter("id"));
            Sala sala = salaDAO.buscarSalaPorId(idSala);
            
            if (sala != null) {
                request.setAttribute("salaEdicao", sala);
                request.setAttribute("abaAtiva", "salas");
                request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            } else {
                response.sendRedirect("SalaServlet?erro=Sala não encontrada!");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("SalaServlet?erro=ID de sala inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao editar sala: " + e.getMessage());
            response.sendRedirect("SalaServlet?erro=Erro ao carregar sala para edição.");
        }
    }
    
    private void atualizarSala(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idSala = Integer.parseInt(request.getParameter("idSala"));
            String nomeSala = request.getParameter("nomeSala");
            String capacidadeStr = request.getParameter("capacidade");
            String descricao = request.getParameter("descricao");
            String equipamentos = request.getParameter("equipamentos");
            String disponivel = request.getParameter("disponivel");
            
            int capacidade = Integer.parseInt(capacidadeStr);
            
            Sala sala = salaDAO.buscarSalaPorId(idSala);
            if (sala == null) {
                response.sendRedirect("SalaServlet?erro=Sala não encontrada!");
                return;
            }
            
            sala.setNomeSala(nomeSala.trim());
            sala.setCapacidade(capacidade);
            sala.setDescricao(descricao != null ? descricao.trim() : "");
            sala.setEquipamentos(equipamentos != null ? equipamentos.trim() : "");
            sala.setDisponivel("on".equals(disponivel));
            
            boolean sucesso = salaDAO.atualizarSala(sala);
            
            if (sucesso) {
                response.sendRedirect("SalaServlet?sucesso=Sala atualizada com sucesso!");
            } else {
                response.sendRedirect("SalaServlet?erro=Erro ao atualizar sala.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("SalaServlet?erro=Dados inválidos!");
        } catch (Exception e) {
            System.err.println("Erro ao atualizar sala: " + e.getMessage());
            response.sendRedirect("SalaServlet?erro=Erro interno do sistema.");
        }
    }
    
    private void excluirSala(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idSala = Integer.parseInt(request.getParameter("id"));
            
            boolean sucesso = salaDAO.excluirSala(idSala);
            
            if (sucesso) {
                response.sendRedirect("SalaServlet?sucesso=Sala excluída com sucesso!");
            } else {
                response.sendRedirect("SalaServlet?erro=Erro ao excluir sala. Verifique se não há reservas associadas.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("SalaServlet?erro=ID de sala inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir sala: " + e.getMessage());
            response.sendRedirect("SalaServlet?erro=Erro interno do sistema.");
        }
    }
}
