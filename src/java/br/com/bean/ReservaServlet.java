/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package br.com.bean;

import br.com.DAO.ReservaDAO;
import br.com.DAO.SalaDAO;
import br.com.entidade.Reserva;
import br.com.entidade.Sala;
import br.com.entidade.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Thalles
 */
@WebServlet(name = "ReservaServlet", urlPatterns = {"/ReservaServlet"})
public class ReservaServlet extends HttpServlet {
    
    private ReservaDAO reservaDAO;
    private SalaDAO salaDAO;
    
    @Override
    public void init() throws ServletException {
        reservaDAO = new ReservaDAO();
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
                listarReservas(request, response);
                break;
            case "nova":
                novaReserva(request, response);
                break;
            case "editar":
                editarReserva(request, response);
                break;
            case "excluir":
                excluirReserva(request, response);
                break;
            case "confirmar":
                confirmarReserva(request, response);
                break;
            case "cancelar":
                cancelarReserva(request, response);
                break;
            default:
                listarReservas(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String acao = request.getParameter("acao");
        
        if ("criar".equals(acao)) {
            criarReserva(request, response);
        } else if ("atualizar".equals(acao)) {
            atualizarReserva(request, response);
        }
    }
    
    private void listarReservas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession();
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            
            List<Reserva> reservas;
            
            if (usuarioLogado != null && usuarioLogado.isAdmin()) {
                reservas = reservaDAO.listarTodasReservas();
            } else if (usuarioLogado != null) {
                reservas = reservaDAO.listarReservasPorUsuario(usuarioLogado.getIdUsuario());
            } else {
                response.sendRedirect("login.jsp");
                return;
            }
            
            request.setAttribute("reservas", reservas);
            request.getRequestDispatcher("reservas.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Erro ao listar reservas: " + e.getMessage());
            response.sendRedirect("reservas.jsp?erro=Erro ao carregar reservas.");
        }
    }
    
    private void novaReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Sala> salas = salaDAO.listarSalasDisponiveis();
            request.setAttribute("salas", salas);
            request.getRequestDispatcher("novaReserva.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar salas: " + e.getMessage());
            response.sendRedirect("novaReserva.jsp?erro=Erro ao carregar salas.");
        }
    }
    
    private void criarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuarioLogado == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String idSalaStr = request.getParameter("idSala");
        String dataReserva = request.getParameter("dataReserva");
        String horaInicio = request.getParameter("horaInicio");
        String horaFim = request.getParameter("horaFim");
        String finalidade = request.getParameter("finalidade");
        
        // Validações
        if (idSalaStr == null || dataReserva == null || horaInicio == null || 
            horaFim == null || finalidade == null || finalidade.trim().isEmpty()) {
            response.sendRedirect("novaReserva.jsp?erro=Por favor, preencha todos os campos!");
            return;
        }
        
        try {
            int idSala = Integer.parseInt(idSalaStr);
            
            // Converter strings para LocalDateTime
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            
            LocalDateTime dataHoraInicio = LocalDateTime.parse(dataReserva + "T" + horaInicio + ":00");
            LocalDateTime dataHoraFim = LocalDateTime.parse(dataReserva + "T" + horaFim + ":00");
            
            // Validações de data/hora
            if (dataHoraInicio.isBefore(LocalDateTime.now())) {
                response.sendRedirect("novaReserva.jsp?erro=Não é possível fazer reservas para datas passadas!");
                return;
            }
            
            if (dataHoraFim.isBefore(dataHoraInicio) || dataHoraFim.equals(dataHoraInicio)) {
                response.sendRedirect("novaReserva.jsp?erro=A hora de fim deve ser posterior à hora de início!");
                return;
            }
            
            // Criar reserva
            Reserva reserva = new Reserva();
            reserva.setIdUsuario(usuarioLogado.getIdUsuario());
            reserva.setIdSala(idSala);
            reserva.setDataHoraInicio(dataHoraInicio);
            reserva.setDataHoraFim(dataHoraFim);
            reserva.setFinalidade(finalidade.trim());
            
            // Verificar conflitos
            if (reservaDAO.verificarConflito(reserva)) {
                response.sendRedirect("novaReserva.jsp?erro=Já existe uma reserva para esta sala no horário solicitado!");
                return;
            }
            
            boolean sucesso = reservaDAO.inserirReserva(reserva);
            
            if (sucesso) {
                response.sendRedirect("ReservaServlet?sucesso=Reserva criada com sucesso!");
            } else {
                response.sendRedirect("novaReserva.jsp?erro=Erro ao criar reserva. Tente novamente.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("novaReserva.jsp?erro=Dados inválidos!");
        } catch (DateTimeParseException e) {
            response.sendRedirect("novaReserva.jsp?erro=Data ou hora inválida!");
        } catch (Exception e) {
            System.err.println("Erro ao criar reserva: " + e.getMessage());
            response.sendRedirect("novaReserva.jsp?erro=Erro interno do sistema.");
        }
    }
    
    private void editarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReserva = Integer.parseInt(request.getParameter("id"));
            Reserva reserva = reservaDAO.buscarReservaPorId(idReserva);
            
            if (reserva != null) {
                List<Sala> salas = salaDAO.listarSalasDisponiveis();
                request.setAttribute("reservaEdicao", reserva);
                request.setAttribute("salas", salas);
                request.getRequestDispatcher("editarReserva.jsp").forward(request, response);
            } else {
                response.sendRedirect("ReservaServlet?erro=Reserva não encontrada!");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("ReservaServlet?erro=ID de reserva inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao editar reserva: " + e.getMessage());
            response.sendRedirect("ReservaServlet?erro=Erro ao carregar reserva para edição.");
        }
    }
    
    private void atualizarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            int idSala = Integer.parseInt(request.getParameter("idSala"));
            String dataReserva = request.getParameter("dataReserva");
            String horaInicio = request.getParameter("horaInicio");
            String horaFim = request.getParameter("horaFim");
            String finalidade = request.getParameter("finalidade");
            
            LocalDateTime dataHoraInicio = LocalDateTime.parse(dataReserva + "T" + horaInicio + ":00");
            LocalDateTime dataHoraFim = LocalDateTime.parse(dataReserva + "T" + horaFim + ":00");
            
            Reserva reserva = reservaDAO.buscarReservaPorId(idReserva);
            if (reserva == null) {
                response.sendRedirect("ReservaServlet?erro=Reserva não encontrada!");
                return;
            }
            
            reserva.setIdSala(idSala);
            reserva.setDataHoraInicio(dataHoraInicio);
            reserva.setDataHoraFim(dataHoraFim);
            reserva.setFinalidade(finalidade.trim());
            
            // Verificar conflitos (excluindo a própria reserva)
            if (reservaDAO.verificarConflito(reserva)) {
                response.sendRedirect("editarReserva.jsp?id=" + idReserva + "&erro=Conflito de horário com outra reserva!");
                return;
            }
            
            boolean sucesso = reservaDAO.atualizarReserva(reserva);
            
            if (sucesso) {
                response.sendRedirect("ReservaServlet?sucesso=Reserva atualizada com sucesso!");
            } else {
                response.sendRedirect("ReservaServlet?erro=Erro ao atualizar reserva.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao atualizar reserva: " + e.getMessage());
            response.sendRedirect("ReservaServlet?erro=Erro interno do sistema.");
        }
    }
    
    private void excluirReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReserva = Integer.parseInt(request.getParameter("id"));
            
            boolean sucesso = reservaDAO.excluirReserva(idReserva);
            
            if (sucesso) {
                response.sendRedirect("ReservaServlet?sucesso=Reserva excluída com sucesso!");
            } else {
                response.sendRedirect("ReservaServlet?erro=Erro ao excluir reserva.");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("ReservaServlet?erro=ID de reserva inválido!");
        } catch (Exception e) {
            System.err.println("Erro ao excluir reserva: " + e.getMessage());
            response.sendRedirect("ReservaServlet?erro=Erro interno do sistema.");
        }
    }
    
    private void confirmarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReserva = Integer.parseInt(request.getParameter("id"));
            Reserva reserva = reservaDAO.buscarReservaPorId(idReserva);
            
            if (reserva != null) {
                reserva.setStatusReserva("CONFIRMADA");
                boolean sucesso = reservaDAO.atualizarReserva(reserva);
                
                if (sucesso) {
                    response.sendRedirect("ReservaServlet?sucesso=Reserva confirmada com sucesso!");
                } else {
                    response.sendRedirect("ReservaServlet?erro=Erro ao confirmar reserva.");
                }
            } else {
                response.sendRedirect("ReservaServlet?erro=Reserva não encontrada!");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao confirmar reserva: " + e.getMessage());
            response.sendRedirect("ReservaServlet?erro=Erro interno do sistema.");
        }
    }
    
    private void cancelarReserva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int idReserva = Integer.parseInt(request.getParameter("id"));
            Reserva reserva = reservaDAO.buscarReservaPorId(idReserva);
            
            if (reserva != null) {
                reserva.setStatusReserva("CANCELADA");
                boolean sucesso = reservaDAO.atualizarReserva(reserva);
                
                if (sucesso) {
                    response.sendRedirect("ReservaServlet?sucesso=Reserva cancelada com sucesso!");
                } else {
                    response.sendRedirect("ReservaServlet?erro=Erro ao cancelar reserva.");
                }
            } else {
                response.sendRedirect("ReservaServlet?erro=Reserva não encontrada!");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao cancelar reserva: " + e.getMessage());
            response.sendRedirect("ReservaServlet?erro=Erro interno do sistema.");
        }
    }
}