# Input-Data-KMP-App

A cross-platform data management and reminder application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, powered by **Firebase** for real-time cloud data storage and synchronization.

The app allows users to create, update, delete, and manage records with associated date/time values, then automatically sends a notification when the scheduled time arrives.

## Overview

Smart Reminder KMP is a simple but practical productivity application designed to demonstrate how a single shared Kotlin codebase can power multiple platforms while integrating with cloud services and platform-specific features such as notifications.

This project focuses on:

- Cross-platform development using Kotlin Multiplatform
- Shared business logic across platforms
- Cloud database integration using Firebase
- Full CRUD operations
- Date-based reminders and notifications
- Clean and scalable project structure

## Features

- Add new data entries
- Edit existing records
- Delete records
- Save data to Firebase
- Retrieve and display saved data
- Schedule reminders based on a selected date and time
- Send notifications when the scheduled reminder time is reached
- Support for multiple platforms from a shared codebase
- Responsive and reusable UI components using Compose Multiplatform

## Use Case

This application can be used as a lightweight reminder/task manager where users store information with a target date and receive an alert when it is due.

Examples:
- Personal reminders
- Task deadlines
- Follow-up dates
- Appointment tracking
- Event-based notifications

## Screenshots

### 📱 App Preview

| Screen 1 | Screen 2 | Screen 3 |
|----------|----------|----------|
| ![1](https://i.postimg.cc/zBJRK7VN/1.jpg) | ![2](https://i.postimg.cc/yxDxybK3/2.jpg) | ![3](https://i.postimg.cc/gJSrRPCV/3.jpg) |

| Screen 4 | Screen 5 | Screen 6 |
|----------|----------|----------|
| ![4](https://i.postimg.cc/SsTj8pBW/4.jpg) | ![5](https://i.postimg.cc/63jTRNkC/5.jpg) | ![6](https://i.postimg.cc/rmnzWLB4/6.jpg) |

| Screen 7 | Screen 8 | Screen 9 |
|----------|----------|----------|
| ![7](https://i.postimg.cc/vBqD9dJc/7.jpg) | ![8](https://i.postimg.cc/LXChfMcY/8.jpg) | ![9](https://i.postimg.cc/HxZj5Cqd/9.jpg) |

## Built With

### Core Technologies
- **Kotlin Multiplatform**
- **Compose Multiplatform**
- **Kotlin**
- **Firebase**

### Architecture & Development
- Shared business logic
- MVVM architecture
- Repository pattern
- Platform-specific notification handling
- Coroutines / Flow for asynchronous operations

### Firebase Services
- Firebase Realtime Database or Cloud Firestore
- Firebase Authentication (if used)
- Firebase Cloud Messaging (if used, optional depending on implementation)

## Supported Platforms

This project is designed to run on multiple platforms from a single codebase, such as:

- Android
- iOS
- Desktop
- Web

> The exact supported targets depend on your current project configuration.

## Project Goals

The main purpose of this project is to demonstrate practical cross-platform app development using KMP while solving a real-world use case involving:

- data creation and management
- cloud persistence
- scheduling logic
- user reminders
- platform interoperability

## App Functionality

### 1. Create Data
Users can insert new records by entering the required information and selecting a target date/time.

### 2. Read Data
Saved records are loaded from Firebase and displayed in the app interface.

### 3. Update Data
Users can edit previously created items and update their details in the database.

### 4. Delete Data
Users can remove records from Firebase when they are no longer needed.

### 5. Reminder Scheduling
Each record can include a date/time. When the scheduled time arrives, the app triggers a notification to remind the user.

### 6. Notifications
Notifications are handled using platform-specific implementations while keeping the scheduling and data logic shared as much as possible.

## Architecture

The project follows a modular and maintainable architecture to separate UI, business logic, and data access.

### Suggested Flow
- UI Layer
- ViewModel Layer
- Repository Layer
- Firebase Data Source
- Platform Notification Manager

### Shared Module Responsibilities
The shared module may include:
- data models
- business logic
- validation
- repository interfaces / implementations
- viewmodels
- date handling logic
- state management

### Platform-Specific Responsibilities
Platform-specific code may include:
- local notification APIs
- permission handling
- scheduling alarms / background tasks
- Firebase platform initialization
- platform entry points

## Folder Structure

project-root/
├── composeApp/
│   ├── src/
│   │   ├── androidMain/
│   │   ├── iosMain/
│   │   ├── desktopMain/
│   │   ├── webMain/
│   │   └── commonMain/
├── shared/
│   ├── src/
│   │   ├── commonMain/
│   │   ├── androidMain/
│   │   ├── iosMain/
│   │   └── ...
├── iosApp/
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts

## Key Technical Highlights
Single shared codebase for multiple platforms
Reusable UI with Compose Multiplatform
Shared state management
Firebase integration for real-time persistence
Reminder-based notification system
Separation between common and native platform code
Clean project organization for scalability

## How Notifications Work
The reminder system is based on the date and time associated with each record.
