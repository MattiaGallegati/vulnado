# Comment.java: Comment Management System

## Overview
The `Comment` class is responsible for managing comments in a system. It provides functionality to create, fetch, and delete comments, as well as commit them to a database. The class interacts with a PostgreSQL database to store and retrieve comment data.

## Process Flow
```mermaid
flowchart TD
    Start("Start")
    CreateComment["Create Comment"]
    CommitComment["Commit Comment to Database"]
    FetchAllComments["Fetch All Comments"]
    DeleteComment["Delete Comment"]
    Database[("PostgreSQL Database")]

    Start --> |"User calls create()"| CreateComment
    CreateComment --> |"Calls commit()"| CommitComment
    CommitComment --> |"Writes to Database"| Database
    Start --> |"User calls fetch_all()"| FetchAllComments
    FetchAllComments --> |"Reads from Database"| Database
    Start --> |"User calls delete()"| DeleteComment
    DeleteComment --> |"Deletes from Database"| Database
```

## Insights
- **Comment Creation**: Generates a unique ID using `UUID`, sets the current timestamp, and commits the comment to the database.
- **Fetching Comments**: Retrieves all comments from the database using a SQL `SELECT` query.
- **Deleting Comments**: Deletes a specific comment by its ID using a SQL `DELETE` query.
- **Error Handling**: Uses custom exceptions (`BadRequest`, `ServerError`) for error handling during comment creation.
- **Database Interaction**: Relies on the `Postgres.connection()` method to establish connections to the database.

## Dependencies
```mermaid
flowchart LR
    Comment --- |"Depends"| Postgres
    Comment --- |"Uses"| BadRequest
    Comment --- |"Uses"| ServerError
```

- `Postgres`: Provides the `connection()` method to interact with the PostgreSQL database.
- `BadRequest`: Used to throw exceptions when comment creation fails.
- `ServerError`: Used to throw exceptions for server-related errors.

## Data Manipulation (SQL)
### Table Structure: `comments`
| Attribute   | Data Type   | Description                          |
|-------------|-------------|--------------------------------------|
| `id`        | `VARCHAR`   | Unique identifier for the comment.  |
| `username`  | `VARCHAR`   | Username of the comment author.     |
| `body`      | `TEXT`      | Content of the comment.             |
| `created_on`| `TIMESTAMP` | Timestamp when the comment was created. |

### SQL Operations
- **INSERT**: Adds a new comment to the `comments` table.
- **SELECT**: Retrieves all comments from the `comments` table.
- **DELETE**: Removes a specific comment from the `comments` table.

## Vulnerabilities
1. **SQL Injection**:
   - The `fetch_all()` method uses raw SQL queries (`stmt.executeQuery(query)`), which are vulnerable to SQL injection attacks. Prepared statements should be used instead.
   
2. **Improper Error Handling**:
   - The `delete()` method always returns `false` in the `finally` block, even if the operation succeeds. This can lead to incorrect behavior.
   
3. **Resource Management**:
   - Connections and statements are not properly closed in the `fetch_all()` and `delete()` methods, which can lead to resource leaks.
   
4. **Unvalidated Input**:
   - The `create()` method does not validate `username` or `body` inputs, which could lead to injection attacks or invalid data being stored in the database.
