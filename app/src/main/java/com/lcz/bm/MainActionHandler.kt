package com.lcz.bm

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
interface MainActionHandler {
    fun onActionStart()
    fun onActionClear()
    fun onActionCBZ2(checked: Boolean)
    fun onActionCBZ5(checked: Boolean)
    fun onActionCBAutoSubmit(checked: Boolean)
    fun onActionCBAutoStart(checked: Boolean)

    fun onActionAddLog()
    fun onActionGetLog()
}