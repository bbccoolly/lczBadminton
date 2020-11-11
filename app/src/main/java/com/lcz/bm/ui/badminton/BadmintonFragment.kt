package com.lcz.bm.ui.badminton

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lcz.bm.adapter.RecyclerMsgAdapter
import com.lcz.bm.databinding.FragmentBadmintonBinding
import com.lcz.bm.entity.SelectFieldPlaceEntity
import com.lcz.bm.entity.ShowMsgEntity
import com.lcz.bm.net.Event
import com.lcz.bm.net.EventObserver
import com.lcz.bm.util.DateFormatterUtil
import com.lcz.bm.util.ProvideOrderDataUtil
import com.lcz.bm.util.SharedPreferenceStorage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
@AndroidEntryPoint
class BadmintonFragment : Fragment(), BadmintonActionHandler {

    private val viewModel: BadmintonViewModel by viewModels()

    @Inject
    lateinit var dateFormatterUtil: DateFormatterUtil

    @Inject
    lateinit var prefs: SharedPreferenceStorage

    @Inject
    lateinit var provideOrderDataUtil: ProvideOrderDataUtil

    @Inject
    lateinit var mAdapter: RecyclerMsgAdapter

    private val _msgList = MutableLiveData<Event<List<ShowMsgEntity>>>()
    private val msgList: LiveData<Event<List<ShowMsgEntity>>> = _msgList

    private var mSelectFPList: ArrayList<SelectFieldPlaceEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBadmintonBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.actionHandler = this
        binding.recyclerView.adapter = mAdapter
        binding.startTime = dateFormatterUtil.getStartTimeString()
        binding.selectTime = dateFormatterUtil.getDayFieldPlaceTimeWeek(1)
        subscribeRecyclerUI()
        return binding.root
    }

    private fun subscribeRecyclerUI() {
        msgList.observe(viewLifecycleOwner, EventObserver {
            mAdapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                            } else {
                                mSelectFPList.add(
                                    SelectFieldPlaceEntity(
                                        fieldId = element.id,
                                        placeId = element.priceList[i].id
                                    )
                                )
                            }

                        } else {
                            //场地不可用
                        }

                    }

                }
            }
        })
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

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

    override fun onAction1() {
        viewModel.checkTokenStatus()
    }

    override fun onAction2() {
        viewModel.login()
    }

    override fun onAction3() {
        viewModel.getPlaceList(dateFormatterUtil.getDayFieldPlaceTime(1))
    }

    override fun onAction5() {
        viewModel.submitOrder(provideOrderDataUtil.providePlaceData(mSelectFPList,dateFormatterUtil.getDayFieldPlaceTime(1)))
    }
}