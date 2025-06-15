<%-- 
    Document   : Cadastro
    Created on : 30 de mai. de 2025, 10:12:18
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.com.entidade.Usuario"%>
<%@page import="br.com.entidade.Sala"%>
<%@page import="java.util.List"%>
<%
    // Verificar se usuário está logado (apenas para administradores)
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
    if (usuarioLogado == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Verificar se é administrador
    if (!usuarioLogado.isAdmin()) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    // Obter listas e objetos para edição
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    List<Sala> salas = (List<Sala>) request.getAttribute("salas");
    Usuario usuarioEdicao = (Usuario) request.getAttribute("usuarioEdicao");
    Sala salaEdicao = (Sala) request.getAttribute("salaEdicao");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastros - Sistema de Reservas</title>
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
                    <a href="UsuarioServlet" class="nav-link active">Usuários</a>
                    <a href="SalaServlet" class="nav-link active">Salas</a>
                <% } %>
                <div class="nav-user">
                    <span>Olá, <%= usuarioLogado.getNomeUsuario() %></span>
                    <a href="LoginServlet?acao=logout" class="btn btn-secondary">Sair</a>
                </div>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="tabs">
            <button class="tab-button active" onclick="openTab(event, 'usuarios')">Usuários</button>
            <button class="tab-button" onclick="openTab(event, 'salas')">Salas</button>
        </div>

        <!-- Tab Usuários -->
        <div id="usuarios" class="tab-content active">
            <h2>Gerenciar Usuários</h2>
            
            <!-- Formulário de Cadastro/Edição de Usuário -->
            <div class="form-section">
                <h3><%= usuarioEdicao != null ? "Editar Usuário" : "Cadastrar Novo Usuário" %></h3>
                <form action="UsuarioServlet" method="post" class="form">
                    <input type="hidden" name="acao" value="<%= usuarioEdicao != null ? "atualizar" : "cadastrar" %>">
                    <% if (usuarioEdicao != null) { %>
                        <input type="hidden" name="idUsuario" value="<%= usuarioEdicao.getIdUsuario() %>">
                    <% } %>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nomeUsuario">Nome:</label>
                            <input type="text" id="nomeUsuario" name="nomeUsuario" required
                                   value="<%= usuarioEdicao != null ? usuarioEdicao.getNomeUsuario() : "" %>">
                        </div>
                        
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" required
                                   value="<%= usuarioEdicao != null ? usuarioEdicao.getEmail() : "" %>">
                        </div>
                    </div>
                    
                    <% if (usuarioEdicao == null) { %>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="senha">Senha:</label>
                            <input type="password" id="senha" name="senha" required minlength="6">
                        </div>
                        
                        <div class="form-group">
                            <label for="confirmarSenha">Confirmar Senha:</label>
                            <input type="password" id="confirmarSenha" name="confirmarSenha" required minlength="6">
                        </div>
                    </div>
                    <% } %>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="tipoUsuario">Tipo de Usuário:</label>
                            <select id="tipoUsuario" name="tipoUsuario" required>
                                <option value="USUARIO" <%= usuarioEdicao != null && "USUARIO".equals(usuarioEdicao.getTipoUsuario()) ? "selected" : "" %>>Usuário</option>
                                <option value="ADMIN" <%= usuarioEdicao != null && "ADMIN".equals(usuarioEdicao.getTipoUsuario()) ? "selected" : "" %>>Administrador</option>
                            </select>
                        </div>
                        
                        <% if (usuarioEdicao != null) { %>
                        <div class="form-group">
                            <label>
                                <input type="checkbox" name="ativo" <%= usuarioEdicao.isAtivo() ? "checked" : "" %>>
                                Usuário Ativo
                            </label>
                        </div>
                        <% } %>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <%= usuarioEdicao != null ? "Atualizar" : "Cadastrar" %>
                        </button>
                        <% if (usuarioEdicao != null) { %>
                            <a href="UsuarioServlet" class="btn btn-secondary">Cancelar</a>
                        <% } %>
                    </div>
                </form>
            </div>
            
            <!-- Lista de Usuários -->
            <% if (usuarios != null && !usuarios.isEmpty()) { %>
            <div class="table-section">
                <h3>Usuários Cadastrados</h3>
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Tipo</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Usuario u : usuarios) { %>
                        <tr>
                            <td><%= u.getIdUsuario() %></td>
                            <td><%= u.getNomeUsuario() %></td>
                            <td><%= u.getEmail() %></td>
                            <td><%= u.getTipoUsuario() %></td>
                            <td>
                                <span class="status <%= u.isAtivo() ? "status-active" : "status-inactive" %>">
                                    <%= u.isAtivo() ? "Ativo" : "Inativo" %>
                                </span>
                            </td>
                            <td class="actions">
                                <a href="UsuarioServlet?acao=editar&id=<%= u.getIdUsuario() %>" class="btn btn-sm btn-secondary">Editar</a>
                                <% if (u.getIdUsuario() != usuarioLogado.getIdUsuario()) { %>
                                    <a href="UsuarioServlet?acao=excluir&id=<%= u.getIdUsuario() %>" 
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('Tem certeza que deseja excluir este usuário?')">Excluir</a>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>
        </div>

        <!-- Tab Salas -->
        <div id="salas" class="tab-content">
            <h2>Gerenciar Salas</h2>
            
            <!-- Formulário de Cadastro/Edição de Sala -->
            <div class="form-section">
                <h3><%= salaEdicao != null ? "Editar Sala" : "Cadastrar Nova Sala" %></h3>
                <form action="SalaServlet" method="post" class="form">
                    <input type="hidden" name="acao" value="<%= salaEdicao != null ? "atualizar" : "cadastrar" %>">
                    <% if (salaEdicao != null) { %>
                        <input type="hidden" name="idSala" value="<%= salaEdicao.getIdSala() %>">
                    <% } %>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="nomeSala">Nome da Sala:</label>
                            <input type="text" id="nomeSala" name="nomeSala" required
                                   value="<%= salaEdicao != null ? salaEdicao.getNomeSala() : "" %>">
                        </div>
                        
                        <div class="form-group">
                            <label for="capacidade">Capacidade:</label>
                            <input type="number" id="capacidade" name="capacidade" required min="1"
                                   value="<%= salaEdicao != null ? salaEdicao.getCapacidade() : "" %>">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="descricao">Descrição:</label>
                        <textarea id="descricao" name="descricao" rows="3"><%= salaEdicao != null ? salaEdicao.getDescricao() : "" %></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="equipamentos">Equipamentos:</label>
                        <textarea id="equipamentos" name="equipamentos" rows="3"><%= salaEdicao != null ? salaEdicao.getEquipamentos() : "" %></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label>
                            <input type="checkbox" name="disponivel" <%= salaEdicao == null || salaEdicao.isDisponivel() ? "checked" : "" %>>
                            Sala Disponível
                        </label>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <%= salaEdicao != null ? "Atualizar" : "Cadastrar" %>
                        </button>
                        <% if (salaEdicao != null) { %>
                            <a href="SalaServlet" class="btn btn-secondary">Cancelar</a>
                        <% } %>
                    </div>
                </form>
            </div>
            
            <!-- Lista de Salas -->
            <% if (salas != null && !salas.isEmpty()) { %>
            <div class="table-section">
                <h3>Salas Cadastradas</h3>
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Capacidade</th>
                            <th>Descrição</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Sala s : salas) { %>
                        <tr>
                            <td><%= s.getIdSala() %></td>
                            <td><%= s.getNomeSala() %></td>
                            <td><%= s.getCapacidade() %> pessoas</td>
                            <td><%= s.getDescricao() %></td>
                            <td>
                                <span class="status <%= s.isDisponivel() ? "status-active" : "status-inactive" %>">
                                    <%= s.isDisponivel() ? "Disponível" : "Indisponível" %>
                                </span>
                            </td>
                            <td class="actions">
                                <a href="SalaServlet?acao=editar&id=<%= s.getIdSala() %>" class="btn btn-sm btn-secondary">Editar</a>
                                <a href="SalaServlet?acao=excluir&id=<%= s.getIdSala() %>" 
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Tem certeza que deseja excluir esta sala?')">Excluir</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>
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
        function openTab(evt, tabName) {
            var i, tabcontent, tablinks;
            tabcontent = document.getElementsByClassName("tab-content");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].classList.remove("active");
            }
            tablinks = document.getElementsByClassName("tab-button");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].classList.remove("active");
            }
            document.getElementById(tabName).classList.add("active");
            evt.currentTarget.classList.add("active");
        }
    </script>
    <script>
// Mostrar a aba correta baseada no servlet chamado
window.onload = function() {
    <% String abaAtiva = (String) request.getAttribute("abaAtiva"); %>
    <% if ("salas".equals(abaAtiva)) { %>
        // Ativar aba de salas
        document.querySelector('.tab-button[onclick*="salas"]').click();
    <% } else { %>
        // Ativar aba de usuários (padrão)
        document.querySelector('.tab-button[onclick*="usuarios"]').click();
    <% } %>
};
</script>
</body>
</html>
