package org.camunda.bpm.getstarted.loanapproval.dto;

import java.util.Map;

public class StartProcessRequest {

    private String businessKey;
    private Map<String, Object> variables;

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
