package com.addzero.web.modules.note.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.addzero.web.base.BaseViewModel
import com.addzero.web.model.PageResult
import com.addzero.web.model.enums.Route
import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
import com.addzero.web.modules.note.qa.Answer
import com.addzero.web.modules.note.qa.Question
import kotlinx.coroutines.launch
import java.util.*

class NotesViewModel(service: NotesService) : BaseViewModel<Note, NotesService>(service) {



    // 知识图谱状态
    var knowledgeGraph by mutableStateOf<KnowledgeGraph?>(null)
        private set

    // 问答状态
    private var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    //当前回答
    var currentAnswer by mutableStateOf<Answer?>(null)
        private set

    // 笔记树相关方法
    fun createNote(parentId: String? = null, title: String = "新建笔记") {
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = "",
            type = NoteType.MARKDOWN,
            parentId = parentId,
            children = emptyList(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            tags = emptyList(),
            path = "",
            fileUrl = "",
            createdBy = "",
            updatedBy = ""
        )
        executeWithLoadingInDsl {
            service.save(note)
            loadItems()
        }
    }

    fun updateNote(note: Note) {
        val updatedNote = note.copy(
            updatedAt = System.currentTimeMillis()
        )
        executeWithLoadingInDsl {
            service.update(updatedNote)
            loadItems()
        }
    }

    // 文件上传
    fun uploadFile(file: ByteArray, filename: String) {
        executeWithLoadingInDsl {
            service.upload(file, filename)
            loadItems()
        }
    }

    // 问答功能
    fun askQuestion(question: String) {
        executeWithLoadingInDsl {
            currentAnswer = service.askQuestion(question)
        }
    }

    fun loadQuestionHistory() {
        executeWithLoadingInDsl {
            questions = service.getQuestionHistory()
        }
    }

    // 知识图谱
    fun loadKnowledgeGraph(query: String? = null) {
        executeWithLoadingInDsl {
            knowledgeGraph = service.getKnowledgeGraph(query)
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