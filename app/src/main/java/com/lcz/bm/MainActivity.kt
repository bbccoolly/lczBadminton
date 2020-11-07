package com.lcz.bm

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lcz.bm.adapter.ShowMsgAdapter
import com.lcz.bm.api.BMApiService
import com.lcz.bm.data.LogEntity
import com.lcz.bm.data.LoggerDataSource
import com.lcz.bm.databinding.ActivityMainBinding
import com.lcz.bm.di.InMemoryLogger
import com.lcz.bm.entity.*
import com.lcz.bm.util.DateFormatterUtil
import com.lcz.bm.util.RefreshStatusUtil
import com.lcz.bm.util.SharedPreferenceStorage
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActionHandler,
    RefreshStatusUtil.OnRefreshStatusListener {

    @InMemoryLogger
    @Inject
    lateinit var logger: LoggerDataSource

    @Inject
    lateinit var dateFormatter: DateFormatterUtil


    private lateinit var binding: ActivityMainBinding
    private var gson: Gson? = null
    private lateinit var showMsgAdapter: ShowMsgAdapter
    private lateinit var smRecyclerView: RecyclerView
    private var msgArrays = ArrayList<ShowMsgEntity>()
    private var mRefreshStatusUtil: RefreshStatusUtil? = null
    private val prefs = SharedPreferenceStorage(this)
    private var mSelectPlaceData = "2020-11-08"

    private var isSelectZ2 = true
    private var isAutoSubmit = true
    private var isAutoStart = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.actionHandler = this
        smRecyclerView = binding.recycleViewTxt
        initCurrentTime()
        initRecyclerView()
        updateCheckBoxStatus()
        updateCheckBoxAutoStart()
    }

    private fun updateCheckBoxAutoStart() {
        binding.isAutoStart = isAutoStart
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateCheckBoxStatus() {
        binding.isSelectZ2 = isSelectZ2
        binding.isAutoSubmit = isAutoSubmit

        val c = Calendar.getInstance()
        if (isSelectZ2) {
            c.add(Calendar.DAY_OF_MONTH, +1)
        } else {
            c.add(Calendar.DAY_OF_MONTH, +4)
        }
        val time = c.time
        mSelectPlaceData = SimpleDateFormat("yyyy-MM-dd").format(time)
        Log.d("TAG", "S2--------$mSelectPlaceData")
        binding.selectPlace = "要预定场地时间为：$mSelectPlaceData"
    }

    private fun initHandlerTime() {
        if (mRefreshStatusUtil == null) {
            mRefreshStatusUtil = RefreshStatusUtil(this, this)
        }
        mRefreshStatusUtil?.start()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true
        showMsgAdapter = ShowMsgAdapter(this)
        smRecyclerView.apply {
//            layoutManager = linearLayoutManager
            adapter = showMsgAdapter
            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }
        }
        showMsgAdapter.submitList(msgArrays)
    }

    @SuppressLint("ShowToast")
    override fun onActionStart() {
        initHandlerTime()
    }

    private fun getRepository(type: Int) {
        var bmApi = Retrofit.Builder().baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BMApiService::class.java)

        when (type) {
            1 -> {
                val login = bmApi.login(
                    mapOf(
                        "phone" to "13530318471",
                        "password" to "meng520",
                        "inviteCode" to "",
                        "source" to "1",
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to ""
                    )
                )
                login.enqueue(object : Callback<BaseUserEntity> {
                    override fun onResponse(
                        call: Call<BaseUserEntity>,
                        response: Response<BaseUserEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            prefs.token = result.data.token
                            mHandler.sendEmptyMessage(SUCCESS_START_NET_LOGIN)
                        } else {
                            if (result != null) {
                                val obtainMessage = mHandler.obtainMessage()
                                obtainMessage.what = ERROR_NET_MSG
                                obtainMessage.obj = result.msg
                                mHandler.sendMessage(obtainMessage)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseUserEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                        val obtainMessage = mHandler.obtainMessage()
                        obtainMessage.what = ERROR_NET_MSG
                        obtainMessage.obj = t.message.toString()
                        mHandler.sendMessage(obtainMessage)
                    }
                })
            }
            2 -> {
                val placeList = bmApi.getPlaceList(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to prefs.token.toString()
                    ),
                    mapOf(
                        "date" to mSelectPlaceData,
                        "sportId" to "4028f0ce5551abf3015551b0aae50001",
                        "departId" to "1418"
                    )
                )
                placeList.enqueue(object : Callback<BasePlaceEntity> {
                    override fun onResponse(
                        call: Call<BasePlaceEntity>,
                        response: Response<BasePlaceEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            Log.d("onResponse2 ", "获取场地成功 ")
                            mHandler.sendEmptyMessage(SUCCESS_START_NET_FIELD_LIST)
                        } else {
                            if (result != null) {
                                val obtainMessage = mHandler.obtainMessage()
                                obtainMessage.what = ERROR_NET_MSG
                                obtainMessage.obj = result.msg
                                mHandler.sendMessage(obtainMessage)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BasePlaceEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                        val obtainMessage = mHandler.obtainMessage()
                        obtainMessage.what = ERROR_NET_MSG
                        obtainMessage.obj = t.message.toString()
                        mHandler.sendMessage(obtainMessage)
                    }
                })
            }
            3 -> {
                val checkSelect = bmApi.checkSelectPlace(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to prefs.token.toString()
                    ),
                    mapOf(
                        "fieldDate" to mSelectPlaceData,
                        "fieldDetailIdsList" to "6679,6680"
                    )
                )
                checkSelect.enqueue(object : Callback<BaseCheckSelectEntity> {
                    override fun onResponse(
                        call: Call<BaseCheckSelectEntity>,
                        response: Response<BaseCheckSelectEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            Log.d("onResponse3 ", "选择场次成功 ")
                            mHandler.sendEmptyMessage(SUCCESS_START_NET_CHECK_SELECT)
                        } else {
                            if (result != null) {
                                val obtainMessage = mHandler.obtainMessage()
                                obtainMessage.what = ERROR_NET_MSG
                                obtainMessage.obj = result.msg
                                mHandler.sendMessage(obtainMessage)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseCheckSelectEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                        val obtainMessage = mHandler.obtainMessage()
                        obtainMessage.what = ERROR_NET_MSG
                        obtainMessage.obj = t.message.toString()
                        mHandler.sendMessage(obtainMessage)
                    }
                })
            }
            4 -> {
                if (!isAutoSubmit) {
                    return
                }
                val submitRetrofit = bmApi.submitOrder(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to prefs.token.toString()
                    ),
                    map2Body(getPlaceData())
                )
                submitRetrofit.enqueue(object : Callback<BaseSubmitEntity> {
                    override fun onResponse(
                        call: Call<BaseSubmitEntity>,
                        response: Response<BaseSubmitEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            Log.d("onResponse4 ", "订单提交成功 ")
                            mHandler.sendEmptyMessage(SUCCESS_START_NET_SUBMIT_ORDER)
                        } else {
                            if (result != null) {
                                val obtainMessage = mHandler.obtainMessage()
                                obtainMessage.what = ERROR_NET_MSG
                                obtainMessage.obj = result.msg
                                mHandler.sendMessage(obtainMessage)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseSubmitEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                        val obtainMessage = mHandler.obtainMessage()
                        obtainMessage.what = ERROR_NET_MSG
                        obtainMessage.obj = t.message.toString()
                        mHandler.sendMessage(obtainMessage)
                    }
                })
            }
            5 -> {//用于校验 token 是否可用
                val checkToken = bmApi.getOrderList(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to prefs.token.toString()
                    ),
                    mapOf(
                        "status" to "0",
                        "curPage" to "1"
                    )
                )
                checkToken.enqueue(object : Callback<BaseEntity> {
                    override fun onResponse(
                        call: Call<BaseEntity>,
                        response: Response<BaseEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            Log.d("onResponse5 ", "token 可用")
                            mHandler.sendEmptyMessage(SUCCESS_TOKEN_CHECK)
                        } else {
                            if (result != null) {
                                if (result.code == 906) {
                                    mHandler.sendEmptyMessage(SUCCESS_TOKEN_CHECK_OUT_TIME)
                                }
                                val obtainMessage = mHandler.obtainMessage()
                                obtainMessage.what = SUCCESS_TOKEN_CHECK_OTHER_ERROR
                                obtainMessage.obj = result.msg
                                mHandler.sendMessage(obtainMessage)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                        val obtainMessage = mHandler.obtainMessage()
                        obtainMessage.what = ERROR_NET_MSG
                        obtainMessage.obj = t.message.toString()
                        mHandler.sendMessage(obtainMessage)
                    }
                })
            }
        }

    }

    private fun getPlaceData(): SubmitEntity {
        val placeList = ArrayList<Fieldlist>()
        val idList31 = ArrayList<String>()
        idList31.add("6396")
        val idList32 = ArrayList<String>()
        idList32.add("6397")
        val idList33 = ArrayList<String>()
        idList33.add("6398")
        val idList34 = ArrayList<String>()
        idList34.add("6399")

        val idList41 = ArrayList<String>()
        idList41.add("6423")
        val idList42 = ArrayList<String>()
        idList42.add("6424")
        val idList43 = ArrayList<String>()
        idList43.add("6425")
        val idList44 = ArrayList<String>()
        idList44.add("6426")

        val field31 = Fieldlist(
            id = "276",
            stime = "20:00",
            etime = "20:30",
            price = "45.00",
            priceidlist = idList31
        )
        val field32 = Fieldlist(
            id = "276",
            stime = "20:30",
            etime = "21:00",
            price = "45.00",
            priceidlist = idList32
        )
        val field33 = Fieldlist(
            id = "276",
            stime = "21:00",
            etime = "21:30",
            price = "45.00",
            priceidlist = idList33
        )
        val field34 = Fieldlist(
            id = "276",
            stime = "21:30",
            etime = "22:00",
            price = "45.00",
            priceidlist = idList34
        )

        val field41 = Fieldlist(
            id = "277",
            stime = "20:00",
            etime = "20:30",
            price = "45.00",
            priceidlist = idList41
        )
        val field42 = Fieldlist(
            id = "277",
            stime = "20:30",
            etime = "21:00",
            price = "45.00",
            priceidlist = idList42
        )
        val field43 = Fieldlist(
            id = "277",
            stime = "21:00",
            etime = "21:30",
            price = "45.00",
            priceidlist = idList43
        )
        val field44 = Fieldlist(
            id = "277",
            stime = "21:30",
            etime = "22:00",
            price = "45.00",
            priceidlist = idList44
        )

        //3号场地 晚8点到10点
        placeList.add(field31)
        placeList.add(field32)
        placeList.add(field33)
        placeList.add(field34)
        //4号场地 晚8点到10点
        placeList.add(field41)
        placeList.add(field42)
        placeList.add(field43)
        placeList.add(field44)

        var submitDataEntity = SubmitEntity(
            venueid = "1418",
            sportid = "4028f0ce5551abf3015551b0aae50001",
            ordertype = "2",
            fieldorder = Fieldorder(
                date = mSelectPlaceData,
                fieldlist = placeList
            )
        )
        return submitDataEntity
    }

    override fun onActionClear() {
        getRepository(5)
    }


    override fun onActionCBZ2(checked: Boolean) {
        isSelectZ2 = checked
        updateCheckBoxStatus()
    }

    override fun onActionCBZ5(checked: Boolean) {
        isSelectZ2 = !checked
        updateCheckBoxStatus()
    }

    override fun onActionCBAutoSubmit(checked: Boolean) {
        isAutoSubmit = checked
        updateCheckBoxStatus()
    }

    override fun onActionCBAutoStart(checked: Boolean) {
        isAutoStart = checked
        if (checked) {
        } else {
            mRefreshStatusUtil?.release()
        }
        updateCheckBoxAutoStart()
    }

    override fun onActionAddLog() {
        logger.addLog("onActionAddLog - ")
    }

    override fun onActionGetLog() {
        logger.getAllLogs { logs ->
            val logs1: List<LogEntity> = logs
            for (i in 0..logs1.size - 1) {
                Log.d(
                    "TAG",
                    logs1[i].msg + "----------" + dateFormatter.formatDate(logs1[i].timestamp)
                )
            }
        }
    }


    fun map2Body(`object`: Any): RequestBody {
        checkGson()
        val data = toJson(`object`)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data)
        //return RequestBody.create("application/json; charset=utf-8".MediaType(), data)
    }

    private fun toJson(`object`: Any): String {
        checkGson()
        return gson!!.toJson(`object`)
    }

    private fun checkGson() {
        if (gson == null) {
            gson = Gson()
        }
    }

    private fun refreshRecyclerViewData(msg: String) {
        if (!smRecyclerView.isComputingLayout) {
            msgArrays.add(ShowMsgEntity(msg))
            showMsgAdapter.notifyDataSetChanged()
            smRecyclerView.layoutManager?.smoothScrollToPosition(
                smRecyclerView,
                null,
                showMsgAdapter.itemCount - 1
            )
        }
    }

    private var mSelectTime = ""
    private var mCurrentStringTime = ""

    @SuppressLint("SimpleDateFormat")
    override fun onRefreshStatus() {
        val mSDF = SimpleDateFormat(TIME_STYLE)
        mCurrentStringTime = mSDF.format(System.currentTimeMillis())
        val mCurrentData: Date? = mSDF.parse(mCurrentStringTime)
        val mSelectData: Date? = mSDF.parse(mSelectTime)
        if (mCurrentData != null && mSelectData != null) {
            if (mCurrentData.time > mSelectData.time) {
                mHandler.sendEmptyMessage(START_REQUEST_NET)
                mRefreshStatusUtil?.release()
            } else {
                mHandler.sendEmptyMessage(LONG_TIME_REFRESH)
            }
        }
    }

    private fun isMonDay(): Boolean {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        return c.get(Calendar.DAY_OF_WEEK) == 2
    }

    private val mHandler: Handler =
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    11 -> {
                        refreshRecyclerViewData("登录成功")
                        getRepository(2)
                    }
                    12 -> {
                        refreshRecyclerViewData("获取场地列表成功")
                        getRepository(3)
                    }
                    13 -> {
                        refreshRecyclerViewData("选择场地成功")
                        getRepository(4)
                    }
                    14 -> {
                        refreshRecyclerViewData("提交订单成功，请前往支付")
                    }
                    15 -> {
                        refreshRecyclerViewData("开始进行登录校验")
                    }
                    16 -> {
                        refreshRecyclerViewData(msg.obj.toString())
                    }
                    17 -> {
                        refreshRecyclerViewData("登录状态校验成功")
                        getRepository(2)
                    }
                    18 -> {
                        refreshRecyclerViewData("登录状态校验失效，正在重新登录")
                        getRepository(1)
                    }
                    19 -> {
                        refreshRecyclerViewData(msg.obj.toString())
                    }
                    1 -> {
                        refreshRecyclerViewData("当前时间：$mCurrentStringTime")
                    }
                    2 -> {
                        refreshRecyclerViewData("开始请求网络")
                        getRepository(5)
                    }
                    3 -> {
                        refreshRecyclerViewData("已超过开抢时间，请用其他软件预定场地")
                    }
                    4 -> {
                        refreshRecyclerViewData(prefs.phone.toString())
                    }
                }
            }
        }

    @SuppressLint("SimpleDateFormat")
    private fun initCurrentTime() {
        val mSDF = SimpleDateFormat(TIME_CURRENT)
        mSelectTime = mSDF.format(System.currentTimeMillis()) + " 09:00:00"
        binding.selectTime = mSelectTime


        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, +1)
        val time = c.time
        mSelectPlaceData = SimpleDateFormat("yyyy-MM-dd").format(time)
        Log.d("TAG", "S2--------$mSelectPlaceData")
        binding.selectPlace = "要预定场地时间为：$mSelectPlaceData"
    }


    companion object {
        const val SUCCESS_START_NET_LOGIN = 11
        const val SUCCESS_START_NET_FIELD_LIST = 12
        const val SUCCESS_START_NET_CHECK_SELECT = 13
        const val SUCCESS_START_NET_SUBMIT_ORDER = 14
        const val SUCCESS_START_NET_PAY = 15
        const val ERROR_NET_MSG = 16
        const val SUCCESS_TOKEN_CHECK = 17
        const val SUCCESS_TOKEN_CHECK_OUT_TIME = 18
        const val SUCCESS_TOKEN_CHECK_OTHER_ERROR = 19
        const val LONG_TIME_REFRESH = 1
        const val START_REQUEST_NET = 2
        const val IS_NOT_MONDAY = 3
        const val IS_TEST = 4
        const val TIME_STYLE = "yyyy-MM-dd HH:mm:ss"
        const val TIME_CURRENT = "yyyy-MM-dd"
        const val API_URL = "https://api.52jiayundong.com/"

    }
}