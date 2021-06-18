package com.oblessing.mobliepay.screens.search.state

import com.oblessing.mobliepay.model.PageMeta
import com.oblessing.mobliepay.model.Place
import org.junit.Test

class SearchStateTest {
    @Test
    fun `SearchState should always be consistent and updated accordingly`() {
        var state = SearchState().reduce(SearchState.Event.TappedFind(""))

        // Usher should not be able to search on empty list
        assert(state.effect == null)
        assert(!state.showProgress)

        // User should be able to search if list isn't empty, current list should be cleared
        state = state.reduce(SearchState.Event.TappedFind("welcome"))
        assert(state.effect is SearchState.Effect.SubmitRequest)
        assert(state.showProgress)
        assert(state.result.isEmpty())

        // Effect should be null after handling the any request
        state = state.reduce(SearchState.Event.HandledEffect)
        assert(state.effect == null)
        assert(state.showProgress)

        // User show error to user on request failed
        state = state.reduce(SearchState.Event.RequestFailed)
        assert(state.effect == SearchState.Effect.ShowError)
        assert(!state.showProgress)

        // Effect should be null after handling the any request
        state = state.reduce(SearchState.Event.HandledEffect)
        assert(state.effect == null)
        assert(!state.showProgress)

        // User show result to user on request success
        state = state.reduce(
            SearchState.Event.RequestSuccessful(
                listOf(
                    Place(
                        "x",
                        "x",
                        "x",
                        "x",
                        "x",
                        "x",
                        1L
                    )
                ), PageMeta(0, 0)
            )
        )
        assert(state.effect == null)
        assert(!state.showProgress)
        assert(state.result.count() == 1)

        // Content should be removed if expired
        state = state.reduce(
            SearchState.Event.PlaceExpired(
                Place(
                    "x",
                    "x",
                    "x",
                    "x",
                    "x",
                    "x",
                    1L
                )
            )
        )
        assert(state.effect == null)
        assert(!state.showProgress)
        assert(state.result.isEmpty())

        // Load next page if we have more
        state =
            state.reduce(
                SearchState.Event.RequestSuccessful(
                    listOf(
                        Place(
                            "x",
                            "x",
                            "x",
                            "x",
                            "x",
                            "x",
                            1L
                        )
                    ), PageMeta(1, 3)
                )
            )
        assert((state.effect as SearchState.Effect.SubmitRequest).meta.offset == 1)
        assert(!state.showProgress)
        assert(state.result.count() == 1)

        // Don't load next if we no longer have content
        state =
            state.reduce(SearchState.Event.TappedFind("xt")).reduce(
                SearchState.Event.RequestSuccessful(
                    listOf(
                        Place(
                            "x",
                            "x",
                            "x",
                            "x",
                            "x",
                            "x",
                            1L
                        )
                    ), PageMeta(1, 1)
                )
            )
        assert((state.effect  == null))
        assert(!state.showProgress)
        assert(state.result.count() == 1)

        // Show no content if the search returns empty after find
        state =
            state.reduce(SearchState.Event.TappedFind("xt")).reduce(
                SearchState.Event.RequestSuccessful(
                    listOf(), PageMeta(0, 0)
                )
            )
        assert((state.effect  == SearchState.Effect.ShowNoContent))
        assert(!state.showProgress)
        assert(state.result.isEmpty())
    }
}