package com.davenieljessondjiwandana.assignmentweek6.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Community(
    val id: String,
    val nama: String,
    val subtitle: String,
    val deskripsi: String,
    val jumlahMember: Int,
    val icon: ImageVector,
    val color: Color,
    val joined: Boolean = false
)