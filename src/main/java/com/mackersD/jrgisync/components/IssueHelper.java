package com.mackersD.jrgisync.components;

import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.util.json.JSONObject;

public interface IssueHelper {

    public boolean createJIRAIssue(JSONObject jObj) throws JSONException;
}