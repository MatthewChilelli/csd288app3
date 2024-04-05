import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.Serializer
import com.example.myapp.This.UserData
import java.io.InputStream
import java.io.OutputStream
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.CorruptionException
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserDataComposable()
        }

        // Example of writing to the DataStore
        CoroutineScope(Dispatchers.IO).launch {
            applicationContext.userDataStore.updateData { currentUserData ->
                // Assuming `toBuilder()` and `setUsername()` are available
                currentUserData.toBuilder().setUsername("NewUsername").build()
            }
        }
    }
}


class MyApp : Application() {
    // Initialize properties for app-level dependency injection if needed
}

class CustomViewModel(app: MyApp) : ViewModel() {
    // Add ViewModel logic here
}

@Composable
fun App() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "your_start_destination") {
                // Define your navigation graph here
            }
        }
    }
}

@Composable
fun MyScreen(viewModel: CustomViewModel) {
    // UI logic using the viewModel
}

@Composable
fun MyComposable() {
    val context = LocalContext.current
    val app = context.applicationContext as MyApp
    val customViewModel: CustomViewModel = viewModel(factory = CustomViewModelFactory(app))
    MyScreen(viewModel = customViewModel)
}

class CustomViewModelFactory(private val app: MyApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

object UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        try {
            return UserData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.userDataStore: DataStore<UserData> by dataStore(
    fileName = "user_data.pb",
    serializer = UserDataSerializer
)

@Composable
fun UserDataComposable() {
    val context = LocalContext.current
    val userDataFlow = context.userDataStore.data.map { it.username }

    // Now, you can collect this flow to get the username
    // Remember to collect it in a lifecycle-aware manner, such as using LaunchedEffect in Compose
}