package com.oblessing.mobliepay.repository

import com.oblessing.mobliepay.core.EntityMapper
import com.oblessing.mobliepay.model.PageResult
import com.oblessing.mobliepay.model.SearchCriteria
import com.oblessing.mobliepay.network.PlaceWebService
import com.oblessing.mobliepay.network.PlaceListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaceRepository(
    private val placeWebService: PlaceWebService,
    private val placeEntityMapper: EntityMapper<PlaceListEntity?, PageResult?>,
    private val requestSizeLimit: Int
) {
    fun findPlaces(q: SearchCriteria): Flow<PageResult?> = flow {
        emit(
            placeEntityMapper.mapFromEntity(
                placeWebService.search(
                    q.query,
                    q.offset,
                    limit = requestSizeLimit
                )
            )
        )
    }
}