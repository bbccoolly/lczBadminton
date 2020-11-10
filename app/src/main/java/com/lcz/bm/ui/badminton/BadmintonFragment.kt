package com.lcz.bm.ui.badminton

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lcz.bm.databinding.FragmentBadmintonBinding
import com.lcz.bm.net.EventObserver
import com.lcz.bm.util.DateFormatterUtil
import com.lcz.bm.util.SharedPreferenceStorage
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBadmintonBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.actionHandler = this
        return binding.root
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
        viewModel.getPlaceList(dateFormatterUtil.getStartNetTimeString())
    }

    override fun onAction4() {
        viewModel.checkPlaceStatus(dateFormatterUtil.getStartNetTimeString())
    }

    override fun onAction5() {
        viewModel.submitOrder(dateFormatterUtil.getStartNetTimeString())
    }
}