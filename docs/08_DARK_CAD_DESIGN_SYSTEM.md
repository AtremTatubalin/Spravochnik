# Дизайн-система Dark CAD

## Палитра

| Токен | Значение | Назначение |
|---|---|---|
| `bg.canvas` | `#0D1117` | основной фон |
| `bg.surface` | `#151B23` | карточки |
| `bg.elevated` | `#1C2430` | модальные элементы |
| `line.primary` | `#344252` | границы |
| `line.grid` | `#22303D` | сетка/чертёж |
| `text.primary` | `#E6EDF3` | основной текст |
| `text.secondary` | `#9EABB8` | подписи |
| `accent.blue` | `#42A5F5` | действие/активное |
| `accent.cyan` | `#5BC0EB` | чертёжные линии |
| `status.warning` | `#FFB74D` | допущение |
| `status.error` | `#EF5350` | ошибка |
| `status.success` | `#66BB6A` | проверено |

## Сетка

- базовый шаг 4 dp;
- основные отступы 8/12/16/24/32 dp;
- радиус карточки 6–8 dp;
- линии 1 dp;
- минимальная зона нажатия 48 dp;
- числовые поля используют tabular figures.

## Типографика

- UI: Inter или Roboto;
- технические обозначения/формулы: IBM Plex Sans + математический рендер;
- код/обозначения: IBM Plex Mono;
- числа результата крупнее подписи минимум на два уровня.

## Экран калькулятора

```text
TopAppBar
TechnicalDiagramCard
InputSection
AssumptionsSection
CalculateButton
ResultSummary
IntermediateValues
WarningsAndValidity
Sources
SaveToProject
```

## Чертёжные элементы

- схемы рисуются Canvas/SVG, а не растровыми картинками;
- размерные линии и обозначения масштабируются;
- выбранный размер подсвечивается синим;
- отсутствующий критичный размер — янтарным;
- противоречащий размер — красным.
