# Этап 02: нормативный контур Room и импорт seed

Реализован первый недеструктивный Room-срез схемы: `datasets`, `sources`, `standards`, `standard_changes` и внешний contentless FTS5-индекс. Domain-модели и repository-контракты находятся в чистом Kotlin-модуле `:core:reference`; Room entity, DAO, importer и реализации repository — в `:core:database`.

## Импорт

В assets скопированы только JSON-файлы, уже предоставленные пакетом. Asset-manifest дополнен вычисленными SHA-256. До открытия транзакции importer проверяет checksum каждого объявленного файла и полностью декодирует поддерживаемые наборы. Запись sources, standards, FTS и dataset provenance выполняется одной Room-транзакцией. Повторный импорт того же manifest возвращает `AlreadyCurrent`.

На этом этапе фактически импортируются только наборы `sources` и `standards`. Остальные предоставленные файлы проверяются по manifest, но не помечаются импортированными и будут подключаться на соответствующих этапах после реализации их entity и DAO. Числовые нормативные пробелы не заполнялись.

## Миграция

Версия Room — 2. Миграция 1→2 только добавляет contentless FTS5 `search_index`; destructive migration запрещена. Экспортированная Room schema хранится в `core/database/schemas`.

## Нерешённые нормативные данные

Все позиции `data/MISSING_DATA_REGISTER.csv` остаются нерешёнными. Материалы, подшипники, профили, формулы и reverse-wizards не объявляются импортированными текущим Room-срезом.
