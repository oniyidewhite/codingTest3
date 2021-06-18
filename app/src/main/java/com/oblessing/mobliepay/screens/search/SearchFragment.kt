package com.oblessing.mobliepay.screens.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.oblessing.mobliepay.R
import com.oblessing.mobliepay.core.SnackbarUtil
import com.oblessing.mobliepay.core.mavericks.viewBinding
import com.oblessing.mobliepay.core.setBlockingOnClickListener
import com.oblessing.mobliepay.core.showIf
import com.oblessing.mobliepay.databinding.FragmentSearchBinding
import com.oblessing.mobliepay.screens.search.state.SearchState
import com.oblessing.mobliepay.views.mapContentRow
import com.oblessing.mobliepay.views.noContentRow

class SearchFragment : Fragment(R.layout.fragment_search), MavericksView {
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: SearchViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.find.setBlockingOnClickListener {
            val input = binding.input.text?.let { it.toString() } ?: ""
            postEvent(SearchState.Event.TappedFind(input.trim()))
        }

        viewModel.onEach(SearchState::effect) {
            when (it) {
                SearchState.Effect.ShowError -> {
                    SnackbarUtil.showSnackBarMessage(binding.root) {
                        postEvent(SearchState.Event.TappedRetry)
                    }
                    postEvent(SearchState.Event.HandledEffect)
                }
                else -> Unit
            }
        }
    }

    private fun postEvent(event: SearchState.Event) {
        viewModel.postEvent(event)
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.progress.showIf(state.showProgress)
        binding.recyclerView.withModels {
            if (state.effect == SearchState.Effect.ShowNoContent) {
                noContentRow {
                    id("no-content")
                }
            } else {

                mapContentRow {
                    id("my-map")
                    updateWithPlaces(state.result)
                }
            }
        }
    }
}