# Premium Weather — Production Android App

Premium, minimal, modern weather app built with **Kotlin, Jetpack Compose, Material 3** — works **without API key, account, Firebase, or backend**.

## Features

- **Current location** via Fused Location Provider (permission requested only after user action). Handles denial, permanent denial, GPS off, approximate/precise.
- **City search** with debounce 300-500ms, cancellation, loading/error/empty, short cache. Uses Open-Meteo Geocoding API.
- **Favorites & default location** offline via Room.
- **Weather data** from Open-Meteo Forecast API: temperature, feels like, condition (WMO codes 0-99), humidity, precipitation probability/amount, rain, snowfall, wind speed/direction/gusts, pressure, visibility, cloud cover, UV, sunrise/sunset, high/low, hourly (24h) and daily (7 days).
- **Domain model WeatherSnapshot** with nullable fields — null never becomes 0.
- **Cache & Offline**: Room caches last weather, location, timezone, fetchedAt. App shows cache instantly on launch (stale-while-revalidate). Supports FRESH (<30m), STALE (<6h), VERY_STALE. Offline banner, last update time.
- **Resilience**: OkHttp with timeouts, retry, exponential backoff, jitter, request deduplication, min refresh interval 5min, throttling. Handles timeout, DNS, 400, 429, 5xx, malformed JSON without stack trace.
- **UI**: Location header, large temp, weather icon, condition, feels like, high/low, hourly LazyRow, daily list, metrics grid (humidity, wind+compass, pressure, visibility, cloud, UV, precipitation), temperature graph, wind compass, sun progress. Responsive for phones/tablets, portrait/landscape, edge-to-edge, notch, safe areas, font scaling, RTL, 48dp touch targets.
- **Visual system**: Premium cinematic backgrounds with procedural Canvas animations (clouds, rain, snow, stars, sun/moon glow, fog). WeatherVisualState holds background type, gradient, particle type/density, cloud density, animation speed, overlay opacity, text mode, sun/moon, lightning flag. States: clear day/night, partly cloudy, cloudy, rain/heavy, thunderstorm, snow, fog, sunrise/sunset. Gradients + particles + parallax + smooth transitions. Lightning rare, disabled on Reduced Motion.
- **Design tokens**: spacing, radius, typography, icon sizes, opacity, elevation, animation durations unified.
- **Animation levels**: High / Balanced (default) / Low / Off. Auto-simplifies on Battery Saver / low-end devices. Pauses when backgrounded.
- **Icons & branding**: Original vector system for all weather conditions + sunrise, sunset, humidity, pressure, visibility, UV, precipitation, wind, location, search, favorite, settings, refresh, error, offline. Adaptive icon (foreground/background), monochrome, round, splash via theme.
- **Settings**: Appearance (System/Light/Dark/Weather), Units (C°/F°, km/h/m/s/mph, mm/in, hPa/inHg, km/miles), Location, Updates, Motion, Language (en/ru/az), Notifications, About. High contrast, Reduced Motion.
- **Accessibility**: TalkBack, contentDescription like "Current temperature 24 degrees Celsius", semantic labels, decorative Canvas not in a11y tree, contrast, font scaling.
- **Share**: Android Sharesheet with text forecast, no coordinates exposed.
- **Widget & Notifications**: Optional, cache-based, not complicating core.

## Architecture

```
data/
  local/
    room/ (AppDatabase, WeatherDao, FavoriteDao, WeatherEntity, FavoriteEntity)
    datastore/ (SettingsDataStore)
  remote/
    dto/ (OpenMeteoForecastResponse, Geocoding DTOs - kotlinx.serialization)
    provider/ (OpenMeteoWeatherProvider, OpenMeteoGeocodingProvider)
  repository/ (WeatherRepositoryImpl, FavoritesRepositoryImpl, SettingsRepositoryImpl)
domain/
  model/ (WeatherSnapshot, WeatherCondition, WeatherVisualState, CacheFreshness, LocationModel, FavoriteLocation, HourlyForecast, DailyForecast)
  mapper/ (WeatherCodeMapper, WeatherVisualMapper, DayNightResolver, UnitConverter, ErrorMapper)
  provider/ (WeatherProvider, GeocodingProvider interfaces)
  repository/ (WeatherRepository, SettingsRepository, FavoritesRepository)
presentation/
  theme/ (Color, Type, Shape, Tokens, Theme)
  util/ (WeatherIconResolver, WeatherBackgroundResolver, WeatherTransitionController)
  viewmodel/ (WeatherViewModel, SearchViewModel, SettingsViewModel)
  ui/components/ (WeatherBackground, WeatherIcon, MetricCard, HourlyForecast, DailyForecast, WeatherErrorCard, OfflineBanner, LocationHeader, SettingRow, TemperatureGraph, WindCompass, SunProgress)
  ui/screens/ (MainScreen, SearchScreen, FavoritesScreen, SettingsScreen, AboutScreen)
```

- Single HTTP library: OkHttp
- Single serialization: kotlinx.serialization-json
- Coroutines, Flow, StateFlow, ViewModel, Repository pattern, DataStore, Room, java.time, Android Location APIs.
- UI never knows API URLs.

## Open-Meteo

Uses public endpoints:
- `https://api.open-meteo.com/v1/forecast`
- `https://geocoding-api.open-meteo.com/v1/search` and `/reverse`

No API key. HTTPS only. Respects rate limits via cache, debounce, dedup, throttling, backoff. Attribution in About screen.

> Check https://open-meteo.com/en/terms for commercial use. This project does not promise unlimited access.

## Permissions

- `INTERNET`
- `ACCESS_COARSE_LOCATION`
- `ACCESS_FINE_LOCATION`
- `POST_NOTIFICATIONS` only if notifications enabled (optional)

No storage, contacts, phone, camera, mic, VPN, accessibility service, ads, analytics, Firebase, advertising ID.

## Build & Run

```bash
./gradlew clean test lint assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk`

Install: `adb install app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions

Workflow `.github/workflows/android.yml`:
- push / pull_request / workflow_dispatch
- checkout, setup-java 17, setup-gradle 8.7, Gradle cache, `test`, `lint`, `assembleDebug`, upload artifact `WeatherApp-debug.apk`

Release workflow uses signing secrets only for release, no keystore committed.

## Privacy

- No accounts, ads, analytics, tracking.
- No logging of precise coordinates.
- Data stays on device.
- Privacy screen in About.

## Limitations

- Provider-dependent: forecast accuracy depends on Open-Meteo.
- No widget in core build (can be added without complicating core).
- Geocoding cache is in-memory 5min.
- Requires internet for first fetch, then works offline with last cache.

## License

MIT
