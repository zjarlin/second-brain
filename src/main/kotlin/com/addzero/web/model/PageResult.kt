package com.addzero.web.model

import BizDotFiles
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.TypeReference
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
fun main() {
    val trimIndent = """
       {"content":[{"id":"1867888063852134424","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"function","name":"showssh","value":"sudo launchctl list | grep ssh","describtion":"查看ssh状态","status":"1","fileUrl":null},{"id":"1867888063852134423","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"function","name":"unssh","value":"sudo launchctl unload -w /System/Library/LaunchDaemons/ssh.plist","describtion":"关闭ssh","status":"1","fileUrl":null},{"id":"1867888063852134422","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"function","name":"enssh","value":"sudo launchctl load -w /System/Library/LaunchDaemons/ssh.plist","describtion":"打开ssh","status":"1","fileUrl":null},{"id":"1867888063852134420","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"var","name":"trash_path","value":"~/.Trash","describtion":"# 定义回收站目录","status":"1","fileUrl":null},{"id":"1867888063852134419","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"sh","name":"#没有brew自动安装","value":"if [[ ! -n ${'$'}(brew -v) ]]; then\n    ibrew\nfi","describtion":"#没有brew自动安装","status":"1","fileUrl":null},{"id":"1867888063852134418","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"linux","osStructure":"不限","defType":"alias","name":"iohmyzsh","value":"sh -c \"${'$'}(curl -fsSL https://gitee.com/allenjia09/ohmyzsh/raw/master/tools/install.sh)\"'","describtion":"安装ohmyzsh","status":"1","fileUrl":null},{"id":"1867888063852134417","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"alias","name":"ibrew","value":"/bin/zsh -c \"${'$'}(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)\"'","describtion":"安装brew","status":"1","fileUrl":null},{"id":"1867888063852134416","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"alias","name":"bui","value":"brew uninstall'","describtion":"brew","status":"1","fileUrl":null},{"id":"1867888063852134415","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"alias","name":"bs","value":"brew search'","describtion":"brew","status":"1","fileUrl":null},{"id":"1867888063852134414","createTime":"2024-12-14 19:03:12","updateTime":"2024-12-14 19:03:12","osType":"mac","osStructure":"不限","defType":"alias","name":"bri","value":"brew reinstall'","describtion":"brew","status":"1","fileUrl":null}],"totalElements":131,"totalPages":14,"pageNumber":1,"pageSize":10,"isFirst":true,"isLast":false} 
    """.trimIndent()

    val typeReference =object : TypeReference<PageResult<BizDotFiles>>() {}
    val parseObject = JSON.parseObject(trimIndent, typeReference)
    println(parseObject)
}
//@Serializable
data class PageResult<T>(
    val content: List<T> = emptyList(),

    val totalElements: Long = 0L,

    val totalPages: Int = 0,

    val pageNumber: Int = 1,

    val pageSize: Int = 10,

    val isFirst: Boolean = true,

    val isLast: Boolean = true
) {
    companion object {
        fun <T> empty(pageSize: Int = 20) = PageResult<T>(
            content = emptyList(),
            totalElements = 0,
            totalPages = 0,
            pageNumber = 0,
            pageSize = pageSize,
            isFirst = true,
            isLast = true
        )
    }
}