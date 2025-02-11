package com.addzero.web.modules.second_brain.note

import com.addzero.web.infra.jimmer.base.BaseCrudController
import com.addzero.web.infra.jimmer.dynamicdatasource.DS
import com.addzero.web.modules.second_brain.note.dto.BizNoteSaveDTO
import com.addzero.web.modules.second_brain.note.dto.BizNoteSpec
import com.addzero.web.modules.second_brain.note.dto.BizNoteUpdateDTO
import com.addzero.web.modules.second_brain.note.dto.BizNoteView
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.mutation.KSimpleSaveResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bizNote")
//@DS("h2")
class BizNoteController(
    private val kSqlClient: KSqlClient,
) : BaseCrudController<BizNote, BizNoteSpec, BizNoteSaveDTO, BizNoteUpdateDTO, BizNoteView> {
    @GetMapping("dhasdjoaisd")
    fun 测试笔记upsert(): KSimpleSaveResult<BizNote> {
//        BizNoteController
        val bizNote = BizNote {

            id = 123123
            title = "测试笔记"
            content = "测试笔记内容"
            tags= listOf("测试标签")
        }
        val save = kSqlClient.save(bizNote)
        return save
    }



}