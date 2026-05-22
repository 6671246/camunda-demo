package org.camunda.bpm.getstarted.loanapproval.dto;

public class JumpTaskRequest {

    private String targetActivityId;

    public String getTargetActivityId() {
        return targetActivityId;
    }

    public void setTargetActivityId(String targetActivityId) {
        this.targetActivityId = targetActivityId;
    }
}
