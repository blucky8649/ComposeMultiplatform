import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import navigation.NavController
import navigation.NavHost
import navigation.builder.composable
import navigation.rememberNavController
import navigation.setNavigateBackButtonIfPossible
import org.jetbrains.compose.resources.ExperimentalResourceApi


lateinit var onBackPressedAction: () -> Boolean
@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val navController by rememberNavController(Screen.FIRST)

        LaunchedEffect(Unit) {
            onBackPressedAction = {
                navController.popBackStack()
            }
        }
        scaffoldLayout(navController = navController) {
            initNavHost(navController)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun scaffoldLayout(
    navController: NavController<Screen>,
    modifier: Modifier = Modifier,
    completion: @Composable (PaddingValues) -> Unit)
{
    Scaffold(
        topBar = { topAppBar(navController) }
    ) {
        completion(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar(
    navController: NavController<Screen>
) {
    TopAppBar(
        title = {
            Text(
                "Compose Multiplatform",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            setNavigateBackButtonIfPossible(navController)
        },
        actions = {
            if (navController.currentScreen.needOptions) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Localized description"
                    )
                }
            }
        }
    )
}

expect fun getPlatformName(): String

@Composable
fun initNavHost(navController: NavController<Screen>) {
    NavHost(navController) {
        composable(Screen.FIRST) { FirstScreen {
            navController.navigateTo(Screen.SECOND) {}
        } }
        composable(Screen.SECOND) { SecondScreen {
            navController.navigateTo(Screen.THIRD) {}
        } }
        composable(Screen.THIRD) { ThirdScreen {
            navController.navigateTo(Screen.FIRST) {}
        } }
    }.build()
}

enum class Screen(val needOptions: Boolean) {
    FIRST(true), SECOND(false), THIRD(true)
}