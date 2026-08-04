Implement Room from `database/schema.sql` using domain/entity separation.

Deliver:
- entities, DAO, database, migrations and repositories;
- seed manifest importer for JSON assets;
- transaction-safe import with checksum and rollback;
- source/standard/change models;
- search normalization and FTS5;
- tests for initial import, repeated import, invalid checksum and migration.

Import only the supplied seed data. Never fill blank normative values.
