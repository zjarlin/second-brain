package com.addzero.common.util.data_structure.graph

import java.awt.Color

object RandomColorGenerator {
    private val randomColor: Color
        // 生成随机颜色的方法
        get() {
            // 限制颜色的亮度范围，避免生成过亮或过暗的颜色
            val minBrightness = 0.3f
            val maxBrightness = 0.7f

            val hue = Math.random().toFloat() // 随机色调
            val saturation = (0.5 + Math.random() * 0.5).toFloat() // 随机饱和度，保证颜色鲜艳
            val brightness = minBrightness + (maxBrightness - minBrightness) * Math.random().toFloat() // 随机亮度

            return Color.getHSBColor(hue, saturation, brightness)
        }

    // 将 Color 对象转换为十六进制表示的字符串
    private fun colorToHex(color: Color): String {
        return String.format("#%06X", color.rgb and 0xFFFFFF)
    }

    val randomHexColor: String
        // 获取随机颜色的方法
        get() {
            val randomColor = randomColor
            return colorToHex(randomColor)
        }

}