package com.mackersD.jrgisync.servlet;

import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AdminServlet extends HttpServlet{
    
    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);
    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer templateRenderer;
    private final ProjectService projectService;
    private final com.atlassian.jira.user.util.UserManager jiraUserManager;
    
    //@Inject
    public AdminServlet(
            UserManager userManager,
            LoginUriProvider loginUriProvider,
            TemplateRenderer templateRenderer,
            ProjectService projectService,
            com.atlassian.jira.user.util.UserManager jiraUserManager
    ) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.templateRenderer = templateRenderer;
        this.projectService = projectService;
        this.jiraUserManager = jiraUserManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        //check to see if the logged-in user is an admin        
        UserProfile user = userManager.getRemoteUser(req);
        if(user == null || !userManager.isSystemAdmin(user.getUserKey())) {
            redirectToLogin(req, resp);
            return;
        }
        
        //render admin form
        resp.setContentType("text/html;charset=utf-8");
        templateRenderer.render("templates/admin.vm", getFormData(req, user), resp.getWriter());
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        
    }
    
    private HashMap<String, Object> getFormData(HttpServletRequest req, UserProfile user) {
        
        HashMap<String, Object> formData = new HashMap<String, Object>();
        ApplicationUser jiraUser = jiraUserManager.getUserByKey(user.getUserKey().getStringValue());
        
        //get projects
        Collection<Project> projects = projectService.getAllProjects(jiraUser).getReturnedValue();
        formData.put("projects", projects);
        
        //get users
        Collection<ApplicationUser> users = jiraUserManager.getAllApplicationUsers();
        formData.put("users", users);
        
        return formData;
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