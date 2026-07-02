# AGENTS.md

## Planning
Every implementation task maps to a **Design Review Action Code** (from the DR Report / CMP §7.3). Plan around it:

- **Require an Action Code first.** If the user hasn't given one, ask before planning. Codes:
    - `A#` — System Use Case Review
    - `M##` / `S##` — Use Case Description Review (Manager / Staff)
    - `UI-A#` — UI Flow Review
    - `DB-#` — Database Design Review
- **One code per branch.** Branch `feature/<ActionCode>-<short-desc>` (e.g. `feature/M02-report-graphs-filters`); commits reference the code. Keep scope to that one item — related items (e.g. S02 vs M02) stay separate branches unless the user says otherwise.
- **The branch name is a hint, not the scope.** Confirm which code you're implementing; don't assume from the branch alone.
- **Ask, don't guess.** If requirements, scope, UI behavior, or data model are ambiguous, stop and prompt the user with concrete options before writing code. Note that the CMP's "Completed" statuses are unreliable — verify against the codebase, not the document.
- **Prefer reuse and the smallest change.** Check for existing repos/services/DTOs/patterns before adding new ones; native Java/Swing before new dependencies (new SOUP means `build.xml` classpath + CMP SOUP-list updates).
- **State DB impact.** Say up front whether the change touches `database/init.sql` (new table/column) or is read-only.

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
- **Manager:** `manager` / `Manager123`
- **Staff:** `staff` / `Staff123`

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