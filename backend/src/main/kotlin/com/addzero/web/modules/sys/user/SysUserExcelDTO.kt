//import cn.idev.excel.annotation.ExcelProperty
//import com.addzero.web.modules.sys.user.SysUser;
//
//fun SysUser.toExcelDTO(): SysUserExcelDTO {
//    var entity = SysUserExcelDTO()
//
//    entity.phone = this.phone
//
//
//    entity.password = this.password
//
//
//    entity.avatar = this.avatar
//
//
//    entity.nickname = this.nickname
//
//
//    entity.gender = this.gender
//
//
//    entity.deptId = this.deptId
//
//    return entity
//}
//
//public open class SysUserExcelDTO {
//
//    @ExcelProperty("手机号")
//    var phone: String? = null
//
//
//    @ExcelProperty("密码")
//    var password: String? = null
//
//
//    @ExcelProperty("头像")
//    var avatar: String? = null
//
//
//    @ExcelProperty("昵称")
//    var nickname: String? = null
//
//
//    @ExcelProperty("性别")
//    var gender: String? = null
//
//
//    @ExcelProperty("")
//    var deptId: String? = null
//
//    fun toEntity(): SysUser {
//        return SysUser {
//
//            phone = this.phone
//
//
//            password = this.password
//
//
//            avatar = this.avatar
//
//
//            nickname = this.nickname
//
//
//            gender = this.gender
//
//
//            deptId = this.deptId
//
//        }
//    }
//}