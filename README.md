# Hybrid QA Automation Framework (UI + API)

Selenium WebDriver · REST Assured · TestNG · Maven · POM + Page Factory · ThreadLocal parallel execution ·
Apache POI / JSON data-driven · ExtentReports (auto screenshots) · Log4j2 · GitHub Actions

**Apps under test:** SauceDemo (UI e-commerce) and ReqRes (REST API).

## Run
```bash
mvn clean test                                   # full suite, 4 parallel threads
mvn clean test -Dheadless=true -Dbrowser=chrome  # headless
mvn clean test -Dsuite.file=testng-smoke.xml     # smoke only
export REQRES_API_KEY=your_free_key              # needed for API tests (free key at reqres.in)
```
Outputs: `target/extent-reports/*.html`, `target/screenshots/`, `target/logs/automation.log`.

## Structure
- `base` – ThreadLocal DriverFactory, BasePage (waits, stale handling), BaseTest
- `pages` – Page Objects (Page Factory)
- `endpoints` / `models` – API service layer, paths, request POJOs
- `utils` – Excel/JSON readers, data providers, screenshots, retry
- `listeners` – Extent reporting + failure screenshots
- `config.properties` – overridable via `-D` flags or environment variables
