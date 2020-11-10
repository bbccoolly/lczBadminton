package com.lcz.bm.util

import com.lcz.bm.entity.Fieldlist
import com.lcz.bm.entity.Fieldorder
import com.lcz.bm.entity.SubmitEntity
import javax.inject.Inject

/**
 * desc: 提交订单所需要数据
 *
 *
 * create by Arrow on 2020-11-10
 */
class ProvideOrderDataUtil @Inject constructor(
    private val fileId1: String,
    private val placeId1: Int,
    private val fileId2: String,
    private val placeId2: Int,
    private val time: String
) {
    fun providePlaceData(): SubmitEntity {
        val placeList = ArrayList<Fieldlist>()
        val idList31 = ArrayList<String>()
        idList31.add(placeId1.toString())
        val idList32 = ArrayList<String>()
        idList32.add((placeId1 + 1).toString())
        val idList33 = ArrayList<String>()
        idList33.add((placeId1 + 2).toString())
        val idList34 = ArrayList<String>()
        idList34.add((placeId1 + 3).toString())

        val idList41 = ArrayList<String>()
        idList41.add(placeId2.toString())
        val idList42 = ArrayList<String>()
        idList42.add((placeId2 + 1).toString())
        val idList43 = ArrayList<String>()
        idList43.add((placeId2 + 2).toString())
        val idList44 = ArrayList<String>()
        idList44.add((placeId2 + 3).toString())

        val field31 = Fieldlist(
            id = fileId1,
            stime = "20:00",
            etime = "20:30",
            price = "45.00",
            priceidlist = idList31
        )
        val field32 = Fieldlist(
            id = fileId1,
            stime = "20:30",
            etime = "21:00",
            price = "45.00",
            priceidlist = idList32
        )
        val field33 = Fieldlist(
            id = fileId1,
            stime = "21:00",
            etime = "21:30",
            price = "45.00",
            priceidlist = idList33
        )
        val field34 = Fieldlist(
            id = fileId1,
            stime = "21:30",
            etime = "22:00",
            price = "45.00",
            priceidlist = idList34
        )

        val field41 = Fieldlist(
            id = fileId2,
            stime = "20:00",
            etime = "20:30",
            price = "45.00",
            priceidlist = idList41
        )
        val field42 = Fieldlist(
            id = fileId2,
            stime = "20:30",
            etime = "21:00",
            price = "45.00",
            priceidlist = idList42
        )
        val field43 = Fieldlist(
            id = fileId2,
            stime = "21:00",
            etime = "21:30",
            price = "45.00",
            priceidlist = idList43
        )
        val field44 = Fieldlist(
            id = fileId2,
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

        return SubmitEntity(
            venueid = "1418",
            sportid = "4028f0ce5551abf3015551b0aae50001",
            ordertype = "2",
            fieldorder = Fieldorder(
                date = time,
                fieldlist = placeList
            )
        )
    }
}