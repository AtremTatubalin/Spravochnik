# Stage 04 — pure Kotlin formula engine

Implemented the first production-oriented formula slice as pure Kotlin modules:

- `:core:formula-api` defines typed quantities, formula inputs/outputs, validation errors, success/invalid/`requires_standard_table` responses and formula registry contracts.
- `:core:formulas` implements the active seed formulas requested for the first calculator set, matching the formula IDs and version `1` from `data/seeds/formulas.json`.
- Every successful result includes formula ID/version, dataset version `formulas:0.1.0`, typed intermediate values, typed output, assumptions, warnings, source references and review status.
- Dimensional validation currently uses exact seed unit identifiers and rejects missing, non-finite, non-positive geometry/load values and invalid geometric relationships.

Implemented calculator IDs:

- mass of solids: `mass_rectangular_prism`, `mass_solid_cylinder`, `mass_hollow_cylinder`;
- power/torque/speed: `power_torque_speed`;
- thermal/fluid utility formulas: `linear_thermal_expansion`, `liquid_volume_by_density`;
- axial stress/elongation: `axial_bar`;
- torsion: `solid_shaft_torsion`, `hollow_shaft_torsion`;
- elementary beams: `beam_ss_center_point`, `beam_ss_udl`, `beam_cantilever_end`, `beam_cantilever_udl`;
- bearing life: `bearing_basic_life`;
- gear geometry/forces: `spur_gear_basic`, `helical_gear_basic`, `gear_forces`;
- belt/pressure/flow/thread utilities: `belt_speed`, `pressure_force_area`, `flow_velocity_area`, `thread_pitch_tpi`;
- layout/development: `bolt_circle_coordinates`, `truncated_cone_development`.

Unresolved normative data:

- `bearing_equivalent_dynamic` intentionally returns `requires_standard_table`; manufacturer-specific X/Y/e coefficients must come from a named bearing catalog dataset.
- Gear strength, profile shifts, quality factors, application factors and material allowable stresses remain out of scope until licensed source tables are present.
- Beam formulas are elementary analytical cases only and require engineer review for safety-critical use.
