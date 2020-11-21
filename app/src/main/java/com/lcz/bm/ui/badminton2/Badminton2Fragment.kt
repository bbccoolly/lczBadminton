package com.lcz.bm.ui.badminton2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lcz.bm.databinding.FragmentBm2Binding
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-21
 */
@AndroidEntryPoint
class Badminton2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentBm2Binding.inflate(inflater, container, false).root
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
        fun newInstance(sectionNumber: Int): Badminton2Fragment {
            return Badminton2Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}