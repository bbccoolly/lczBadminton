package com.lcz.bm.ui.badminton

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
import javax.inject.Inject

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

        viewModel.placeInfo.observe(viewLifecycleOwner, EventObserver {
            val placeList = it.data
            val data = placeList[0]
            val fieldList = data.fieldList
            for (element in fieldList) {//场地
                for (i in element.priceList.indices) {
                    Log.d(
                        "observe",
                        "index i- " + i + " 场地号 id- " + element.id + " 时间段 id- " + element.priceList[i].id + " " + element.priceList[i].startTime + " " + element.priceList[i].status
                    )
                    if (i == 22) {
                        if (element.priceList[i].status == "0") {
                            if (mSelectFPList.size >= 2) {
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
                            mShowMsgList.add(ShowMsgEntity(element.fieldName + " 不可选...", false))
                            subscribeRecyclerUI()
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
        binding.mcB2 = checked
        binding.mcB5 = !checked
        if (checked) {
            mSelectDay = 2
//            _msgList.postValue(Event())
        }
        binding.selectTime = dateFormatterUtil.getDayFieldPlaceTimeWeek(mSelectDay)
    }

    override fun onActionCBZ5(checked: Boolean) {
        binding.mcB2 = !checked
        binding.mcB5 = checked
        if (checked) {
            mSelectDay = 5
        }
        binding.selectTime = dateFormatterUtil.getDayFieldPlaceTimeWeek(mSelectDay)
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
            }
        }
    }

    private var isStartNet = false
    override fun onRefreshStatus() {
        if (dateFormatterUtil.getCurrentTimeLong() >= dateFormatterUtil.getAutoSelectTImeLong()) {
            isStartNet = true
            onAction3()
            mRefreshStatusUtil!!.release()
        } else {
            isStartNet = false
            if (dateFormatterUtil.getCurrentTimeLong() == dateFormatterUtil.getAutoSelectCheckTimeLong()) {
                onAction1()
            } else {
                mHandler.sendEmptyMessage(MSG_TIME_REFRESH)
            }
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