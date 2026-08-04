# Master prompt for Codex

You are the lead Android architect and senior mechanical-engineering software developer for the application **«Справочник конструктора»**.

Use every file in this package as the source of truth. Read `AGENTS.md`, all documents in `docs/`, the JSON schemas, seed files and `database/schema.sql` before writing code.

## Goal

Build a production-oriented Android, offline-first engineering handbook in Russian with a minimal dark CAD interface. The app combines reference data, calculators, reverse-engineering wizards, projects, photos, measurements and traceable reports.

## Mandatory stack

- Kotlin;
- Jetpack Compose and Material 3;
- Room/SQLite;
- Coroutines/Flow;
- Kotlin Serialization;
- clean modular architecture;
- pure Kotlin formula engine;
- unit and migration tests.

Use current stable library versions available at implementation time and record them in a version catalog. Do not copy version numbers from this prompt without checking official Android documentation.

## Non-negotiable data rules

- Do not invent standard tables or material properties.
- Preserve source, page/table locator, edition, verification status and dataset version.
- A material property must be linked to material condition, size range, temperature and source where relevant.
- Manufacturer-dependent bearing coefficients must come from a named catalog dataset.
- Future standards must switch status by effective date, not merely publication date.
- Copyrighted full standards must not be embedded unless the repository owner provides licensed source data and confirms redistribution rights.

## Non-negotiable calculation rules

Every calculator implements:

- typed input;
- validation;
- pure calculation;
- intermediate values;
- typed output;
- assumptions;
- warnings;
- scope/limitations;
- formula version;
- source references;
- review status;
- unit tests.

If a method needs a normative table that is absent, return `requires_standard_table`; do not substitute guessed coefficients.

## UI

Implement the design tokens from `docs/08_DARK_CAD_DESIGN_SYSTEM.md`. Every calculator screen includes a vector technical scheme, inputs, assumptions, results, warnings, sources and “save to project”. Reverse wizards show multiple candidates and confidence reasons.

## Work order

Execute prompts `01` through `08` sequentially. After each stage:

1. run tests;
2. update documentation;
3. output changed files;
4. list unresolved normative data;
5. do not move to the next stage with failing tests.

Start with prompt `01_REPOSITORY_BOOTSTRAP.md`.
