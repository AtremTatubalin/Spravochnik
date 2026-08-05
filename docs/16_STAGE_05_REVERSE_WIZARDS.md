# Stage 05 — data-driven reverse wizards

Implemented the first reverse-engineering wizard engine from `data/seeds/reverse_wizards.json`.

Delivered scope:

- Parsed seed wizard definitions for bearing by dimensions, metric thread, straight-sided spline, involute spline and cylindrical gear.
- Added typed measurement input with unit, uncertainty and wear flags.
- Added candidate scoring with weighted measurement contributions, uncertainty tolerance, wear penalties, confidence reasons and contradiction detection for low-confidence matches.
- Added “next best measurement” selection from missing required/optional measurements using seed weights.
- Added `requires_standard_table` stops for table-backed wizards. Straight-sided and involute spline wizards therefore stop before candidate generation until licensed standard tables are imported.
- Added a reverse feature screen that shows wizard status, warnings/limitations and next measurement prompts in the Dark CAD shell.

Unresolved normative data:

- Metric thread, straight-sided spline and involute spline tables are not embedded. They require licensed/verified table imports before candidate matching.
- Gear wizard formula candidates are only start candidates based on supplied measurements; they are not normative confirmation of module, profile shift, quality or strength.
