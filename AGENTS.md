# Codex project rules

1. Never invent normative values, standard status, material properties, bearing ratings or tolerance tables.
2. Any record without a traceable source stays `draft` or `unverified`.
3. Keep formula engine pure Kotlin and independent of Android.
4. Every calculation result must contain formula version, data versions, assumptions, warnings and source references.
5. Never perform destructive Room migration in production.
6. UI is Russian-first, but internal keys and identifiers are stable English ASCII.
7. Use immutable UI state and unidirectional data flow.
8. Feature modules may depend on core modules, not on other features.
9. Safety-critical calculations must end with `requires_engineer_review`.
10. Run package validation and tests before every commit.
