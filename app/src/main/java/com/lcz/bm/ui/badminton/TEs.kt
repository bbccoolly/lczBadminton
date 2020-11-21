package com.lcz.bm.ui.badminton

import android.os.CountDownTimer

/**
 * desc: TODO
 *
 *
 * create by Arrow on 2020-11-21
 */
class TEs {
    private val countDownTimer: CountDownTimer

    init {
        countDownTimer = object : CountDownTimer(10, 10) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {}
        }
    }
}