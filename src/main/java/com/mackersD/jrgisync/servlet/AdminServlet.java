package com.mackersD.jrgisync.servlet;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.io.IOException;

//@Scanned
public class AdminServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);
    //@ComponentImport
    private final UserManager userManager;
    //@ComponentImport
    private final LoginUriProvider loginUriProvider;
    //@ComponentImport
    private final TemplateRenderer templateRenderer;
    
    //@Inject
    public AdminServlet(
            UserManager userManager,
            LoginUriProvider loginUriProvider,
            TemplateRenderer templateRenderer
    ) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = templateRenderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        //check to see if the logged-in user is an admin
        String username = userManager.getRemoteUsername(req);
        if(username == null || !userManager.isSystemAdmin(username)) {
            redirectToLogin(req, resp);
            return;
        }
        
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render("templates/admin.vm", resp.getWriter());
    }
    
    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(loginUriProvider.getLoginUri(getUri(req)).toASCIIString());
    }
    
    private URI getUri(HttpServletRequest req) {
        StringBuffer builder = req.getRequestURL();
        if(req.getQueryString() != null)
        {
            builder.append("?");
            builder.append(req.getQueryString());
        }
        return URI.create(builder.toString());
    }
}