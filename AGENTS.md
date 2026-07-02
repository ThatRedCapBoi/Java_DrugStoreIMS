# AGENTS.md

## Development
- **Build:** `ant clean jar`
- **Run:** `ant run`
- **Environment:** JDK 1.8 (enforced in `nbproject/project.properties`). README says JDK 18+, but project config uses 1.8.
- **Persistence:** MySQL (`drugstoreinventorydb`). DB config: `src/infra/DBManager.java`.
- **DB Setup:** Run `database/init.sql` to create schema. Default: `root`/``@`localhost:3306`.

## Architecture & Structure
- **Pattern:** MVC Layered.
- **Root Packages:**
    - `app/`: Entry point
    - `view/`: Swing UI (NetBeans `.form`)
    - `controller/`: UI interactions
    - `service/`: Business logic
    - `repository/`: Data access (MySQL)
    - `model/`: Domain objects
    - `infra/`: Config & Utilities (includes `AppConfig.java`)
    - `state/`: Role-based access logic (`StateFactory`)
    - `adapter/`: Data import/export (CSV/JSON)
    - `validation/`: Input sanitization
    - `dto/`: Data transfer objects
    - `exception/`: Custom exceptions
- **Theme:** `FlatLaf` (initialized in `App.java`).
- **Rules:**
    - NetBeans `.form` files control layouts. Do not manually edit generated `initComponents()` in view files.
    - Use `infra.AppConfig` for centralized colors/fonts.

## Default Credentials
- **Manager:** `manager` / `manager123`
- **Staff:** `staff` / `staff123`

## Features
- User Auth (Role-based: Manager/Staff)
- Product Management (CRUD, search, filter)
- Category Management (CRUD)
- Dashboard (Statistics/Analytics)
- Data Exchange (CSV/JSON Import/Export)
- Inventory Tracking (Stock, Price)

## Git Commits
- Use Conventional Commits: `feat`, `fix`, `perf`, `ref`, `style`, `chore`, `test`.
- For branches, create the branch using this format: `feat/actionCode-description`.
    - `feat` is the example if the branch is a feature branch. Use other conventions for other branch purposes.
    - `actionCode` will require the user to specify which action item from the Design Review or SCMP document. Prompt the user if not specified by them.
    - `description` will be the description of what the branch is doing, keep it simple and ensure spaces are changed to dashes following branch naming conventions.
- When creating pull request titles, follow this naming convention: `Action Code: Description`. Infer this from the branch name.
    - Descriptions for these pull requests must include the action code from the relevant documents and the main objective of the pull request itself (infer from branch name).

## Troubleshooting
- **DB Connection:** Verify MySQL running, credentials in `DBManager.java`, DB exists.
- **Build Errors:** Ensure JDK 1.8+ configured, MySQL Connector/J in classpath, clean rebuild.
- **GUI Issues:** Check Swing support, display scaling.