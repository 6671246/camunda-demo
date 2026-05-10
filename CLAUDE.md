# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 构建与运行命令

```bash
# 构建项目
mvn clean package

# 运行应用程序
mvn spring-boot:run

# 运行特定 Java 版本（需要 Java 17）
java -jar target/loan-approval-spring-boot-0.0.1-SNAPSHOT.jar
```

## 架构

这是一个 Camunda Platform 7 的入门示例。Spring Boot 应用程序，使用嵌入式 Camunda 引擎运行 BPMN 流程实例。

### 核心组件

- **流程定义**: `src/main/resources/loanApproval.bpmn` - 包含用户任务、嵌入式子流程和排他网关的 BPMN 2.0 流程
- **流程应用**: `WebappExampleProcessApplication.java` - 带 `@EnableProcessApplication` 注解的 Spring Boot 入口类
- **数据库**: H2 内存数据库（由 Camunda Spring Boot starter 自动配置）
- **Web UI**: 启动后可访问 `http://localhost:8080` 的 Camunda Web 应用

### 流程说明

1. 用户任务 "Check the request"（办理人: demo）
2. 嵌入式子流程 "Quotation Review"（SD → VP 任务）
3. 排他网关根据 `${x==1}` 变量进行判断
4. 根据条件分支到 "YES" 或 "No" 任务
5. 流程结束

### 配置

Camunda 自动配置由 `camunda-bpm-spring-boot-starter-webapp` 提供。流程实例通过 `@EventListener(PostDeployEvent)` 自动启动，部署时触发 `runtimeService.startProcessInstanceByKey("loanApproval")`。