package com.lcz.bm.ui.badminton

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.lcz.bm.data.LogDao
import com.lcz.bm.entity.LoginUserEntity
import com.lcz.bm.entity.PlaceEntity
import com.lcz.bm.entity.SubmitEntity
import com.lcz.bm.net.Event
import com.lcz.bm.net.Result
import com.lcz.bm.ui.login.LoginRemoteDataSource
import com.lcz.bm.ui.order.OrderRemoteDataSource
import com.lcz.bm.ui.place.PlaceRemoteDataSource
import com.lcz.bm.util.CoroutinesDispatcherProvider
import com.lcz.bm.util.SharedPreferenceStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
class BadmintonViewModel @ViewModelInject internal constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource,
    private val orderRemoteDataSource: OrderRemoteDataSource,
    private val placeRemoteDataSource: PlaceRemoteDataSource,
    private val prefs: SharedPreferenceStorage,
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val plantDao: LogDao,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _resultCheckStatus = MutableLiveData<Event<Boolean>>()
    val resultCheckStatus: LiveData<Event<Boolean>> = _resultCheckStatus

    private val _resultLoginInfo = MutableLiveData<Event<LoginUserEntity>>()
    val resultLoginInfo: LiveData<Event<LoginUserEntity>> = _resultLoginInfo

    private val _resultPlaceInfo = MutableLiveData<Event<PlaceEntity>>()
    val resultPlaceInfo: LiveData<Event<PlaceEntity>> = _resultPlaceInfo

    private val _resultSubmitOrder = MutableLiveData<Event<String>>()
    val resultSubmitOrder: LiveData<Event<String>> = _resultSubmitOrder

    private val _uiState = MutableLiveData<BMUiModel>()
    val uiState: LiveData<BMUiModel> = _uiState

    private var netJob1: Job? = null
    private var netJob2: Job? = null
    private var netJob3: Job? = null
    private var netJob5: Job? = null

    //校验场地 1
    fun checkTokenStatus() {
        if (netJob1?.isActive == true) {
            return
        }
        netJob1 = launchCheckToken()
    }

    //登录 2
    fun login() {
        if (netJob2?.isActive == true) {
            return
        }
        netJob2 = launchLogin()
    }

    //获取场地列表 3
    fun getPlaceList(time: String) {
        if (netJob3?.isActive == true) {
            return
        }
        netJob3 = launchGetPlaceList(time)
    }

    //提交订单 5
    fun submitOrder(submitEntity: SubmitEntity) {
        if (netJob5?.isActive == true) {
            return
        }
        netJob5 = launchSubmitOrder(submitEntity)
    }


    private fun launchCheckToken(): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            val result = orderRemoteDataSource.getOrderList(prefs.token.toString())
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    _resultCheckStatus.postValue(Event(false))
                    emitUiState(
                        showMsg = Event(content = "登录状态校验成功")
                    )
                } else {
                    _resultCheckStatus.postValue(Event((result as Result.ErrorReLogin).reLogin))
                    emitUiState(
                        showMsg = Event(
                            content = (result as? Result.Error)?.exception?.message ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun launchLogin(): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            val result = loginRemoteDataSource.login()
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    _resultLoginInfo.postValue(Event(result.data))
                    emitUiState(
                        showMsg = Event(content = "登录成功")
                    )
                } else {
                    emitUiState(
                        showMsg = Event(
                            content = (result as? Result.Error)?.exception?.message ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun launchGetPlaceList(time: String): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            val result = placeRemoteDataSource.getPlaceList(prefs.token.toString(), time)
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    _resultPlaceInfo.postValue(Event(result.data))
                    emitUiState(
                        showMsg = Event(content = "场地获取成功")
                    )
                } else {
                    emitUiState(
                        showMsg = Event(
                            content = (result as? Result.Error)?.exception?.message ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun launchSubmitOrder(submitEntity: SubmitEntity): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            val result = orderRemoteDataSource.submitOrder(prefs.token.toString(), submitEntity)
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    _resultSubmitOrder.postValue(Event("下单成功，请前往支付"))
                } else {
                    _resultSubmitOrder.postValue(
                        Event((result as? Result.Error)?.exception?.message ?: "")
                    )
                }
            }
        }
    }

    private fun emitUiState(
        showMsg: Event<String>? = null,
        reLogin: Event<Boolean>? = null
    ) {
        val uiModel = BMUiModel(showMsg, reLogin)
        _uiState.value = uiModel
    }

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }

}

data class BMUiModel(
    val showMsg: Event<String>?,
    val reLogin: Event<Boolean>?
)