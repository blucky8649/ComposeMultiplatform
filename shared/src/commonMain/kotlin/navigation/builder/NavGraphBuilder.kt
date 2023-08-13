package navigation.builder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import navigation.NavController
import navigation.NavHost

class NavGraphBuilder<T> (
    private val navHost: NavHost<T>,
    val navController: NavController<T>
) {

    private val destinations = mutableListOf<NavDestination<T>>()
    @Composable
    fun renderComposable() {
        navHost.contents(this)
    }

    fun addNavGraph(destination: NavDestination<T>) {
        destinations += destination
    }
}

data class NavDestination<T>(
    val name: String,
    val startDestination: T
)

@Composable
fun <T>NavGraphBuilder<T>.composable(
    route: T, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = navController.currentScreen == route,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        content()
    }
}