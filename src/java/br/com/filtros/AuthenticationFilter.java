/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package br.com.filtros;

import br.com.entidade.Usuario;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Thalles
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Obter a URI da requisição
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String page = requestURI.substring(contextPath.length());
        
        // Páginas que não precisam de autenticação
        if (page.equals("/login.jsp") || 
            page.equals("/LoginServlet") ||
            page.equals("/testeBanco.jsp") ||
            page.startsWith("/css/") ||
            page.startsWith("/js/") ||
            page.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Obter a sessão
        HttpSession session = httpRequest.getSession(false);
        
        // Verificar se o usuário está logado
        Usuario usuarioLogado = null;
        if (session != null) {
            usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        }
        
        // Se não estiver logado, redirecionar para login
        if (usuarioLogado == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }
        
        // Verificar permissões para páginas administrativas
        if (isAdminPage(requestURI, contextPath) && !usuarioLogado.isAdmin()) {
            httpResponse.sendRedirect(contextPath + "/index.jsp?erro=Acesso negado. Privilégios de administrador necessários.");
            return;
        }
        
        // Continuar com a requisição
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Limpeza do filtro
    }
    
    private boolean isAdminPage(String requestURI, String contextPath) {
        String page = requestURI.substring(contextPath.length());
        
        return page.contains("UsuarioServlet") || 
               page.contains("SalaServlet") || 
               page.contains("cadastro.jsp") ||
               (page.contains("ReservaServlet") && 
                page.contains("acao=confirmar"));
    }
}
