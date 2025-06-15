<%-- 
    Document   : reservas.jsp
    Created on : 30 de mai. de 2025, 16:12:17
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.com.entidade.Usuario"%>
<%@page import="br.com.entidade.Reserva"%>
<%@page import="java.util.List"%>
<%
    // Verificar se usuário está logado
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservas - Sistema de Reservas</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-brand">
                <h2>Sistema de Reservas</h2>
            </div>
            <div class="nav-menu">
                <a href="index.jsp" class="nav-link">Início</a>
                <% if (usuarioLogado.isAdmin()) { %>
                    <a href="ReservaServlet" class="nav-link">Todas as Reservas</a>
                <% } else {%>                
                    <a href="ReservaServlet" class="nav-link">Minhas Reservas</a>
                <% } %>
                <a href="ReservaServlet?acao=nova" class="nav-link">Nova Reserva</a>
                <% if (usuarioLogado.isAdmin()) { %>
                    <a href="UsuarioServlet" class="nav-link">Usuários</a>
                    <a href="SalaServlet" class="nav-link">Salas</a>
                <% } %>
                <div class="nav-user">
                    <span>Olá, <%= usuarioLogado.getNomeUsuario() %></span>
                    <a href="LoginServlet?acao=logout" class="btn btn-secondary">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1><%= usuarioLogado.isAdmin() ? "Todas as Reservas" : "Minhas Reservas" %></h1>
            <div class="page-actions">
                <a href="ReservaServlet?acao=nova" class="btn btn-primary">Nova Reserva</a>
            </div>
        </div>

        <% if (reservas != null && !reservas.isEmpty()) { %>
        <div class="table-section">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <% if (usuarioLogado.isAdmin()) { %>
                            <th>Usuário</th>
                        <% } %>
                        <th>Sala</th>
                        <th>Data</th>
                        <th>Horário</th>
                        <th>Finalidade</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Reserva reserva : reservas) { %>
                    <tr>
                        <td><%= reserva.getIdReserva() %></td>
                        <% if (usuarioLogado.isAdmin()) { %>
                            <td><%= reserva.getNomeUsuario() %></td>
                        <% } %>
                        <td><%= reserva.getNomeSala() %></td>
                        <td><%= reserva.getDataFormatada() %></td>
                        <td><%= reserva.getHoraInicioFormatada() %> - <%= reserva.getHoraFimFormatada() %></td>
                        <td class="finalidade-cell">
                            <span title="<%= reserva.getFinalidade() %>">
                                <%= reserva.getFinalidade().length() > 50 ? 
                                    reserva.getFinalidade().substring(0, 50) + "..." : 
                                    reserva.getFinalidade() %>
                            </span>
                        </td>
                        <td>
                            <span class="status status-<%= reserva.getStatusReserva().toLowerCase() %>">
                                <%= reserva.getStatusReserva() %>
                            </span>
                        </td>
                        <td class="actions">
                            <% if ("PENDENTE".equals(reserva.getStatusReserva())) { %>
                                <a href="ReservaServlet?acao=editar&id=<%= reserva.getIdReserva() %>" 
                                   class="btn btn-sm btn-secondary">Editar</a>
                                
                                <% if (usuarioLogado.isAdmin()) { %>
                                    <a href="ReservaServlet?acao=confirmar&id=<%= reserva.getIdReserva() %>" 
                                       class="btn btn-sm btn-success"
                                       onclick="return confirm('Confirmar esta reserva?')">Confirmar</a>
                                <% } %>
                                
                                <a href="ReservaServlet?acao=cancelar&id=<%= reserva.getIdReserva() %>" 
                                   class="btn btn-sm btn-warning"
                                   onclick="return confirm('Cancelar esta reserva?')">Cancelar</a>
                            <% } else if ("CONFIRMADA".equals(reserva.getStatusReserva())) { %>
                                <a href="ReservaServlet?acao=cancelar&id=<%= reserva.getIdReserva() %>" 
                                   class="btn btn-sm btn-warning"
                                   onclick="return confirm('Cancelar esta reserva?')">Cancelar</a>
                            <% } %>
                            
                            <% if (usuarioLogado.isAdmin() || 
                                   (reserva.getIdUsuario() == usuarioLogado.getIdUsuario() && 
                                    "CANCELADA".equals(reserva.getStatusReserva()))) { %>
                                <a href="ReservaServlet?acao=excluir&id=<%= reserva.getIdReserva() %>" 
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Excluir esta reserva permanentemente?')">Excluir</a>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <% } else { %>
        <div class="empty-state">
            <div class="empty-icon">📅</div>
            <h3>Nenhuma reserva encontrada</h3>
            <p>Você ainda não possui reservas. Que tal fazer sua primeira reserva?</p>
            <a href="ReservaServlet?acao=nova" class="btn btn-primary">Fazer Primeira Reserva</a>
        </div>
        <% } %>
        
        <div class="legend">
            <h4>Legenda de Status:</h4>
            <div class="status-legend">
                <span class="status status-pendente">PENDENTE</span> - Aguardando confirmação
                <span class="status status-confirmada">CONFIRMADA</span> - Reserva confirmada
                <span class="status status-cancelada">CANCELADA</span> - Reserva cancelada
            </div>
        </div>
    </div>

    <!-- Mensagens de erro e sucesso -->
    <% 
        String erro = request.getParameter("erro");
        String sucesso = request.getParameter("sucesso");
        
        if (erro != null) {
    %>
        <div class="alert alert-error">
            <%= erro %>
        </div>
    <% } %>
    
    <% if (sucesso != null) { %>
        <div class="alert alert-success">
            <%= sucesso %>
        </div>
    <% } %>
</body>
</html>