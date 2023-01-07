package com.arc.fast.sample.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arc.fast.core.extensions.string
import com.arc.fast.sample.*
import com.arc.fast.sample.common.data.DataSource
import com.arc.fast.sample.common.data.entity.ApiResult
import com.arc.fast.sample.common.data.entity.Menu
import com.arc.fast.sample.common.data.entity.Response
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // 全局viewModel
    var appViewModel: AppViewModel? = null

    // 数据源
    private val dataSource by lazy { DataSource() }

    // 菜单
    val valueMenuList = MutableStateFlow<ApiResult<List<Menu>?>?>(null)

    // 菜单跳转
    val eventMenuClick = MutableSharedFlow<Menu>()

    fun loadMenu(isFirstLoad: Boolean = false) {
//        dataSource.menuList().onEach {
////            if (isFirstLoad && !it.isLoading) delay(1500)
////            valueMenuList.value = it
//            if (!it.isLoading) {
//                // 测试数据
//                val response = Response<List<Menu>?>(
//                    "success", null, null, null, null, arrayListOf(
//                        Menu("webview", "http://www.baidu.com", 0),
//                        Menu("scan", ACTION_SCAN, 1),
//                        Menu(R.string.immersive_dialog.string, ACTION_DIALOG, 1),
//                        Menu(R.string.immersive_popup.string, ACTION_POPUP, 1),
//                        Menu("test", ACTION_TEST, 0)
//                    )
//                )
//                valueMenuList.value = ApiResult.Success(response, response.data)
//            }
//        }.launchIn(viewModelScope)

        // 测试数据
        val response = Response<List<Menu>?>(
            "success", null, null, null, null,
            arrayListOf(
                Menu(R.string.permission.string, ACTION_PERMISSION, 0),
                Menu(R.string.immersive_dialog.string, ACTION_DIALOG, 0),
                Menu(R.string.immersive_popup.string, ACTION_POPUP, 0),
                Menu(R.string.span.string, ACTION_SPAN, 0),
                Menu(R.string.mask.string, ACTION_MASK, 0),
                Menu(R.string.view.string, ACTION_VIEW, 1),
                Menu(R.string.fast_text_view.string, ACTION_FAST_TEXT_VIEW, 1),
                Menu("test", ACTION_TEST, 0),
                Menu("test_webview", "http://www.baidu.com", 0),
                Menu("test_scan", ACTION_SCAN, 0),
            )
        )
        valueMenuList.value = ApiResult.Success(response, response.data)
    }

    fun onMenuClick(item: Menu) {
        viewModelScope.launch {
            eventMenuClick.emit(item)
        }
    }
}