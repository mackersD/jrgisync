package com.mackersD.jrgisync.rest;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * A resource of message.
 */
@Path("")
public class Admin {
    
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    @ComponentImport
    private final TransactionTemplate transactionTemplate;
    
    @Inject
    public Admin(
            UserManager userManager, 
            PluginSettingsFactory pluginSettingsFactory,
            TransactionTemplate transactionTemplate)
    {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    @GET
    @AnonymousAllowed
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage(@Context HttpServletRequest request)
    {
        UserProfile user = userManager.getRemoteUser(request);
        if(user == null || !userManager.isSystemAdmin(user.getUserKey())) {
            return Response.status(Status.FORBIDDEN).build();
        }
        
        return Response.ok(transactionTemplate.execute(new TransactionCallback()
            {
                public Object doInTransaction()
                {
                    PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
                    AdminModel model = new AdminModel();
                    model.setProjectKey((String) settings.get(AdminModel.class.getName() + ".projectKey"));
                    model.setDefaultUser((String) settings.get(AdminModel.class.getName() + ".defaultUser"));

                    return model;
                }
            })).build();
    }
}