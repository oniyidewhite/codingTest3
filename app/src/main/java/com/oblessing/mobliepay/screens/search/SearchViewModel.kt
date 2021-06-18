package com.oblessing.mobliepay.screens.search

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.oblessing.mobliepay.core.support.AssistedViewModelFactory
import com.oblessing.mobliepay.core.support.hiltMavericksViewModelFactory
import com.oblessing.mobliepay.model.Place
import com.oblessing.mobliepay.model.SearchCriteria
import com.oblessing.mobliepay.repository.PlaceRepository
import com.oblessing.mobliepay.screens.search.state.SearchState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.concurrent.timerTask

class SearchViewModel @AssistedInject constructor(
    @Assisted state: SearchState,
    private val repository: PlaceRepository
) : MavericksViewModel<SearchState>(state) {
    // Manage the expiration
    private val dataset = mutableListOf<Timer>()
    private var owner = ""

    init {
        onEach(SearchState::effect) { e ->
            when (e) {
                is SearchState.Effect.SubmitRequest -> loadContent(
                    SearchCriteria(
                        e.query,
                        e.meta.offset
                    )
                )
                else -> Unit
            }
        }
    }

    fun postEvent(event: SearchState.Event) {
        setState { reduce(event) }
    }

    private fun loadContent(searchCriteria: SearchCriteria) {
        withState {
            repository.findPlaces(searchCriteria).execute(Dispatchers.IO) { async ->
                when (async) {
                    is Success -> {
                        val data = async()
                        reduce(data?.let {
                            registerForExpiration(searchCriteria.query, it.places)
                            SearchState.Event.RequestSuccessful(it.places, it.meta)
                        } ?: SearchState.Event.RequestFailed)
                    }
                    is Fail -> reduce(SearchState.Event.RequestFailed)
                    else -> this
                }
            }

            postEvent(SearchState.Event.HandledEffect)
        }
    }

    private fun registerForExpiration(owner: String, list: List<Place>) {
        if (this.owner != owner) dataset.clear()
        this.owner = owner
        dataset.addAll(list.map { p ->
            Timer().apply {
                schedule(timerTask {
                    postEvent(SearchState.Event.PlaceExpired(p))
                }, p.expiresAt)
            }
        })
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SearchViewModel, SearchState> {
        override fun create(state: SearchState): SearchViewModel
    }

    companion object :
        MavericksViewModelFactory<SearchViewModel, SearchState> by hiltMavericksViewModelFactory()
}