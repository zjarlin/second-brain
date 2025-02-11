//package com.addzero.web.modules.sjzyxt.datazone.gis
//
//import org.babyfish.jimmer.sql.JSqlClient
//import org.babyfish.jimmer.sql.runtime.Selectors.select
//import org.springframework.stereotype.Repository
//import org.springframework.stereotype.Service
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//// 1. 几何图形定义
//sealed interface GeometryShape {
//    fun toWKT(): String
//}
//
//data class Point(
//    val latitude: Double,
//    val longitude: Double
//) : GeometryShape {
//    override fun toWKT() = "POINT($longitude $latitude)"
//}
//
//data class Line(
//    val points: List<Pair<Double, Double>>
//) : GeometryShape {
//    override fun toWKT(): String {
//        val coordinates = points.joinToString(", ") { (lat, lon) -> "$lon $lat" }
//        return "LINESTRING($coordinates)"
//    }
//}
//
//data class Polygon(
//    val points: List<Pair<Double, Double>>
//) : GeometryShape {
//    override fun toWKT(): String {
//        val coordinates = points.joinToString(", ") { (lat, lon) -> "$lon $lat" }
//        return "POLYGON(($coordinates))"
//    }
//}
//
//data class Circle(
//    val centerLat: Double,
//    val centerLon: Double,
//    val radiusMeters: Double
//) : GeometryShape {
//    override fun toWKT(): String {
//        return "ST_Buffer(ST_SetSRID(ST_MakePoint($centerLon, $centerLat), 4326)::geography, $radiusMeters)::geometry"
//    }
//}
//
//// 2. Repository
//@Repository
//class SpatialRepository(
//    private val sql: JSqlClient
//) {
//    // 点与圆的相交
//    fun isPointIntersectCircle(point: Point, circle: Circle): Boolean {
//        return sql.createQuery(SpatialTable::class) {
//            select(
//                sql.raw(
//                    """
//                    ST_Intersects(
//                        ST_GeomFromText(:point, 4326),
//                        :circle
//                    )
//                    """.trimIndent(),
//                    mapOf(
//                        "point" to point.toWKT(),
//                        "circle" to circle.toWKT()
//                    )
//                )
//            )
//        }.fetchOne() ?: false
//    }
//
//    // 线与多边形的相交
//    fun isLineIntersectPolygon(line: Line, polygon: Polygon): Boolean {
//        return sql.createQuery(SpatialTable::class) {
//            select(
//                sql.raw(
//                    """
//                    ST_Intersects(
//                        ST_GeomFromText(:line, 4326),
//                        ST_GeomFromText(:polygon, 4326)
//                    )
//                    """.trimIndent(),
//                    mapOf(
//                        "line" to line.toWKT(),
//                        "polygon" to polygon.toWKT()
//                    )
//                )
//            )
//        }.fetchOne() ?: false
//    }
//
//    // 两个多边形的相交
//    fun isPolygonIntersectPolygon(polygon1: Polygon, polygon2: Polygon): Boolean {
//        return sql.createQuery(SpatialTable::class) {
//            select(
//                sql.raw(
//                    """
//                    ST_Intersects(
//                        ST_GeomFromText(:polygon1, 4326),
//                        ST_GeomFromText(:polygon2, 4326)
//                    )
//                    """.trimIndent(),
//                    mapOf(
//                        "polygon1" to polygon1.toWKT(),
//                        "polygon2" to polygon2.toWKT()
//                    )
//                )
//            )
//        }.fetchOne() ?: false
//    }
//}
////sql("rank() over(partition by %e order by %e desc)") {
////    expression(table.store.id)
////    expression(table.price)
////}
//// 3. Service
//@Service
//class SpatialService(
//    private val spatialRepository: SpatialRepository
//) {
//    // 点与圆的相交检查
//    fun checkPointInCircle(
//        pointLat: Double,
//        pointLon: Double,
//        circleLat: Double,
//        circleLon: Double,
//        radiusMeters: Double
//    ): Boolean {
//        val point = Point(pointLat, pointLon)
//        val circle = Circle(circleLat, circleLon, radiusMeters)
//        return spatialRepository.isPointIntersectCircle(point, circle)
//    }
//
//    // 线与多边形的相交检查
//    fun checkLineIntersectPolygon(
//        linePoints: List<Pair<Double, Double>>,
//        polygonPoints: List<Pair<Double, Double>>
//    ): Boolean {
//        val line = Line(linePoints)
//        val polygon = Polygon(polygonPoints)
//        return spatialRepository.isLineIntersectPolygon(line, polygon)
//    }
//
//    // 两个多边形的相交检查
//    fun checkPolygonIntersectPolygon(
//        polygon1Points: List<Pair<Double, Double>>,
//        polygon2Points: List<Pair<Double, Double>>
//    ): Boolean {
//        val polygon1 = Polygon(polygon1Points)
//        val polygon2 = Polygon(polygon2Points)
//        return spatialRepository.isPolygonIntersectPolygon(polygon1, polygon2)
//    }
//}
//
//// 4. Controller
//@RestController
//@RequestMapping("/api/spatial")
//class SpatialController(
//    private val spatialService: SpatialService
//) {
//    // 点与圆的相交
//    @PostMapping("/point-circle")
//    fun checkPointInCircle(@RequestBody request: PointCircleRequest): Boolean {
//        return spatialService.checkPointInCircle(
//            pointLat = request.pointLat,
//            pointLon = request.pointLon,
//            circleLat = request.circleLat,
//            circleLon = request.circleLon,
//            radiusMeters = request.radiusMeters
//        )
//    }
//
//    // 线与多边形的相交
//    @PostMapping("/line-polygon")
//    fun checkLineIntersectPolygon(@RequestBody request: LinePolygonRequest): Boolean {
//        return spatialService.checkLineIntersectPolygon(
//            linePoints = request.linePoints,
//            polygonPoints = request.polygonPoints
//        )
//    }
//
//    // 两个多边形的相交
//    @PostMapping("/polygon-polygon")
//    fun checkPolygonIntersectPolygon(@RequestBody request: PolygonPolygonRequest): Boolean {
//        return spatialService.checkPolygonIntersectPolygon(
//            polygon1Points = request.polygon1Points,
//            polygon2Points = request.polygon2Points
//        )
//    }
//}
//
//// 5. 请求数据类
//data class PointCircleRequest(
//    val pointLat: Double,
//    val pointLon: Double,
//    val circleLat: Double,
//    val circleLon: Double,
//    val radiusMeters: Double
//)
//
//data class LinePolygonRequest(
//    val linePoints: List<Pair<Double, Double>>,
//    val polygonPoints: List<Pair<Double, Double>>
//)
//
//data class PolygonPolygonRequest(
//    val polygon1Points: List<Pair<Double, Double>>,
//    val polygon2Points: List<Pair<Double, Double>>
//)
//
//// 6. 使用示例
//fun usage(spatialService: SpatialService) {
//    // 检查点是否在圆内
//    val isPointInCircle = spatialService.checkPointInCircle(
//        pointLat = 31.2304,
//        pointLon = 121.4737,
//        circleLat = 31.2314,
//        circleLon = 121.4747,
//        radiusMeters = 1000.0
//    )
//
//    // 检查线是否与多边形相交
//    val isLineIntersectPolygon = spatialService.checkLineIntersectPolygon(
//        linePoints = listOf(
//            31.2304 to 121.4737,
//            31.2314 to 121.4747
//        ),
//        polygonPoints = listOf(
//            31.2304 to 121.4737,
//            31.2314 to 121.4747,
//            31.2324 to 121.4757,
//            31.2304 to 121.4737  // 闭合多边形
//        )
//    )
//
//    // 检查两个多边形是否相交
//    val isPolygonIntersectPolygon = spatialService.checkPolygonIntersectPolygon(
//        polygon1Points = listOf(
//            31.2304 to 121.4737,
//            31.2314 to 121.4747,
//            31.2324 to 121.4757,
//            31.2304 to 121.4737
//        ),
//        polygon2Points = listOf(
//            31.2314 to 121.4747,
//            31.2324 to 121.4757,
//            31.2334 to 121.4767,
//            31.2314 to 121.4747
//        )
//    )
//}