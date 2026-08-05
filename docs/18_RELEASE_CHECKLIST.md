# Release checklist

Before a release candidate:

- Run `python scripts/validate_package.py` and `python scripts/check_reference_examples.py`.
- Run `./gradlew test lint assembleDebug` with a valid Android SDK.
- Verify report exports include inputs, units, formula versions, intermediate values, assumptions, warnings, sources, dataset versions and review status.
- Run update package dry-run and confirm all entries are verified and have SHA-256 checksums.
- Verify project backup dry-run restore before accepting a backup file.
- Confirm photo attachments use privacy-safe content/document URIs and do not persist raw EXIF metadata.
- Confirm all `requires_standard_table` and `requires_engineer_review` paths remain visible in UI and reports.
- Archive APK, version catalog, dataset manifest and test output for traceability.
