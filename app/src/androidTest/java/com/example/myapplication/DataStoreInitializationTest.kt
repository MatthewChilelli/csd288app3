package com.example.myapplication
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Test
import userDataStore

class DataStoreInitializationTest {

    @Test
    fun dataStoreIsInitialized() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dataStore = context.userDataStore
        assertNotNull(dataStore)
    }
}