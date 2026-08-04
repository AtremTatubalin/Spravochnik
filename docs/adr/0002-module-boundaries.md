# ADR 0002: Границы модулей

- Статус: принято
- Дата: 2026-08-04

## Решение
Feature-модули зависят только от core-контрактов и не зависят друг от друга. `app` является composition root и связывает маршруты. `core:formula-api` и `core:formulas` — чистые Kotlin/JVM-модули без Android API. Room доступен только через `core:database`, а UI получает данные через репозитории и use case.
