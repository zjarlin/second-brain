package com.addzero.web.infra.jimmer.base.baseentity

import com.addzero.common.kt_util.isNull
import com.addzero.common.util.LoginUtil
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BaseEntityDraftInterceptor : DraftInterceptor<BaseEntity, BaseEntityDraft> {

    override fun beforeSave(draft: BaseEntityDraft, original: BaseEntity?) {
        val loginUserId = LoginUtil.getLoginUserId()
        val now = LocalDateTime.now()
        if (!isLoaded(draft, BaseEntityDraft::updateTime)|| draft.updateTime.isNull()) {
            draft.updateTime = now
        }

        if (!isLoaded(draft, BaseEntityDraft::updateBy)|| draft.updateBy.isNull()) {
            draft.updateBy {
                id = loginUserId
            }
        }

        if (original === null) {
            if (!isLoaded(draft, BaseEntityDraft::createTime)|| draft.createTime.isNull()) {
                draft.createTime = now
            }

            if (!isLoaded(draft, BaseEntityDraft::createBy)|| draft.createBy.isNull()) {
                draft.createBy {
                    id = loginUserId
                }
            }
        }
    }
}