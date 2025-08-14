# Twitch-like Android App

A sample Android application that mimics Twitch's main viewing screen with a video player and live chat feed.

## Features

### Video Player
- **ExoPlayer Integration**: Uses ExoPlayer to stream sample videos from public sources
- **Random Video Selection**: Automatically selects from a curated list of sample videos
- **Auto-loop**: Videos automatically repeat for continuous playback
- **16:9 Aspect Ratio**: Maintains proper video aspect ratio

### Live Chat Feed
- **High-Frequency Message Simulation**: Generates ~30-50 messages per second
- **Smart Buffering**: Uses a buffering mechanism to handle high message volume
- **Batch Processing**: Emits updates to UI in batches at ~15 FPS for smooth performance
- **Message Overflow Handling**: Discards oldest messages when buffer exceeds capacity (200 messages)
- **Auto-scrolling**: Automatically scrolls to show latest messages
- **Pause/Resume**: Ability to pause chat generation while maintaining buffer

### Performance Optimizations
- **MVVM Architecture**: Clean separation of concerns with ViewModel and Repository
- **StateFlow**: Reactive UI updates using Kotlin Flow
- **LazyColumn**: Efficient list rendering for chat messages
- **Message Keying**: Proper item keys for optimal list performance
- **Synchronized Buffering**: Thread-safe message buffer management

### UI Controls
- **Chat Pause/Resume**: Toggle chat message generation
- **Auto-scroll Toggle**: Enable/disable automatic scrolling to latest messages
- **Skipped Messages Counter**: Track messages discarded due to buffer overflow
- **Reset Counter**: Clear the skipped messages count

## Architecture

### MVVM Pattern
```
UI Layer (Compose) → ViewModel → Repository → Data Layer
```

### Components
- **ChatRepository**: Simulates incoming chat events and manages buffering
- **ChatViewModel**: Manages UI state and user interactions
- **VideoPlayer**: ExoPlayer wrapper for video playback
- **ChatList**: LazyColumn-based chat message display
- **ControlPanel**: User controls for chat management

### Data Flow
1. **Repository** generates messages at high frequency (~50 msg/sec)
2. **Buffer** accumulates messages with overflow protection
3. **Batch Processor** emits updates to UI at fixed rate (~15 FPS)
4. **ViewModel** exposes state to UI components
5. **UI** renders messages and handles user interactions

## Technical Details

### Dependencies
- **ExoPlayer**: Video playback (media3-exoplayer, media3-ui, media3-datasource)
- **Jetpack Compose**: Modern UI toolkit
- **ViewModel**: Lifecycle-aware UI state management
- **Kotlin Flow**: Reactive programming for state management
- **Coroutines**: Asynchronous programming

### Performance Metrics
- **Message Generation**: ~50 messages/second
- **UI Updates**: ~15 FPS (66ms intervals)
- **Buffer Capacity**: 200 messages
- **Memory Management**: Automatic cleanup of old messages

### Sample Data
- **20 Usernames**: Gaming and development-themed usernames
- **40 Messages**: Twitch-style chat messages and emotes
- **5 Sample Videos**: Public domain videos from Google's sample collection

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run on an Android device or emulator
4. The app will automatically start playing a random video and generating chat messages

## Controls

- **Pause Chat**: Stops message generation but maintains buffer
- **Resume Chat**: Restarts message generation
- **Auto-scroll Toggle**: Controls automatic scrolling to latest messages
- **Reset Counter**: Clears the skipped messages counter

## Performance Goals Achieved

✅ **30-50 messages/second** simulation
✅ **Buffering mechanism** for smooth UI updates
✅ **Batch processing** at fixed rate
✅ **Buffer overflow handling** with message discarding
✅ **MVVM architecture** with clean separation
✅ **Jetpack Compose** with LazyColumn for efficient rendering
✅ **ExoPlayer** integration for video playback
✅ **Pause chat toggle** functionality
✅ **Skipped messages counter** display

The app demonstrates how to handle high-frequency data streams in Android while maintaining smooth UI performance through intelligent buffering and batch processing strategies.
