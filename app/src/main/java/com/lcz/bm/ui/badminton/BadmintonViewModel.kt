package com.lcz.bm.ui.badminton

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.lcz.bm.data.LogDao
import com.lcz.bm.entity.LoginUserEntity
import com.lcz.bm.entity.PlaceEntity
import com.lcz.bm.entity.ShowMsgEntity
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

    private val _loginInfo = MutableLiveData<Event<LoginUserEntity>>()
    val loginInfo: LiveData<Event<LoginUserEntity>> = _loginInfo

    private val _placeInfo = MutableLiveData<Event<PlaceEntity>>()
    val placeInfo: LiveData<Event<PlaceEntity>> = _placeInfo

    private val _resultSubmitOrder = MutableLiveData<Event<ShowMsgEntity>>()
    val resultSubmitOrder: LiveData<Event<ShowMsgEntity>> = _resultSubmitOrder

    private val _uiState = MutableLiveData<LoginUiModel>()
    val uiState: LiveData<LoginUiModel> = _uiState

    private var netJob1: Job? = null
    private var netJob2: Job? = null
    private var netJob3: Job? = null
    private var netJob4: Job? = null
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
            withContext(dispatcherProvider.main) {
                emitUiState(isLoading = Event(true))
            }
            val result = orderRemoteDataSource.getOrderList(prefs.token.toString())
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    emitUiState(
                        isLoading = Event(false),
                        isResultSuccess = Event(true),
                        showMsg = Event(content = "登录状态校验成功")
                    )
                } else {
                    emitUiState(
                        reLogin = Event((result as Result.ErrorReLogin).reLogin),
                        isLoading = Event(false),
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
            withContext(dispatcherProvider.main) {
                emitUiState(isLoading = Event(true))
            }
            val result = loginRemoteDataSource.login()
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    _loginInfo.postValue(Event(result.data))
                    emitUiState(
                        isLoading = Event(false),
                        isResultSuccess = Event(true),
                        showMsg = Event(content = "登录成功")
                    )
                } else {
                    emitUiState(
                        isLoading = Event(false),
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
                    _placeInfo.postValue(Event(result.data))
                    emitUiState(
                        isLoading = Event(false),
                        isResultSuccess = Event(true),
                        showMsg = Event(content = "场地获取成功")
                    )
                } else {
                    emitUiState(
                        isLoading = Event(false),
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
                    _resultSubmitOrder.postValue(Event(ShowMsgEntity("下单成功，请前往支付",true)))
                } else {
                    _resultSubmitOrder.postValue(Event(ShowMsgEntity((result as? Result.Error)?.exception?.message ?: "",true)))
                }
            }
        }
    }

    private fun emitUiState(
        isLoading: Event<Boolean>? = null,
        isResultSuccess: Event<Boolean>? = null,
        showMsg: Event<String>? = null,
        reLogin: Event<Boolean>? = null
    ) {
        val uiModel = LoginUiModel(isLoading, isResultSuccess, showMsg, reLogin)
        _uiState.value = uiModel
    }

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }

}

data class LoginUiModel(
    val isLoading: Event<Boolean>?,
    val isResultSuccess: Event<Boolean>?,
    val showMsg: Event<String>?,
    val reLogin: Event<Boolean>?
)