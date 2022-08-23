# ![VJK Solutions](https://vjk.solutions/images/logo-64.png) Catalist

**Catalist** is a demonstration app for Android devices. It implements a categorized to-do list. Multiple named lists are supported, although each list must have at least one category to assign items to.

**Catalist** supports assigning tasks to others, assigning due dates to tasks, and even tracking a budget for an item. 

* Assigned tasks merely attach a name to a task. There is no support for any sort of notification. It's just a reminder to the user that the task has been assigned to another to complete. Assigned tasks are shown with a person icon.
* Tasks with due dates are shown in bold type with a clock icon. 
* Tasks with a budget can track the total amount budget as well as the amount spent so far. Tasks with budgets are shown with a money icon.
* Tasks can be marked as "In Progress" and if so are shown with an ellipsis icon.
* Tasks can be marked as "Complete" and if so are shown with strikethrough text and a checkmark icon. If the user prefers, they can (in settings) indicate that tasks should be deleted when marked complete instead.

The following technologies are used:

* [Kotlin](https://kotlinlang.org/) - now the preferred language for Android applications.
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - replaces the older XML way of building user interfaces. Similar to React functional components, UI elements are built ("composed") from functions.
* [Room](https://www.geeksforgeeks.org/overview-of-room-in-android-architecture-components/) - a simplified data access layer over SQLite. 
* [Hilt](https://dagger.dev/hilt/) - a dependency injection library that utilizes Dagger.
