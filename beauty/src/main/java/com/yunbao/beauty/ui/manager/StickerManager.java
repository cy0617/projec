package com.yunbao.beauty.ui.manager;

import android.content.Context;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.meihu.beautylibrary.MHSDK;
import com.meihu.beautylibrary.constant.Constants;
import com.meihu.beautylibrary.filter.glfilter.resource.ResourceHelper;
import com.meihu.beautylibrary.manager.SharedPreferencesManager;
import com.meihu.beautylibrary.manager.StickerDownLoader;
import com.meihu.beautylibrary.utils.DownloadUtil;
import com.meihu.beautylibrary.utils.FileUtil;
import com.meihu.beautylibrary.utils.ToastUtil;
import com.meihu.beautylibrary.utils.WordUtil;
import com.meihu.kalle.simple.SimpleCallback;
import com.meihu.kalle.simple.SimpleResponse;
import com.yunbao.beauty.R;
import com.yunbao.beauty.ui.adapter.StickerAdapter;
import com.yunbao.beauty.ui.bean.StickerBeautyBean;
import com.yunbao.beauty.ui.bean.StickerCategaryBean;
import com.yunbao.beauty.ui.bean.StickerServiceBean;
import com.yunbao.beauty.ui.interfaces.IBeautyViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class StickerManager {

    private static boolean flag = false;
    private String mCurStickerName;

    private StickerManager() {
        synchronized (StickerManager.class) {
            if (!flag) {
                mCurStickerName = MHSDK.getInstance().getAppContext().getResources().getString(R.string.filter_no);
                flag = true;
            } else {
                throw new RuntimeException("StickerManager instance has created");
            }
        }
    }

    private static class InstanceHolder {
        private static StickerManager instance = new StickerManager();
    }

    public static StickerManager getInstance() {
        return InstanceHolder.instance;
    }

    public void getStickerCategaryList(IBeautyViewHolder beautyViewHolderNew) {
        getStickerCategary(beautyViewHolderNew);
    }

    @Keep
    public void getStickerCategary(SimpleCallback<String> callback) {
        StickerDownLoader.getStickerCategary(callback);
    }

    @Keep
    private void getStickerList(String id, SimpleCallback<String> callback) {
        StickerDownLoader.getStickerList(id, callback);
    }

    @Keep
    public void downloadSticker(Context mContext, String stickerUrl, String name, DownloadUtil.Callback callback) {
        StickerDownLoader.downloadSticker(name, stickerUrl, callback);
    }

    private void getStickerCategary(IBeautyViewHolder beautyViewHolder) {
        WeakReference<IBeautyViewHolder> beautyViewHolderNewWeakReference = new WeakReference<>(beautyViewHolder);
        StickerDownLoader.getStickerCategary(new SimpleCallback<String>() {
            @Override
            public void onResponse(SimpleResponse<String, String> response) {
                IBeautyViewHolder beautyViewHolder = beautyViewHolderNewWeakReference.get();
                if (beautyViewHolder == null /*|| pagerAdapter == null*/) return;
                if (!response.isSucceed()) {
                    onException(new Exception("request failed:" + response.failed()));
                    return;
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.succeed());
                    if (!jsonObject.has("code")) {
                        onException(new Exception("response error"));
                        return;
                    }
                    int code = jsonObject.getInt("code");
                    if (code != 0) {
                        onException(new Exception("request failed"));
                        return;
                    }
                    if (!jsonObject.has("list")) {
                        onException(new Exception("list is Empty"));
                        return;
                    }
                    List<StickerCategaryBean> stickerCategaryBeans = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = "", name = "";
                        JSONObject beanObject = jsonArray.getJSONObject(i);
                        if (beanObject == null) break;
                        if (beanObject.has("id")) {
                            id = beanObject.getString("id");
                        }
                        if (beanObject.has("name")) {
                            name = beanObject.getString("name");
                            if (name.equals("推荐")) {
                                name = MHSDK.getInstance().getAppContext().getResources().getString(com.meihu.beautylibrary.R.string.sticker_clasic);
                            } else if (name.equals("高级")) {
                                name = MHSDK.getInstance().getAppContext().getResources().getString(com.meihu.beautylibrary.R.string.sticker_update);
                            } else if (name.equals("简约")) {
                                name = MHSDK.getInstance().getAppContext().getResources().getString(com.meihu.beautylibrary.R.string.sticker_dim);
                            } else if (name.equals("萌物")) {
                                name = MHSDK.getInstance().getAppContext().getResources().getString(com.meihu.beautylibrary.R.string.sticker_sticker);
                            } else if (name.equals("少女")) {
                                name = MHSDK.getInstance().getAppContext().getResources().getString(com.meihu.beautylibrary.R.string.sticker_shyly);
                            }
                        }
                        StickerCategaryBean stickerCategaryBean = new StickerCategaryBean(id, name);
                        stickerCategaryBeans.add(stickerCategaryBean);
                    }
//                            pagerAdapter.setTitlesData(stickerCategaryBeans);
                    beautyViewHolder.setStickerCategaryData(stickerCategaryBeans);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getStickerThumbList(StickerAdapter stickerAdapter, String id) {
//        getStickerList(stickerAdapter, id);
        StickerDownLoader.getNewStickerList(id, new SimpleCallback<String>() {
            WeakReference<StickerAdapter> tieZhiAdapterWeakReference = new WeakReference<>(stickerAdapter);
            @Override
            public void onResponse(SimpleResponse<String, String> response) {
                if (tieZhiAdapterWeakReference.get() == null) return;
                if (!response.isSucceed()) {
                    onException(new Exception("request failed:" + response.failed()));
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.succeed());
                    if (jsonObject.has("code")) {
                        int code = jsonObject.getInt("code");
                        if (code != 0) {
                            onException(new Exception("request failed"));
                            return;
                        }
                        if (!jsonObject.has("list")) {
                            onException(new Exception("list is Empty"));
                            return;
                        }
                        List<StickerServiceBean> stickerServiceBeanList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String name = "", category = "", thumb = "", resource = "", uptime = "";
                            StickerBeautyBean stickerBeautyBean = null;
                            boolean is_downloaded = false;
                            int type = 1;
                            JSONObject beanObject = jsonArray.getJSONObject(i);
                            if (beanObject == null) break;
                            if (beanObject.has("name")) {
                                name = beanObject.getString("name");
                            }
                            if (beanObject.has("category")) {
                                category = beanObject.getString("category");
                            }
                            if (beanObject.has("thumb")) {
                                thumb = beanObject.getString("thumb");
                            }
                            if (beanObject.has("resource")) {
                                resource = beanObject.getString("resource");
                            }
                            if (beanObject.has("uptime  ")) {
                                uptime = beanObject.getString("uptime");
                            }
//                                    if (beanObject.has("is_downloaded")){
//                                        is_downloaded = beanObject.getBoolean("is_downloaded");
//                                    }
                            if (beanObject.has("type")) {
                                type = beanObject.getInt("type");
                            }
                            if (beanObject.has("beauty")) {
                                String beanObjectString = beanObject.getString("beauty");
                                stickerBeautyBean = decodeBeautyData(beanObjectString);
                            }
                            StickerServiceBean stickerServiceBean = new StickerServiceBean(name, category, thumb, resource, uptime, type);
                            if (TextUtils.isEmpty(name)) {
                                stickerServiceBean.setIs_downloaded(true);
                                stickerServiceBean.setName(MHSDK.getInstance().getAppContext().getResources().getString(R.string.filter_no));
                            }
                            stickerServiceBean.setStickerBeautyData(stickerBeautyBean);
                            boolean fileExist = isStickerFileExist(MHSDK.getInstance().getAppContext(), name);
                            SharedPreferencesManager sharedPreferencesManager = MHSDK.getInstance().getSharedPreferencesManager();
                            if (fileExist) {
                                String upTimeLast = sharedPreferencesManager.getString(name, "");
                                if (upTimeLast != null && upTimeLast.equals(uptime)) {
                                    stickerServiceBean.setIs_downloaded(true);
                                    String zipPath = Constants.VIDEO_TIE_ZHI_RESOURCE_ZIP_PATH + File.separator + name;
                                    ResourceHelper.addStickerSource(name, zipPath, name, thumb);
                                }
                            }
                            sharedPreferencesManager.commitString(name, uptime);
                            stickerServiceBeanList.add(stickerServiceBean);
                        }
//                                StickerServiceBean tiezhiFirstBean = stickerServiceBeanList.get(0);
//                                if (tiezhiFirstBean != null) {
//                                    tiezhiFirstBean.setChecked(true);
//                                    tiezhiFirstBean.setIs_downloaded(true);
//                                }
                        if (tieZhiAdapterWeakReference.get() != null) {
                            tieZhiAdapterWeakReference.get().setData(stickerServiceBeanList);
                        }
                        return;
                    }
                    onException(new Exception("sticker not Exist"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public boolean isStickerFileExist(Context context, String name) {
                return StickerDownLoader.getStickerIsExist(context, name);
            }

            private StickerBeautyBean decodeBeautyData(String beanObjectString) {
                StickerBeautyBean stickerBeautyBean = new StickerBeautyBean();
                try {
                    JSONObject jsonObject = new JSONObject(beanObjectString);
                    if (jsonObject.has("skin_whiting")) {
                        String skin_whiting = jsonObject.getString("skin_whiting");
                        stickerBeautyBean.setSkin_whiting(skin_whiting);
                    }
                    if (jsonObject.has("skin_smooth")) {
                        String skin_whiting = jsonObject.getString("skin_smooth");
                        stickerBeautyBean.setSkin_smooth(skin_whiting);
                    }
                    if (jsonObject.has("skin_tenderness")) {
                        String skin_whiting = jsonObject.getString("skin_tenderness");
                        stickerBeautyBean.setSkin_tenderness(skin_whiting);
                    }
                    if (jsonObject.has("skin_saturation")) {
                        String skin_whiting = jsonObject.getString("skin_saturation");
                        stickerBeautyBean.setSkin_saturation(skin_whiting);
                    }
                    if (jsonObject.has("eye_brow")) {
                        String skin_whiting = jsonObject.getString("eye_brow");
                        stickerBeautyBean.setEye_brow(skin_whiting);
                    }
                    if (jsonObject.has("big_eye")) {
                        String skin_whiting = jsonObject.getString("big_eye");
                        stickerBeautyBean.setBig_eye(skin_whiting);
                    }
                    if (jsonObject.has("eye_length")) {
                        String skin_whiting = jsonObject.getString("eye_length");
                        stickerBeautyBean.setEye_length(skin_whiting);
                    }
                    if (jsonObject.has("eye_corner")) {
                        String skin_whiting = jsonObject.getString("eye_corner");
                        stickerBeautyBean.setEye_corner(skin_whiting);
                    }
                    if (jsonObject.has("eye_alat")) {
                        String skin_whiting = jsonObject.getString("eye_alat");
                        stickerBeautyBean.setEye_alat(skin_whiting);
                    }
                    if (jsonObject.has("face_lift")) {
                        String skin_whiting = jsonObject.getString("face_lift");
                        stickerBeautyBean.setFace_lift(skin_whiting);
                    }
                    if (jsonObject.has("face_shave")) {
                        String skin_whiting = jsonObject.getString("face_shave");
                        stickerBeautyBean.setFace_shave(skin_whiting);
                    }
                    if (jsonObject.has("mouse_lift")) {
                        String skin_whiting = jsonObject.getString("mouse_lift");
                        stickerBeautyBean.setMouse_lift(skin_whiting);
                    }
                    if (jsonObject.has("nose_lift")) {
                        String skin_whiting = jsonObject.getString("nose_lift");
                        stickerBeautyBean.setNose_lift(skin_whiting);
                    }
                    if (jsonObject.has("chin_lift")) {
                        String skin_whiting = jsonObject.getString("chin_lift");
                        stickerBeautyBean.setChin_lift(skin_whiting);
                    }
                    if (jsonObject.has("forehead_lift")) {
                        String skin_whiting = jsonObject.getString("forehead_lift");
                        stickerBeautyBean.setForehead_lift(skin_whiting);
                    }
                    if (jsonObject.has("lengthen_noseLift")) {
                        String skin_whiting = jsonObject.getString("lengthen_noseLift");
                        stickerBeautyBean.setLengthen_noseLift(skin_whiting);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    stickerBeautyBean = null;
                }
                return stickerBeautyBean;
            }

            @Override
            public void onException(Exception e) {
                super.onException(e);
            }
        });
    }

    public void downloadSticker(StickerAdapter stickerAdapter, Context mContext, StickerServiceBean mBean, int mPosition, View v, String name) {
        downloadStickerZip(stickerAdapter, mContext, mBean, mPosition, v, name);
    }

    private void downloadStickerZip(StickerAdapter stickerAdapter, Context mContext, StickerServiceBean mBean, int mPosition, View v, String name) {
        WeakReference<StickerAdapter> tieZhiAdapterWeakReference = new WeakReference<>(stickerAdapter);
        StickerDownLoader.downloadSticker(name, mBean.getResource(), new DownloadUtil.Callback() {
            @Override
            public void onSuccess(File file) {
                StickerAdapter stickerAdapterInstance = tieZhiAdapterWeakReference.get();
                if (stickerAdapterInstance == null) return;
                if (file == null) {
                    onError(new Exception("file is null"));
                    return;
                }
                File targetDir = new File(ResourceHelper.getStickerResourceDirectory(mContext));
                try {
                    //解压到贴纸目录
                    FileUtil.unzip(file, targetDir);
                    mBean.setIs_downloaded(true);
                    ResourceHelper.addStickerSource(name, file.getAbsolutePath(), name, mBean.getThumb());
                } catch (Exception e) {
                    ToastUtil.show(mContext, WordUtil.getString(mContext, com.meihu.beautylibrary.R.string.tiezhi_download_failed));
                } finally {
                    mBean.setDownLoading(false);
                    file.delete();
                    if (stickerAdapterInstance.touchPos == mPosition && mBean.isIs_downloaded()) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        if (parent != null && parent.getVisibility() == View.VISIBLE) {
                            mBean.setChecked(true);
                            stickerAdapterInstance.mList.get(stickerAdapterInstance.mCheckedPosition).setChecked(false);
                            if (stickerAdapterInstance.mOnItemClickListener != null) {
                                stickerAdapterInstance.mOnItemClickListener.onItemClick(mBean, mPosition);
                            }
                            stickerAdapterInstance.notifyItemChanged(stickerAdapterInstance.mCheckedPosition, Constants.PAYLOAD);
                            stickerAdapterInstance.mCheckedPosition = mPosition;
                        }
                    }
                    stickerAdapterInstance.notifyItemChanged(mPosition, Constants.PAYLOAD);
                    stickerAdapterInstance.mLoadingTaskMap.remove(mPosition);
                }
            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(Throwable e) {
                StickerAdapter stickerAdapterInstance = tieZhiAdapterWeakReference.get();
                if (stickerAdapterInstance == null) return;
                ToastUtil.show(mContext, WordUtil.getString(mContext, com.meihu.beautylibrary.R.string.tiezhi_download_failed));
                mBean.setDownLoading(false);
                stickerAdapterInstance.notifyItemChanged(mPosition, Constants.PAYLOAD);
                stickerAdapterInstance.mLoadingTaskMap.remove(mPosition);
            }
        });
    }


    public synchronized String getmCurStickerName() {
        return mCurStickerName;
    }

    public synchronized void setmCurStickerName(String mCurStickerName) {
        this.mCurStickerName = mCurStickerName;
    }

    public void release() {
        mCurStickerName = "";
    }
}
