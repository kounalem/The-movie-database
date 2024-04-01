# The movie db
Displays results for movies using the movie db API.

### Design approach:
The project is a simple app showing a list of the most popular movies and their details using the 
movie db API and the local database. Furthermore, the user has the ability to search through the 
movie results. The app is available online and offline.

Despite being simple in features, I wanted the project to showcase the use of the MVI design pattern
and the modularise by feature approach to improve the overall architecture of the app, allowing the
behaviour of the app to be super clean and unit testable.

### Libraries used:
Retrofit, Hilt, Glide, Room

For testing: mockk showkase, Paparazzi

[Code Structure](docs/architecture.md)