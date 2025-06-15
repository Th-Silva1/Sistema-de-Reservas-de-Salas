<%-- 
    Document   : logout
    Created on : 30 de mai. de 2025, 19:07:10
    Author     : Thalles
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Invalidar a sessão
    session.invalidate();
    
    // Redirecionar para a página de login
    response.sendRedirect("login.jsp?mensagem=Logout realizado com sucesso!");
%>
