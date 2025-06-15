<%-- 
    Document   : editarReserva
    Created on : 30 de mai. de 2025, 19:43:23
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.com.entidade.Usuario"%>
<%@page import="br.com.entidade.Reserva"%>
<%@page import="br.com.entidade.Sala"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%
    // Verificar se usuário está logado
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Reserva reservaEdicao = (Reserva) request.getAttribute("reservaEdicao");
    List<Sala> salas = (List<Sala>) request.getAttribute("salas");
    
    if (reservaEdicao == null) {
        response.sendRedirect("ReservaServlet?erro=Reserva não encontrada!");
        return;
    }
    
    // Verificar se o usuário pode editar esta reserva
    if (!usuarioLogado.isAdmin() && reservaEdicao.getIdUsuario() != usuarioLogado.getIdUsuario()) {
        response.sendRedirect("ReservaServlet?erro=Você não tem permissão para editar esta reserva!");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Reserva - Sistema de Reservas</title>
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
                <a href="ReservaServlet" class="nav-link">Minhas Reservas</a>
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
            <h1>Editar Reserva #<%= reservaEdicao.getIdReserva() %></h1>
            <p>Modifique os dados da reserva conforme necessário</p>
        </div>

        <div class="form-container">
            <form action="ReservaServlet" method="post" class="form" id="formEditarReserva">
                <input type="hidden" name="acao" value="atualizar">
                <input type="hidden" name="idReserva" value="<%= reservaEdicao.getIdReserva() %>">
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="idSala">Sala:</label>
                        <select id="idSala" name="idSala" required onchange="mostrarInfoSala()">
                            <option value="">Selecione uma sala</option>
                            <% if (salas != null) {
                                for (Sala sala : salas) { %>
                                    <option value="<%= sala.getIdSala() %>" 
                                            <%= sala.getIdSala() == reservaEdicao.getIdSala() ? "selected" : "" %>
                                            data-capacidade="<%= sala.getCapacidade() %>"
                                            data-descricao="<%= sala.getDescricao() %>"
                                            data-equipamentos="<%= sala.getEquipamentos() %>">
                                        <%= sala.getNomeSala() %> (Capacidade: <%= sala.getCapacidade() %> pessoas)
                                    </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="dataReserva">Data da Reserva:</label>
                        <input type="date" id="dataReserva" name="dataReserva" required 
                               value="<%= reservaEdicao.getDataHoraInicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) %>">
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="horaInicio">Hora de Início:</label>
                        <input type="time" id="horaInicio" name="horaInicio" required 
                               value="<%= reservaEdicao.getDataHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")) %>"
                               min="07:00" max="21:00" onchange="validarHorarios()">
                    </div>
                    
                    <div class="form-group">
                        <label for="horaFim">Hora de Fim:</label>
                        <input type="time" id="horaFim" name="horaFim" required 
                               value="<%= reservaEdicao.getDataHoraFim().format(DateTimeFormatter.ofPattern("HH:mm")) %>"
                               min="08:00" max="22:00" onchange="validarHorarios()">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="finalidade">Finalidade da Reserva:</label>
                    <textarea id="finalidade" name="finalidade" required rows="4"><%= reservaEdicao.getFinalidade() %></textarea>
                </div>
                
                <!-- Informações da Sala Selecionada -->
                <div id="infoSala" class="info-card">
                    <h3>Informações da Sala</h3>
                    <div class="info-grid">
                        <div class="info-item">
                            <strong>Capacidade:</strong>
                            <span id="capacidadeSala"></span>
                        </div>
                        <div class="info-item">
                            <strong>Descrição:</strong>
                            <span id="descricaoSala"></span>
                        </div>
                        <div class="info-item">
                            <strong>Equipamentos:</strong>
                            <span id="equipamentosSala"></span>
                        </div>
                    </div>
                </div>
                
                <div class="current-status">
                    <h4>Status Atual:</h4>
                    <span class="status status-<%= reservaEdicao.getStatusReserva().toLowerCase() %>">
                        <%= reservaEdicao.getStatusReserva() %>
                    </span>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Atualizar Reserva</button>
                    <a href="ReservaServlet" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
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

    <script>
        function mostrarInfoSala() {
            const select = document.getElementById('idSala');
            const infoDiv = document.getElementById('infoSala');
            const option = select.options[select.selectedIndex];
            
            if (option.value) {
                document.getElementById('capacidadeSala').textContent = option.dataset.capacidade + ' pessoas';
                document.getElementById('descricaoSala').textContent = option.dataset.descricao || 'Não informado';
                document.getElementById('equipamentosSala').textContent = option.dataset.equipamentos || 'Não informado';
                infoDiv.style.display = 'block';
            } else {
                infoDiv.style.display = 'none';
            }
        }
        
        function validarHorarios() {
            const horaInicio = document.getElementById('horaInicio').value;
            const horaFim = document.getElementById('horaFim').value;
            
            if (horaInicio && horaFim) {
                const inicio = new Date('2000-01-01T' + horaInicio);
                const fim = new Date('2000-01-01T' + horaFim);
                
                if (fim <= inicio) {
                    alert('A hora de fim deve ser posterior à hora de início!');
                    document.getElementById('horaFim').value = '';
                    return;
                }
                
                // Verificar duração máxima de 4 horas
                const diferencaHoras = (fim - inicio) / (1000 * 60 * 60);
                if (diferencaHoras > 4) {
                    alert('A duração máxima da reserva é de 4 horas!');
                    document.getElementById('horaFim').value = '';
                    return;
                }
            }
        }
        
        // Mostrar informações da sala ao carregar a página
        window.onload = function() {
            mostrarInfoSala();
        };
    </script>
</body>
</html>