package com.example.searchlistitems.di

import com.example.searchlistitems.data.ChatRepository
import com.example.searchlistitems.ui.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repository
    single { ChatRepository() }

    // ViewModels
    viewModel { ChatViewModel(get()) }
}
