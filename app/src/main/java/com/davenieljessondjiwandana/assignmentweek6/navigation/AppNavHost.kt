package com.davenieljessondjiwandana.assignmentweek6.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.davenieljessondjiwandana.assignmentweek6.ui.screen.CommunitiesScreen
import com.davenieljessondjiwandana.assignmentweek6.ui.screen.HomeScreen
import com.davenieljessondjiwandana.assignmentweek6.ui.screen.ProjectDetailsScreen
import com.davenieljessondjiwandana.assignmentweek6.ui.screen.ProjectsScreen
import com.davenieljessondjiwandana.assignmentweek6.viewmodel.SharedViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: SharedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()

    val destination = navController.currentBackStackEntryAsState().value?.destination
    val inDashboard = destination?.hasRoute<DashboardRoute>() == true
    val inCommunities = destination?.hasRoute<CommunitiesRoute>() == true

    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF2F5BEA),
        selectedTextColor = Color.Black,
        indicatorColor = Color(0xFFDBE9FF),
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
    )

    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
        bottomBar = {
            if (inDashboard || inCommunities) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = inDashboard,
                        onClick = {
                            navController.navigate(DashboardRoute) {
                                popUpTo<DashboardRoute> { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Dashboard") },
                        label = { Text("Dashboard") },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        selected = inCommunities,
                        onClick = {
                            navController.navigate(CommunitiesRoute) {
                                popUpTo<DashboardRoute> { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Groups, contentDescription = "Communities") },
                        label = { Text("Communities") },
                        colors = navItemColors
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<DashboardRoute> {
                HomeScreen(
                    student = viewModel.student,
                    uiState = uiState
                )
            }

            composable<CommunitiesRoute> {
                CommunitiesScreen(
                    uiState = uiState,
                    snackbarEvents = viewModel.snackbar,
                    onToggleJoin = viewModel::joinOrLeave,
                    onCommunityClick = { communityId ->
                        viewModel.openCommunity(communityId) {
                            navController.navigate(ClubProjectsRoute(communityId))
                        }
                    }
                )
            }

            composable<ClubProjectsRoute> { entry ->
                val route: ClubProjectsRoute = entry.toRoute()
                ProjectsScreen(
                    communityId = route.communityId,
                    uiState = uiState,
                    form = form,
                    techOptions = viewModel.options,
                    onBack = { navController.popBackStack() },
                    onProjectClick = { projectId ->
                        navController.navigate(ProjectDetail.createRoute(projectId))
                    },
                    onDelete = viewModel::removeProject,
                    onAddClick = viewModel::showAddDialog,
                    onTitleChange = viewModel::setTitle,
                    onDeskripsiChange = viewModel::setDeskripsi,
                    onTechToggle = viewModel::toggleTech,
                    onSave = { viewModel.addProject(route.communityId) },
                    onCancel = viewModel::hideAddDialog
                )
            }

            composable(
                route = ProjectDetail.ROUTE,
                arguments = listOf(
                    navArgument(ProjectDetail.ARG_ID) { type = NavType.StringType }
                )
            ) { entry ->
                val projectId = entry.arguments?.getString(ProjectDetail.ARG_ID).orEmpty()
                ProjectDetailsScreen(
                    projectId = projectId,
                    uiState = uiState,
                    onToggleCompleted = viewModel::toggleCompleted,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}