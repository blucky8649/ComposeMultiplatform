package navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import navigation.builder.NavOptionsBuilder

class NavController<T>(
    startDestination: T,
) {
    var currentScreen by mutableStateOf(startDestination)
    var navigatable by mutableStateOf(false)
    private var backStackEntry = listOf<T>()
    private val navBuilder get() = NavOptionsBuilder<T>()

    fun navigateTo(route: T, builder: NavOptionsBuilder<T>.() -> Unit) {
        backStackEntry += listOf(currentScreen)
        navigatable = backStackEntry.isNotEmpty()
        navBuilder.apply {
            currentScreen = route
            navBuilder.apply(builder)
        }
    }
    fun popBackStack(): Boolean {
        println("backStack = $backStackEntry")
        if (backStackEntry.isEmpty()) return false
        currentScreen = backStackEntry.last()
        navigatable = backStackEntry.size > 1
        backStackEntry = backStackEntry.dropLast(1)
        return true
    }
}

@Composable
fun <T>rememberNavController(startDestination: T): MutableState<NavController<T>> = rememberSaveable(stateSaver = navSaver()) {
    mutableStateOf(NavController(startDestination = startDestination))
}

fun <T>navSaver() = listSaver<NavController<T>, T>(
    save = { listOf(it.currentScreen) },
    restore = { NavController(it[0]) }
)

@Composable
fun <T>setNavigateBackButtonIfPossible(
    navController: NavController<T>
) : Boolean {
    AnimatedVisibility(
        visible = navController.navigatable,
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "popBackStack"
            )
        }
    }
    return navController.navigatable
}