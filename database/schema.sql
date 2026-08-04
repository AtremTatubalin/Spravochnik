PRAGMA foreign_keys = ON;

CREATE TABLE datasets (
 id TEXT PRIMARY KEY,
 version TEXT NOT NULL,
 schema_version INTEGER NOT NULL,
 verified_as_of TEXT,
 checksum TEXT,
 imported_at TEXT NOT NULL,
 status TEXT NOT NULL
);
CREATE TABLE sources (
 id TEXT PRIMARY KEY,
 title TEXT NOT NULL,
 source_type TEXT NOT NULL,
 url TEXT,
 edition TEXT,
 accessed_at TEXT,
 verification_status TEXT NOT NULL,
 license_scope TEXT
);
CREATE TABLE standards (
 id TEXT PRIMARY KEY,
 designation TEXT NOT NULL UNIQUE,
 title_ru TEXT NOT NULL,
 status TEXT NOT NULL,
 effective_from TEXT,
 effective_to TEXT,
 early_application_allowed INTEGER NOT NULL DEFAULT 0,
 source_id TEXT NOT NULL REFERENCES sources(id),
 verified_as_of TEXT NOT NULL,
 scope TEXT,
 notes_json TEXT NOT NULL DEFAULT '[]'
);
CREATE TABLE standard_relations (
 standard_id TEXT NOT NULL REFERENCES standards(id),
 relation_type TEXT NOT NULL,
 related_designation TEXT NOT NULL,
 PRIMARY KEY (standard_id, relation_type, related_designation)
);
CREATE TABLE standard_changes (
 id TEXT PRIMARY KEY,
 standard_id TEXT NOT NULL REFERENCES standards(id),
 change_type TEXT NOT NULL,
 designation TEXT,
 publication TEXT,
 registered_at TEXT,
 effective_from TEXT,
 source_id TEXT REFERENCES sources(id)
);
CREATE TABLE record_sources (
 record_type TEXT NOT NULL,
 record_id TEXT NOT NULL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 extraction_method TEXT,
 reviewer_1 TEXT,
 reviewer_2 TEXT,
 verified_at TEXT,
 PRIMARY KEY(record_type, record_id, source_id, locator)
);

CREATE TABLE materials (
 id TEXT PRIMARY KEY,
 designation TEXT NOT NULL,
 category TEXT NOT NULL,
 verification_status TEXT NOT NULL,
 notes_json TEXT NOT NULL DEFAULT '[]'
);
CREATE TABLE material_aliases (
 material_id TEXT NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
 alias TEXT NOT NULL,
 normalized_alias TEXT NOT NULL,
 PRIMARY KEY(material_id, alias)
);
CREATE TABLE material_standards (
 material_id TEXT NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
 standard_id TEXT NOT NULL REFERENCES standards(id),
 relation_type TEXT NOT NULL DEFAULT 'governing',
 PRIMARY KEY(material_id, standard_id, relation_type)
);
CREATE TABLE material_conditions (
 id TEXT PRIMARY KEY,
 material_id TEXT NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
 label_ru TEXT NOT NULL,
 heat_treatment TEXT,
 product_form TEXT,
 size_min_mm REAL,
 size_max_mm REAL,
 temp_min_c REAL,
 temp_max_c REAL
);
CREATE TABLE material_properties (
 id TEXT PRIMARY KEY,
 condition_id TEXT NOT NULL REFERENCES material_conditions(id) ON DELETE CASCADE,
 property_key TEXT NOT NULL,
 min_value REAL,
 max_value REAL,
 nominal_value REAL,
 unit TEXT NOT NULL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 verification_status TEXT NOT NULL,
 CHECK (min_value IS NULL OR max_value IS NULL OR min_value <= max_value)
);
CREATE TABLE material_composition (
 id TEXT PRIMARY KEY,
 material_id TEXT NOT NULL REFERENCES materials(id) ON DELETE CASCADE,
 element_symbol TEXT NOT NULL,
 min_percent REAL,
 max_percent REAL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 CHECK (min_percent IS NULL OR max_percent IS NULL OR min_percent <= max_percent)
);

CREATE TABLE bearings (
 id TEXT PRIMARY KEY,
 designation TEXT NOT NULL,
 bearing_type TEXT NOT NULL,
 bore_mm REAL NOT NULL CHECK(bore_mm>0),
 outside_mm REAL NOT NULL CHECK(outside_mm>bore_mm),
 width_mm REAL NOT NULL CHECK(width_mm>0),
 source_id TEXT NOT NULL REFERENCES sources(id),
 verification_status TEXT NOT NULL
);
CREATE INDEX idx_bearings_dimensions ON bearings(bore_mm,outside_mm,width_mm);
CREATE TABLE bearing_variants (
 id TEXT PRIMARY KEY,
 bearing_id TEXT NOT NULL REFERENCES bearings(id) ON DELETE CASCADE,
 suffix TEXT,
 seal_type TEXT,
 clearance_class TEXT,
 precision_class TEXT,
 cage TEXT,
 notes TEXT
);
CREATE TABLE bearing_ratings (
 id TEXT PRIMARY KEY,
 bearing_or_variant_id TEXT NOT NULL,
 C_N REAL,
 C0_N REAL,
 mass_kg REAL,
 reference_speed_rpm REAL,
 limiting_speed_rpm REAL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 verification_status TEXT NOT NULL
);
CREATE TABLE bearing_coefficients (
 id TEXT PRIMARY KEY,
 bearing_or_variant_id TEXT NOT NULL,
 rule_key TEXT NOT NULL,
 condition_json TEXT NOT NULL,
 X REAL,
 Y REAL,
 e REAL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT
);

CREATE TABLE profile_families (
 id TEXT PRIMARY KEY,
 title_ru TEXT NOT NULL,
 active_standard_id TEXT REFERENCES standards(id),
 table_status TEXT NOT NULL
);
CREATE TABLE profile_sizes (
 id TEXT PRIMARY KEY,
 family_id TEXT NOT NULL REFERENCES profile_families(id),
 designation TEXT NOT NULL,
 dimensions_json TEXT NOT NULL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 verification_status TEXT NOT NULL
);
CREATE TABLE profile_properties (
 profile_size_id TEXT PRIMARY KEY REFERENCES profile_sizes(id) ON DELETE CASCADE,
 area_mm2 REAL,
 mass_kg_m REAL,
 Ix_mm4 REAL,
 Iy_mm4 REAL,
 Wx_mm3 REAL,
 Wy_mm3 REAL,
 ix_mm REAL,
 iy_mm REAL
);

CREATE TABLE size_intervals (
 id TEXT PRIMARY KEY,
 standard_id TEXT NOT NULL REFERENCES standards(id),
 min_exclusive_mm REAL,
 max_inclusive_mm REAL NOT NULL
);
CREATE TABLE tolerance_values (
 id TEXT PRIMARY KEY,
 standard_id TEXT NOT NULL REFERENCES standards(id),
 interval_id TEXT NOT NULL REFERENCES size_intervals(id),
 field_letter TEXT NOT NULL,
 grade INTEGER NOT NULL,
 upper_um REAL NOT NULL,
 lower_um REAL NOT NULL,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 verification_status TEXT NOT NULL
);
CREATE TABLE fit_recommendations (
 id TEXT PRIMARY KEY,
 fit_designation TEXT NOT NULL,
 application_ru TEXT,
 load_case TEXT,
 source_id TEXT NOT NULL REFERENCES sources(id),
 locator TEXT,
 verification_status TEXT NOT NULL
);

CREATE TABLE calculators (
 id TEXT PRIMARY KEY,
 title_ru TEXT NOT NULL,
 category TEXT NOT NULL,
 current_version INTEGER NOT NULL,
 classification TEXT NOT NULL,
 review_status TEXT NOT NULL
);
CREATE TABLE formula_definitions (
 calculator_id TEXT NOT NULL REFERENCES calculators(id),
 version INTEGER NOT NULL,
 definition_json TEXT NOT NULL,
 checksum TEXT,
 PRIMARY KEY(calculator_id, version)
);
CREATE TABLE calculation_runs (
 id TEXT PRIMARY KEY,
 project_id TEXT,
 part_id TEXT,
 calculator_id TEXT NOT NULL REFERENCES calculators(id),
 formula_version INTEGER NOT NULL,
 dataset_versions_json TEXT NOT NULL,
 input_json TEXT NOT NULL,
 intermediate_json TEXT NOT NULL,
 output_json TEXT NOT NULL,
 assumptions_json TEXT NOT NULL,
 warnings_json TEXT NOT NULL,
 review_status TEXT NOT NULL,
 created_at TEXT NOT NULL
);

CREATE TABLE reverse_wizards (
 id TEXT PRIMARY KEY,
 title_ru TEXT NOT NULL,
 definition_json TEXT NOT NULL,
 version INTEGER NOT NULL
);

CREATE TABLE projects (
 id TEXT PRIMARY KEY,
 name TEXT NOT NULL,
 description TEXT,
 created_at TEXT NOT NULL,
 updated_at TEXT NOT NULL,
 archived INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE assemblies (
 id TEXT PRIMARY KEY,
 project_id TEXT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
 parent_assembly_id TEXT REFERENCES assemblies(id),
 name TEXT NOT NULL,
 notes TEXT
);
CREATE TABLE parts (
 id TEXT PRIMARY KEY,
 project_id TEXT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
 assembly_id TEXT REFERENCES assemblies(id),
 name TEXT NOT NULL,
 part_type TEXT,
 designation TEXT,
 material_id TEXT REFERENCES materials(id),
 notes TEXT,
 created_at TEXT NOT NULL,
 updated_at TEXT NOT NULL
);
CREATE TABLE measurements (
 id TEXT PRIMARY KEY,
 part_id TEXT NOT NULL REFERENCES parts(id) ON DELETE CASCADE,
 key TEXT NOT NULL,
 value REAL NOT NULL,
 unit TEXT NOT NULL,
 uncertainty REAL,
 method TEXT,
 instrument TEXT,
 measured_at TEXT,
 notes TEXT
);
CREATE TABLE attachments (
 id TEXT PRIMARY KEY,
 project_id TEXT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
 part_id TEXT REFERENCES parts(id),
 uri TEXT NOT NULL,
 mime_type TEXT,
 caption TEXT,
 checksum TEXT,
 created_at TEXT NOT NULL
);
CREATE TABLE notes (
 id TEXT PRIMARY KEY,
 project_id TEXT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
 part_id TEXT REFERENCES parts(id),
 body TEXT NOT NULL,
 created_at TEXT NOT NULL,
 updated_at TEXT NOT NULL
);
CREATE TABLE favorites (
 record_type TEXT NOT NULL,
 record_id TEXT NOT NULL,
 created_at TEXT NOT NULL,
 PRIMARY KEY(record_type, record_id)
);

CREATE VIRTUAL TABLE search_index USING fts5(record_type, record_id UNINDEXED, title, designation, aliases, content='');
