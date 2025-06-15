<%-- 
    Document   : index.jsp
    Created on : 30 de mai. de 2025, 19:07:50
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.com.entidade.Usuario"%>
<%
    // Verificar se usuário está logado
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Início - Sistema de Reservas</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-container">
            <div class="nav-brand">
                <h2>Sistema de Reservas</h2>
            </div>
            <div class="nav-menu">
                <a href="index.jsp" class="nav-link active">Início</a>
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
        <div class="welcome-section">
            <h1>Bem-vindo ao Sistema de Reservas de Salas</h1>
            <p class="welcome-text">
                Gerencie suas reservas de salas de forma fácil e eficiente.
            </p>
        </div>

        <div class="dashboard-grid">
            <div class="dashboard-card">
                <div class="card-icon">📅</div>
                <h3>Nova Reserva</h3>
                <p>Faça uma nova reserva de sala</p>
                <a href="ReservaServlet?acao=nova" class="btn btn-primary">Reservar Sala</a>
            </div>

            <div class="dashboard-card">
                <div class="card-icon">📋</div>
                <h3>Minhas Reservas</h3>
                <p>Visualize e gerencie suas reservas</p>
                <a href="ReservaServlet" class="btn btn-primary">Ver Reservas</a>
            </div>

            <% if (usuarioLogado.isAdmin()) { %>
            <div class="dashboard-card">
                <div class="card-icon">👥</div>
                <h3>Gerenciar Usuários</h3>
                <p>Administrar usuários do sistema</p>
                <a href="UsuarioServlet" class="btn btn-primary">Gerenciar</a>
            </div>

            <div class="dashboard-card">
                <div class="card-icon">🏢</div>
                <h3>Gerenciar Salas</h3>
                <p>Administrar salas disponíveis</p>
                <a href="SalaServlet" class="btn btn-primary">Gerenciar</a>
            </div>
            <% } %>
        </div>

        <div class="info-section">
            <h2>Informações do Sistema</h2>
            <div class="info-grid">
                <div class="info-item">
                    <h4>Horário de Funcionamento</h4>
                    <p>Segunda a Sexta: 07:00 às 22:00<br>
                       Sábado: 08:00 às 18:00</p>
                </div>
                <div class="info-item">
                    <h4>Regras de Reserva</h4>
                    <p>• Reservas com antecedência mínima de 1 hora<br>
                       • Máximo de 4 horas por reserva<br>
                       • Cancelamento até 30 minutos antes</p>
                </div>
                <div class="info-item">
                    <h4>Suporte</h4>
                    <p>Em caso de dúvidas, entre em contato:<br>
                       Email: suporte@reservas.com<br>
                       Telefone: (11) 1234-5678</p>
                </div>
            </div>
        </div>
    </div>

    <footer class="footer">
        <div class="container">
            <p>&copy; 2024 Sistema de Reservas de Salas. Todos os direitos reservados.</p>
        </div>
    </footer>
</body>
</html>