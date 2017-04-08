package com.mackersD.jrgisync.servlet;

import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;
import com.mackersD.jrgisync.components.IssueHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class IssueServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(IssueServlet.class);
    
    private final IssueHelper issueHelper;
    
    public IssueServlet(IssueHelper issueHelper) {
        this.issueHelper = issueHelper;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        resp.getWriter().write("<html><body>Hello World<form action=\"\" method=\"post\"><button name=\"foo\" value=\"bar\">POST</button></form></body></html>");
        
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contentType = req.getContentType();
        if(contentType != null && !contentType.isEmpty() && "application/json".equals(contentType)) {
            String json = IOUtils.toString(req.getInputStream());
            try {
                JSONObject jObj = new JSONObject(json);
                if(!issueHelper.createJIRAIssue(jObj)) {
                    log.error("jrgisync failed to create a JIRA issue.");
                }
            }
            catch(JSONException jex) {
                log.error(jex.getMessage());
            }           
        }
        else {
            log.error("POST jrgisync expected JSON but got " + contentType + " instead.");
        }        
    }
}