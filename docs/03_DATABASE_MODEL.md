# Модель базы данных

Полная SQL-схема находится в `database/schema.sql`.

## Нормативный контур

- `datasets` — версия набора данных;
- `sources` — официальный документ или каталог;
- `standards` — карточка стандарта;
- `standard_changes` — поправки, изменения, будущие замены;
- `record_sources` — связь любой записи с источником и страницей/таблицей.

## Материалы

- `materials` — марка и классификация;
- `material_aliases` — транслитерации и аналоги;
- `material_conditions` — состояние поставки/термообработка/размерный диапазон;
- `material_properties` — значение или диапазон свойства в конкретном состоянии;
- `material_composition` — диапазон содержания химического элемента;
- `material_applications` — ненормативные области применения с типом источника.

Механические свойства запрещено хранить непосредственно в `materials`, потому что они зависят от состояния, сечения и обработки.

## Подшипники

- `bearings` — базовое обозначение и геометрия;
- `bearing_variants` — уплотнения, зазор, точность, исполнение;
- `bearing_ratings` — C, C0, предел скорости и условия каталога;
- `bearing_aliases` — отечественные/международные соответствия;
- `bearing_coefficients` — X, Y, e и другие коэффициенты конкретного производителя.

## Допуски и посадки

- `size_intervals`;
- `tolerance_grades`;
- `fundamental_deviations`;
- `fit_recommendations`;
- `fit_values_cache` — вычисленный/импортированный кэш с версией источника.

## Сортамент

- `profile_families`;
- `profile_sizes`;
- `profile_properties`;
- `profile_tolerances`.

## Формулы

- `calculators`;
- `calculator_versions`;
- `formula_definitions`;
- `calculator_input_definitions`;
- `calculator_output_definitions`;
- `calculation_runs`;
- `calculation_values`.

## Проекты

- `projects`;
- `assemblies`;
- `parts`;
- `measurements`;
- `attachments`;
- `notes`;
- `calculation_runs`;
- `favorites`.

## Миграции

Каждая версия приложения должна иметь:

- `schemaVersion`;
- миграционный SQL;
- тест открытия предыдущей БД;
- тест сохранности пользовательских проектов;
- checksum seed-наборов.
