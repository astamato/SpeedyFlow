package com.example.searchlistitems

import com.example.searchlistitems.data.ChatRepository
import com.example.searchlistitems.ui.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

//@OptIn(ExperimentalCoroutinesApi::class)
//class ChatViewModelTest : KoinTest {
//
//    private val testDispatcher = UnconfinedTestDispatcher()
//    private val repository: ChatRepository by inject()
//    private lateinit var viewModel: ChatViewModel
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(testDispatcher)
//
//        // Start Koin with test module
//        startKoin {
//            modules(
//                module {
//                    single { ChatRepository() }
//                    factory { ChatViewModel(get()) }
//                })
//        }
//
//        viewModel = ChatViewModel(repository)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `viewModel starts with correct initial state`() = runTest {
//        assertEquals(0, viewModel.chatMessages.value.size)
//        assertEquals(0, viewModel.skippedMessagesCount.value)
//        assertEquals(false, viewModel.isChatPaused.value)
//        assertEquals(true, viewModel.isAutoScrolling.value)
//    }
//
//    @Test
//    fun `toggleChatPause changes pause state`() = runTest {
//        assertEquals(false, viewModel.isChatPaused.value)
//
//        viewModel.toggleChatPause()
//        assertEquals(true, viewModel.isChatPaused.value)
//
//        viewModel.toggleChatPause()
//        assertEquals(false, viewModel.isChatPaused.value)
//    }
//
//    @Test
//    fun `toggleAutoScroll changes auto scroll state`() = runTest {
//        assertEquals(true, viewModel.isAutoScrolling.value)
//
//        viewModel.toggleAutoScroll()
//        assertEquals(false, viewModel.isAutoScrolling.value)
//
//        viewModel.toggleAutoScroll()
//        assertEquals(true, viewModel.isAutoScrolling.value)
//    }
//
//    @Test
//    fun `resetSkippedCount resets the counter`() = runTest {
//        repository.resetSkippedCount()
//        assertEquals(0, viewModel.skippedMessagesCount.value)
//    }
//}
