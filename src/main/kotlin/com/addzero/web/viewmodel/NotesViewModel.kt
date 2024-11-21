package com.addzero.web.viewmodel

import androidx.compose.runtime.*
import com.addzero.web.model.notes.*
import com.addzero.web.service.NotesService
import com.addzero.web.config.AppConfig
import com.addzero.web.service.ApiNotesService
import com.addzero.web.service.MockNotesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel {
    private val service: NotesService = if (AppConfig.USE_MOCK_DATA) {
        MockNotesService()
    } else {
        ApiNotesService()
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    var notes by mutableStateOf<List<Note>>(emptyList())
        private set

    var currentNote by mutableStateOf<Note?>(null)
        public set

    var knowledgeGraph by mutableStateOf<KnowledgeGraph?>(null)
        private set

    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentAnswer by mutableStateOf<Answer?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadNotes(parentId: String? = null) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                notes = service.getNotes(parentId)
            } catch (e: Exception) {
                error = "加载笔记失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createNote(note: Note) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.createNote(note)
                loadNotes(note.parentId)
            } catch (e: Exception) {
                error = "创建笔记失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateNote(note: Note) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.updateNote(note)
                loadNotes(note.parentId)
            } catch (e: Exception) {
                error = "更新笔记失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteNote(id: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.deleteNote(id)
                loadNotes()
            } catch (e: Exception) {
                error = "删除笔记失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun uploadFile(file: ByteArray, filename: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                service.uploadFile(file, filename)
            } catch (e: Exception) {
                error = "上传文件失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun askQuestion(question: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                currentAnswer = service.askQuestion(question)
            } catch (e: Exception) {
                error = "提问失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadQuestionHistory() {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                questions = service.getQuestionHistory()
            } catch (e: Exception) {
                error = "加载问答历史失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadKnowledgeGraph(query: String? = null) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                knowledgeGraph = service.getKnowledgeGraph(query)
            } catch (e: Exception) {
                error = "加载知识图谱失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}