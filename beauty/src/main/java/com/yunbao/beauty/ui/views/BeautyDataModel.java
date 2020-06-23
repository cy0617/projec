package com.yunbao.beauty.ui.views;

import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.bean.IBeautyData;
import com.meihu.beautylibrary.bean.WaterAlignEnum;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.bean.QuickBeautyBean;
import com.yunbao.beauty.ui.bean.WatermarkBean;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.enums.FilterEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyShapeEnum;

import java.util.HashMap;

/**
 * BeautyDataModel 美颜数据存储
 * Created by Kongxr on 2019/10/31.
 */
public class BeautyDataModel implements IBeautyData {

    private HashMap<String, Integer> beautyDataMap = new HashMap<>();
    private HashMap<String, Integer> lastNormalBeautyDataMap = new HashMap<>();
    private String[] beautyAllNames;
    private String[] beautyBasicNames;
    private final int[] beautyDefaultDatas;
    private final int[] beautyOriginDatas;
    private String currentStickerName;  //贴纸
    private FilterEnum filterEnum;  //滤镜
    private String speciallyName;   //特效
    private WatermarkBean watermarkBean; //当前水印bean
    private String distortionName;  //哈哈镜
    //    private QuickBeautyEnum quickBeautyEnum; //一键美颜
    private QuickBeautyBean quickBeautyBean;
    private int[] quickBeautyDatas;
    private boolean isQuickBeautyMode;  //是否一键美颜模式
    private int quickProgress = 50; //一键美颜整体进度条 默认50
    private HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> quickBeautyDataMap = new HashMap<>(); //一键美颜被选项详细数据

    private BeautyDataModel() {
        beautyDefaultDatas = MHSDK.getInstance().getAppContext().getResources().getIntArray(R.array.name_beauty_default_progress);
        beautyAllNames = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.name_beauty_name_array);
        beautyBasicNames = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.name_basic_beauty_name_array);
        beautyOriginDatas = MHSDK.getInstance().getAppContext().getResources().getIntArray(R.array.name_beauty_origin_progress);
        resetDefaultBasicBeautyData();
        if ("1".equals(MHSDK.getInstance().getVer())) {
            resetDefaultShapeData();
        } else {
            resetOriginShapeData();
        }
//        initQuickData();
    }

//    private void initQuickData(){
//        int length = beautyAllNames.length;
//        for (int i = 0; i < length; i++) {
//            quickBeautyDataMap.put(beautyAllNames[i], beautyOriginDatas[i]);
//        }
//    }

    /**
     * 用户sdk初始化默认采用的水印
     *
     * @param i (0、1、2、3、4)分别为为无、左上、右上、左下、右下
     */
    public void setWatermark(int i) {
        String[] stringArray = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.specially__watermark_name_list);
        TypedArray speciallyArray = MHSDK.getInstance().getAppContext().getResources().obtainTypedArray(R.array.specially_watermark_icon_list);
        TypedArray speciallyArraySel = MHSDK.getInstance().getAppContext().getResources().obtainTypedArray(R.array.specially_watermark_sel_list);
        String beautyName = stringArray[i];
        int imgSrc = speciallyArray.getResourceId(i, R.mipmap.beauty_origin);
        int ImgSrcSel = speciallyArraySel.getResourceId(i, R.mipmap.beauty_origin);
        this.watermarkBean = new WatermarkBean(imgSrc, ImgSrcSel, beautyName, BeautyTypeEnum.WATER_TYPE_ENUM, false, WaterAlignEnum.getValue(i));
    }

    public void setFilterChanged(FilterEnum noFilter) {
        this.filterEnum = noFilter;
    }

    public FilterEnum getFilterEnum() {
        return filterEnum;
    }

    public String getSpeciallyName() {
        return speciallyName;
    }

    public void setSpeciallyName(String speciallyName) {
        this.speciallyName = speciallyName;
    }

    public WatermarkBean getWatermarkBean() {
        return watermarkBean;
    }

    public void setWatermarkBean(WatermarkBean watermarkBean) {
        this.watermarkBean = watermarkBean;
    }

    public String getDistortionName() {
        return distortionName;
    }

    public void setDistortionName(String distortionName) {
        this.distortionName = distortionName;
    }

//    public QuickBeautyEnum getQuickBeautyEnum() {
//        return quickBeautyEnum;
//    }

//    public void setQuickBeautyEnum(QuickBeautyBean quickBeautyBean) {
//        this.quickBeautyBean = quickBeautyBean;
//        QuickBeautyEnum quickBeautyEnum = quickBeautyBean.getQuickBeautyEnum();
////        this.quickBeautyEnum = this.quickBeautyEnum;
//        if (quickBeautyEnum == QuickBeautyEnum.ORIGIN_BEAUTY){
//            isQuickBeautyMode = false;
//        }else {
//            isQuickBeautyMode = true;
//        }
//    }

    public QuickBeautyBean getQuickBeautyBean() {
        return quickBeautyBean;
    }

    public void setQuickBeautyBean(QuickBeautyBean quickBeautyBean) {
        this.quickBeautyBean = quickBeautyBean;
        QuickBeautyEnum quickBeautyEnum = quickBeautyBean.getQuickBeautyEnum();
//        this.quickBeautyEnum = this.quickBeautyEnum;
        isQuickBeautyMode = quickBeautyEnum != QuickBeautyEnum.ORIGIN_BEAUTY;
        HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> beautyMap = quickBeautyBean.getBeautyMap();
        if (beautyMap == null) {
            quickBeautyDatas = new int[beautyAllNames.length - beautyBasicNames.length];
        } else {
            QuickBeautyBean.ElementValue bigEyeElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_BIGEYE);
            QuickBeautyBean.ElementValue eyeBrowElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEBROW);
            QuickBeautyBean.ElementValue eyeLengthElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYELENGTH);
            QuickBeautyBean.ElementValue eyeCornerElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYECORNER);
            QuickBeautyBean.ElementValue faceElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FACE);
            QuickBeautyBean.ElementValue mouseElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_MOUSE);
            QuickBeautyBean.ElementValue noseElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_NOSE);
            QuickBeautyBean.ElementValue chinElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_CHIN);
            QuickBeautyBean.ElementValue foreheadElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FOREHEAD);
            QuickBeautyBean.ElementValue lengthNoseElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE);
            QuickBeautyBean.ElementValue shaveFaceElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE);
            QuickBeautyBean.ElementValue eyealatElementValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEALAT);
            quickBeautyDatas = new int[]{bigEyeElementValue.getValue(), eyeBrowElementValue.getValue(), eyeLengthElementValue.getValue()
                    , eyeCornerElementValue.getValue(), faceElementValue.getValue(), mouseElementValue.getValue(), noseElementValue.getValue()
                    , chinElementValue.getValue(), foreheadElementValue.getValue(), lengthNoseElementValue.getValue(), shaveFaceElementValue.getValue()
                    , eyealatElementValue.getValue()};
        }
    }

    @Override
    public int[] getBeautyAndShapeData() {
        return getCurrentBeautyMap();
    }

    @Override
    public String getStickerName() {
        return currentStickerName;
    }

    @Override
    public String getSpeciallyEffectName() {
        return speciallyName;
    }

    @Override
    public String getDistortionEffectName() {
        return distortionName;
    }

    @Override
    public SparseArray<WaterAlignEnum> getWatermark() {
        if (watermarkBean == null) return null;
        int imgSrcSel = watermarkBean.getImgSrcSel();
        WaterAlignEnum pos = watermarkBean.getPos();
        SparseArray<WaterAlignEnum> watermarkMap = new SparseArray<>();
        watermarkMap.put(imgSrcSel, pos);
        return watermarkMap;
    }

    @Override
    public String[] getAllBeautyName() {
        return beautyAllNames;
    }

    @Override
    public String[] getBasicBeautyName() {
        return beautyBasicNames;
    }

    @Override
    public boolean getIsQuickMode() {
        return isQuickBeautyMode;
    }

    @Override
    public int[] getQuickShapeData() {
        return quickBeautyDatas;
    }

    @Override
    public String getFilterName() {
        return null;
    }

    private static class InstanceHolder {
        static private BeautyDataModel mInstance = new BeautyDataModel();
    }

    public static BeautyDataModel getInstance() {
        return InstanceHolder.mInstance;
    }

//    public void setBeautyData(int index, int progress){
//        beautyDefaultDatas[index] = progress;
//    }
//
//    public void setBeautyData(int[] beautyDefaultDatas) {
//        this.beautyDefaultDatas = beautyDefaultDatas;
//    }
//
//    public int[] getBeautyDatas(){
//        return beautyDefaultDatas;
//    }
//
//    public int getBeautyData(int index){
//        return beautyDefaultDatas[index];
//    }

    public synchronized void setBeautyProgress(String beautyName, int progress) {
        beautyDataMap.put(beautyName, progress);
        Log.d("meihu_sdk_BeautyModel", beautyName + "---" + +progress);
    }

    public synchronized int getBeautyProgress(String beautyName) {
        Integer progress = beautyDataMap.get(beautyName);
        return progress == null ? 0 : progress;
    }

    public synchronized void setBeautyDataMap(int[] progressList) {
        if (progressList == null || progressList.length < 16) return;
        String[] stringArray = MHSDK.getInstance().getAppContext().getResources().getStringArray(R.array.name_beauty_name_array);
        for (int i = 0; i < progressList.length; i++) {
            if (progressList[i] >= 0) {
                beautyDataMap.put(stringArray[i], progressList[i]);
            }
        }
    }

    public synchronized int[] getCurrentBeautyMap() {
        int[] currentBeautyData = new int[beautyAllNames.length];
        for (int i = 0; i < beautyAllNames.length; i++) {
            Integer data = beautyDataMap.get(beautyAllNames[i]);
            currentBeautyData[i] = (data == null ? 0 : data);
        }
        return currentBeautyData;
    }

    public void setCurrentStickerName(String currentStickerName) {
        this.currentStickerName = currentStickerName;
    }

    /*
     * 恢复美型为真实人脸数据
     * */
    public synchronized void resetOriginShapeData() {
//        int shapeNumber = beautyAllNames.length - beautyBasicNames.length;
//        beautyDataMap.clear();
        int length = beautyAllNames.length;
        for (int i = beautyBasicNames.length; i < length; i++) {
            beautyDataMap.put(beautyAllNames[i], beautyOriginDatas[i]);
        }
    }

    /*
     * 恢复美颜为真实人脸数据
     * */
    public synchronized void resetOriginBasicBeautyData() {
//        beautyDataMap.clear();
        int length = beautyBasicNames.length;
        for (int i = 0; i < length; i++) {
//            beautyDataMap.put(beautyBasicNames[i], beautyDefaultDatas[i]);
            beautyDataMap.put(beautyBasicNames[i], beautyOriginDatas[i]);
        }
    }

    /*
     * 恢复美型为默认人脸数据
     * */
    public synchronized void resetDefaultShapeData() {
//        int shapeNumber = beautyAllNames.length - beautyBasicNames.length;
//        beautyDataMap.clear();
        int length = beautyAllNames.length;
        for (int i = beautyBasicNames.length; i < length; i++) {
            beautyDataMap.put(beautyAllNames[i], beautyDefaultDatas[i]);
        }
    }

    /*
     * 恢复美颜为默认人脸数据
     * */
    public synchronized void resetDefaultBasicBeautyData() {
//        beautyDataMap.clear();
        int length = beautyBasicNames.length;
        for (int i = 0; i < length; i++) {
//            beautyDataMap.put(beautyBasicNames[i], beautyDefaultDatas[i]);
            beautyDataMap.put(beautyBasicNames[i], beautyDefaultDatas[i]);
        }
    }

    public synchronized void destroyData() {
//        resetDefaultAllBeautyData();

        /*  取消默认美颜参数，改为从后台获取
        resetDefaultBasicBeautyData();
        if ("1".equals(MHSDK.getInstance().getVer())) {
            resetDefaultShapeData();
        } else {
            resetOriginShapeData();
        }
        */


        currentStickerName = "";
        lastNormalBeautyDataMap.clear();
        filterEnum = null;
        speciallyName = "";
//    private String watermarkName;
        watermarkBean = null;
        distortionName = "";
//        quickBeautyEnum = null;
        quickBeautyDatas = null;
        quickBeautyBean = null;
        isQuickBeautyMode = false;
        quickProgress = 50;
        quickBeautyDataMap = new HashMap<>();
    }

//    @Override
//    public String getStickerName() {
//        return StickerManager.getInstance().getmCurStickerName();
//    }

    /*
     * 所有美颜美型恢复为真实人脸数据
     * */
    private synchronized void resetOriginAllBeautyData() {
        beautyDataMap.clear();
        int length = beautyAllNames.length;
        for (int i = 0; i < length; i++) {
            beautyDataMap.put(beautyAllNames[i], beautyOriginDatas[i]);
        }
        filterEnum = null;
        speciallyName = "";
    }

    /*
     * 所有美颜美型恢复为默认数据
     * */
    private synchronized void resetDefaultAllBeautyData() {
        beautyDataMap.clear();
        int length = beautyAllNames.length;
        for (int i = 0; i < length; i++) {
            beautyDataMap.put(beautyAllNames[i], beautyDefaultDatas[i]);
        }
        filterEnum = null;
        speciallyName = "";
    }

    /*
     * 获取美颜美型默认数据
     * */
    public int[] getDefaultAllBeautyDatas() {
        return beautyDefaultDatas;
    }

    /*
     * 获取美颜美型真实人脸数据
     * */
    public int[] getOriginAllBeautyDatas() {
        return beautyOriginDatas;
    }

    public synchronized void storageLastNormalBeautyData() {
        if (lastNormalBeautyDataMap.size() > 0) {
            return;
        }
        lastNormalBeautyDataMap.clear();
        lastNormalBeautyDataMap.putAll(beautyDataMap);
    }

    public synchronized HashMap<String, Integer> getLastNormalBeautyDataMap() {
        return lastNormalBeautyDataMap;
    }

    public synchronized void clearLastNormalBeautyData() {
        if (lastNormalBeautyDataMap.size() > 0) lastNormalBeautyDataMap.clear();
    }

    public String[] getBeautyBasicNames() {
        return beautyBasicNames;
    }

    public String[] getBeautyAllNames() {
        return beautyAllNames;
    }

    public int getQuickProgress() {
        return quickProgress;
    }

    public void setQuickProgress(int quickProgress) {
        this.quickProgress = quickProgress;
    }

//    public HashMap<String, Integer> getQuickBeautyDataMap() {
//        return quickBeautyDataMap;
//    }
//
//    public void setQuickBeautyDataMap(HashMap<String, Integer> quickBeautyDataMap) {
//        this.quickBeautyDataMap = quickBeautyDataMap;
//    }
//
//    public int getQuickProgress(String name){
//        Integer integer = quickBeautyDataMap.get(name);
//        return integer == null? 0 : integer;
//    }


    public HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> getQuickBeautyDataMap() {
        return quickBeautyDataMap;
    }

    public void setQuickBeautyDataMap(HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> quickBeautyDataMap) {
        this.quickBeautyDataMap = quickBeautyDataMap;
    }
}
