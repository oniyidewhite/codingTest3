package com.oblessing.mobliepay.network

import org.junit.Test

class PlaceEntityMapperTest {
    @Test
    fun `Result should have updated meta and empty response as data contains invalid information`() {
        val limit = 1
        val mapper = PlaceEntityMapper(limit)
        var result = mapper.mapFromEntity(PlaceListEntity(1, 0, listOf(PlaceEntity("a", "a", "a", "c", LatLng("a", "b"), LifeSpan("a")))))
        assert(result!!.places.isEmpty())
        assert(result.meta.offset >= limit)
    }

    @Test
    fun `Result should have updated meta and response as data has valid information`() {
        val limit = 1
        val mapper = PlaceEntityMapper(limit)
        var result = mapper.mapFromEntity(PlaceListEntity(1, 0, listOf(PlaceEntity("a", "a", "a", "c", LatLng("a", "b"), LifeSpan("2020")))))
        assert(result!!.places.isNotEmpty())
        assert(result.meta.offset >= limit)
    }
}