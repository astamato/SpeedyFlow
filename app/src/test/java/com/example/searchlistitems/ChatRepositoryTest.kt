package com.example.searchlistitems

import com.example.searchlistitems.data.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

//@OptIn(ExperimentalCoroutinesApi::class)
//class ChatRepositoryTest {
//
//    private lateinit var repository: ChatRepository
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//        repository = ChatRepository()
//    }
//
//    @After
//    fun tearDown() {
//        repository.stopGenerating()
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `repository starts with empty messages`() = runTest {
//        assertEquals(0, repository.messagesFlow.value.size)
//        assertEquals(0, repository.skippedMessagesCount.value)
//    }
//
//    @Test
//    fun `repository generates messages when started`() = runTest {
//        repository.startGenerating(this)
//
//        // Advance time to allow message generation
//        advanceTimeBy(1000) // 1 second
//
//        val messages = repository.messagesFlow.value
//        assertTrue("Should have generated messages", messages.isNotEmpty())
//
//        // Stop generating to clean up
//        repository.stopGenerating()
//    }
//
//    @Test
//    fun `repository can be stopped and restarted`() = runTest {
//        repository.startGenerating(this)
//        advanceTimeBy(500)
//
//        val initialCount = repository.messagesFlow.value.size
//        repository.stopGenerating()
//
//        advanceTimeBy(500)
//        val afterStopCount = repository.messagesFlow.value.size
//
//        // Messages should not increase after stopping
//        assertEquals(initialCount, afterStopCount)
//    }
//
//    @Test
//    fun `skipped count can be reset`() = runTest {
//        repository.resetSkippedCount()
//        assertEquals(0, repository.skippedMessagesCount.value)
//    }
//}
