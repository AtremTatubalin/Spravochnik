# Техническая архитектура Android

## Стек

- Kotlin;
- Jetpack Compose + Material 3;
- Navigation Compose;
- Room/SQLite;
- Kotlin Serialization;
- Coroutines/Flow;
- Hilt или ручной DI (выбрать один вариант в ADR);
- Coil для изображений;
- WorkManager только для локальных задач импорта/резервного копирования;
- чистый Kotlin-модуль формул без Android-зависимостей.

## Слои

```text
Compose UI
  ↓ events / ↑ immutable UiState
ViewModel
  ↓ use cases
Domain
  ├── Calculation engine
  ├── Reverse-engineering engine
  ├── Search/query services
  └── Report model builder
  ↓ repositories
Data
  ├── Room database
  ├── seed importer
  ├── file/photo storage
  └── update package importer
```

## Модули

```text
:app
:core:common
:core:ui
:core:database
:core:formula-api
:core:formulas
:core:validation
:core:search
:core:reference
:core:report
:feature:home
:feature:search
:feature:projects
:feature:materials
:feature:bearings
:feature:fits
:feature:threads
:feature:fasteners
:feature:profiles
:feature:calculators
:feature:reverse
:feature:reports
:feature:settings
```

## Правила зависимостей

- feature-модули не зависят друг от друга;
- UI не читает Room DAO напрямую;
- формульное ядро не знает об Android, Room и Compose;
- каждая формула имеет версию;
- каждый результат хранит версию формулы и версии наборов данных;
- источник нормативного числа должен быть доступен из карточки результата;
- миграции Room обязательны, destructive migration запрещена для production.

## Поиск

- SQLite FTS5 для названий, обозначений и синонимов;
- числовые фильтры — обычные индексы;
- отдельный нормализованный поисковый ключ без пробелов, дефисов и регистра;
- поддержка кириллицы/латиницы: `40Х` и `40X`, `12Х18Н10Т` и `12Kh18N10T`.

## Offline-first

Локальная БД является источником истины. Обновления справочников — подписанные пакеты JSON/SQLite с manifest, checksum, версией схемы и журналом изменений.
