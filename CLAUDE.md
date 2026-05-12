# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

**Run the application (development):**
```
mvn spring-boot:run
```
Application starts at http://localhost:8080.

**Build for production:**
```
mvn clean package -Pproduction
```

**Run all tests:**
```
mvn test
```

**Run a single test class:**
```
mvn test -Dtest=CalendarioServiceImplTest
```

**Run integration tests** (starts/stops Spring Boot server automatically):
```
mvn verify -Pit
```

## Architecture Overview

BocaPlus is a **Vaadin 14 + Spring Boot** membership management system (likely for a sports club). The UI is built entirely server-side in Java using Vaadin Flow, with a small amount of LitElement/Polymer web components in `frontend/src/`.

### Domain Model

The core domain revolves around:
- **Socios** (members): tracked by `numero_socio`, with `CategoriaSocio` and `TagSocio` labels
- **Comercios** (merchants): businesses with `Sucursal` (branches), `CategoriaComercio`, `TagComercio`, and `Promocion` (discounts)
- **PromocionComercio**: join table linking promotions to merchants; stores a `ReglaRecurrente` (recurrence rule) serialized as JSON
- **Ventas** (sales): purchases recording which `Socio` used which `Promocion` at which `Sucursal`
- **Provincia / Localidad**: geographic hierarchy used by both Socios and Comercios

### Data Layer

Uses **Spring Data JDBC** (not JPA). Key patterns:
- All entities extend `AbstractEntity<T>` which implements `Persistable` and requires calling `entity.setNew(true)` before the first save — repositories won't insert otherwise
- String IDs (Provincia, Localidad) and BIGINT auto-generated IDs (most others) are both used
- `CrudService<T, I>` is the base service interface; all service implementations follow the pattern of wrapping a repository and implementing `createNew()` (which calls `setNew(true)`)
- `FilterableCrudService` extends this for services needing filtered queries
- `DataJdbcConfiguration` registers custom converters: `ReglaRecurrente` ↔ JSON string for persistence

### Recurrence Rules (`calendario` package)

`PromocionComercio` supports promotional schedules via `ReglaRecurrente` — a Jackson-polymorphic interface with concrete types: `ReglaAnual`, `ReglaMensual`, `ReglaSemanal`, `ReglaDiaria`. The type discriminator is the `"type"` JSON property. `ReglasFactory` handles serialization/deserialization. These rules produce RFC 5545 RRULE strings via `lib-recur`.

### Security

Spring Security with form login. Four roles defined in `Role.java`: `admin`, `club`, `comercio`, `socio`. Access control is applied at the Vaadin view level via `@RolesAllowed` (checked by `SecurityUtils.isAccessGranted()`). The `User` entity has a nullable `sucursal` field that restricts `comercio`-role users to a single branch. Demo users are seeded by `DataGenerator` on first startup:
- `admin@bocaplus.com.ar` / `admin` (ADMIN)
- `comercio1@bocaplus.com.ar` / `comercio1` (COMERCIO)
- `comercio2@bocaplus.com.ar` / `comercio2` (COMERCIO + ADMIN)

### Database

H2 in-memory database with schema defined in `src/main/resources/schema.sql`. Schema is created fresh on each startup (`ddl-auto=create-drop`). Demo data is loaded by `DataGenerator` via `@PostConstruct` — it checks `provinciaService.count() != 0` to skip if data already exists.

### View Layer

All views live in `src/main/java/.../views/` and are registered automatically via Vaadin's routing. `MainView` (extends `AppLayout`) is the shell; it builds the side navigation menu dynamically based on the current user's role using `SecurityUtils.isAccessGranted()`. The `views/config/Edit*` views provide CRUD grids for reference data (categories, provinces, etc.).

Frontend assets in `frontend/` are bundled by the Vaadin Maven plugin using webpack. CSS is per-view in `frontend/styles/views/`. The `frontend/src/views/` LitElement components are used for specific client-side UI needs.

### Export

`SocioExcelExporter` uses Apache POI to generate `.xlsx` files. `ExportDataView` wraps the download in `FileDownloadWrapper` from the Vaadin community add-on.

### Query / Statistics

`DataQueryService` executes raw SQL via `NamedParameterJdbcTemplate` and returns `ResultTable` (a structured row/column result). `EstadisticasService` uses this to produce chart data consumed by `EstadisticasView`.
