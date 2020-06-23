package com.yunbao.beauty.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.constant.Constants;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.QuickBeautyBean;
import com.yunbao.beauty.ui.enums.QuickBeautyEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyShapeEnum;
import com.yunbao.beauty.ui.views.BeautyDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kongxr on 2019/8/23.
 * filterAdapter
 */

public class QuickBeautyAdapter extends  BaseBeautyAdapter<QuickBeautyAdapter.ViewHolder, QuickBeautyBean> {

    QuickBeautyAdapter(Context context) {
        super(context,1);
        initData();
        mOnClickListener = v -> {
            Object tag = v.getTag();
            if (tag != null) {
                int position = (int) tag;
                if (mCheckedPosition == position) {
                    return;
                }
                if (mCheckedPosition >= 0 && mCheckedPosition < mList.size()) {
                    mList.get(mCheckedPosition).setChecked(false);
                    notifyItemChanged(mCheckedPosition, Constants.PAYLOAD);
                }
                mList.get(position).setChecked(true);
//                QuickBeautyBean quickBeautyBean = mList.get(position);
//                BeautyDataModel.getInstance().setQuickBeautyEnum(quickBeautyBean.getQuickBeautyEnum());
                notifyItemChanged(position, Constants.PAYLOAD);
                mCheckedPosition = position;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mList.get(position), position);
                }
            }
        };
    }

    private void initData() {
        mList = new ArrayList<>();
        QuickBeautyBean quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.ORIGIN_BEAUTY, "", R.mipmap.icon_quick_beauty_origin,null, true);
        mList.add(quickBeautyBean);

        //标准
        HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue0 = new QuickBeautyBean.ElementValue(3);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue0);
        QuickBeautyBean.ElementValue elementValue1 = new QuickBeautyBean.ElementValue(3);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue1);
        QuickBeautyBean.ElementValue elementValue2 = new QuickBeautyBean.ElementValue(2);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue2);
        QuickBeautyBean.ElementValue elementValue3 = new QuickBeautyBean.ElementValue(37,37,44);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue3);
        QuickBeautyBean.ElementValue elementValue4 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue4);
        QuickBeautyBean.ElementValue elementValue5 = new QuickBeautyBean.ElementValue(72,68,84);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue5);
        QuickBeautyBean.ElementValue elementValue6 = new QuickBeautyBean.ElementValue(52,46,56);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue6);
        QuickBeautyBean.ElementValue elementValue7 = new QuickBeautyBean.ElementValue(55,49,58);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue7);
        QuickBeautyBean.ElementValue elementValue8 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue8);
        QuickBeautyBean.ElementValue elementValue9 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue9);
        QuickBeautyBean.ElementValue elementValue10 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue10);
        QuickBeautyBean.ElementValue elementValue11 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue11);
        QuickBeautyBean.ElementValue elementValue12 = new QuickBeautyBean.ElementValue(41,35,41);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue12);
        QuickBeautyBean.ElementValue elementValue13 = new QuickBeautyBean.ElementValue(0);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue13);
        QuickBeautyBean.ElementValue elementValue14 = new QuickBeautyBean.ElementValue(55,50,55);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.STANDARD_BEAUTY, "",R.mipmap.icon_quick_beauty_standard,quickBeautyMap);
        mList.add(quickBeautyBean);

        //优雅
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue1_0 = new QuickBeautyBean.ElementValue(2);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue1_0);
        QuickBeautyBean.ElementValue elementValue1_1 = new QuickBeautyBean.ElementValue(6);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue1_1);
        QuickBeautyBean.ElementValue elementValue1_2 = new QuickBeautyBean.ElementValue(5);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue1_2);
        QuickBeautyBean.ElementValue elementValue1_3 = new QuickBeautyBean.ElementValue(28,22,32);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue1_3);
        QuickBeautyBean.ElementValue elementValue1_4 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue1_4);
        QuickBeautyBean.ElementValue elementValue1_5 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue1_5);
        QuickBeautyBean.ElementValue elementValue1_6 = new QuickBeautyBean.ElementValue(55,46,58);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue1_6);
        QuickBeautyBean.ElementValue elementValue1_7 = new QuickBeautyBean.ElementValue(37,28,44);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue1_7);
        QuickBeautyBean.ElementValue elementValue1_8 = new QuickBeautyBean.ElementValue(58,50,67);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue1_8);
        QuickBeautyBean.ElementValue elementValue1_9 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue1_9);
        QuickBeautyBean.ElementValue elementValue1_10 = new QuickBeautyBean.ElementValue(27,23,30);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue1_10);
        QuickBeautyBean.ElementValue elementValue1_11 = new QuickBeautyBean.ElementValue(80,77,86);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue1_11);
        QuickBeautyBean.ElementValue elementValue1_12 = new QuickBeautyBean.ElementValue(20,13,27);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue1_12);
        QuickBeautyBean.ElementValue elementValue1_13 = new QuickBeautyBean.ElementValue(0);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue1_13);
        QuickBeautyBean.ElementValue elementValue1_14 = new QuickBeautyBean.ElementValue(77,74,85);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue1_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.ELEGANT_BEAUTY, "",R.mipmap.icon_quick_beauty_youya,quickBeautyMap);
        mList.add(quickBeautyBean);

        //精致
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue2_0 = new QuickBeautyBean.ElementValue(8);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue2_0);
        QuickBeautyBean.ElementValue elementValue2_1 = new QuickBeautyBean.ElementValue(4);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue2_1);
        QuickBeautyBean.ElementValue elementValue2_2 = new QuickBeautyBean.ElementValue(1);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue2_2);
        QuickBeautyBean.ElementValue elementValue2_3 = new QuickBeautyBean.ElementValue(64,56,67);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue2_3);
        QuickBeautyBean.ElementValue elementValue2_4 = new QuickBeautyBean.ElementValue(26,20,30);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue2_4);
        QuickBeautyBean.ElementValue elementValue2_5 = new QuickBeautyBean.ElementValue(65,61,73);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue2_5);
        QuickBeautyBean.ElementValue elementValue2_6 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue2_6);
        QuickBeautyBean.ElementValue elementValue2_7 = new QuickBeautyBean.ElementValue(55,48,58);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue2_7);
        QuickBeautyBean.ElementValue elementValue2_8 = new QuickBeautyBean.ElementValue(70,65,78);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue2_8);
        QuickBeautyBean.ElementValue elementValue2_9 = new QuickBeautyBean.ElementValue(71,64,74);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue2_9);
        QuickBeautyBean.ElementValue elementValue2_10 = new QuickBeautyBean.ElementValue(38,32,42);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue2_10);
        QuickBeautyBean.ElementValue elementValue2_11 = new QuickBeautyBean.ElementValue(70,66,73);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue2_11);
        QuickBeautyBean.ElementValue elementValue2_12 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue2_12);
        QuickBeautyBean.ElementValue elementValue2_13 = new QuickBeautyBean.ElementValue(13,0,25);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue2_13);
        QuickBeautyBean.ElementValue elementValue2_14 = new QuickBeautyBean.ElementValue(26,20,29);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue2_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.EXQUISITE_BEAUTY, "",R.mipmap.icon_quick_beauty_jingzhi,quickBeautyMap);
        mList.add(quickBeautyBean);

        //可爱
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue3_0 = new QuickBeautyBean.ElementValue(5);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue3_0);
        QuickBeautyBean.ElementValue elementValue3_1 = new QuickBeautyBean.ElementValue(5);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue3_1);
        QuickBeautyBean.ElementValue elementValue3_2 = new QuickBeautyBean.ElementValue(0);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue3_2);
        QuickBeautyBean.ElementValue elementValue3_3 = new QuickBeautyBean.ElementValue(69,66,73);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue3_3);
        QuickBeautyBean.ElementValue elementValue3_4 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue3_4);
        QuickBeautyBean.ElementValue elementValue3_5 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue3_5);
        QuickBeautyBean.ElementValue elementValue3_6 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue3_6);
        QuickBeautyBean.ElementValue elementValue3_7 = new QuickBeautyBean.ElementValue(62,55,68);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue3_7);
        QuickBeautyBean.ElementValue elementValue3_8 = new QuickBeautyBean.ElementValue(53,50,60);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue3_8);
        QuickBeautyBean.ElementValue elementValue3_9 = new QuickBeautyBean.ElementValue(78,74,84);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue3_9);
        QuickBeautyBean.ElementValue elementValue3_10 = new QuickBeautyBean.ElementValue(86,80,100);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue3_10);
        QuickBeautyBean.ElementValue elementValue3_11 = new QuickBeautyBean.ElementValue(92,85,100);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue3_11);
        QuickBeautyBean.ElementValue elementValue3_12 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue3_12);
        QuickBeautyBean.ElementValue elementValue3_13 = new QuickBeautyBean.ElementValue(39,37,47);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue3_13);
        QuickBeautyBean.ElementValue elementValue3_14 = new QuickBeautyBean.ElementValue(12,0,15);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue3_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.LOVELY_BEAUTY, "",R.mipmap.icon_quick_beauty_lovely,quickBeautyMap);
        mList.add(quickBeautyBean);

        //自然
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue4_0 = new QuickBeautyBean.ElementValue(3);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue4_0);
        QuickBeautyBean.ElementValue elementValue4_1 = new QuickBeautyBean.ElementValue(4);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue4_1);
        QuickBeautyBean.ElementValue elementValue4_2 = new QuickBeautyBean.ElementValue(3);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue4_2);
        QuickBeautyBean.ElementValue elementValue4_3 = new QuickBeautyBean.ElementValue(23,10,28);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue4_3);
        QuickBeautyBean.ElementValue elementValue4_4 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue4_4);
        QuickBeautyBean.ElementValue elementValue4_5 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue4_5);
        QuickBeautyBean.ElementValue elementValue4_6 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue4_6);
        QuickBeautyBean.ElementValue elementValue4_7 = new QuickBeautyBean.ElementValue(30,20,37);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue4_7);
        QuickBeautyBean.ElementValue elementValue4_8 = new QuickBeautyBean.ElementValue(53,50,55);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue4_8);
        QuickBeautyBean.ElementValue elementValue4_9 = new QuickBeautyBean.ElementValue(55,55,65);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue4_9);
        QuickBeautyBean.ElementValue elementValue4_10 = new QuickBeautyBean.ElementValue(52,48,55);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue4_10);
        QuickBeautyBean.ElementValue elementValue4_11 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue4_11);
        QuickBeautyBean.ElementValue elementValue4_12 = new QuickBeautyBean.ElementValue(35,30,40);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue4_12);
        QuickBeautyBean.ElementValue elementValue4_13 = new QuickBeautyBean.ElementValue(19,15,30);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue4_13);
        QuickBeautyBean.ElementValue elementValue4_14 = new QuickBeautyBean.ElementValue(13,10,20);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue4_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.NATURAL_BEAUTY, "",R.mipmap.icon_quick_beauty_natural,quickBeautyMap);
        mList.add(quickBeautyBean);

        //网红
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue5_0 = new QuickBeautyBean.ElementValue(9);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue5_0);
        QuickBeautyBean.ElementValue elementValue5_1 = new QuickBeautyBean.ElementValue(5);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue5_1);
        QuickBeautyBean.ElementValue elementValue5_2 = new QuickBeautyBean.ElementValue(7);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue5_2);
        QuickBeautyBean.ElementValue elementValue5_3 = new QuickBeautyBean.ElementValue(72,65,80);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue5_3);
        QuickBeautyBean.ElementValue elementValue5_4 = new QuickBeautyBean.ElementValue(55,52,62);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue5_4);
        QuickBeautyBean.ElementValue elementValue5_5 = new QuickBeautyBean.ElementValue(66,53,73);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue5_5);
        QuickBeautyBean.ElementValue elementValue5_6 = new QuickBeautyBean.ElementValue(77,63,81);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue5_6);
        QuickBeautyBean.ElementValue elementValue5_7 = new QuickBeautyBean.ElementValue(85,78,90);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue5_7);
        QuickBeautyBean.ElementValue elementValue5_8 = new QuickBeautyBean.ElementValue(80,73,83);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue5_8);
        QuickBeautyBean.ElementValue elementValue5_9 = new QuickBeautyBean.ElementValue(87,83,100);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue5_9);
        QuickBeautyBean.ElementValue elementValue5_10 = new QuickBeautyBean.ElementValue(70,65,78);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue5_10);
        QuickBeautyBean.ElementValue elementValue5_11 = new QuickBeautyBean.ElementValue(77,71,80);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue5_11);
        QuickBeautyBean.ElementValue elementValue5_12 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue5_12);
        QuickBeautyBean.ElementValue elementValue5_13 = new QuickBeautyBean.ElementValue(38,22,48);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue5_13);
        QuickBeautyBean.ElementValue elementValue5_14 = new QuickBeautyBean.ElementValue(55,48,58);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue5_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.ONLINE_BEAUTY, "",R.mipmap.icon_quick_beauty_wanghong,quickBeautyMap);
        mList.add(quickBeautyBean);

        //脱俗
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue6_0 = new QuickBeautyBean.ElementValue(6);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue6_0);
        QuickBeautyBean.ElementValue elementValue6_1 = new QuickBeautyBean.ElementValue(4);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue6_1);
        QuickBeautyBean.ElementValue elementValue6_2 = new QuickBeautyBean.ElementValue(0);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue6_2);
        QuickBeautyBean.ElementValue elementValue6_3 = new QuickBeautyBean.ElementValue(40,35,45);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue6_3);
        QuickBeautyBean.ElementValue elementValue6_4 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue6_4);
        QuickBeautyBean.ElementValue elementValue6_5 = new QuickBeautyBean.ElementValue(42,37,47);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue6_5);
        QuickBeautyBean.ElementValue elementValue6_6 = new QuickBeautyBean.ElementValue(77,63,81);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue6_6);
        QuickBeautyBean.ElementValue elementValue6_7 = new QuickBeautyBean.ElementValue(40,36,46);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue6_7);
        QuickBeautyBean.ElementValue elementValue6_8 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue6_8);
        QuickBeautyBean.ElementValue elementValue6_9 = new QuickBeautyBean.ElementValue(52,50,58);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue6_9);
        QuickBeautyBean.ElementValue elementValue6_10 = new QuickBeautyBean.ElementValue(31,29,39);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue6_10);
        QuickBeautyBean.ElementValue elementValue6_11 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue6_11);
        QuickBeautyBean.ElementValue elementValue6_12 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue6_12);
        QuickBeautyBean.ElementValue elementValue6_13 = new QuickBeautyBean.ElementValue(54,50,60);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue6_13);
        QuickBeautyBean.ElementValue elementValue6_14 = new QuickBeautyBean.ElementValue(53,50,62);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue6_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.REFINED_BEAUTY, "",R.mipmap.icon_quick_beauty_tuosu,quickBeautyMap);
        mList.add(quickBeautyBean);

        //高雅
        quickBeautyMap = new HashMap<>();
        QuickBeautyBean.ElementValue elementValue7_0 = new QuickBeautyBean.ElementValue(4);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_WHITE, elementValue7_0);
        QuickBeautyBean.ElementValue elementValue7_1 = new QuickBeautyBean.ElementValue(6);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_GRIND, elementValue7_1);
        QuickBeautyBean.ElementValue elementValue7_2 = new QuickBeautyBean.ElementValue(4);
        quickBeautyMap.put(QuickBeautyShapeEnum.BEAUTY_TENDER, elementValue7_2);
        QuickBeautyBean.ElementValue elementValue7_3 = new QuickBeautyBean.ElementValue(29,22,32);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_BIGEYE, elementValue7_3);
        QuickBeautyBean.ElementValue elementValue7_4 = new QuickBeautyBean.ElementValue(19,0,27);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEBROW, elementValue7_4);
        QuickBeautyBean.ElementValue elementValue7_5 = new QuickBeautyBean.ElementValue(50);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYELENGTH, elementValue7_5);
        QuickBeautyBean.ElementValue elementValue7_6 = new QuickBeautyBean.ElementValue(23,18,28);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYECORNER, elementValue7_6);
        QuickBeautyBean.ElementValue elementValue7_7 = new QuickBeautyBean.ElementValue(17,12,22);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FACE, elementValue7_7);
        QuickBeautyBean.ElementValue elementValue7_8 = new QuickBeautyBean.ElementValue(56,50,63);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_MOUSE, elementValue7_8);
        QuickBeautyBean.ElementValue elementValue7_9 = new QuickBeautyBean.ElementValue(70,60,75);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_NOSE, elementValue7_9);
        QuickBeautyBean.ElementValue elementValue7_10 = new QuickBeautyBean.ElementValue(47,37,54);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_CHIN, elementValue7_10);
        QuickBeautyBean.ElementValue elementValue7_11 = new QuickBeautyBean.ElementValue(55,50,61);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_FOREHEAD, elementValue7_11);
        QuickBeautyBean.ElementValue elementValue7_12 = new QuickBeautyBean.ElementValue(7,0,10);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE, elementValue7_12);
        QuickBeautyBean.ElementValue elementValue7_13 = new QuickBeautyBean.ElementValue(61,56,70);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE, elementValue7_13);
        QuickBeautyBean.ElementValue elementValue7_14 = new QuickBeautyBean.ElementValue(27,23,34);
        quickBeautyMap.put(QuickBeautyShapeEnum.SHAPE_EYEALAT, elementValue7_14);
        quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.SOLEMN_BEAUTY, "",R.mipmap.icon_quick_beauty_gaoya,quickBeautyMap);
        mList.add(quickBeautyBean);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_filter_new, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {}

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, List<Object> payloads) {
        Object payload = payloads.size() > 0 ? payloads.get(0) : null;
        viewHolder.setData(mList.get(position), position, payload);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends BaseBeautyAdapter.Vh{

        //        ImageView mImg;
        ImageView mCheckImg;
//        TextView mFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckImg = itemView.findViewById(R.id.check_img);
        }

        void setData(QuickBeautyBean bean, int position, Object payload) {
            itemView.setTag(position);
            if (payload == null) {
                mImg.setImageResource(bean.getImgSrc());
                mBeautyName.setText(bean.getEffectName());
            }
            if (position == 0 ){
                QuickBeautyBean quickBeautyBean = BeautyDataModel.getInstance().getQuickBeautyBean();
                if (quickBeautyBean == null || quickBeautyBean.getQuickBeautyEnum() == null || quickBeautyBean.getQuickBeautyEnum()==QuickBeautyEnum.ORIGIN_BEAUTY){
                    bean.setChecked(true);
                    mCheckedPosition = 0;
                }else {
                    bean.setChecked(false);
                }
            }else {
                QuickBeautyBean quickBeautyBean = BeautyDataModel.getInstance().getQuickBeautyBean();
                if (quickBeautyBean != null && quickBeautyBean.getQuickBeautyEnum()!= null && bean.getQuickBeautyEnum()==quickBeautyBean.getQuickBeautyEnum()){
                    bean.setChecked(true);
                    mCheckedPosition = position;
                }else {
                    bean.setChecked(false);
                }
            }
            if (bean.isChecked()) {
                if (mCheckImg.getVisibility() != View.VISIBLE) {
                    mCheckImg.setVisibility(View.VISIBLE);
                }
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.shape_icon_select_color));
            } else {
                if (mCheckImg.getVisibility() == View.VISIBLE) {
                    mCheckImg.setVisibility(View.INVISIBLE);
                }
                mBeautyName.setTextColor(MHSDK.getInstance().getAppContext().getResources().getColor(R.color.bg_black));
            }
        }
    }
}
