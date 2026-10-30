package com.davenieljessondjiwandana.assignmentweek6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davenieljessondjiwandana.assignmentweek6.data.CommunityRepository
import com.davenieljessondjiwandana.assignmentweek6.data.ProjectRepository
import com.davenieljessondjiwandana.assignmentweek6.model.Community
import com.davenieljessondjiwandana.assignmentweek6.model.Project
import com.davenieljessondjiwandana.assignmentweek6.model.Student
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class ProjekUiState(
    val communities: List<Community> = emptyList(),
    val projects: List<Project> = emptyList()
) {
    val joinedCommunities: List<Community> get() = communities.filter {it.joined}
    val joinedCount: Int get() = joinedCommunities.size
    val visibleProjects: List<Project>
        get() = projects.filter { p -> joinedCommunities.any { it.id == p.communityId } }
    val totalProjects: Int get() = visibleProjects.size
    fun findCommunity(id: String): Community? = communities.find {it.id == id}
    fun findProject(id: String): Project? = projects.find {it.id == id}
    fun projectsIn(communityId: String): List<Project> =
        projects.filter {it.communityId == communityId}
}

data class ProjectFormState(
    val show: Boolean = false,
    val title: String = "",
    val deskripsi: String = "",
    val tech: List<String> = emptyList()
) {
    val isValid: Boolean
        get() = title.isNotBlank() && deskripsi.isNotBlank() && tech.isNotEmpty()
}

class SharedViewModel(
    private val communityRepository: CommunityRepository = CommunityRepository,
    private val projectRepository: ProjectRepository = ProjectRepository) : ViewModel() {
    val student = Student(
        nama = "Daveniel Jesson Djiwandana",
        prodi = "Teknik Informatika",
        angkatan = 2025,
        nim = "1076012510001"
    )
    val options: List<String> = projectRepository.options
    val uiState: StateFlow<ProjekUiState> = combine(
        communityRepository.communities,
        projectRepository.projects
    ) {communities, projects ->
        ProjekUiState(communities, projects)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProjekUiState(
            communities = communityRepository.communities.value,
            projects = projectRepository.projects.value
        )
    )

    private val _form = MutableStateFlow(ProjectFormState())
    val form: StateFlow<ProjectFormState> = _form.asStateFlow()
    private val _snackbar = Channel<String>(Channel.BUFFERED)
    val snackbar = _snackbar.receiveAsFlow()

    fun joinOrLeave(communityId: String) {
        communityRepository.toggleJoin(communityId)
    }
    fun openCommunity(communityId: String, onOpen: () -> Unit) {
        val community = uiState.value.findCommunity(communityId)
        if (community?.joined == true) {
            onOpen()
        } else {
            _snackbar.trySend("Harap bergabung ke komunitas terlebih dahulu!")
        }
    }

    fun removeProject(projectId: String) {
        projectRepository.deleteProject(projectId)
    }
    fun toggleCompleted(projectId: String) {
        projectRepository.toggleStatus(projectId)
    }
    fun showAddDialog() {
        _form.value = ProjectFormState(show = true)
    }

    fun hideAddDialog() {
        _form.value = ProjectFormState()
    }
    fun setTitle(value: String) {
        _form.update {it.copy(title = value)}
    }
    fun setDeskripsi(value: String) {
        _form.update {it.copy(deskripsi = value)}
    }
    fun toggleTech(tech: String) {
        _form.update {
            val newTech = if (tech in it.tech) it.tech - tech else it.tech + tech
            it.copy(tech = newTech)
        }
    }

    fun addProject(communityId: String) {
        val f = _form.value
        if (!f.isValid) return
        projectRepository.addProject(
            communityId = communityId,
            title = f.title.trim(),
            deskripsi = f.deskripsi.trim(),
            tech = f.tech
        )
        hideAddDialog()
    }
}