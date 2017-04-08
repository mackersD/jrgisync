package com.mackersD.jrgisync.components;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.IssueService.CreateValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.bc.issue.link.RemoteIssueLinkService;
import com.atlassian.jira.bc.issue.link.RemoteIssueLinkService.RemoteIssueLinkResult;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.issue.link.RemoteIssueLinkBuilder;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IssueHelperImpl implements IssueHelper {
    private static final Logger log = LoggerFactory.getLogger(IssueHelperImpl.class);
    private final IssueService issueService;
    private final RemoteIssueLinkService remoteIssueLinkService;
    private final UserManager userManager;
    private final ProjectService projectService;
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public IssueHelperImpl(
            IssueService issueService, 
            RemoteIssueLinkService remoteIssueLinkService,
            UserManager userManager,
            ProjectService projectService,
            JiraAuthenticationContext jiraAuthenticationContext) {
        this.issueService = issueService;
        this.remoteIssueLinkService = remoteIssueLinkService;
        this.userManager = userManager;
        this.projectService = projectService;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }
     
    
     public boolean createJIRAIssue(JSONObject jObj) throws JSONException {
         
        IssueInputParameters issueParams = issueService.newIssueInputParameters();
        
        //get user that is reporting the ticket
        ApplicationUser user = userManager.getUserByKey("admin");
        
        //this is necessary to give IssueService the authorization context to be used
        jiraAuthenticationContext.setLoggedInUser(user);         
        
        //set the user as reporting the issue
        issueParams.setReporterId(user.getName());
        
        //set the issue's summary and description
        JSONObject objAttr = jObj.optJSONObject("object_attributes");
        issueParams.setSummary(objAttr.optString("title"));
        issueParams.setDescription(objAttr.optString("description"));
        
        //get issue project
        Project project = projectService.getProjectByKey(user, "TP").getProject();
        issueParams.setProjectId(project.getId());
        issueParams.setIssueTypeId("10000");
        
       
        CreateValidationResult validationResult = issueService.validateCreate(user, issueParams);
        
        IssueResult issueResult;
        if(validationResult.getErrorCollection().hasAnyErrors()) {
            log.error(validationResult.getErrorCollection().getErrors().toString());
            return false;
        } else {
            issueResult = issueService.create(user, validationResult);
        }
        
        //set issue web link for Gitlab issue
        RemoteIssueLinkBuilder rmlb = new RemoteIssueLinkBuilder();
        rmlb.title("Gitlab Issue");
        rmlb.url(objAttr.getString("url"));
        rmlb.issueId(issueResult.getIssue().getId());
        RemoteIssueLink issueLink = rmlb.build();
        
        RemoteIssueLinkService.CreateValidationResult linkValdResult = remoteIssueLinkService.validateCreate(user, issueLink);
        
        RemoteIssueLinkResult linkResult;
        if(linkValdResult.getErrorCollection().hasAnyErrors()) {
            log.error(linkValdResult.getErrorCollection().getErrors().toString());
            return false;
        } else {
            linkResult = remoteIssueLinkService.create(user, linkValdResult);
        }        
        
        return true;
    }
}