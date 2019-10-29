# BlaBla Social Media

This project aims to create an open-source social media application.


## Main Features
* Architecture: MVVM (Model-View-ViewModel)

* Programming Language: [Kotlin](https://kotlinlang.org)

* Testing: Non-instrumented (unit and integration testing) and instrumented (integration and user interface testing) using [Mockito](https://site.mockito.org) and [MockK](https://mockk.io)

* Navigation: [Android Jetpack's Navigation](https://developer.android.com/guide/navigation) 

* Dependency Injection: [Kodein](https://kodein.org/Kodein-DI/)

* User Authenticator: [Firebase Authentication](https://firebase.google.com/docs/auth/android/password-auth)

## Getting Started
### Using Git Bash
```
git clone git@github.com:MurilloComino/android-kotlin-blabla-social-media.git
```

### Using Android Studio
```
in File > New > Project from Version Control... > git

in URL put: https://github.com/MurilloComino/android-kotlin-blabla-social-media.git

and then clone
```

## Installing
* Connect via usb your android phone.
* After cloning the project, open the terminal and navigate to the root folder.

#### Windows
````
gradlew installDebug
````
#### Linux & MacOS
````
./gradlew installDebug
````

## Built With

* [Android Studio 3.5](https://developer.android.com/studio) - The IDE used

## License

* [Apache License 2.0](gitresources/LICENSE.md)