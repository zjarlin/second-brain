//package com.addzero.web.modules.second_brain.dotfiles
//
//import cn.idev.excel.FastExcel
//import com.addzero.common.kt_util.isNotEmpty
//import com.addzero.common.kt_util.isNotNew
//import com.addzero.web.infra.jimmer.base.BaseCrudController
//import com.addzero.web.infra.jimmer.base.BaseFastExcelApi
//import com.addzero.web.infra.jimmer.base.ExcelDataListener
//import com.addzero.web.modules.second_brain.dotfiles.dto.DotfilesOutVO
//import com.addzero.web.modules.second_brain.dotfiles.dto.DotfilesSaveInputDTO
//import com.addzero.web.modules.second_brain.dotfiles.dto.DotfilesSpec
//import com.addzero.web.modules.second_brain.dotfiles.dto.DotfilesUpdateInputDTO
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestPart
//import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.multipart.MultipartFile
//
//
//@RestController
//@RequestMapping("/dotfiles")
//class DotfilesController(
//) : BaseCrudController<BizDotfiles, DotfilesSpec, DotfilesSaveInputDTO, DotfilesUpdateInputDTO, DotfilesOutVO>,
//    BaseFastExcelApi<BizDotfiles, DotfilesSpec, BizDotfilesExcelDTO> {
//
//
//    @PostMapping("/import")
//
//    override fun import(@RequestPart file: MultipartFile): Int {
//
//        val use = file.inputStream.use {
//            val excelDataListener = ExcelDataListener<BizDotfilesExcelDTO>()
//            val excelWriteDTOCLASS = BizDotfilesExcelDTO::class.java
//            FastExcel.read(it, excelWriteDTOCLASS, excelDataListener).sheet().doRead()
//            val caches = excelDataListener.caches
//            caches
//        }
//        val map = use.filter { it.isNotEmpty() }.filter { it.isNotNew() }.map { toEntity(it) }
//        val filter = map.filter {
//            val b = false
//            b
//        }
//        val totalAffectedRowCount = sql.saveEntities(map).totalAffectedRowCount
//        return totalAffectedRowCount
//    }
//
//
//    override fun toExcelWriteDTO(entity: BizDotfiles): BizDotfilesExcelDTO {
//        val toExcelDTO = entity.toExcelDTO()
//        return toExcelDTO
//    }
//
//    override fun toEntity(excelWriteDTO: BizDotfilesExcelDTO): BizDotfiles {
//        val toEntity = excelWriteDTO.toEntity()
//        return toEntity
//    }
//
//
////    @GetMapping(pageRestUtl)
////    @Operation(summary = "分页查询")
////    fun page(
////        @RequestParam spec: BizEnvVarsSpec,
////        @RequestParam(defaultValue = "1") pageNum: Int,
////        @RequestParam(defaultValue = "10") pageSize: Int,
////    ): Page<DotfilesItem> {
////
////        var pageNum = pageNum
////        pageNum -= 1
////
////
////        val execute1 = sql.createQuery(CLASS()) {
////            orderBy(table.createTime.desc())
////            select(
////                table.fetch(DotfilesItem::class)
////            )
////        }.fetchPage(pageNum, pageSize)
////
////        return execute1
////    }
//
////    @PostMapping("/create")
////    @SaIgnore
////    fun doaijsdoi(@RequestBody dotfilesInput: DotfilesInput) {
////
////        val listOf = listOf("export", "alias", "function", "sh")
////        val map = listOf.map {
////            val bizEnvVars = BizEnvVars(dotfilesInput.toEntity { }) {
////                name = it
////            }
////        }
////    }
////
////    @GetMapping("linuxConfigurationFileContentExtractionAssistant")
////    @Operation(summary = "linux配置文件内容抽取助手")
////    fun linuxConfigurationFileContentExtractionAssistant(@RequestParam hello: String): List<DotfilesStructuredOutput>? {
////        val readUtf8Lines =
////            FileUtil.readUtf8String("/Users/zjarlin/IdeaProjects/jl-tools/jl-starter-web/src/main/resources/dotfiles/.zshrc")
////        val promptTemplate = """
////            你是一个dotfiles管理助手,你能从unix文件中找出有用的信息,包括但不限于
////            环境变量export, 别名alias,  自定义函数function, 自定义脚本片段sh
////        """.trimIndent()
////
////        val chatClient = AiCtx.defaultChatClient(ChatModels.KIMI_MOONSHOT)
////        val formatClass = DotfilesStructuredOutput::class.java
////        val (newPrompt, quesCtx) = structuredOutputContext(readUtf8Lines, promptTemplate, formatClass)
////        val call = chatClient.prompt().user { u: ChatClient.PromptUserSpec -> u.text(newPrompt).params(quesCtx) }.call()
////        val entity = call.entity(object : ParameterizedTypeReference<List<DotfilesStructuredOutput>>() {})
////        val toJson = entity.toJSONString()
////        return entity
////    }
//
//
//}