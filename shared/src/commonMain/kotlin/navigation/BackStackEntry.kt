package navigation

import navigation.builder.NavOptions

data class BackStackEntry<T> (
    val route: T,
    val navOptions: NavOptions<T>
)