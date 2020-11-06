package com.lcz.bm

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lcz.bm.adapter.ShowMsgAdapter
import com.lcz.bm.api.BMApiService
import com.lcz.bm.databinding.ActivityMainBinding
import com.lcz.bm.entity.*
import com.lcz.bm.util.RefreshStatusUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainActionHandler,
    RefreshStatusUtil.OnRefreshStatusListener {

    private lateinit var binding: ActivityMainBinding
    private var API_URL = "https://api.52jiayundong.com/"
    private var mToken = ""
    private var gson: Gson? = null
    private lateinit var showMsgAdapter: ShowMsgAdapter
    private lateinit var smRecyclerView: RecyclerView
    private var msgArrays = ArrayList<ShowMsgEntity>()
    private var mRefreshStatusUtil: RefreshStatusUtil? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.actionHandler = this
        smRecyclerView = binding.recycleViewTxt
        initCurrentTime()
        initRecyclerView()
        initHandlerTime()

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
    override fun onActionLogin() {
        getRepository(1)
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
                            mToken = result.data.token
                            Log.d("mToken 1 ", mToken)
                            Log.d("onResponse1 ", "登录成功")
                        } else {
                            if (result != null) {
                                Log.d("onResponse2 ", result.msg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseUserEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                    }
                })
            }
            2 -> {
                Log.d("mToken 2 ", mToken)
                val placeList = bmApi.getPlaceList(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to mToken
                    ),
                    mapOf(
                        "date" to "2020-11-08",
                        "sportId" to "4028f0ce5551abf3015551b0aae50001",
                        "departId" to "1418",
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
                        } else {
                            if (result != null) {
                                Log.d("onResponse2 ", result.msg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BasePlaceEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                    }
                })
            }
            3 -> {
                Log.d("mToken 3 ", mToken)
                val checkSelect = bmApi.checkSelectPlace(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to mToken
                    ),
                    mapOf(
                        "fieldDate" to "2020-11-08",
                        "fieldDetailIdsList" to "6679,6680",
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
                        } else {
                            if (result != null) {
                                Log.d("onResponse2 ", result.msg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseCheckSelectEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                    }
                })
            }
            4 -> {
                Log.d("mToken 4 ", mToken)
                var placeList = ArrayList<Fieldlist>()
                var idList1 = ArrayList<String>()
                idList1.add("6679")
                var idList2 = ArrayList<String>()
                idList2.add("6680")

                var field1 = Fieldlist(
                    id = "287",
                    stime = "13:30",
                    etime = "14:00",
                    price = "45.00",
                    priceidlist = idList1
                )
                var field2 = Fieldlist(
                    id = "287",
                    stime = "13:30",
                    etime = "14:00",
                    price = "45.00",
                    priceidlist = idList2
                )
                placeList.add(field1)
                placeList.add(field2)

                var submitDataEntity = SubmitEntity(
                    venueid = "1418",
                    sportid = "4028f0ce5551abf3015551b0aae50001",
                    ordertype = "2",
                    fieldorder = Fieldorder(
                        date = "2020-11-08",
                        fieldlist = placeList
                    )
                )

                val submitRetrofit = bmApi.submitOrder(
                    mapOf(
                        "api_version" to "5",
                        "platform" to "1",
                        "token" to mToken
                    ),
                    map2Body(submitDataEntity)
                )
                submitRetrofit.enqueue(object : Callback<BaseSubmitEntity> {
                    override fun onResponse(
                        call: Call<BaseSubmitEntity>,
                        response: Response<BaseSubmitEntity>
                    ) {
                        val result = response.body()
                        if (result != null && result.code == 900) {
                            Log.d("onResponse4 ", "订单提交成功 ")
                        } else {
                            if (result != null) {
                                Log.d("onResponse2 ", result.msg)
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseSubmitEntity>, t: Throwable) {
                        Log.d("onFailure ", t.message.toString())
                    }
                })
            }
        }

    }

    @SuppressLint("ShowToast")
    override fun onActionGetListData() {
        getRepository(2)

    }

    @SuppressLint("ShowToast")
    override fun onActionCheckSelect() {
        getRepository(3)
    }

    @SuppressLint("ShowToast")
    override fun onActionSubmitOrder() {
        getRepository(4)
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
//        if (!isMonDay()) {
//            mHandler.sendEmptyMessage(IS_NOT_MONDAY)
//            mRefreshStatusUtil?.release()
//            return
//        }
        val mSDF = SimpleDateFormat(TIME_STYLE)
        mCurrentStringTime = mSDF.format(System.currentTimeMillis())
        val mCurrentData = mSDF.parse(mCurrentStringTime)
        val mSelectData = mSDF.parse(mSelectTime)
        if (mCurrentData.time > mSelectData.time) {
            mHandler.sendEmptyMessage(START_REQUEST_NET)
            mRefreshStatusUtil?.release()
        } else {
            mHandler.sendEmptyMessage(LONG_TIME_REFRESH)
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
                    0 -> {
                        refreshRecyclerViewData("开始进行登录校验")
                    }
                    1 -> {
                        refreshRecyclerViewData("当前时间：$mCurrentStringTime")
                    }
                    2 -> {
                        refreshRecyclerViewData("开始请求网络")
                    }
                    3 -> {
                        refreshRecyclerViewData("已超过开抢时间，请用其他软件预定场地")
                    }
                }
            }
        }

    private fun initCurrentTime() {
        val mSDF = SimpleDateFormat(TIME_CURRENT)
        mSelectTime = mSDF.format(System.currentTimeMillis()) + " 18:00:00"
        binding.selectTime = "开始订场时间：$mSelectTime"
    }


    companion object {
        const val START_LOGIN = 0
        const val LONG_TIME_REFRESH = 1
        const val START_REQUEST_NET = 2
        const val IS_NOT_MONDAY = 3
        const val TIME_STYLE = "yyyy-MM-dd HH:mm:ss"
        const val TIME_CURRENT = "yyyy-MM-dd"

    }
}