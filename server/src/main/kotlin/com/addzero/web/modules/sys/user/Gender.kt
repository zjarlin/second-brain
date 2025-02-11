package com.addzero.web.modules.sys.user

import org.babyfish.jimmer.sql.EnumItem

enum class Gender {
    @EnumItem(name = "M")
    male,

    @EnumItem(name = "F")
    female
}