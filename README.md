# **Stopwatch**

This task by CODECRAFT INFOTECH is simple stopwatch where you can see hours, minutes, and seconds with smooth animations and you can also reset it.

---

## **Features**

#### **Animations and Time Tracking**
  - You can see a smooth UI animations where numbers slide in from the right and exit to the left, creating a seamless transition effect.
  - The stopwatch displays hours, minutes, and seconds.
    
#### **Intuitive controls**
  - The stopwatch has a Play/Pause button from which you can play or pause the stopwatch.
  - It also has a Reset button which only appears when stopwatch is paused, allowing you to reset the stopwatch.
    
#### **Foreground services with Notification**
  - A notification appears when the stopwatch is running, displaying hours, minutes, and seconds.
  - You can control the stopwatch directly from the notification with Play/Pause and Reset buttons.
  - The stopwatch continues running even if the app is closed or killed.
  - When you click on the notification it opens the stopwatch.

---

## **Demo**

<video src="https://github.com/user-attachments/assets/88fb3fa1-9a90-4e55-b67e-138e60144508" controls="controls" style="max-width: 100%; height: auto;">
    Demo how the app works.
</video>

---

## **Libraries and Methods Used**

1. **Kotlin**: First-class and official programming language for Android development.
2. **Jetpack compose**: A toolkit for building Android apps that uses a declarative API to simplify and speed up UI development
3. **Material Components for Android**: For modular and customizable Material Design UI components.
4. **MVVM**: It is an architectural pattern that separates UI (View) from business logic (ViewModel) and data handling (Model) to improve maintainability and testability.
5. **Kotlin Coroutines**: They are a concurrency framework that simplifies asynchronous programming by allowing tasks to be written sequentially while managing threading and suspensions efficiently.
6. **Dagger Hilt**: It is a dependency injection library for Android that simplifies providing and managing dependencies across the app's lifecycle.
7. **Splash API**: The Splash API in Android provides a customizable launch screen with a smooth transition into the app using the SplashScreen class.
8. **Jetpack Compose Animation APIs**: It is used for smooth UI transitions with animate*AsState, AnimatedVisibility, and updateTransition to enhance user experience.
9. **Foreground services**: It is a service that runs in the background but remains actively visible to the user through a persistent notification, ensuring it is not killed by the system.

---

## **Lesson Learned**

While building this app I learned:

1. **Foreground Services and Pending Intent**
   - Learned what to define in the Android Manifest like permission, service name, service type, etc.
   - Understood what are the key lifecycle methods like onBind(), onStartCommand(), onDestroy(), etc., and how to use them.
   - Get to know how to create and use PendingIntent for communication between the service and notification.

2. **Coroutines, Jobs and Service connection**
   - Get to know what are coroutineScope() and Jobs, especially SupervisorJob() which is used for structured concurrency.
   - Understood what ServiceConnection() is and how to use it to bind and communicate with the service.

3. **Animations**
   - Explored more animations and from that found and learn about AnimatedContent() and AnimatedContentTransitionScope<>() to create smooth UI transitions.

---

## **Contact**
For any questions or feedback, feel free to contact me at sakhare1181likhit@gmail.com and also connect with me on LinkedIn at www.linkedin.com/in/likhit-sakhare and on Twitter at https://x.com/likhit_sakhare
