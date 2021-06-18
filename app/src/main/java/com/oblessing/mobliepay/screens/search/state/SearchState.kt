package com.oblessing.mobliepay.screens.search.state

import com.airbnb.mvrx.MavericksState
import com.oblessing.mobliepay.model.PageMeta
import com.oblessing.mobliepay.model.Place

data class SearchState(
    val search: String = "",
    val result: List<Place> = emptyList(),
    val resultMeta: PageMeta? = null,
    val event: Event? = null,
    val effect: Effect? = null,
    private val loading: Boolean = false
) : MavericksState {
    val showProgress
        get() = loading

    fun reduce(e: Event): SearchState {
        return when (e) {
            Event.HandledEffect -> copy(event = e, effect = null)
            is Event.PlaceExpired -> {
                val updatedResult = result.filter { it.id != e.place.id }
                copy(event = e, result = updatedResult)
            }
            is Event.TappedFind -> {
                if (e.value.isNotBlank()) {
                    copy(
                        event = e,
                        effect = Effect.SubmitRequest(e.value),
                        loading = true,
                        search = e.value,
                        result = emptyList(),
                        resultMeta = null
                    )
                } else this
            }
            is Event.RequestSuccessful -> {
                // check if we should load a next page
                val loadNextPageEffect =
                    if (e.meta.offset < e.meta.totalRecords) Effect.SubmitRequest(
                        search,
                        e.meta
                    ) else null

                // check if first result
                // append if not first result
                // trigger no content page if first request is empty
                if (resultMeta == null) {
                    copy(
                        event = e,
                        loading = false,
                        result = e.places,
                        resultMeta = e.meta,
                        effect = if (e.places.isEmpty() && loadNextPageEffect == null && result.isEmpty()) Effect.ShowNoContent else loadNextPageEffect
                    )
                } else {
                    copy(
                        event = e,
                        loading = false,
                        result = result.toMutableList().apply { addAll(e.places) },
                        resultMeta = e.meta,
                        effect = loadNextPageEffect
                    )
                }
            }
            Event.RequestFailed -> copy(event = e, loading = false, effect = Effect.ShowError)
            Event.TappedRetry -> {
                if (resultMeta == null) {
                    reduce(Event.TappedFind(search))
                } else {
                    copy(event = e, resultMeta = resultMeta)
                }
            }
        }
    }


    sealed class Event {
        object HandledEffect : Event()
        object RequestFailed : Event()
        object TappedRetry : Event()


        data class TappedFind(val value: String) : Event()
        data class PlaceExpired(val place: Place) : Event()
        data class RequestSuccessful(val places: List<Place>, val meta: PageMeta) : Event()
    }

    sealed class Effect {
        object ShowError : Effect()
        object ShowNoContent : Effect()
        data class SubmitRequest(val query: String, val meta: PageMeta = PageMeta()) : Effect()
    }
}