package org.camunda.bpm.getstarted.loanapproval.service;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessService {

    @Autowired
    private ProcessEngine processEngine;

    public String startProcess(String processDefinitionKey, Map<String, Object> variables) {
        return startProcess(processDefinitionKey, null, variables);
    }

    public String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        org.camunda.bpm.engine.RuntimeService runtimeService = processEngine.getRuntimeService();
        org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder builder =
            runtimeService.createProcessInstanceByKey(processDefinitionKey);

        if (businessKey != null) {
            builder.businessKey(businessKey);
        }

        if (variables != null && !variables.isEmpty()) {
            builder.setVariables(variables);
        }

        ProcessInstance instance = builder.execute();
        return instance.getId();
    }

    public List<Map<String, Object>> getProcessInstances(String processDefinitionKey, Boolean active) {
        org.camunda.bpm.engine.RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();

        if (processDefinitionKey != null && !processDefinitionKey.isEmpty()) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (active != null && active) {
            query.active();
        }

        return query.list().stream().map(this::mapProcessInstance).collect(Collectors.toList());
    }

    public Map<String, Object> getProcessInstance(String processInstanceId) {
        org.camunda.bpm.engine.RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();

        if (instance == null) {
            throw new RuntimeException("Process instance not found: " + processInstanceId);
        }

        return mapProcessInstance(instance);
    }

    public List<Map<String, Object>> getTasks(String assignee, String processDefinitionKey) {
        org.camunda.bpm.engine.TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery();

        if (assignee != null && !assignee.isEmpty()) {
            query.taskAssignee(assignee);
        }
        if (processDefinitionKey != null && !processDefinitionKey.isEmpty()) {
            query.processDefinitionKey(processDefinitionKey);
        }

        return query.list().stream().map(this::mapTask).collect(Collectors.toList());
    }

    public Map<String, Object> getTask(String taskId) {
        org.camunda.bpm.engine.TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        return mapTask(task);
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        org.camunda.bpm.engine.TaskService taskService = processEngine.getTaskService();
        if (variables != null && !variables.isEmpty()) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
    }

    public void jumpToActivity(String taskId, String targetActivityId) {
        org.camunda.bpm.engine.TaskService taskService = processEngine.getTaskService();
        org.camunda.bpm.engine.RuntimeService runtimeService = processEngine.getRuntimeService();

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        taskService.complete(taskId);

        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
            .startAfterActivity(targetActivityId)
            .execute();
    }

    public Map<String, Object> getProcessDefinition(String processDefinitionKey) {
        org.camunda.bpm.engine.RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(processDefinitionKey)
            .latestVersion()
            .singleResult();

        if (definition == null) {
            throw new RuntimeException("Process definition not found: " + processDefinitionKey);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", definition.getId());
        map.put("key", definition.getKey());
        map.put("name", definition.getName());
        map.put("version", definition.getVersion());
        map.put("resourceName", definition.getResourceName());
        map.put("isSuspended", definition.isSuspended());
        map.put("isStartableInTasklist", definition.isStartableInTasklist());

        return map;
    }

    public String getProcessDefinitionXml(String processDefinitionKey) {
        org.camunda.bpm.engine.RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(processDefinitionKey)
            .latestVersion()
            .singleResult();

        if (definition == null) {
            throw new RuntimeException("Process definition not found: " + processDefinitionKey);
        }

        InputStream inputStream = repositoryService.getProcessModel(definition.getId());
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    public InputStream getProcessDefinitionDiagram(String processDefinitionKey) {
        org.camunda.bpm.engine.RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(processDefinitionKey)
            .latestVersion()
            .singleResult();

        if (definition == null) {
            throw new RuntimeException("Process definition not found: " + processDefinitionKey);
        }

        return repositoryService.getProcessModel(definition.getId());
    }

    private Map<String, Object> mapProcessInstance(ProcessInstance instance) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", instance.getId());
        map.put("processDefinitionId", instance.getProcessDefinitionId());
        map.put("processDefinitionKey", instance.getProcessDefinitionKey());
        map.put("businessKey", instance.getBusinessKey());
        map.put("state", instance.isEnded() ? "COMPLETED" : "ACTIVE");
        return map;
    }

    private Map<String, Object> mapTask(Task task) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", task.getId());
        map.put("name", task.getName());
        map.put("description", task.getDescription());
        map.put("assignee", task.getAssignee());
        map.put("processInstanceId", task.getProcessInstanceId());
        map.put("executionId", task.getExecutionId());
        map.put("taskDefinitionKey", task.getTaskDefinitionKey());
        map.put("createTime", task.getCreateTime());
        map.put("dueDate", task.getDueDate());
        map.put("priority", task.getPriority());
        return map;
    }
}