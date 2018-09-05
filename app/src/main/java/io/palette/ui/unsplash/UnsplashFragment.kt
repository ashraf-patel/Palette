package io.palette.ui.unsplash

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.Response
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import kotlinx.android.synthetic.main.fragment_unsplash.*
import javax.inject.Inject

@FragmentScoped
class UnsplashFragment @Inject constructor() : BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UnsplashViewModel
    private lateinit var mAdapter: UnsplashAdapter

    companion object {
        fun newInstance() = UnsplashFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_unsplash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UnsplashViewModel::class.java, viewModelFactory)

        mAdapter = UnsplashAdapter(requireContext()) { viewModel.retry() }
        rvUnsplash.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            adapter = mAdapter
        }

        observe(viewModel.unsplash) { mAdapter.submitList(it) }
        observe(viewModel.getNetworkState()) { mAdapter.setNetworkState(it) }
        observe(viewModel.getRefreshState()) {
            it ?: return@observe
            when (it.status) {
                Response.Status.SUCCESS -> {
                }
                Response.Status.LOADING -> {
                }
                Response.Status.ERROR -> {
                }
            }
        }
    }
}