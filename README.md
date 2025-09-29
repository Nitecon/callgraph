# C++ Call Graph Plugin for IntelliJ Rider

A powerful tool plugin for IntelliJ Rider that allows you to visualize call graphs of C++ classes, showing both inbound and outbound calls for comprehensive code analysis.

## Features

- **Visual Call Graph**: Interactive tree-based visualization of code dependencies
- **Inbound Call Analysis**: See what functions/classes call into your selected element
- **Outbound Call Analysis**: View what your selected element calls or uses
- **Context Menu Integration**: Right-click on any C++ class, function, or method
- **Statistics Panel**: Get detailed metrics about call relationships
- **Navigation Support**: Click to navigate to referenced code elements

## Usage

1. Open a C++ project in IntelliJ Rider
2. Right-click on any C++ class, function, or method in the editor
3. Select "Show Call Graph" from the context menu (or use Ctrl+Shift+G)
4. View the call graph in the dedicated tool window on the right panel

## Requirements

- IntelliJ Rider 2023.2 or later
- C++ projects with proper indexing

## Development

This plugin is built using:
- Kotlin/JVM
- IntelliJ Platform SDK
- Gradle build system

### Building

```bash
./gradlew build
```

### Running in Development

```bash
./gradlew runIde
```

## Installation

Download from the JetBrains Plugin Repository or build from source.

## License

Licensed under the Apache License 2.0. See LICENSE file for details.
