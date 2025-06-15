<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="br.com.DAO.DAO"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teste de Conexão - Sistema de Reservas</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="test-container">
            <h1>Teste de Conexão com Banco de Dados</h1>
            
            <div class="test-result">
                <%
                    boolean conexaoOk = false;
                    String mensagem = "";
                    
                    try {
                        conexaoOk = DAO.testarConexao();
                        
                        if (conexaoOk) {
                            mensagem = "✅ Conexão com o banco de dados estabelecida com sucesso!";
                        } else {
                            mensagem = "❌ Falha ao conectar com o banco de dados.";
                        }
                        
                    } catch (Exception e) {
                        mensagem = "❌ Erro ao testar conexão: " + e.getMessage();
                    }
                %>
                
                <div class="alert <%= conexaoOk ? "alert-success" : "alert-error" %>">
                    <%= mensagem %>
                </div>
                
                <% if (conexaoOk) { %>
                    <div class="connection-details">
                        <h3>Detalhes da Conexão:</h3>
                        <ul>
                            <li><strong>URL:</strong> jdbc:mysql://localhost:3306/reserva_salas</li>
                            <li><strong>Driver:</strong> MySQL Connector/J</li>
                            <li><strong>Status:</strong> Conectado</li>
                            <li><strong>Data/Hora:</strong> <%= new java.util.Date() %></li>
                        </ul>
                    </div>
                    
                    <div class="next-steps">
                        <h3>Próximos Passos:</h3>
                        <ol>
                            <li>Execute o script SQL para criar as tabelas (database/schema.sql)</li>
                            <li>Verifique se os dados iniciais foram inseridos</li>
                            <li>Acesse o sistema através da página de login</li>
                        </ol>
                    </div>
                <% } else { %>
                    <div class="troubleshooting">
                        <h3>Solução de Problemas:</h3>
                        <ol>
                            <li>Verifique se o MySQL está rodando</li>
                            <li>Confirme se o banco 'reserva_salas' foi criado</li>
                            <li>Verifique as credenciais de acesso (usuário/senha)</li>
                            <li>Confirme se o driver MySQL está no classpath</li>
                        </ol>
                    </div>
                <% } %>
            </div>
            
            <div class="test-actions">
                <a href="testeBanco.jsp" class="btn btn-primary">Testar Novamente</a>
                <% if (conexaoOk) { %>
                    <a href="login.jsp" class="btn btn-success">Ir para Login</a>
                <% } %>
                <a href="index.jsp" class="btn btn-secondary">Voltar ao Início</a>
            </div>
        </div>
    </div>
</body>
</html>