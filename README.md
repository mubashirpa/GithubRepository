# Github Repository

Github Repository is a Kotlin-based mobile application that allows users to search for and explore
GitHub repositories. The app fetches real-time repository data from
the [GitHub REST API](https://docs.github.com/en/rest), provides offline support with Room database,
and features Google Sign-In along with Firebase Cloud Messaging for push notifications.

## **Screenshots**

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="screenshots/screenshot-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="screenshots/screenshot.png">
  <img alt="App Screenshot" src="screenshots/screenshot.png">
</picture>

## Features

### GitHub Repository Search

- Search public repositories.
- Display repository details such as name, description, language, and stars.

### Offline Caching

- Store searched repositories locally using Room Database.
- Allow access to previously viewed data without internet connection.

### Authentication

- Sign in using Google Sign-In via Firebase Authentication.

### Notifications

- Receive push notifications via Firebase Cloud Messaging (FCM).

## Architecture & Tech Stack

- **Clean Architecture with MVVM (Model-View-ViewModel)**
- **Ktor** for network communication
- **Room Database** for offline storage
- **Coroutines & Flow** for asynchronous and reactive programming
- **Koin** for dependency injection
- **Firebase** for authentication and messaging
- **Material Design Components** for a clean and responsive UI
- **Paging3** for pagination

## Installation & Setup

1. **Clone the repository**
   ```sh
   git clone https://github.com/yourusername/GitHubExplorer.git
   cd GitHubExplorer
   ```

2. **Add API Token (Optional but recommended to avoid rate limits)**

- Add the following to `local.properties`:
  ```properties
  GITHUB_TOKEN=your_github_token_here
  ```

3. **Configure Firebase**

- Create a Firebase project.
- Enable Google Sign-In and Firebase Cloud Messaging.
- Download `google-services.json` and place it in the `app/` directory.

4. **Open the project in Android Studio**

5. **Sync Gradle and build the project**

6. **Run the app on an emulator or physical device**

## API Reference

The app uses the following endpoint:

```
https://api.github.com/users/{username}/repos
https://api.github.com/search/repositories
```

Refer to [GitHub API Docs](https://docs.github.com/en/rest) for more.

## Contact

For any questions, feel free to reach out
at [mubashirpa2002@gmail.com](mailto:mubashirpa2002@gmail.com)