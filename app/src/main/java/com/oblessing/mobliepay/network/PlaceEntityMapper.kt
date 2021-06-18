package com.oblessing.mobliepay.network

import com.oblessing.mobliepay.core.EntityMapper
import com.oblessing.mobliepay.model.PageMeta
import com.oblessing.mobliepay.model.PageResult
import com.oblessing.mobliepay.model.Place
import javax.inject.Inject
import javax.inject.Named

class PlaceEntityMapper @Inject constructor(@Named("limit") private val requestSizeLimit: Int) :
    EntityMapper<PlaceListEntity?, PageResult?> {
    override fun mapFromEntity(entity: PlaceListEntity?): PageResult? {
        entity?.let {
            val pageMeta = PageMeta(offset = it.offset + requestSizeLimit, totalRecords = it.count)
            // Display places should be open from 1990
            val places = it.places.filter {
                (it.lifeSpan?.begin?.split("-")?.getOrNull(0)?.toIntOrNull()
                    ?: 0) >= 1990 && !it.coordinates?.lat.isNullOrBlank()
            }.map {
                Place(
                    id = it.id,
                    name = it.name,
                    address = it.address ?: "",
                    type = it.type,
                    latitude = it.coordinates!!.lat,
                    longitude = it.coordinates.lng,
                    expiresAt = (it.lifeSpan!!.begin.split("-")[0].toLong() - 1990L) * 1000
                )
            }

            return PageResult(meta = pageMeta, places)
        } ?: return null

    }
}