plugins {
    id("com.example.android-application")
}

group = "${group}.android-app"

dependencies {
    implementation("com.example.myproduct.user-feature:table")
}

android {
    namespace "com.example.myproduct.app"
}
