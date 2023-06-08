# The Movie DB App

An Android application that displays popular movies and detailed movie information using [The Movie Database (TMDB) API](https://www.themoviedb.org/documentation/api).

## Overview & Architecture
This app allows users to browse a list of popular movies, view movie details, and search through results. It supports both online and offline viewing via local caching.

While the feature set is intentionally lightweight, this project showcases a clean, scalable, and unit-testable architecture using:
* **MVI (Model-View-Intent)** design pattern
* **Modularization by feature**

## Setup & Configuration
To build and run the project, you need a TMDB API key. Add the following line to your `local.properties` file:

```properties
DATABASE_API_KEY="0154126bcc52cfe539c99204454466a9"
```

## Tech Stack & Libraries

- Networking: Retrofit
- Dependency Injection: Hilt
- Image Loading: Glide
- Local Persistence: Room
- Testing: MockK, Paparazzi
- Code Formatting: ktlint

[Code Structure](docs/architecture.md)
