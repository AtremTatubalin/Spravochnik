# Stage 06 — reports, export, backup and updates

Implemented the offline export and maintenance layer:

- Structured reports now have explicit inputs, units, formula versions, intermediate values, outputs, assumptions, warnings, sources, dataset versions and review status.
- Reports can be rendered to a minimal PDF byte stream and exported as CSV or JSON without embedding binary assets in the repository.
- Project backup export and restore dry-run planning were added for project details.
- Photo handling stores sanitized URI references, strips query/fragment data, records lowercase MIME type and marks metadata as stripped; content URIs require persisted permission.
- Dataset version UI state and Settings page copy show current data versions and require update-package dry-run before import.
- Update package planning blocks unverified entries and entries without a SHA-256 checksum before import planning.

Unresolved production items:

- Full visual PDF layout, signing and long-term archival metadata remain future work.
- Real update import must run inside the Room transaction/importer path before production use.
