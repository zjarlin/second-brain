package com.addzero.web.modules.note.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.base.BaseViewModel
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question
import kotlinx.coroutines.launch


class NotesViewModel(service: NotesService) : BaseViewModel<Note, NotesService>(service) {
    private val notesService = service

    var knowledgeGraph by mutableStateOf<KnowledgeGraph?>(null)
        private set

    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentAnswer by mutableStateOf<Answer?>(null)
        private set

    fun uploadFile(file: ByteArray, filename: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                notesService.uploadFile(file, filename)
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
                currentAnswer = notesService.askQuestion(question)
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
                questions = notesService.getQuestionHistory()
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
                knowledgeGraph = notesService.getKnowledgeGraph(query)
            } catch (e: Exception) {
                error = "加载知识图谱失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}