package navigation

import androidx.compose.runtime.Composable
import navigation.builder.NavGraphBuilder

class NavHost<T>(
    private val navController: NavController<T>,
    val contents: @Composable NavGraphBuilder<T>.() -> Unit
) {
    private val navRenderer get() = NavGraphBuilder(this, navController)
    @Composable
    fun build() {
        navRenderer.renderComposable()
    }
}