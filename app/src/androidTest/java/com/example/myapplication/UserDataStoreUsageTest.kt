package com.example.myapplication
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDataStoreUsageTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDataStoreUsageInUi() {
        // This is a skeleton test. You need to tailor it based on your actual UI.
        composeTestRule.setContent {
            UserDataComposable()
        }

    }
}