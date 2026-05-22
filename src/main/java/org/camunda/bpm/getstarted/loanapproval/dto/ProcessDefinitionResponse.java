package org.camunda.bpm.getstarted.loanapproval.dto;

import java.util.List;
import java.util.Map;

public class ProcessDefinitionResponse {

    private String id;
    private String key;
    private String name;
    private int version;
    private String resourceName;
    private boolean isSuspended;
    private boolean isStartableInTasklist;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public boolean isSuspended() { return isSuspended; }
    public void setSuspended(boolean suspended) { isSuspended = suspended; }
    public boolean isStartableInTasklist() { return isStartableInTasklist; }
    public void setStartableInTasklist(boolean startableInTasklist) { isStartableInTasklist = startableInTasklist; }
}
