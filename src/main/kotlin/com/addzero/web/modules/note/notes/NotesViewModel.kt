package com.addzero.web.modules.note.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.web.base.BaseViewModel
import com.addzero.web.model.PageResult
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question
import kotlinx.coroutines.launch
import java.util.*

class NotesViewModel(service: NotesService) : BaseViewModel<Note, NotesService>(service) {
    private val notesService = service

    // 知识图谱状态
    var knowledgeGraph by mutableStateOf<KnowledgeGraph?>(null)
        private set

    // 问答状态
    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentAnswer by mutableStateOf<Answer?>(null)
        private set

    // 笔记树相关方法
    fun createNote(parentId: String? = null, title: String = "新建笔记") {
        val note = Note(
            id = UUID.randomUUID().toString(), title = title, content = "", type = NoteType.MARKDOWN, parentId = parentId, children = emptyList(), createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis(), tags = TODO(), path = TODO(), fileUrl = TODO(), createdBy = TODO(), updatedBy = TODO()
        )
        createItem(note)
    }

    fun updateNote(note: Note) {
        val updatedNote = note.copy(
            updatedAt = System.currentTimeMillis()
        )
        updateItem(updatedNote)
    }

    // 文件上传
    fun uploadFile(file: ByteArray, filename: String) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                notesService.upload(file, filename)
                loadItems() // 刷新笔记列表
            } catch (e: Exception) {
                error = "上传文件失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // 问答功能
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

    // 知识图谱
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

    // 构建树形结构
    private fun buildNoteTree(notes: List<Note>): List<Note> {
        val noteMap = notes.associateBy { it.id }.toMutableMap()
        val rootNotes = mutableListOf<Note>()

        notes.forEach { note ->
            if (note.parentId == null) {
                rootNotes.add(note)
            } else {
                val parent = noteMap[note.parentId]
                if (parent != null) {
                    val updatedParent = parent.copy(
                        children = (parent.children ?: emptyList()) + note
                    )
                    noteMap[parent.id] = updatedParent
                }
            }
        }

        return rootNotes
    }

    // 重写handlePageResult以支持树形结构
    override fun handlePageResult(result: PageResult<Note>) {
        items = buildNoteTree(result.content)
        totalPages = result.totalPages
        totalElements = result.totalElements
        currentPage = result.pageNumber
        pageSize = result.pageSize
    }
}