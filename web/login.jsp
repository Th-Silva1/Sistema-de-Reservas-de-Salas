<%-- 
    Document   : Login
    Created on : 30 de mai. de 2025, 10:12:13
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistema de Reservas</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-body">
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h1>Sistema de Reservas de Salas</h1>
                <p>Faça login para continuar</p>
            </div>
            
            <form action="LoginServlet" method="post" class="login-form">
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required 
                           placeholder="Digite seu email">
                </div>
                
                <div class="form-group">
                    <label for="senha">Senha:</label>
                    <input type="password" id="senha" name="senha" required 
                           placeholder="Digite sua senha">
                </div>
                
                <button type="submit" class="btn btn-primary btn-full">Entrar</button>
            </form>
            
            
            <!-- Mensagens de erro e sucesso -->
            <% 
                String erro = request.getParameter("erro");
                String mensagem = request.getParameter("mensagem");
                
                if (erro != null) {
            %>
                <div class="alert alert-error">
                    <%= erro %>
                </div>
            <% } %>
            
            <% if (mensagem != null) { %>
                <div class="alert alert-success">
                    <%= mensagem %>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
