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

## Troubleshooting
- **DB Connection:** Verify MySQL running, credentials in `DBManager.java`, DB exists.
- **Build Errors:** Ensure JDK 1.8+ configured, MySQL Connector/J in classpath, clean rebuild.
- **GUI Issues:** Check Swing support, display scaling.