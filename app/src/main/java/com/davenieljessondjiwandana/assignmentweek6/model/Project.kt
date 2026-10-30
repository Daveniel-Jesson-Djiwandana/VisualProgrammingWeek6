package com.davenieljessondjiwandana.assignmentweek6.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Project(
    val id: String,
    val communityId: String,
    val title: String,
    val deskripsi: String,
    val tech: List<String>,
    val icon: ImageVector,
    val color: Color,
    val completed: Boolean = false
)