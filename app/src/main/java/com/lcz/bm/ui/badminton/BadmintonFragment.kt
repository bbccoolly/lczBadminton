package com.lcz.bm.ui.badminton

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lcz.bm.adapter.RecyclerMsgAdapter
import com.lcz.bm.databinding.FragmentBadmintonBinding
import com.lcz.bm.entity.SelectFieldPlaceEntity
import com.lcz.bm.entity.ShowMsgEntity
import com.lcz.bm.net.EventObserver
import com.lcz.bm.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
@AndroidEntryPoint
class BadmintonFragment : Fragment(), BadmintonActionHandler,
    RefreshStatusUtil.OnRefreshStatusListener {

    private val viewModel: BadmintonViewModel by viewModels()
    private lateinit var binding: FragmentBadmintonBinding

    @Inject
    lateinit var dateFormatterUtil: DateFormatterUtil

    @Inject
    lateinit var prefs: SharedPreferenceStorage

    @Inject
    lateinit var provideOrderDataUtil: ProvideOrderDataUtil

    @Inject
    lateinit var mAdapter: RecyclerMsgAdapter

    @Inject
    lateinit var recyclerViewUtil: RecyclerViewUtil

    private var mRefreshStatusUtil: RefreshStatusUtil? = null

    private var mSelectFPList: ArrayList<SelectFieldPlaceEntity> = ArrayList()
    private var mSelectDay = 2

    private var mShowMsgList: ArrayList<ShowMsgEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadmintonBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.actionHandler = this
        binding.recyclerView.adapter = mAdapter
        binding.startTime = dateFormatterUtil.getAutoStartTimeString()
        binding.selectTime = dateFormatterUtil.getDayFieldPlaceTimeWeek(mSelectDay)
        return binding.root
    }

    private fun subscribeRecyclerUI() {
        mAdapter.submitList(mShowMsgList)
        mAdapter.notifyDataSetChanged()
        recyclerViewUtil.scrollTo(binding.recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mRefreshStatusUtil == null) mRefreshStatusUtil = RefreshStatusUtil(context, this)
        mRefreshStatusUtil!!.start()
        onActionCBZ2(true)
        observe()
    }

    private fun observe() {
        viewModel.loginInfo.observe(viewLifecycleOwner, EventObserver {
            prefs.token = it.token
        })

        viewModel.uiState.observe(viewLifecycleOwner, Observer { it ->
            val uiModel = it ?: return@Observer
            if (uiModel.showMsg != null && !uiModel.showMsg.hasBeenHandled) {
                uiModel.showMsg.getContentIfNotHandled()?.let {
                    Log.d("TAG", it)
                    mShowMsgList.add(ShowMsgEntity(it, false))
                    subscribeRecyclerUI()
                    onAction3()
                }
            }

            if (uiModel.reLogin != null && !uiModel.reLogin.hasBeenHandled) {
                uiModel.reLogin.getContentIfNotHandled()?.let {
                    if (it) {
                        mShowMsgList.add(ShowMsgEntity("已经掉线，正在重新登录...", false))
                        subscribeRecyclerUI()
                        viewModel.login()
                    }
                }
            }
        })
        var isGetNetData = false
        viewModel.placeInfo.observe(viewLifecycleOwner, EventObserver {
            val placeList = it.data
            val data = placeList[0]
            val fieldList = data.fieldList
            for (element in fieldList) {//场地
                for (i in element.priceList.indices) {
                    if (i == 22) {
                        Log.d(
                            "observe",
                            "index i- " + i + " 场地号 id- " + element.id + " 时间段 id- " + element.priceList[i].id + " " + element.priceList[i].startTime + " " + element.priceList[i].status
                        )
                        if (element.priceList[i].status == "0") {
                            if (mSelectFPList.size >= 2) {
                                isGetNetData = true
                                //场地选择成功
                                mShowMsgList.add(ShowMsgEntity("场地选择成功，正在提交订单...", false))
                                subscribeRecyclerUI()
                                onAction5()
                                return@EventObserver
                            } else {
                                mShowMsgList.add(
                                    ShowMsgEntity(
                                        element.fieldName + " 可选，加入场地池中...",
                                        false
                                    )
                                )
                                subscribeRecyclerUI()
                                mSelectFPList.add(
                                    SelectFieldPlaceEntity(
                                        fieldId = element.id,
                                        placeId = element.priceList[i].id
                                    )
                                )
                            }
                        } else {
                            //场地不可用
                            mShowMsgList.add(
                                ShowMsgEntity(
                                    element.fieldName + " 不可选...",
                                    false
                                )
                            )
                            if (element.id == 287 && !isGetNetData && mSelectFPList.size > 0) {//最后一个场地 && 没有青丘国
                                mShowMsgList.add(ShowMsgEntity("场地选择成功，正在提交订单...", false))
                                subscribeRecyclerUI()
                                onAction5()
                            }
                        }

                    }

                }
            }
        })

        viewModel.resultSubmitOrder.observe(viewLifecycleOwner, EventObserver {
            mSelectFPList.clear()
            mShowMsgList.add(ShowMsgEntity(it.showMsg, it.isSuccess))
            subscribeRecyclerUI()
        })
    }

    override fun onAction1() {
        viewModel.checkTokenStatus()
    }

    override fun onAction2() {
        viewModel.login()
    }

    override fun onAction3() {
        if (isStartNet) {
            mSelectFPList.clear()
            viewModel.getPlaceList(dateFormatterUtil.getDayFieldPlaceTime(mSelectDay))
        }
    }

    override fun onAction5() {
        viewModel.submitOrder(
            provideOrderDataUtil.providePlaceData(
                mSelectFPList,
                dateFormatterUtil.getDayFieldPlaceTime(mSelectDay)
            )
        )
    }

    override fun onActionCBDS(checked: Boolean) {
    }

    override fun onActionCBZ2(checked: Boolean) {
        if (checked) {
            mSelectDay = 2
            updateCheckBoxStatus(mSelectDay)
        }
    }

    override fun onActionCBZ5(checked: Boolean) {
        if (checked) {
            mSelectDay = 5
            updateCheckBoxStatus(mSelectDay)
        }
    }

    override fun onActionCBZ4(checked: Boolean) {
        if (checked) {
            mSelectDay = 4
            updateCheckBoxStatus(mSelectDay)
        }
    }

    private fun updateCheckBoxStatus(type: Int) {
        binding.selectTime = dateFormatterUtil.getDayFieldPlaceTimeWeek(mSelectDay)
        when (type) {
            2 -> {
                binding.mcB2 = true
                binding.mcB4 = false
                binding.mcB5 = false
            }
            4 -> {
                binding.mcB2 = false
                binding.mcB4 = true
                binding.mcB5 = false
            }
            5 -> {
                binding.mcB2 = false
                binding.mcB4 = false
                binding.mcB5 = true
            }
        }
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_TEST -> {
                }
                MSG_TIME_REFRESH -> {
                    mShowMsgList.add(ShowMsgEntity(dateFormatterUtil.getCurrentTimeString(), false))
                    subscribeRecyclerUI()
                }
                MSG_ACTION_1 -> {
                    onAction1()

                }
                MSG_ACTION_3 -> {
                    onAction3()
                }
            }
        }
    }

    private var isStartNet = false
    private val calendar = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    private val formatter1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    private val formatter2 = SimpleDateFormat("yyyy-MM-dd")

    override fun onRefreshStatus() {
        if (dateFormatterUtil.getCurrentTimeLong() >= dateFormatterUtil.getAutoSelectTImeLong()) {
            mRefreshStatusUtil!!.release()
            isStartNet = true
            mHandler.sendEmptyMessage(MSG_ACTION_3)
        } else {
            if (dateFormatterUtil.getCurrentTimeLong() == dateFormatterUtil.getAutoSelectCheckTimeLong()) {
                mHandler.sendEmptyMessage(MSG_ACTION_1)
            } else {
                mHandler.sendEmptyMessage(MSG_TIME_REFRESH)
            }
            isStartNet = false
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        const val MSG_TEST = -1
        const val MSG_TIME_REFRESH = 1
        const val MSG_ACTION_1 = 2
        const val MSG_ACTION_3 = 3

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): BadmintonFragment {
            return BadmintonFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }


}