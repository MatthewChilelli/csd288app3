package com.example.myapplication
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserDataSerializerReadWriteTest {

    @Test
    fun writeAndReadUserData() = runBlockingTest {
        val originalUserData = UserData.newBuilder().setUsername("TestUser").build()
        val outputStream = ByteArrayOutputStream()

        UserDataSerializer.writeTo(originalUserData, outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val recoveredUserData = UserDataSerializer.readFrom(inputStream)

        assertEquals("TestUser", recoveredUserData.username)
    }
}