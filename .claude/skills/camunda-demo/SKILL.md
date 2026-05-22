```markdown
# camunda-demo Development Patterns

> Auto-generated skill from repository analysis

## Overview
This skill teaches you the development conventions and common workflows used in the `camunda-demo` Java repository. The project demonstrates clean code practices, consistent commit messaging, and a structured approach to file organization and testing, making it easy to contribute and maintain code quality.

## Coding Conventions

### File Naming
- Use **PascalCase** for all file names.
  - Example: `OrderProcessor.java`, `UserService.java`

### Import Style
- Use **relative imports** within the codebase.
  - Example:
    ```java
    import com.example.camunda.OrderService;
    ```

### Export Style
- Use **named exports** (public classes and methods).
  - Example:
    ```java
    public class OrderProcessor {
        public void processOrder(Order order) {
            // implementation
        }
    }
    ```

### Commit Messages
- Follow **conventional commit** patterns.
- Use the `feat` prefix for new features.
  - Example: `feat: add order processing logic`
- Keep commit messages concise (average 52 characters).

## Workflows

### Feature Development
**Trigger:** When adding a new feature  
**Command:** `/feature-development`

1. Create a new branch for your feature.
2. Implement the feature using PascalCase for new files.
3. Use relative imports for any dependencies.
4. Export new classes or methods as `public`.
5. Write or update corresponding test files (`*.test.*`).
6. Commit your changes using the `feat` prefix and a concise message.
7. Open a pull request for review.

### Testing
**Trigger:** When verifying code functionality  
**Command:** `/run-tests`

1. Identify or create test files matching the `*.test.*` pattern.
2. Write tests for all public methods and classes.
3. Run the test suite using your preferred Java test runner.
4. Ensure all tests pass before merging or deploying.

## Testing Patterns

- Test files follow the `*.test.*` naming convention.
  - Example: `OrderProcessor.test.java`
- Testing framework is not specified; use standard Java testing tools (e.g., JUnit).
- Tests should cover all public classes and methods.

  Example:
  ```java
  public class OrderProcessorTest {
      @Test
      public void testProcessOrder() {
          // Arrange
          OrderProcessor processor = new OrderProcessor();
          Order order = new Order();
          // Act
          processor.processOrder(order);
          // Assert
          // ...assertions...
      }
  }
  ```

## Commands
| Command              | Purpose                                      |
|----------------------|----------------------------------------------|
| /feature-development | Start a new feature development workflow     |
| /run-tests           | Run the test suite and verify functionality  |
```
