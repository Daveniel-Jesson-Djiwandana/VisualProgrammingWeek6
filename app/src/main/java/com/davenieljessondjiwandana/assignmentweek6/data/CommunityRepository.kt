package com.davenieljessondjiwandana.assignmentweek6.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.ui.graphics.Color
import com.davenieljessondjiwandana.assignmentweek6.model.Community
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CommunityRepository {

    private val _communities = MutableStateFlow(
        listOf(
            Community(
                id = "gdsc",
                nama = "GDSC",
                subtitle = "Google Developer Student Club",
                deskripsi = "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.",
                jumlahMember = 142,
                icon = Icons.Filled.Code,
                color = Color(0xFF2F5BEA)
            ),
            Community(
                id = "cybersec",
                nama = "CyberSecurity Guild",
                subtitle = "Security & Pentesting",
                deskripsi = "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.",
                jumlahMember = 88,
                icon = Icons.Filled.Shield,
                color = Color(0xFFD32F2F)
            ),
            Community(
                id = "ai",
                nama = "AI Society",
                subtitle = "Machine Learning & LLM",
                deskripsi = "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.",
                jumlahMember = 115,
                icon = Icons.Filled.AutoAwesome,
                color = Color(0xFF7C3AED)
            ),
            Community(
                id = "mobiledev",
                nama = "Mobile Dev Community",
                subtitle = "Jetpack Compose & KMP",
                deskripsi = "Native Jetpack Compose, Kotlin Multiplatform, dan pengembangan aplikasi iOS.",
                jumlahMember = 95,
                icon = Icons.Filled.PhoneAndroid,
                color = Color(0xFF0288D1)
            ),
            Community(
                id = "webdev",
                nama = "Web Dev Group",
                subtitle = "Frontend & Backend",
                deskripsi = "Belajar web pakai laravel dapat mencari client real",
                jumlahMember = 120,
                icon = Icons.Filled.Language,
                color = Color(0xFF059669)
            ),
            Community(
                id = "calculus",
                nama = "Calculus Club",
                subtitle = "Math & Complex Math",
                deskripsi = "Membuat mahasiswa bingung dengan matematika yang rumit",
                jumlahMember = 64,
                icon = Icons.Filled.Calculate,
                color = Color(0xFFEA580C)
            )
        )
    )
    val communities: StateFlow<List<Community>> = _communities.asStateFlow()

    fun toggleJoin(communityId: String) {
        _communities.update { list ->
            list.map { if (it.id == communityId) it.copy(joined = !it.joined) else it }
        }
    }
}