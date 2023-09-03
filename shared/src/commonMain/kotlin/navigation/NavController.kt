package navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import navigation.builder.NavOptions
import navigation.builder.NavOptionsBuilder

class NavController<T>(
    private val startDestination: T,
) {
    var currentScreen by mutableStateOf(startDestination)
    var navigatable by mutableStateOf(false)
    private var backStack = listOf(BackStackEntry(route = startDestination, navOptions = NavOptions()))

    fun navigateTo(route: T, builder: NavOptionsBuilder<T>.() -> Unit) {
        val navOptions = NavOptionsBuilder<T>().apply(builder).build()
        val entry = BackStackEntry(route, navOptions)
        backStack += listOf(entry)
        currentScreen = route
        navigatable = backStack.isNotEmpty()
    }
    fun popBackStack(): Boolean {
        try {
            println("backStack = $backStack")
            if (backStack.isEmpty()) return false
            val entry = backStack.last()

            val option = entry.navOptions
            backStack = if (option.isPopUpTo && option.popUpToRoute != null) {
                backStack.calcBackStackEntryAfterPopUpTo(option.popUpToRoute, option.isPopUpToInclusive)
            } else {
                backStack.dropLast(1)
            }
            currentScreen = backStack.last().route
            navigatable = backStack.size > 1
            return true
        } catch (e: NoSuchElementException) {
            navigatable = false
            backStack += listOf(BackStackEntry(route = startDestination, NavOptions()))
            currentScreen = startDestination
            return false
        }


    }

    fun List<BackStackEntry<T>>.calcBackStackEntryAfterPopUpTo(
        popUpToRoute: T,
        isPopUpToInclusive: Boolean
    ): List<BackStackEntry<T>> {
        for (i in size - 1 downTo 0) {
            val entry = get(i)
            if (entry.route == popUpToRoute) {
                return subList(0, if (isPopUpToInclusive) i else i + 1)
            }
        }
        return this
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