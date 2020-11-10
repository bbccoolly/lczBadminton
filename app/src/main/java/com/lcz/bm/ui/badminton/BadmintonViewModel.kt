package com.lcz.bm.ui.badminton

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lcz.bm.entity.LoginUserEntity
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
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _loginInfo = MutableLiveData<Event<LoginUserEntity>>()
    val loginInfo: LiveData<Event<LoginUserEntity>> = _loginInfo

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

    //获取场地列表 4
    fun checkPlaceStatus(time: String) {
        if (netJob4?.isActive == true) {
            return
        }
        netJob4 = launchCheckPlaceStatus(time)
    }

    //提交订单 5
    fun submitOrder(time:String) {
        if (netJob5?.isActive == true) {
            return
        }
        netJob5 = launchSubmitOrder(time)
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
                        showMsg = Event(content = "token 校验成功")
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
            withContext(dispatcherProvider.main) {
                emitUiState(isLoading = Event(true))
            }
            val result = placeRemoteDataSource.getPlaceList(prefs.token.toString(), time)
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
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

    private fun launchCheckPlaceStatus(time: String): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                emitUiState(isLoading = Event(true))
            }
            val result = placeRemoteDataSource.checkSelectPlace(prefs.token.toString(), time)
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    emitUiState(
                        isLoading = Event(false),
                        isResultSuccess = Event(true),
                        showMsg = Event(content = "场地校验成功")
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

    private fun launchSubmitOrder(time: String): Job? {
        return viewModelScope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                emitUiState(isLoading = Event(true))
            }
            val result = orderRemoteDataSource.submitOrder(prefs.token.toString(),time)
            withContext(dispatcherProvider.main) {
                if (result is Result.Success) {
                    emitUiState(
                        isLoading = Event(false),
                        isResultSuccess = Event(true),
                        showMsg = Event(content = "下单成功，请前往支付")
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

    private fun emitUiState(
        isLoading: Event<Boolean>? = null,
        isResultSuccess: Event<Boolean>? = null,
        showMsg: Event<String>? = null
    ) {
        val uiModel = LoginUiModel(isLoading, isResultSuccess, showMsg)
        _uiState.value = uiModel
    }


}

data class LoginUiModel(
    val isLoading: Event<Boolean>?,
    val isResultSuccess: Event<Boolean>?,
    val showMsg: Event<String>?
)