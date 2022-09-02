package com.arc.fast.sample.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arc.fast.sample.BaseFragment
import com.arc.fast.sample.R
import com.arc.fast.sample.databinding.FragmentWebViewBinding
import com.arc.fast.sample.extension.titleTextView
import com.arc.fast.sample.utils.NavTransitionOptions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {
    private val args: WebViewFragmentArgs by navArgs()

    private val webView by lazy {
        ContentWebView(
            context = requireActivity(),
            defaultUrl = args.menu.url,
            onProgress = { newProgress ->
                if (newProgress == 100) {
                    binding.progressBar.isVisible = false
                } else {
                    binding.progressBar.isVisible = true
                    binding.progressBar.progress = newProgress
                }
            },
            onGoBack = {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            },
            onGoScan = {
                findNavController().navigate(WebViewFragmentDirections.actionWebViewFragmentToScanFragment())
            },
            onScrollToTop = {
                if (!args.menu.isFullScreen)
                    binding.appbar.isLifted = !it
            })
    }

    override fun onCreateTransition(): NavTransitionOptions {
        return NavTransitionOptions(
            isSharedElementsDestination = true
        ).addSharedElementViews(
            { _: View ->
                binding.toolbar.titleTextView
            } to args.menu.title
        )
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentWebViewBinding = FragmentWebViewBinding.inflate(inflater, container, false).apply {
        this.menu = args.menu
        /** 如果通过binding设置title，会造成转场动画异常 **/
        toolbar.title = args.menu.title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            this.navigationIcon = navigationIconForBack
            this.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) webView.goBack()
                    else findNavController().navigateUp()
                }
            })
        binding.clRoot.addView(
            webView,
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0).apply {
                topToBottom = R.id.progressBar
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            }
        )
//        binding.nsvContent.addView(
//            webView,
//            ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        )
        appViewModel.eventScanResult.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            webView.onScanResult(it)
        }.launchIn(lifecycleScope)
    }


    override fun onDestroyView() {
        (webView.parent as? ViewGroup)?.removeView(webView)
        super.onDestroyView()
    }
}