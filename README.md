# Research on Switching to `Either` for Error Handling in the NGC App

---

## **Introduction**
The NGC app currently uses Kotlin's `Result` type for handling network requests and error handling. This document explores the advantages and disadvantages of switching to the `Either` type for error handling. The goal is to provide a clear comparison to help the team make an informed decision.

---

## **What is `Either`?**
`Either` is a functional programming construct that represents a value that can be one of two types: `Left` or `Right`. By convention:
- `Left` is used to represent an error or failure.
- `Right` is used to represent a successful result.

This approach provides a more structured way to handle errors compared to Kotlin's `Result`, which primarily uses `Throwable` for errors.

---

## **Pros of Using `Either`**

1. **Explicit Error Handling**:
   - `Either` makes error handling more explicit by separating success (`Right`) and failure (`Left`) cases. This improves code readability and reduces the chance of missing error cases.

2. **Custom Error Types**:
   - Unlike `Result`, which uses `Throwable` for errors, `Either` allows you to define custom error types (`Left`). This provides more context and flexibility in error handling.

3. **Functional Programming Benefits**:
   - `Either` aligns with functional programming principles, making it easier to chain operations using `map`, `flatMap`, and `fold`. This can lead to cleaner and more maintainable code.

4. **Better Type Safety**:
   - `Either` enforces type safety by requiring explicit handling of both success and error cases. This reduces the risk of runtime errors.

5. **Interoperability with Libraries**:
   - Libraries like Arrow provide utilities for working with `Either`, making it easier to integrate with other functional programming constructs.

6. **Improved Debugging**:
   - Custom error types in `Either` can include additional metadata (e.g., error codes, messages), making debugging easier.

---

## **Cons of Using `Either`**

1. **Learning Curve**:
   - Developers unfamiliar with functional programming may find `Either` harder to understand compared to Kotlin's `Result`.

2. **Verbosity**:
   - `Either` can introduce more boilerplate code, especially when chaining multiple operations. This may make the codebase more complex.

3. **Integration with Existing Code**:
   - Switching to `Either` may require significant refactoring of the existing codebase, especially if `Result` is widely used.

4. **Performance Overhead**:
   - While negligible in most cases, `Either` may introduce a slight performance overhead due to additional object allocations.

5. **Tooling and Ecosystem**:
   - Kotlin's `Result` is a built-in type with strong IDE support, whereas `Either` relies on external libraries like Arrow, which may have less mature tooling.

---

## **Comparison: `Result` vs. `Either`**

| Feature                  | `Result`                          | `Either`                          |
|--------------------------|-----------------------------------|-----------------------------------|
| **Error Type**           | `Throwable`                       | Custom type (e.g., `CoxException`)|
| **Explicit Handling**    | Less explicit                     | More explicit                     |
| **Functional Chaining**  | Limited                           | Better support                    |
| **Type Safety**          | Good                              | Excellent                         |
| **Learning Curve**       | Easier                            | Steeper                           |
| **Boilerplate**          | Less                              | More                              |
| **Integration Effort**   | Minimal                           | Significant                       |
| **Performance**          | Slightly better                   | Slightly worse                    |

---

## **Recommendation**
Switching to `Either` can improve error handling and code clarity, especially in a functional programming context. However, the decision should consider:
- The team's familiarity with functional programming.
- The effort required to refactor the existing codebase.
- The need for custom error types and explicit error handling.

If the team is comfortable with functional programming and the benefits outweigh the costs, switching to `Either` is a good choice. Otherwise, sticking with `Result` may be more practical.

<img src="docs/images/modularization_diagram.png" width="400" />
---
## Project Structure

The project is modularized into three main layers:

- **App**: Contains the user interface and presentation logic.
- **Domain**: Contains the business logic, which is platform-independent.
- **Data**: Handles data access, including API connections and local database management.

### Modularization Diagram

<img src="https://github.com/user-attachments/assets/984fa8fe-4468-463d-8c31-a22c879ac5e0" width="400" />

### Key Dependencies

- **Retrofit**: Used in the `Data` module for API calls.
- **Room**: Used in the `Data` module for local database management.
- **Hilt**: Used across all modules for dependency injection.
---
## **Conclusion**
The decision to switch from `Result` to `Either` should be based on the team's needs, familiarity with functional programming, and the specific requirements of the NGC app. While `Either` offers several advantages, such as explicit error handling and custom error types, it also introduces challenges like a steeper learning curve and increased boilerplate. A careful evaluation of these factors will help the team make the best decision for the project.

---
## **Diagram APP**
<img src="https://github.com/user-attachments/assets/562cc7e9-2d4a-4d5d-b8c1-9b5fc052b986" width="400" />
<img src="https://github.com/user-attachments/assets/a73ddb8f-2c5b-4ac9-9fdf-4b88bbda44b9" width="600"  />

