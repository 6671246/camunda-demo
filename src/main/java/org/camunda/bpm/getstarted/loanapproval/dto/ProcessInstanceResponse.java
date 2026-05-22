package org.camunda.bpm.getstarted.loanapproval.dto;

import java.util.Date;

public class ProcessInstanceResponse {

    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String businessKey;
    private String state;
    private Date startTime;
    private Date endTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProcessDefinitionId() { return processDefinitionId; }
    public void setProcessDefinitionId(String processDefinitionId) { this.processDefinitionId = processDefinitionId; }
    public String getProcessDefinitionKey() { return processDefinitionKey; }
    public void setProcessDefinitionKey(String processDefinitionKey) { this.processDefinitionKey = processDefinitionKey; }
    public String getBusinessKey() { return businessKey; }
    public void setBusinessKey(String businessKey) { this.businessKey = businessKey; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
}
