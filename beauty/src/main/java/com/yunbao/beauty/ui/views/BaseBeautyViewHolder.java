package com.yunbao.beauty.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.bean.WaterAlignEnum;
import com.meihu.beautylibrary.manager.MHBeautyManager;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.adapter.BeautyPagerAdapter;
import com.yunbao.beauty.ui.adapter.DistortionAdapter;
import com.yunbao.beauty.ui.adapter.SpeciallyPagerAdapter;
import com.yunbao.beauty.ui.adapter.StickerPagerAdapter;
import com.yunbao.beauty.ui.bean.BeautyBean;
import com.yunbao.beauty.ui.bean.FilterBean;
import com.yunbao.beauty.ui.bean.QuickBeautyBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.bean.WatermarkBean;
import com.yunbao.beauty.ui.enums.BeautyTypeEnum;
import com.yunbao.beauty.ui.enums.FilterEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyEnum;
import com.yunbao.beauty.ui.enums.QuickBeautyShapeEnum;
import com.yunbao.beauty.ui.interfaces.BeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.DefaultBeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.IBeautyViewHolder;
import com.yunbao.beauty.ui.interfaces.MHBeautyEffectListener;
import com.yunbao.beauty.ui.interfaces.MHCameraClickListener;
import com.yunbao.beauty.ui.interfaces.OnItemClickListener;
import com.yunbao.beauty.ui.views.custom.ScaleImageButton;
import com.yunbao.beauty.ui.views.custom.TextSeekBarNew;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Created by Kongxr on 2019/11/01.
 * 美颜UI相关基类
 */
public abstract class BaseBeautyViewHolder extends AbsViewHolder implements MHBeautyEffectListener, IBeautyViewHolder, View.OnClickListener, TextSeekBarNew.OnSeekChangeListener, OnItemClickListener<BeautyBean> {

    private SparseArray<View> mSparseArray;
    private int mCurKey;
    private VisibleListener mVisibleListener;
    private boolean mShowed;
    private RelativeLayout mBeautyControlContainer;
    private RelativeLayout mBeautyControlRoot;
    private View mBeautyControlView;
    protected View mStickerControlView;
    private View mSpeciallyControlView;
    private View mDistortionControlView;
    protected String curShownProgressName;
    protected String[] beautyShownName = new String[BeautyTypeEnum.values().length];
    protected int[] tabShownName = new int[]{BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue(),BeautyTypeEnum.SPECIALLY_TYPE_ENUM.getValue()};
    //    protected HashMap<Integer, TextSeekBarNew> shownSeekBarsMap;
    protected StickerPagerAdapter stickerPagerAdapter;
    protected BeautyPagerAdapter beautyPagerAdapter;
    protected SpeciallyPagerAdapter speciallyPagerAdapter;
    private LinearLayout topControlContainer;
    private MHCameraClickListener mCameraClickListener;
    private ConstraintLayout mCLayoutContainer;
    protected MHBeautyManager mhBeautyManager;
    protected String[] beautyNames;
    private DefaultBeautyEffectListener mEffectListener;
    TextSeekBarNew textSeekBar;
    //    private int shownTabName= 0;
    private final int BEAUTYTAB = 0;
    private final int SPECIALLYTAB = 1;
    private ViewPager beautyViewpager;
    protected ViewPager stickerViewpager;
    private ViewPager speciallyViewpager;

    BaseBeautyViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    BaseBeautyViewHolder(Context context, ViewGroup parentView, Object... args) {
        super(context, parentView, args);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_beauty_new;
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void init() {
        curShownProgressName = mContext.getResources().getString(R.string.beauty_origin);
        beautyNames = mContext.getResources().getStringArray(R.array.name_beauty_name_array);
        topControlContainer = (LinearLayout) findViewById(R.id.ll_beauty_control);
        mBeautyControlContainer = (RelativeLayout) findViewById(R.id.rl_beauty_control_container);
        mBeautyControlRoot = (RelativeLayout) findViewById(R.id.rl_beauty_control_root);
        textSeekBar = (TextSeekBarNew) findViewById(R.id.tsb_beauty);
        textSeekBar.setVisibility(View.GONE);
        textSeekBar.setOnSeekChangeListener(this);
        mCLayoutContainer = (ConstraintLayout) findViewById(R.id.cl_beauty_layout_container);
        LinearLayout mStickerEntry = (LinearLayout) findViewById(R.id.ll_sticker_entry);
        LinearLayout mBeautyEntry = (LinearLayout) findViewById(R.id.ll_beauty_entry);
//        LinearLayout mshapeEntry = (LinearLayout) findViewById(R.id.ll_shape_entry);
        ScaleImageButton mIbTakePicture = (ScaleImageButton) findViewById(R.id.btn_beauty_take_camera);
        LinearLayout mspeciallyEntry = (LinearLayout) findViewById(R.id.ll_specially_entry);
        LinearLayout distortionEntry = (LinearLayout) findViewById(R.id.ll_distortion_entry);
        setBtnClickListener(mCLayoutContainer, mBeautyControlRoot, mBeautyEntry, mspeciallyEntry, distortionEntry, mIbTakePicture, mStickerEntry);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        mStickerControlView = inflater.inflate(R.layout.control_sticker_layout, mBeautyControlContainer, false);
        mBeautyControlView = inflater.inflate(R.layout.control_beauty_layout, mBeautyControlContainer, false);
        mSpeciallyControlView = inflater.inflate(R.layout.control_specially_layout, mBeautyControlContainer, false);
        mDistortionControlView = inflater.inflate(R.layout.control_distortion_layout, mBeautyControlContainer, false);
        mSparseArray = new SparseArray<>();
//        shownSeekBarsMap = new HashMap<>();
        mSparseArray.put(R.id.ll_sticker_entry, mStickerControlView);
        mSparseArray.put(R.id.ll_beauty_entry, mBeautyControlView);
        mSparseArray.put(R.id.ll_specially_entry, mSpeciallyControlView);
        mSparseArray.put(R.id.ll_distortion_entry, mDistortionControlView);
//        processMap = new HashMap<>();
        mCurKey = 0;
        initStickerTab();
        initBeautyView(getVer());
        initSpeciallyView();
        initDistortionView();
        addToParent();
    }

    private int getVer(){
        String ver = MHSDK.getInstance().getVer();
        if (TextUtils.isEmpty(ver)){
            return 0;
        }else {
            return Integer.valueOf(ver);
        }
    }

    protected void setBtnClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    protected void initBeautyView(int ver) {
        List<StickerCategaryBean> titles = new ArrayList<>();
        String[] nameArray;
        String[] idArray;
        if (ver == 1) {
            nameArray = mContext.getResources().getStringArray(R.array.beauty_pro_type_name_array);
            idArray = mContext.getResources().getStringArray(R.array.beauty_pro_type_id_array);
        }else {
            nameArray = mContext.getResources().getStringArray(R.array.beauty_basic_type_name_array);
            idArray = mContext.getResources().getStringArray(R.array.beauty_basic_type_id_array);
        }
        for (int i = 0; i< nameArray.length; i++) {
            StickerCategaryBean stickerCategaryBean = new StickerCategaryBean(idArray[i], nameArray[i]);
            titles.add(stickerCategaryBean);
        }
        beautyViewpager = mBeautyControlView.findViewById(R.id.vp_beauty_sticker);
        beautyPagerAdapter = new BeautyPagerAdapter(titles,ver);
        beautyPagerAdapter.setOnItemClickListener(this);
        beautyViewpager.setAdapter(beautyPagerAdapter);
        TabLayout tabs = mBeautyControlView.findViewById(R.id.tl_beauty_sticker);
        beautyViewpager.setOnPageChangeListener(getPageChangeListener(BEAUTYTAB,tabs));
        View.OnClickListener mTabOnClickListener = v -> {
            int pos = (int) v.getTag();
            beautyViewpager.setCurrentItem(pos, true);
        };
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabs.newTab();
            TextView textView = new TextView(mContext);
            textView.setText(titles.get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mContext.getResources().getColor(R.color.tab_default_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tab.setCustomView(textView);
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(mTabOnClickListener);
            }
            tabs.addTab(tab, i);
        }
        setTabSelected(Objects.requireNonNull(tabs.getTabAt(0)), 0, true);
    }

    protected void initStickerTab() {
//        StickerManager.getInstance().getStickerCategaryList(this);
        List<StickerCategaryBean> titles = new ArrayList<>();
        String[] stringArray = new String[0];
        String[] idArray = new String[0];
        int ver = getVer();
        if (ver == 1){
            stringArray = mContext.getResources().getStringArray(R.array.sticker_pro_type_name_array);
            idArray = mContext.getResources().getStringArray(R.array.sticker_pro_type_id_array);
        }else {
            stringArray = mContext.getResources().getStringArray(R.array.sticker_basic_type_name_array);
            idArray = mContext.getResources().getStringArray(R.array.sticker_basic_type_id_array);
        }
        for (int i = 0; i < stringArray.length; i++) {
            StickerCategaryBean stickerCategaryBean = new StickerCategaryBean(idArray[i], stringArray[i]);
            titles.add(stickerCategaryBean);
        }
        setStickerCategaryData(titles);
    }

    protected void setTabSelected(TabLayout.Tab tabAt, int position, boolean isSelected) {
        if (isSelected) tabAt.select();
        TextView textView = ((TextView) tabAt.getCustomView());
        if (isSelected && textView != null) {
//            shownTabName = position;
            textView.setTextColor(mContext.getResources().getColor(R.color.shape_icon_select_color));
        } else if (textView != null) {
            textView.setTextColor(mContext.getResources().getColor(R.color.tab_default_text_color));
        }
    }

    private void initSpeciallyView() {
        List<StickerCategaryBean> titles = new ArrayList<>();
        String[] nameArray;
        String[] idArray;
        nameArray = mContext.getResources().getStringArray(R.array.beauty_specially_type_name_array);
        idArray = mContext.getResources().getStringArray(R.array.beauty_specially_type_id_array);
        for (int i = 0; i< nameArray.length; i++) {
            StickerCategaryBean stickerCategaryBean = new StickerCategaryBean(idArray[i], nameArray[i]);
            titles.add(stickerCategaryBean);
        }
        speciallyViewpager = mSpeciallyControlView.findViewById(R.id.vp_beauty_sticker);
        speciallyPagerAdapter = new SpeciallyPagerAdapter(titles, getVer());
        speciallyPagerAdapter.setOnItemClickListener(this);
        speciallyViewpager.setAdapter(speciallyPagerAdapter);
        TabLayout tabs = mSpeciallyControlView.findViewById(R.id.tl_beauty_sticker);
        speciallyViewpager.setOnPageChangeListener(getPageChangeListener(SPECIALLYTAB,tabs));
        View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                speciallyViewpager.setCurrentItem(pos, true);
            }
        };
        for (int i = 0; i < titles.size(); i++) {
            TabLayout.Tab tab = tabs.newTab();
            TextView textView = new TextView(mContext);
            textView.setText(titles.get(i).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mContext.getResources().getColor(R.color.tab_default_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tab.setCustomView(textView);
            if (tab.getCustomView() != null) {
                View tabView = (View) tab.getCustomView().getParent();
                tabView.setTag(i);
                tabView.setOnClickListener(mTabOnClickListener);
            }
            tabs.addTab(tab, i);
        }
        setTabSelected(Objects.requireNonNull(tabs.getTabAt(0)), 0, true);
    }

    protected void initDistortionView() {
        //哈哈镜
        RecyclerView distortionRecyclerView = mDistortionControlView.findViewById(R.id.rv_beauty_distortion);
        distortionRecyclerView.setHasFixedSize(true);
        distortionRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        DistortionAdapter distortionAdapter = new DistortionAdapter(mContext);
        distortionAdapter.setOnItemClickListener((bean, position) -> {
            BaseBeautyViewHolder.this.onDistortionChanged(bean.getSpeciallyName());
        });
        distortionRecyclerView.setAdapter(distortionAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_beauty_layout_container) {
            hide();
        } else if (id == R.id.ll_beauty_entry || id == R.id.ll_shape_entry || id == R.id.ll_sticker_entry || id == R.id.ll_filter_entry || id == R.id.ll_distortion_entry || id == R.id.ll_specially_entry) {
            showChildControlView();
            toggle(id);
            topControlContainer.setVisibility(View.GONE);
        } else if (id == R.id.rl_beauty_control_root) {
            hideChildControlView();
            topControlContainer.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_beauty_take_camera) {
            if (mCameraClickListener != null) {
                mCameraClickListener.onCameraClick();
            }
        }
    }

    protected void showChildControlView() {

        if (mBeautyControlRoot == null || mBeautyControlRoot.isShown()) return;
        mBeautyControlRoot.setVisibility(View.VISIBLE);
//        if (mBeautyControlContainer == null || mBeautyControlContainer.isShown()) return;
//        mBeautyControlContainer.setVisibility(View.VISIBLE);
    }

    protected void hideChildControlView() {
        if (mBeautyControlRoot == null || !mBeautyControlRoot.isShown()) return;
//        mBeautyControlContainer.setVisibility(View.GONE);
        mBeautyControlRoot.setVisibility(View.GONE);
    }

    protected void toggle(int key) {
//        if (mCurKey == key) {
//            return;
//        }
        mCurKey = key;
        int togleButtonId = 0;
        for (int i = 0, size = mSparseArray.size(); i < size; i++) {
            View v = mSparseArray.valueAt(i);
            if (mSparseArray.keyAt(i) == key) {
                if (mBeautyControlContainer != null) {
                    if (mBeautyControlContainer.indexOfChild(v) < 0)
                        mBeautyControlContainer.addView(v);
                    else {
                        mBeautyControlContainer.removeView(v);
                        mBeautyControlContainer.addView(v);
                    }
                    togleButtonId = key;
                }
            } else {
                if (mBeautyControlContainer != null) {
                    if (mBeautyControlContainer.indexOfChild(v) >= 0)
                        mBeautyControlContainer.removeView(v);
                }
            }
        }
        restoreProgressData(togleButtonId);
    }

    protected void restoreProgressData(int togleButtonId) {
        textSeekBar.setVisibility(View.GONE);
        if (togleButtonId == R.id.ll_beauty_entry) {
            if (textSeekBar != null) {
                String effectName = beautyShownName[tabShownName[BEAUTYTAB]];
                if (!TextUtils.isEmpty(effectName) && !mContext.getResources().getString(R.string.beauty_origin).equals(effectName) && !mContext.getResources().getString(R.string.filter_no).equals(effectName)){
                    curShownProgressName = effectName;
                    textSeekBar.setProgress(BeautyDataModel.getInstance().getBeautyProgress(effectName));
//                    textSeekBar.setMaxProgress(tabShownName[BEAUTYTAB]==BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()?9:100);
                    if (tabShownName[BEAUTYTAB]==BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()){
                        if (beautyShownName[BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()].equals(mContext.getResources().getString(R.string.beauty_liangdu))){
                            textSeekBar.setMaxProgress(100);
                        }else {
                            textSeekBar.setMaxProgress(9);
                        }
                    }else {
                        textSeekBar.setMaxProgress(100);
                    }
                    textSeekBar.setVisibility(View.VISIBLE);
                }
            }
        }else if (togleButtonId == R.id.ll_specially_entry){
            if (textSeekBar != null) {
                textSeekBar.setVisibility(View.GONE);
//                }
            }
        }
    }

    @Override
    public void setEffectListener(BeautyEffectListener effectListener) {
        if (effectListener != null && effectListener instanceof DefaultBeautyEffectListener) {
            mEffectListener = (DefaultBeautyEffectListener) effectListener;
        }
    }

    @Override
    public void show() {
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(true);
        }
        if (mCLayoutContainer != null) {
            mCLayoutContainer.setVisibility(View.VISIBLE);
        }
        if (topControlContainer != null)
            topControlContainer.setVisibility(View.VISIBLE);
        mShowed = true;
    }

    @Override
    public void hide() {
        if (mVisibleListener != null) {
            mVisibleListener.onVisibleChanged(false);
        }
//        if (mBeautyControlContainer != null)
//            mBeautyControlContainer.setVisibility(View.GONE);
        if (mBeautyControlRoot != null)
            mBeautyControlRoot.setVisibility(View.GONE);
        if (topControlContainer != null) {
            topControlContainer.setVisibility(View.VISIBLE);
        }
        if (mCLayoutContainer != null) {
            mCLayoutContainer.setVisibility(View.GONE);
        }
        mShowed = false;
    }

    @Override
    public boolean isShowed() {
        return mShowed;
    }

    @Override
    public void release() {
        super.release();
        Log.d("meihu_beauty", "BeautyViewHolderNew_release");

        mVisibleListener = null;
        mEffectListener = null;
        mCameraClickListener =  null;
        if (stickerPagerAdapter != null) {
            stickerPagerAdapter.release();
        }
        if (beautyViewpager != null) {
            beautyViewpager.setOnPageChangeListener(null);
        }
        if (stickerViewpager != null) {
            stickerViewpager.setOnPageChangeListener(null);
        }
        if (speciallyViewpager != null) {
            speciallyViewpager.setOnPageChangeListener(null);
        }
        curShownProgressName = "";
        mBeautyControlContainer = null;
        mStickerControlView = null;
        mBeautyControlView = null;
        mSpeciallyControlView = null;
        mDistortionControlView = null;
    }

    @Override
    public void setVisibleListener(VisibleListener visibleListener) {
        mVisibleListener = visibleListener;
    }

    @Override
    public void setCameraClickListener(MHCameraClickListener cameraClickListener) {
        this.mCameraClickListener = cameraClickListener;
    }

    public void setMhBeautyManager(MHBeautyManager mhBeautyManager) {
        this.mhBeautyManager = mhBeautyManager;
    }

    @Override
    public void onFilterChanged(FilterEnum mhFilterEnum) {
        if (mEffectListener != null) {
            mEffectListener.onFilterChanged(mhFilterEnum);
        }
    }

    @Override
    public void onBeautyOrigin() {
        if (mEffectListener != null) {
            BeautyDataModel.getInstance().resetOriginBasicBeautyData();
            int beautyProgress = BeautyDataModel.getInstance().getBeautyProgress(beautyNames[3]);
            onBrightChanged(beautyProgress);
            mEffectListener.onBeautyOrigin();
        }
    }

    @Override
    public void onShapeOrigin() {
        if (mhBeautyManager == null) return;
        BeautyDataModel.getInstance().resetOriginShapeData();
        int[] currentBeautyDatas = BeautyDataModel.getInstance().getCurrentBeautyMap();
        String[] beautyBasicNames = BeautyDataModel.getInstance().getBeautyBasicNames();
        int[] shapeOriginDatas = Arrays.copyOfRange(currentBeautyDatas, beautyBasicNames.length, currentBeautyDatas.length);
        mhBeautyManager.setAllShapes(shapeOriginDatas);
    }

    @Override
    public void onMeiBaiChanged(int progress) {
        if (mEffectListener != null) {
            BeautyDataModel.getInstance().setBeautyProgress(beautyNames[0], progress);
            mEffectListener.onMeiBaiChanged(progress);
        }
    }

    @Override
    public void onMoPiChanged(int progress) {
        if (mEffectListener != null) {
            BeautyDataModel.getInstance().setBeautyProgress(beautyNames[1], progress);
            mEffectListener.onMoPiChanged(progress);
        }
    }

    @Override
    public void onFengNenChanged(int progress) {
        if (mEffectListener != null) {
            BeautyDataModel.getInstance().setBeautyProgress(beautyNames[2], progress);
            mEffectListener.onFengNenChanged(progress);
        }
    }

    @Override
    public void onBrightChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setBrightness(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[3], progress);
    }

    @Override
    public void onBigEyeChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setBigEye(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[4], progress);
    }

    @Override
    public void onFaceChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setFaceLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[8], progress);
    }

    @Override
    public void onMouseChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setMouseLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[9], progress);
    }

    @Override
    public void onNoseChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setNoseLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[10], progress);
    }

    @Override
    public void onChinChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setChinLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[11], progress);
    }

    @Override
    public void onForeheadChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setForeheadLift(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[12], progress);
    }

    @Override
    public void onFaceShaveChanged(int progress) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setFaceShave(progress);
        BeautyDataModel.getInstance().setBeautyProgress(beautyNames[14], progress);
    }

    @Override
    public void onEyeBrowChanged(int progress) {

    }

    @Override
    public void onEyeLengthChanged(int progress) {

    }

    @Override
    public void onEyeCornerChanged(int progress) {

    }

    @Override
    public void onEyeAlatChanged(int progress) {

    }

    @Override
    public void onLengthenNoseChanged(int progress) {

    }

    @Override
    public void onStickerChanged(String stickerName) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setSticker(stickerName);
        BeautyDataModel.getInstance().setCurrentStickerName(stickerName);
    }

    @Override
    public void onSpeciallyChanged(String speciallyName) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setSpeciallyEffect(speciallyName);
        BeautyDataModel.getInstance().setSpeciallyName(speciallyName);
    }

    @Override
    public void onWaterMarkChanged(WatermarkBean watermarkBean) {
        if (mhBeautyManager == null) return;
        String effectName = watermarkBean.getEffectName();
        if (TextUtils.isEmpty(effectName) || effectName.equals(mContext.getResources().getString(R.string.beauty_origin)) || effectName.equals(mContext.getResources().getString(R.string.filter_no))) {
            mhBeautyManager.setWaterMark(Bitmap.createBitmap(1,1, Bitmap.Config.ALPHA_8), WaterAlignEnum.MHWatermarkAlign_TOP_LEFT);
        }else {
//            int imgSrcSel = watermarkBean.getImgSrcSel();
            String resPath = watermarkBean.getResPath();
            WaterAlignEnum posEnum = watermarkBean.getPos();
            InputStream resIs = null;
            try {
                resIs = mContext.getAssets().open(resPath);
                Bitmap waterBitmap = BitmapFactory.decodeStream(resIs);
                mhBeautyManager.setWaterMark(waterBitmap, posEnum);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (resIs != null)  {
                    try {
                        resIs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        BeautyDataModel.getInstance().setWatermarkBean(watermarkBean);
    }

    @Override
    public void onDistortionChanged(String distortionName) {
        if (mhBeautyManager == null) return;
        mhBeautyManager.setDistortionEffect(distortionName);
        BeautyDataModel.getInstance().setDistortionName(distortionName);
    }

    @Override
    public void onQuickBeautyChanged(QuickBeautyBean beautyBean){}

    @Override
    public void onProgressChanged(View view, int progress) {
        if (mhBeautyManager == null) {
            return;
        }
        String[] stringArray = mContext.getResources().getStringArray(R.array.name_beauty_name_array);
        // TODO: 2019/11/8 进度条监听
        boolean isQuickModeProgress = false;
        if (QuickBeautyEnum.STANDARD_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.ELEGANT_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.EXQUISITE_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.LOVELY_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.NATURAL_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.ONLINE_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.REFINED_BEAUTY.getString(mContext).equals(curShownProgressName) || QuickBeautyEnum.SOLEMN_BEAUTY.getString(mContext).equals(curShownProgressName) ){
            isQuickModeProgress = true;
            BeautyDataModel.getInstance().setQuickProgress(progress);
            HashMap<QuickBeautyShapeEnum, QuickBeautyBean.ElementValue> beautyMap = BeautyDataModel.getInstance().getQuickBeautyDataMap();
            QuickBeautyBean.ElementValue whiteValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_WHITE);
            QuickBeautyBean.ElementValue mopiValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_GRIND);
            QuickBeautyBean.ElementValue hongrunValue = beautyMap.get(QuickBeautyShapeEnum.BEAUTY_TENDER);
            int whiteMaxValue = whiteValue.getMaxValue();
            int whiteMinValue = whiteValue.getMinValue();
            int mopiMaxValue = mopiValue.getMaxValue();
            int mopiMinValue = mopiValue.getMinValue();
            int hongrunMaxValue = hongrunValue.getMaxValue();
            int hongrunMinValue = hongrunValue.getMinValue();
            if (progress > whiteMinValue && progress < whiteMaxValue){
                onMeiBaiChanged(progress);
            }
            if (progress > mopiMinValue && progress < mopiMaxValue){
                onMoPiChanged(progress);
            }
            if (progress > hongrunMinValue && progress < hongrunMaxValue){
                onFengNenChanged(progress);
            }
            onMoPiChanged(mopiValue.getValue());
            onFengNenChanged(hongrunValue.getValue());
            QuickBeautyBean.ElementValue bigEyeValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_BIGEYE);
            QuickBeautyBean.ElementValue eyeBrowValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEBROW);
            QuickBeautyBean.ElementValue eyeLengthValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYELENGTH);
            QuickBeautyBean.ElementValue eyeCornerValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYECORNER);
            QuickBeautyBean.ElementValue faceValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FACE);
            QuickBeautyBean.ElementValue mouseValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_MOUSE);
            QuickBeautyBean.ElementValue noseValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_NOSE);
            QuickBeautyBean.ElementValue chinValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_CHIN);
            QuickBeautyBean.ElementValue foreHeadValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_FOREHEAD);
            QuickBeautyBean.ElementValue lengthValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_LENGTHNOSE);
            QuickBeautyBean.ElementValue shaveFaceValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_SHAVE_FACE);
            QuickBeautyBean.ElementValue eyeAlatValue = beautyMap.get(QuickBeautyShapeEnum.SHAPE_EYEALAT);
//            int[] shapeData = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            if (bigEyeValue != null && progress > bigEyeValue.getMinValue() && progress < bigEyeValue.getMaxValue()) {
//                shapeData[0] = progress;
                onBigEyeChanged(progress);
            }
            if (eyeBrowValue != null && progress > eyeBrowValue.getMinValue() && progress < eyeBrowValue.getMaxValue()) {
//                shapeData[1] = progress;
                onEyeBrowChanged(progress);
            }
            if (eyeLengthValue != null && progress > eyeLengthValue.getMinValue() && progress < eyeLengthValue.getMaxValue()) {
//                shapeData[2] = progress;
                onEyeLengthChanged(progress);
            }
            if (eyeCornerValue != null && progress > eyeCornerValue.getMinValue() && progress < eyeCornerValue.getMaxValue()) {
//                shapeData[3] = progress;
                onEyeCornerChanged(progress);
            }
            if (faceValue != null && progress > faceValue.getMinValue() && progress < faceValue.getMaxValue()) {
//                shapeData[4] = progress;
                onFaceChanged(progress);
            }
            if (mouseValue != null && progress > mouseValue.getMinValue() && progress < mouseValue.getMaxValue()) {
//                shapeData[5] = progress;
                onMouseChanged(progress);
            }
            if (noseValue != null && progress > noseValue.getMinValue() && progress < noseValue.getMaxValue()) {
//                shapeData[6] = progress;
                onNoseChanged(progress);
            }
            if (chinValue != null && progress > chinValue.getMinValue() && progress < chinValue.getMaxValue()) {
//                shapeData[7] = progress;
                onChinChanged(progress);
            }
            if (foreHeadValue != null && progress > foreHeadValue.getMinValue() && progress < foreHeadValue.getMaxValue()) {
//                shapeData[8] = progress;
                onForeheadChanged(progress);
            }
            if (lengthValue != null && progress > lengthValue.getMinValue() && progress < lengthValue.getMaxValue()) {
//                shapeData[9] = progress;
                onLengthenNoseChanged(progress);
            }
            if (shaveFaceValue != null && progress > shaveFaceValue.getMinValue() && progress < shaveFaceValue.getMaxValue()) {
//                shapeData[10] = progress;
                onFaceShaveChanged(progress);
            }
            if (eyeAlatValue != null && progress > eyeAlatValue.getMinValue() && progress < eyeAlatValue.getMaxValue()) {
//                shapeData[11] = progress;
                onEyeAlatChanged(progress);
            }
        }
        if (!isQuickModeProgress) {
            if (BeautyDataModel.getInstance().getIsQuickMode()) {
                //取消一键美颜模式
                QuickBeautyBean quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.ORIGIN_BEAUTY, "", R.mipmap.icon_quick_beauty_origin,null, true);
                BeautyDataModel.getInstance().setQuickBeautyBean(quickBeautyBean);
                beautyShownName[BeautyTypeEnum.ONKEY_TYPE_ENUM.getValue()] = mContext.getResources().getString(R.string.beauty_origin);
                BeautyDataModel.getInstance().setQuickProgress(50);
                //恢复默认美颜数值
                BeautyDataModel.getInstance().resetDefaultBasicBeautyData();
                BeautyDataModel.getInstance().resetDefaultShapeData();
                int[] currentBeautyMap = BeautyDataModel.getInstance().getCurrentBeautyMap();
                onMeiBaiChanged(currentBeautyMap[0]);
                onMoPiChanged(currentBeautyMap[1]);
                onFengNenChanged(currentBeautyMap[2]);
                onBrightChanged(currentBeautyMap[3]);
                String[] beautyBasicNames = BeautyDataModel.getInstance().getBeautyBasicNames();
                int[] shapeArray = Arrays.copyOfRange(currentBeautyMap, beautyBasicNames.length, currentBeautyMap.length);
                mhBeautyManager.setAllShapes(shapeArray);
                setBeautyShape(progress, stringArray);
                beautyPagerAdapter.notifyRvNotifyDataSetChanged();
            }else {
                setBeautyShape(progress, stringArray);
            }
        }
    }

    private void setBeautyShape(int progress, String[] stringArray) {
        if (stringArray[0].equals(curShownProgressName)) {
            onMeiBaiChanged(progress);
        } else if (stringArray[1].equals(curShownProgressName)) {
            onMoPiChanged(progress);
        } else if (stringArray[2].equals(curShownProgressName)) {
            onFengNenChanged(progress);
        } else if (stringArray[3].equals(curShownProgressName)) {
            onBrightChanged(progress);
        } else if (stringArray[4].equals(curShownProgressName)) {
            onBigEyeChanged(progress);
        } else if (stringArray[5].equals(curShownProgressName)) {
            onEyeBrowChanged(progress);
        } else if (stringArray[6].equals(curShownProgressName)) {
            onEyeLengthChanged(progress);
        } else if (stringArray[7].equals(curShownProgressName)) {
            onEyeCornerChanged(progress);
        } else if (stringArray[8].equals(curShownProgressName)) {
            onFaceChanged(progress);
        } else if (stringArray[9].equals(curShownProgressName)) {
            onMouseChanged(progress);
        } else if (stringArray[10].equals(curShownProgressName)) {
            onNoseChanged(progress);
        } else if (stringArray[11].equals(curShownProgressName)) {
            onChinChanged(progress);
        } else if (stringArray[12].equals(curShownProgressName)) {
            onForeheadChanged(progress);
        } else if (stringArray[13].equals(curShownProgressName)) {
            onLengthenNoseChanged(progress);
        } else if (stringArray[14].equals(curShownProgressName)) {
            onFaceShaveChanged(progress); //onFaceChanged->onFaceShaveChanged  修改为削脸
        } else if (stringArray[15].equals(curShownProgressName)) {
            onEyeAlatChanged(progress);
        }
    }

    @Override
    public void onItemClick(BeautyBean bean, int position) {
        BeautyTypeEnum type = bean.getType();
        String effectName = bean.getEffectName();
        curShownProgressName = effectName;
        textSeekBar.setVisibility(View.GONE);
        switch (type) {
            case FILTER_TYPE_ENUM:
                beautyShownName[type.getValue()] = effectName;
                if (bean instanceof FilterBean) {
                    onFilterChanged(((FilterBean)bean).getFilterEnum());
                }
                break;
            case BEAUTY_TYPE_ENUM:
            case SHAPE_TYPE_ENUM:
                beautyShownName[type.getValue()] = effectName;
                if (!effectName.equals(mContext.getResources().getString(R.string.beauty_origin))){
                    textSeekBar.setMaxProgress(type.getValue()==BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()?9:100);
                    if (type.getValue()==BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue() && beautyShownName[type.getValue()].equals(mContext.getResources().getString(R.string.beauty_liangdu))){
                        textSeekBar.setMaxProgress(100);
                    }
                    textSeekBar.setProgress(BeautyDataModel.getInstance().getBeautyProgress(effectName));
                    textSeekBar.setVisibility(View.VISIBLE);
                }else {
                    boolean isQuickMode = BeautyDataModel.getInstance().getIsQuickMode();
                    if (isQuickMode) {
                        QuickBeautyBean quickBeautyBean = new QuickBeautyBean(QuickBeautyEnum.ORIGIN_BEAUTY, "", R.mipmap.icon_quick_beauty_origin,null, true);
                        onQuickBeautyChanged(quickBeautyBean);
                        beautyShownName[BeautyTypeEnum.ONKEY_TYPE_ENUM.getValue()] = mContext.getResources().getString(R.string.beauty_origin);
                    }else {
                        if (type.getValue() == BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()) {
                            onBeautyOrigin();
                        } else {
                            onShapeOrigin();
                        }
                    }
                }
                break;
            case ONKEY_TYPE_ENUM:
                if (!(bean instanceof QuickBeautyBean)) {
                    return;
                }
                beautyShownName[type.getValue()] = effectName;
                onQuickBeautyChanged(((QuickBeautyBean)bean));
                break;
            case SPECIALLY_TYPE_ENUM:   //特效
                beautyShownName[type.getValue()] = effectName;
                onSpeciallyChanged(effectName);
                break;
            case DISTORTION_TYPE_ENUM:  //哈哈镜
                beautyShownName[type.getValue()] = effectName;
                onDistortionChanged(effectName);
                break;
            case WATER_TYPE_ENUM:   //水印
                beautyShownName[type.getValue()] = effectName;
                if (bean instanceof WatermarkBean) {
                    onWaterMarkChanged(((WatermarkBean)bean));
                }
                break;
        }
    }

    protected OnPageChangeListener getPageChangeListener(int type, TabLayout tabs) {
        return new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                int pos = position;
                if (type == SPECIALLYTAB) {
                    pos = position + BeautyTypeEnum.SPECIALLY_TYPE_ENUM.getValue();
                }
                tabShownName[type] = pos;
                String effectName = beautyShownName[tabShownName[type]];
                curShownProgressName = effectName;
                if (type == SPECIALLYTAB|| TextUtils.isEmpty(effectName) || mContext.getResources().getString(R.string.beauty_origin).equals(effectName) || mContext.getResources().getString(R.string.filter_no).equals(effectName)){
                    textSeekBar.setVisibility(View.GONE);
                }else {
                    int beautyProgress = BeautyDataModel.getInstance().getBeautyProgress(curShownProgressName);
                    if (tabShownName[type] == BeautyTypeEnum.SHAPE_TYPE_ENUM.getValue()){
                        textSeekBar.setMaxProgress(100);
                    }else if (tabShownName[type] == BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()){
                        if (beautyShownName[BeautyTypeEnum.BEAUTY_TYPE_ENUM.getValue()].equals(mContext.getResources().getString(R.string.beauty_liangdu))){
                            textSeekBar.setMaxProgress(100);
                        }else {
                            textSeekBar.setMaxProgress(9);
                        }
                    }else if (tabShownName[type] == BeautyTypeEnum.ONKEY_TYPE_ENUM.getValue()){
                        textSeekBar.setMaxProgress(100);
                        beautyProgress = BeautyDataModel.getInstance().getQuickProgress();
                    }
                    textSeekBar.setProgress(beautyProgress);
                    textSeekBar.setVisibility(View.VISIBLE);
                }
                setTabSelected(Objects.requireNonNull(tabs.getTabAt(position)), position, true);
                int count = tabs.getTabCount();
                for (int i = 0; i < count; i++) {
                    if (i != position) {
                        setTabSelected(Objects.requireNonNull(tabs.getTabAt(i)), i, false);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        };
    }


}

