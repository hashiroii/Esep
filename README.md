<div align="center">

# Esep — Personal Finance Analytics

**Есеп** (Kazakh: *account, calculation*) — a native Android app that turns your bank statements into clear, visual personal finance analytics.

![Platform](https://img.shields.io/badge/platform-Android-green?style=flat-square)
![Min SDK](https://img.shields.io/badge/min%20SDK-29-blue?style=flat-square)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?style=flat-square&logo=kotlin)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square)
![Architecture](https://img.shields.io/badge/architecture-Clean%20%2B%20MVI-orange?style=flat-square)

</div>

---

## About

Most banking apps in Kazakhstan show you your transaction history — but they don't tell you *where your money actually goes*. Esep fixes that.

Import the PDF statement that your bank lets you export, and Esep does the rest: it parses every transaction, categorizes it automatically, and visualizes your spending through charts and breakdowns so you can finally understand your financial habits.

Works with any Kazakhstani bank that exports PDF account statements — including **Kaspi Bank**, Halyk Bank, Forte Bank, and others.

---

## How it works

1. Open your banking app and export your account statement as a PDF
2. Open Esep, tap **Import**, and select the PDF
3. Esep parses and categorizes all transactions automatically
4. Browse your spending through charts, category breakdowns, and monthly summaries

No accounts, no logins, no bank API access. Everything runs fully on-device.

---

## Features

- **One-tap PDF import** — Import statements directly from your device storage
- **Automatic categorization** — Transactions sorted into: Food, Transport, Groceries, Fuel, Health, Entertainment, Utilities, and more — tuned for Kazakhstani merchants
- **Home dashboard** — Monthly overview: total income vs. expenses, top categories, recent transactions
- **Spending charts** — Interactive bar and line charts built with [Vico](https://github.com/patrykandpatrick/vico) show trends over days, weeks, and months
- **Category breakdown** — Donut charts show how your budget splits across categories
- **Dark & light theme** — Full Material You support with dynamic color on Android 12+

---

## Screenshots

> *Coming soon*

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Charts | Vico for Compose |
| Architecture | Clean Architecture + MVI |
| Dependency Injection | Hilt (Dagger) |
| Local Database | Room |
| Annotation Processing | KSP |
| Async | Coroutines + Flow |
| PDF Parsing | PdfBox Android |
| Build System | Gradle Kotlin DSL + Version Catalog |

---

## Architecture

Strict **Clean Architecture** split into independent Gradle modules:

```
app/                    ← Entry point, Hilt setup, navigation
│
├── core/
│   ├── domain/         ← Entities, repository interfaces, use cases (pure Kotlin)
│   ├── data/           ← Room DB, repository implementations, PDF parser, Hilt modules
│   └── ui/             ← Shared Compose components, design tokens, theme
│
└── feature/
    ├── home/           ← Dashboard screen, ViewModel, MVI state
    └── analytics/      ← Charts, category breakdowns, period filters
```

**MVI pattern** across all feature modules — the screen sends `Intent` events to a `ViewModel`, which produces a single `UiState` stream consumed by the composable. No side effects leak into the UI layer.

Feature modules depend only on `:core:domain` and are completely blind to the data layer. The `:app` module is the glue that assembles the Hilt dependency graph.

---

## Roadmap

- [ ] Monthly budget limits with alerts
- [ ] SMS-based import for banks that send transaction notifications
- [ ] Export analytics as image or PDF report
- [ ] Home screen widget for balance summary
- [ ] Multi-account support

---

## Disclaimer

Esep is an independent open-source project. It is **not affiliated with, endorsed by, or connected to** any bank mentioned in this document. Bank names are referenced solely to describe file format compatibility. The app does not access any bank API, server, or account — it only reads PDF files that users export themselves from their banking app.