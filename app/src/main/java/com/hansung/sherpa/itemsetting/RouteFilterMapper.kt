package com.hansung.sherpa.itemsetting

import android.util.Log
import com.hansung.sherpa.transit.routegraphic.RouteGraphicResponse
import com.hansung.sherpa.transit.odsay.ODsaySubPath
import com.hansung.sherpa.transit.odsay.ODsayTransitRouteResponse
import com.hansung.sherpa.transit.pedestrian.PedestrianResponse
import com.naver.maps.geometry.LatLng

class RouteFilterMapper {

    val NOT_FOUND = "Not Found"
    val EMPTY_INT = -1
    val EMPTY_DOUBLE = -1.0

    enum class TrafficType(val value: Int) {
        SUBWAY(1),
        BUS(2),
        WALK(3)
    }

    // ODsay response 값에 대해서 TransportRoute 값 매핑 ( 보행자 데이터 없음 )
    fun mappingODsayResponseToTransportRouteList(
        transitResponse: ODsayTransitRouteResponse, graphicResponseList: List<RouteGraphicResponse>
    ): List<TransportRoute>? {
        if (transitResponse.result == null || graphicResponseList.isEmpty()) return null

        val list = mutableListOf<TransportRoute>()

        transitResponse.result.path.forEachIndexed { idx, path ->
            var transportIndex = -1;
            val oDsayInfo = path.info
            val oDsayPath = path.subPath

            val info = Info(
                totalDistance   = oDsayInfo.totalDistance,
                totalTime       = oDsayInfo.totalTime,
                transferCount   = oDsayInfo.busTransitCount + oDsayInfo.subwayTransitCount,
                totalWalk       = oDsayInfo.totalWalk
            )
            val subPathList: List<SubPath> = oDsayPath.mapIndexed { _, it ->
                if(isTransport(it.trafficType)) transportIndex++
                SubPath(
                    trafficType     = it.trafficType,
                    sectionInfo     = castSectionInfo(it.trafficType, it),
                    sectionRoute    = castSectionRoute(it.trafficType, graphicResponseList[idx], transportIndex)
                )
            }

            val transportRoute = TransportRoute(
                info = info,
                subPath = subPathList
            )
            list.add(transportRoute)
        }
        return list.toList()
    }

    /**
     * ODSay 정보를 공통 데이터 TransportRoute의 SectionInfo로 매핑시켜주는 함수
     *
     * @param trafficType
     * @param path
     * @return
     */
    private fun castSectionInfo(trafficType: Int, path: ODsaySubPath): SectionInfo {
        return when (trafficType) {
            TrafficType.SUBWAY.value -> SubwaySectionInfo(
                lane = path.lane!!.map {
                    SubwayLane(
                        name            = it.name ?: NOT_FOUND,
                        subwayCode      = it.subwayCode ?: EMPTY_INT,
                        subwayCityCode  = it.subwayCityCode ?: EMPTY_INT
                    )
                },
                way                         = path.way ?: NOT_FOUND,
                wayCode                     = path.wayCode ?: EMPTY_INT,
                door                        = path.door ?: NOT_FOUND,
                stationCount                = path.stationCount ?: EMPTY_INT,
                startID                     = path.startID ?: EMPTY_INT,
                startStationProviderCode    = path.startStationProviderCode ?: EMPTY_INT,
                startLocalStationID         = path.startLocalStationID ?: NOT_FOUND,
                endID                       = path.endID ?: EMPTY_INT,
                endStationProviderCode      = path.endStationProviderCode ?: EMPTY_INT,
                endLocalStationID           = path.endLocalStationID ?: NOT_FOUND,
                stationNames                = path.passStopList.stations.map { it.stationName }
            ).withCommonProperties(path)

            TrafficType.BUS.value -> BusSectionInfo(
                lane = path.lane!!.map {
                    BusLane(
                        busNo           = it.busNo ?: NOT_FOUND,
                        type            = it.type ?: EMPTY_INT,
                        busID           = it.busID ?: EMPTY_INT,
                        busLocalBlID    = it.busLocalBlID ?: NOT_FOUND,
                        busProviderCode = it.busProviderCode ?: EMPTY_INT,
                    )
                },
                stationCount                = path.stationCount ?: EMPTY_INT,
                startID                     = path.startID ?: EMPTY_INT,
                startStationCityCode        = path.startStationCityCode ?: EMPTY_INT,
                startStationProviderCode    = path.startStationProviderCode ?: EMPTY_INT,
                startLocalStationID         = path.startLocalStationID ?: NOT_FOUND,
                endID                       = path.endID ?: EMPTY_INT,
                endStationCityCode          = path.endStationCityCode ?: EMPTY_INT,
                endStationProviderCode      = path.endStationProviderCode ?: EMPTY_INT,
                endLocalStationID           = path.endLocalStationID ?: NOT_FOUND,
                stationNames                = path.passStopList.stations.map { it.stationName }
            ).withCommonProperties(path)

            TrafficType.WALK.value -> PedestrianSectionInfo(
                contents = mutableListOf()
            ).withCommonProperties(path)

            else -> {
                PedestrianSectionInfo(contents = mutableListOf()).withCommonProperties(path)
            }
        }
    }

    private fun isTransport(trafficType: Int): Boolean {
        return when (trafficType) {
            TrafficType.SUBWAY.value, TrafficType.BUS.value ->
                true
            TrafficType.WALK.value ->
                false
            else ->
                false
        }
    }

    // ODSay 노선 그래픽 API 데이터를 TransportRoute의 SectionRoute로 매핑시켜주는 함수
    private fun castSectionRoute(trafficType: Int, response: RouteGraphicResponse, transportIndex: Int): SectionRoute {
        return when (trafficType) {
            TrafficType.SUBWAY.value, TrafficType.BUS.value ->
                SectionRoute(routeList = response
                    .result!!.lane?.get(transportIndex)!!.section?.get(0)?.graphPos!!
                    .map { value -> LatLng(value.y!!, value.x!!) }.toMutableList()
                )

            TrafficType.WALK.value ->
                SectionRoute(routeList = mutableListOf())

            else ->
                SectionRoute(routeList = mutableListOf())
        }
    }

    private fun SectionInfo.withCommonProperties(it: ODsaySubPath): SectionInfo {
        distance    = it.distance
        sectionTime = it.sectionTime
        startName   = it.startName
        endName     = it.endName
        startX      = it.startX
        startY      = it.startY
        endX        = it.endX
        endY        = it.endY
        return this
    }

    // 선택한 TransPortRoute에 보행자 데이터 추가
    fun mappingPedestrianRouteToTransportRoute(
        transportRoute: TransportRoute, pedestrianResponseList: List<PedestrianResponse>,
        depart: String = "", destination: String = ""
    ): TransportRoute {
        var index = 0
        val pSize = pedestrianResponseList.size
        transportRoute.subPath.forEach {
            when (it.trafficType) {
                TrafficType.WALK.value -> {
                    if (it.sectionInfo is PedestrianSectionInfo) {
                        it.sectionInfo.contents =
                            pedestrianResponseList[index].features
                                ?.map { feat -> feat.properties.description }
                                ?.toMutableList()!!
                        val features = pedestrianResponseList[index].features
                        if (features != null) {
                            val size = features.size
                            it.sectionInfo.startName = features[0].properties.facilityName ?: ""
                            it.sectionInfo.endName = features[size-1].properties.facilityName ?: ""
                            if(index == 0) it.sectionInfo.startName = depart
                            if(index == pSize-1) it.sectionInfo.endName = destination
                        }
                    }

                    pedestrianResponseList[index].features?.forEach { feat ->
                        feat.geometry.coordinates.forEach { coordinate ->
                            it.sectionRoute.routeList.add(
                                LatLng(coordinate[1], coordinate[0])
                            )
                            //Log.i("MAPPER","routeList: ${it.sectionRoute.routeList}")
                            //Log.i("MAPPER","Coordinate added: ${coordinate[0]}, ${coordinate[1]}")
                        }
                    }
                    index++
                }

                else -> {}
            }
        }

        return transportRoute
    }

    /**
     * transportRoute에서 보행자 경로 nowSubPath 부분만 수정하는 함수
     */
    fun mappingOnePedestrianRoute(
        transportRoute: TransportRoute, nowSubPath:Int, pedstrianResponse: PedestrianResponse
    ): TransportRoute{
        transportRoute.subPath[nowSubPath].sectionRoute.routeList.clear()
        pedstrianResponse.features?.forEach { feat ->
            feat.geometry.coordinates.forEach { coordinate ->
                transportRoute.subPath[nowSubPath].sectionRoute.routeList.add(
                    LatLng(coordinate[1], coordinate[0])
                )
                //Log.i("MAPPER","routeList: ${transportRoute.subPath[nowSubPath].sectionRoute.routeList}")
                //Log.i("MAPPER","Coordinate added: ${coordinate[0]}, ${coordinate[1]}")
            }
        }
        return transportRoute
    }

    fun pedstrianResponseToRouteList(pedstrianResponse: PedestrianResponse):MutableList<LatLng> {
        val mappingValue = mutableListOf<LatLng>()

        pedstrianResponse.features?.forEach { feat ->
            feat.geometry.coordinates.forEach { coordinate ->
                mappingValue.add(
                    LatLng(coordinate[1], coordinate[0])
                )
                //Log.i("MAPPER","routeList: ${mappingValue}")
                //Log.i("MAPPER","Coordinate added: ${coordinate[0]}, ${coordinate[1]}")
            }
        }
        return mappingValue
    }
}