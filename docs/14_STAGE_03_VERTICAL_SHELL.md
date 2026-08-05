# Stage 03 — Vertical application shell

Implemented a vertical Dark CAD application shell with NavigationRail destinations for home, global search, projects, calculators and settings. The stage adds only application/workspace models and UI placeholders; it does not add normative tables, material properties, bearing ratings or calculator coefficients.

Delivered scope:

- Home dashboard cards for search, projects and calculators.
- Global search UI state for standards, materials, bearings and calculators, with explicit placeholder behavior when indexed data is absent.
- Project domain models for projects, assemblies, parts, measurements, photo/document attachments and notes.
- Room project entities, DAO, non-destructive `2 -> 3` migration and repository adapter.
- Project screen with create flow, Dark CAD detail panel and Android Photo Picker / OpenDocument launchers for attachments.
- UI/ViewModel unit tests for home dashboard, search state and project creation reducer.

Unresolved normative data:

- Materials, bearings and calculators are still not imported into feature search results until their supplied seed schemas are mapped without filling blank normative values.
- Project measurements are user-entered observations only; no tolerances, material properties or safety conclusions are inferred.

Stage 04 reference-card extension:

- Added reusable reference cards that expose lifecycle status, source id and dataset version.
- Added visible reference sections for standards, materials, bearings, profile families and placeholders for fits, threads and fasteners.
- Material screens still hide mechanical properties until they can be attached to a specific material condition.
