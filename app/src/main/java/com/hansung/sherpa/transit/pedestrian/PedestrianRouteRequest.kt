package com.hansung.sherpa.transit.pedestrian

import com.google.gson.annotations.SerializedName

data class PedestrianRouteRequest(
    @SerializedName("startX") val startX: Float,
    @SerializedName("startY") val startY: Float,
    @SerializedName("endX") val endX: Float,
    @SerializedName("endY") val endY: Float,
    @SerializedName("angle") val angle: Int=20,
    @SerializedName("speed") val speed: Int=30,
    @SerializedName("endPoiId") val endPoiId: String="10001",
    @SerializedName("passList") val passList: String="${startX},${startY}_${endX},${endY}",
    @SerializedName("reqCoordType") val reqCoordType: String = "WGS84GEO",
    @SerializedName("startName") val startName: String = "%EC%B6%9C%EB%B0%9C",
    @SerializedName("endName") val endName: String = "%EB%8F%84%EC%B0%A9",
    @SerializedName("searchOption") val searchOption: String = "0",
    @SerializedName("resCoordType") val resCoordType: String = "WGS84GEO",
    @SerializedName("sort") val sort: String = "index"
)