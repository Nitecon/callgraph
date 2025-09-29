# CallGraph

An IntelliJ CLion plugin for generating call graphs for C++ projects.

<!-- Plugin description -->
A call graph generator for C++ projects in IntelliJ CLion.

This plugin helps developers visualize function call relationships in C++ codebases,
making it easier to understand code structure and dependencies.
<!-- Plugin description end -->

## Features

- Generate visual call graphs for C++ functions
- Navigate code dependencies
- Understand code structure at a glance

## Installation

Install from the JetBrains Plugin Repository or build from source.

## Development

### Building the Plugin

This project uses Gradle with the IntelliJ Platform Plugin template.

```bash
# Build the plugin
./gradlew build

# Run tests
./gradlew test

# Run the plugin in a sandboxed IDE
./gradlew runIde
```

### GitHub Actions CI/CD

This repository includes automated GitHub Actions workflows:

#### Build Workflow (`.github/workflows/build.yml`)

Triggers on:
- Push to `main` or `develop` branches
- Pull requests to `main` branch

Actions:
- Sets up JDK 17
- Builds the plugin using Gradle
- Uploads build artifacts

#### Release Workflow (`.github/workflows/release.yml`)

Triggers on:
- Push of version tags (e.g., `v1.0.0`, `v0.1.0`)

Actions:
- Builds the plugin
- Creates a GitHub release
- Attaches the plugin ZIP file to the release

### Creating a Release

To create a new release:

1. Update the version in `gradle.properties`:
   ```properties
   pluginVersion = 1.0.0
   ```

2. Update the `CHANGELOG.md` file with your changes

3. Commit your changes:
   ```bash
   git add .
   git commit -m "Release version 1.0.0"
   ```

4. Create and push a version tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

5. The GitHub Actions release workflow will automatically:
   - Build the plugin
   - Create a GitHub release
   - Upload the plugin distribution files

### Project Structure

```
.
├── .github/workflows/     # GitHub Actions workflows
├── src/
│   ├── main/
│   │   ├── kotlin/       # Plugin source code
│   │   └── resources/    # Plugin resources and configuration
│   └── test/             # Test source code
├── build.gradle.kts      # Gradle build configuration
├── gradle.properties     # Plugin and platform configuration
└── CHANGELOG.md          # Release notes and changes
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
