package org.SmartShop.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.SmartShop.entity.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. Allow Public Endpoints (Login/Swagger)
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/auth") || uri.contains("swagger") || uri.contains("api-docs")) {
            return true;
        }

        // 2. Check Session Existence
        HttpSession session = request.getSession(false); // false = do not create new session
        if (session == null || session.getAttribute("USER_ID") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non authentifié"); // 401 [cite: 192]
            return false;
        }

        // 3. Role-Based Access Control (RBAC) [cite: 168]
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        String method = request.getMethod();

        // Admin can do everything [cite: 170]
        if (role == UserRole.ADMIN) {
            return true;
        }

        // Client Restrictions [cite: 169]
        if (role == UserRole.CLIENT) {
            // Clients can only GET (View) specific resources or POST orders
            // This is a basic check. Finer control should be done in Services if needed.
            if (method.equals("DELETE")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé: Clients cannot delete"); // 403 [cite: 194]
                return false;
            }
            // Add specific logic here if Clients try to access Admin-only routes
        }

        return true;
    }
}