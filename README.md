# RexLogger - Advanced Android Logging Library 📝  

[![JitPack](https://jitpack.io/v/ilokeshmeena/RexLogger.svg)](https://jitpack.io/#ilokeshmeena/RexLogger)  
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)  

**RexLogger** is a powerful Android logging library with enhanced features like **file logging, log rotation, JSON pretty-printing, thread info, and more!**  

---

## 🔥 Features  

✅ **Multi-Logging Support** (Logcat + File)  
✅ **Log Rotation** (Size-based & Time-based)  
✅ **JSON & XML Pretty Printing**  
✅ **Thread Information** (Thread name & ID)  
✅ **Custom Log Formatting**  
✅ **Log Filtering** (By tag, level, or custom rules)  
✅ **Crash Logging** (Auto-save uncaught exceptions)  
✅ **Lightweight & No Dependencies**  

---

## 📦 Installation  

### Step 1: Add JitPack Repository  
Add this to your **root `build.gradle`**:  
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add Dependency  
Add this to your **app-level `build.gradle`**:  
```gradle
dependencies {
    implementation 'com.github.ilokeshmeena:RexLogger:1.0.1' // Replace with latest version
}
```

---

## 🚀 Usage  

### 1. Initialize (In `Application` Class)  
```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        RexLogger.init(
            context = this,
            config = RexLogger.Config(
                minLogLevel = LogLevel.DEBUG,  // Minimum log level (DEBUG, INFO, WARN, ERROR)
                enableFileLogging = true,      // Save logs to file
                logFileName = "app_logs.log", // Custom log file name
                maxLogSizeMB = 5,             // Max log file size (MB) before rotation
                maxLogFiles = 3                // Max rotated log files to keep
            )
        )
    }
}
```

### 2. Log Messages  
```kotlin
RexLogger.d("TAG", "Debug message")  
RexLogger.i("TAG", "Info message")  
RexLogger.w("TAG", "Warning message")  
RexLogger.e("TAG", "Error message", exception)  

// Pretty-print JSON
RexLogger.json("TAG", "{ \"key\": \"value\" }")  

// Pretty-print XML
RexLogger.xml("TAG", "<root><item>value</item></root>")  
```

### 3. Advanced Usage  
#### **Custom Log Formatting**  
```kotlin
RexLogger.setFormatter { level, tag, message ->
    "[${System.currentTimeMillis()}] [$level] $tag → $message"
}
```

#### **Log File Management**  
```kotlin
// Get all saved logs
val logs = RexLogger.getLogFiles()  

// Clear logs
RexLogger.clearLogs()  

// Force log flush
RexLogger.flush()  
```

---

## 🛠 Build from Source  

### Step 1: Clone the Repo  
```bash
git clone https://github.com/ilokeshmeena/RexLogger.git
cd RexLogger
```

### Step 2: Build & Publish Locally  
```bash
./gradlew clean build publishToMavenLocal
```

### Step 3: Use in Another Project  
```gradle
implementation 'com.ilokeshmeena:RexLogger:1.0.1'
```

---

## 🔄 **Logging Modes**  
Configure RexLogger to run in different modes based on your needs:  

### 1. **Logcat Only (Default)**  
_For development - logs only to Android Studio's Logcat._  
```kotlin
RexLogger.init(
    context = this,
    config = RexLogger.Config(
        enableFileLogging = false,  // Disable file logging
        isLogcatEnabled = true      // Default (can be omitted)
    )
)
```

### 2. **File Logging Only**  
_For production - saves logs to files only (no Logcat)._  
```kotlin
RexLogger.init(
    context = this,
    config = RexLogger.Config(
        enableFileLogging = true,
        isLogcatEnabled = false,    // Disable Logcat
        logFileName = "prod_logs.log"
    )
)
```

### 3. **Both Logcat and File Logging**  
_For debugging in production - logs to both places._  
```kotlin
RexLogger.init(
    context = this,
    config = RexLogger.Config(
        enableFileLogging = true,
        isLogcatEnabled = true,    // Both enabled
        maxLogSizeMB = 5           // Rotate after 5MB
    )
)
```

---

## 📂 **Retrieving Saved Logs**  
Access saved logs programmatically:  

### 1. **Get All Log Files**  
```kotlin
val logFiles: List<File> = RexLogger.getLogFiles()
logFiles.forEach { file ->
    println("Log file: ${file.name} (${file.length()} bytes)")
}
```

### 2. **Read Logs from a Specific File**  
```kotlin
val latestLogFile = RexLogger.getLogFiles().firstOrNull()
val logs: String = latestLogFile?.readText() ?: "No logs found"
```

### 3. **Export Logs for Debugging**  
```kotlin
val logs = RexLogger.getLogFiles()
    .joinToString("\n\n") { it.readText() }

// Share via email/cloud
val intent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, logs)
}
startActivity(Intent.createChooser(intent, "Share Logs"))
```

---

## 🛠 **Complete Configuration Options**  
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `enableFileLogging` | `Boolean` | `false` | Enable file logging |
| `isLogcatEnabled` | `Boolean` | `true` | Enable Logcat output |
| `logFileName` | `String` | `"app_logs.log"` | Custom log filename |
| `maxLogSizeMB` | `Int` | `5` | Max file size before rotation (MB) |
| `maxLogFiles` | `Int` | `5` | Max rotated files to keep |
| `minLogLevel` | `LogLevel` | `DEBUG` | Minimum log level to record |

---

## 🚀 **Usage Example**  
```kotlin
// Initialize (in Application class)
RexLogger.init(
    context = this,
    config = RexLogger.Config(
        enableFileLogging = true,
        isLogcatEnabled = true,
        minLogLevel = LogLevel.INFO
    )
)

// Log a message
RexLogger.i("MainActivity", "App started")

// Retrieve logs later
val logs = RexLogger.getLogFiles().first()?.readText()
```



### **Default Format**  
```
[2023-10-20 14:30:45] [DEBUG] MainActivity → App started successfully
```

### **With Thread Info**  
```
[2023-10-20 14:30:45] [Thread-2] [DEBUG] Network → Fetching data...
```

### **JSON Pretty Print**  
```json
{
  "user": {
    "name": "John",
    "age": 25
  }
}
```

---

## 🚨 Troubleshooting  

| Issue | Solution |
|-------|----------|
| **Logs not appearing?** | Check `minLogLevel` in config |
| **File logging not working?** | Verify storage permissions |
| **Class not found?** | Ensure JitPack repo is added |
| **Build errors?** | Check Java & Gradle versions |

---

## 📜 License  
```text
Copyright 2023 ilokeshmeena

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 💡 Contributions  
PRs & issues welcome!  
📌 **Steps to contribute**:  
1. Fork the repo  
2. Create a branch (`git checkout -b feature/new-logger`)  
3. Commit changes (`git commit -m "Add feature"`)  
4. Push (`git push origin feature/new-logger`)  
5. Open a PR  

---

## 📬 Contact  
📧 **Email**: ilokeshmeena@example.com  
🐦 **Twitter**: [@ilokeshmeena](https://twitter.com/ilokeshmeena)  

---

🚀 **Happy Logging!** Let me know if you need any improvements! 🎉
