//package com.addzero.web.modules.note.notes
//
//import androidx.compose.runtime.*
//import com.addzero.web.base.BaseViewModel
//import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
//import com.addzero.web.modules.note.knowlagegraph.KnowledgeGraph
//import com.addzero.web.modules.note.qa.Answer
//import com.addzero.web.modules.note.qa.Question
//import com.addzero.web.modules.second_brain.note.BizNote
//import com.addzero.web.modules.second_brain.note.copy
//import com.addzero.web.modules.second_brain.note.dto.BizNoteSaveDTO
//import com.addzero.web.modules.second_brain.note.dto.BizNoteSpec
//import com.addzero.web.modules.second_brain.note.dto.BizNoteUpdateDTO
//import com.addzero.web.modules.second_brain.note.dto.BizNoteView
//import kotlinx.coroutines.CoroutineScope
//import org.apache.poi.hwpf.model.NoteType
//import java.util.*
//
//class NotesViewModel : BaseViewModel<BizNote, BizNoteSpec, BizNoteSaveDTO, BizNoteUpdateDTO, BizNoteView, BizNoteView>() {
//
//    // 知识图谱状态
//    var knowledgeGraph by mutableStateOf<KnowledgeGraph?>(null)
//
//    // 问答状态
//    private var questions by mutableStateOf<List<Question>>(emptyList())
//
//    //当前回答
//    var currentAnswer by mutableStateOf<Answer?>(null)
//
//
//    //上传加载变量
//    var showUploadDialog by remember { mutableStateOf(false) }
//
//    //当前项
//    var currentItem by remember { mutableStateOf(null) }
//
//    // 问答功能
//    fun askQuestion(question: String) {
//        // TODO:
//    }
//
//
//    // 知识图谱
//    fun loadKnowledgeGraph(query: String? = null) {
//        // TODO:
//    }
//
//    // 构建树形结构
//    private fun buildNoteTree(notes: List<BizNote>): List<BizNote> {
//        return TODO("提供返回值")
//    }
//
//}
//
