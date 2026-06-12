package kz.hashiroii.esep

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kz.hashiroii.home.HomeScreen
import kz.hashiroii.home.navigation.HomeRoute

@Composable
fun EsepApp() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen()
        }
    }
}