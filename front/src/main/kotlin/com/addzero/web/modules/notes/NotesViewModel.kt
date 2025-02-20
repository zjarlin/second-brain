//package com.addzero.web.modules.notes
//
//import androidx.compose.runtime.*
//import com.addzero.web.base.BaseViewModel
//import com.addzero.web.modules.notes.dto.NotesSaveDTO
//import com.addzero.web.modules.notes.dto.NotesSpec
//import com.addzero.web.modules.notes.dto.NotesUpdateDTO
//import com.addzero.web.modules.notes.dto.NotesView
//import org.springframework.stereotype.Component
//
//@Component
//class NotesViewModel : BaseViewModel<Notes, NotesSpec, NotesSaveDTO, NotesUpdateDTO, NotesView>() {
//    // 树形结构的笔记数据
//    private val _treeData = mutableStateOf<List<NoteTreeNode>>(emptyList())
//    val treeData: State<List<NoteTreeNode>> = _treeData
//
//    // 当前选中的笔记
//    private val _selectedNote = mutableStateOf<NoteTreeNode?>(null)
//    val selectedNote: State<NoteTreeNode?> = _selectedNote
//
//    // 编辑器内容
//    private val _editorContent = mutableStateOf("")
//    val editorContent: State<String> = _editorContent
//
//    init {
//        // 初始化模拟数据
//        _treeData.value = createMockData()
//    }
//
//    private fun createMockData(): List<NoteTreeNode> {
//        return listOf(
//            NoteTreeNode(
//                id = 1,
//                title = "工作笔记",
//                content = "# 工作笔记\n这是一些工作相关的笔记",
//                children = listOf(
//                    NoteTreeNode(
//                        id = 2,
//                        title = "项目计划",
//                        content = "## 项目计划\n- [ ] 完成需求分析\n- [ ] 技术方案设计",
//                        children = emptyList()
//                    ),
//                    NoteTreeNode(
//                        id = 3,
//                        title = "会议记录",
//                        content = "## 会议记录\n### 2024-01-10\n讨论了项目进度",
//                        children = emptyList()
//                    )
//                )
//            ),
//            NoteTreeNode(
//                id = 4,
//                title = "个人笔记",
//                content = "# 个人笔记\n记录一些个人想法",
//                children = listOf(
//                    NoteTreeNode(
//                        id = 5,
//                        title = "学习计划",
//                        content = "## 学习计划\n1. Kotlin进阶\n2. Compose Desktop开发",
//                        children = emptyList()
//                    )
//                )
//            )
//        )
//    }
//
//    fun selectNote(note: NoteTreeNode) {
//        _selectedNote.value = note
//        _editorContent.value = note.content
//    }
//
//    fun updateNoteContent(content: String) {
//        _editorContent.value = content
//        // TODO: 实现保存到后端的逻辑
//    }
//
//    fun addNote(parentId: Long?, title: String) {
//        // TODO: 实现添加笔记的逻辑
//    }
//
//    fun deleteNote(noteId: Long) {
//        // TODO: 实现删除笔记的逻辑
//    }
//}
//
//data class NoteTreeNode(
//    val id: Long,
//    val title: String,
//    val content: String,
//    val children: List<NoteTreeNode>
//)
