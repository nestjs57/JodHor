package com.pohnpawit.jodhor.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pohnpawit.jodhor.feature.adddorm.AddDormScreen
import com.pohnpawit.jodhor.feature.detail.DormDetailScreen
import com.pohnpawit.jodhor.feature.list.DormListScreen
import com.pohnpawit.jodhor.feature.photo.PhotoViewerScreen

@Composable
fun JodHorNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.DormList,
    ) {
        composable<Route.DormList> {
            DormListScreen(
                onAddDorm = { navController.navigate(Route.AddDorm()) },
                onOpenDorm = { id -> navController.navigate(Route.DormDetail(id)) },
            )
        }
        composable<Route.DormDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.DormDetail>()
            DormDetailScreen(
                dormId = route.dormId,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Route.AddDorm(route.dormId)) },
                onOpenPhoto = { photoId ->
                    navController.navigate(Route.PhotoViewer(route.dormId, photoId))
                },
            )
        }
        composable<Route.AddDorm> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.AddDorm>()
            AddDormScreen(
                dormId = route.dormId,
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
        composable<Route.PhotoViewer> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.PhotoViewer>()
            PhotoViewerScreen(
                dormId = route.dormId,
                photoId = route.photoId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
