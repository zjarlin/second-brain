package com.addzero.web.base

import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import cn.hutool.core.util.TypeUtil
import cn.hutool.extra.spring.SpringUtil
import cn.idev.excel.FastExcel
import com.addzero.common.kt_util.isNotEmpty
import com.addzero.common.kt_util.isNotNew
import com.addzero.web.infra.jimmer.base.BaseController
import com.addzero.web.infra.jimmer.base.BaseCrudController
import com.addzero.web.infra.jimmer.base.BaseFastExcelApi
import com.addzero.web.infra.jimmer.base.ExcelDataListener
import com.addzero.web.infra.jimmer.base.pagefactory.PageResult
import com.addzero.web.infra.jimmer.base.pagefactory.createPageFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.apache.poi.ss.formula.functions.T
import org.babyfish.jimmer.Input
import org.babyfish.jimmer.View
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.specification.KSpecification
import org.babyfish.jimmer.sql.kt.ast.table.makeOrders
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass

@Component
abstract class BaseViewModel<T : Any, Spec : KSpecification<T>, SaveInputDTO : Input<T>, UpdateInputDTO : Input<T>, V : View<T>, ExcelDTO : Any> :
    BaseCrudController<T, Spec, SaveInputDTO, UpdateInputDTO, V>, BaseFastExcelApi<T, Spec, ExcelDTO> {








}
