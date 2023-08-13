package navigation.builder

class NavOptionsBuilder<T> {
    private var isAlreadyExecutedPopUpTo = false
    var isPopUpTo = false
        private set
    var isPopUpToInclusive = false
        private set
    var popUpToRoute: T? = null
        private set

    fun popUpTo(route: T, isInclusive: Boolean = false) {
        if (isAlreadyExecutedPopUpTo) throw DuplicateNavOptionsException(
            "popUpTo() can only be used once."
        )
        isPopUpTo = true
        isPopUpToInclusive = isInclusive
        popUpToRoute = route
        isAlreadyExecutedPopUpTo = true
    }

    fun build() : NavOptions<T> {
        isAlreadyExecutedPopUpTo = false
        return NavOptions(isPopUpTo, isPopUpToInclusive, popUpToRoute)
    }
}

data class NavOptions<T>(
    val isPopUpTo: Boolean = false,
    val isPopUpToInclusive: Boolean = false,
    val popUpToRoute: T? = null
)

data class DuplicateNavOptionsException(val msg: String) : Exception(msg)