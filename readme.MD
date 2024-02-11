# Nu Tax App

CLI application for calculating taxes.

## Requirements

Please install the requirements below, [sdkman](https://sdkman.io) is recommended for this task:

    Java 21 (Temurin)  sdk install java 17.0.0-tem
    Gradle 8.5  sdk install gradle 8.5

To run this app:

Jar:

```bash
./gradlew build jar
java -jar app/build/libs/app.jar
```

Using Gradle:

```bash
    ./gradlew build run --console=plain
```

Console will be ready to receive input once `>Task :app:build` or `>Task :app:run` is prompted.

## Docker

run the app in a container using:

```bash
    docker build -t nu-tax-app .
    docker run -i nu-tax-app
```
