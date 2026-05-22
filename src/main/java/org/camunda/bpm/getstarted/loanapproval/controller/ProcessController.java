package org.camunda.bpm.getstarted.loanapproval.controller;

import org.camunda.bpm.getstarted.loanapproval.dto.StartProcessRequest;
import org.camunda.bpm.getstarted.loanapproval.dto.CompleteTaskRequest;
import org.camunda.bpm.getstarted.loanapproval.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping("/process/start/{processDefinitionKey}")
    public ResponseEntity<Map<String, Object>> startProcess(
            @PathVariable String processDefinitionKey,
            @RequestBody(required = false) StartProcessRequest request) {
        String instanceId = processService.startProcess(
            processDefinitionKey,
            request != null ? request.getBusinessKey() : null,
            request != null ? request.getVariables() : null
        );
        return ResponseEntity.ok(Map.of("processInstanceId", instanceId, "processDefinitionKey", processDefinitionKey));
    }

    @GetMapping("/process/instances")
    public ResponseEntity<List<Map<String, Object>>> getProcessInstances(
            @RequestParam(required = false) String processDefinitionKey,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(processService.getProcessInstances(processDefinitionKey, active));
    }

    @GetMapping("/process/instances/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> getProcessInstance(@PathVariable String processInstanceId) {
        return ResponseEntity.ok(processService.getProcessInstance(processInstanceId));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Map<String, Object>>> getTasks(
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String processDefinitionKey) {
        return ResponseEntity.ok(processService.getTasks(assignee, processDefinitionKey));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Map<String, Object>> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(processService.getTask(taskId));
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) CompleteTaskRequest request) {
        processService.completeTask(taskId, request != null ? request.getVariables() : null);
        return ResponseEntity.ok(Map.of("message", "Task completed", "taskId", taskId));
    }

    @PostMapping("/tasks/{taskId}/jump/{targetActivityId}")
    public ResponseEntity<Map<String, Object>> jumpToActivity(
            @PathVariable String taskId,
            @PathVariable String targetActivityId) {
        processService.jumpToActivity(taskId, targetActivityId);
        return ResponseEntity.ok(Map.of("message", "Jumped to activity", "taskId", taskId, "targetActivityId", targetActivityId));
    }

    @GetMapping("/process/definition/{processDefinitionKey}")
    public ResponseEntity<Map<String, Object>> getProcessDefinition(@PathVariable String processDefinitionKey) {
        return ResponseEntity.ok(processService.getProcessDefinition(processDefinitionKey));
    }

    @GetMapping("/process/definition/{processDefinitionKey}/xml")
    public ResponseEntity<String> getProcessDefinitionXml(@PathVariable String processDefinitionKey) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(processService.getProcessDefinitionXml(processDefinitionKey));
    }

    @GetMapping("/process/definition/{processDefinitionKey}/diagram")
    public ResponseEntity<byte[]> getProcessDefinitionDiagram(@PathVariable String processDefinitionKey) throws IOException {
        InputStream diagramStream = processService.getProcessDefinitionDiagram(processDefinitionKey);
        byte[] bytes = diagramStream.readAllBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(bytes);
    }
}