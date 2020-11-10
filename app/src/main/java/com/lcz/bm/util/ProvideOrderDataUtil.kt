package com.lcz.bm.util

import com.lcz.bm.entity.Fieldlist
import com.lcz.bm.entity.Fieldorder
import com.lcz.bm.entity.SubmitEntity
import javax.inject.Inject

/**
 * desc: TODO
 *
 *
 * create by Arrow on 2020-11-10
 */
class ProvideOrderDataUtil @Inject constructor() {
    fun providePlaceData(time: String): SubmitEntity {
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