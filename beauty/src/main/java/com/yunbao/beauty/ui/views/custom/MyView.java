package com.yunbao.beauty.ui.views.custom;//package com.yunbao.beauty.ui.views.custom;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//
//public class MyView extends View {
//
//    private Paint paint;
//    private Canvas canv;
//
//    public static final  int []mapping ={11,10,9,8,7,6,5,102,66,82,81,80,57,78,77,76,0,97,95,96,100,101,98,99,49,56,18,68,16,17,14,15,13,19,84,29,79,28,24,73,70,75,74,21,23,22,69,88,89,46,87,86,94,1,53,59,67,12,27,104,85,20,47,51,62,83,58,60,44,54,71,48,34,3,55,41,43,52,35,91,90,92,31,93,45,37,39,38,26,33,50,4,30,32,64,65,61,40,36,25,42,2,103,63,72,105};
//
//
//    private Face face;
//
//    public Face getFace() {
//        return face;
//    }
//
//    public void setFace(Face face) {
//        this.face = face;
//        invalidate();
//    }
//
//    private String text;
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    private  int textureWidth;
//    private  int textureHeight;
//
//    public MyView(Context context) {
//        super(context);
//        init();
//    }
//
//
//    public MyView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    public int getTextureWidth() {
//        return textureWidth;
//    }
//
//    public void setTextureWidth(int textureWidth) {
//        this.textureWidth = textureWidth;
//    }
//
//    public int getTextureHeight() {
//        return textureHeight;
//    }
//
//    public void setTextureHeight(int textureHeight) {
//        this.textureHeight = textureHeight;
//    }
//
//    private void init(){
//
//        text = "";
//        paint = new Paint();
//        paint.setColor(Color.RED);
//        // 设置抗锯齿
//        paint.setAntiAlias(true);
//        // 设置线宽
//        paint.setStrokeWidth(3);
//        // 设置文字大小
//        paint.setTextSize(30);
//        // 设置非填充
//        paint.setStyle(Paint.Style.STROKE);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        /*
//         *  Draw your rectangle
//         */
//
//        //super.onDraw(canvas);
//
//        // 设置颜色
//
////        for (int i=0;i<106;i++)
////        {
////            canvas.drawText(String.valueOf(i),i*i,i*i,paint);
////        }
//
//        //canvas.drawRect(new Rect(150, 150, 500, 500), paint);
//
//        if(face!=null)
//        {
//
//            int height = this.getHeight();
//            int width = this.getWidth();
//
////            for (int i = 0; i < 106; i++) {
////                float rela_x = (float) face.landmarks[i * 2] / 544;
////                float rela_y = (float) face.landmarks[i * 2 + 1] / 960;
////                int x = (int) (rela_x * width);
////                int y = (int) (rela_y * height);
////
////                canvas.drawCircle(x, y, 5, paint);
////            }
//
//            for(int i=0;i<106;i++)
//            {
//                 int x =   face.landmarks[2*mapping[i]];
//                 int y =   face.landmarks[2*mapping[i]+1];
//
////                 float pointX = textureWidth - x;
//                 float pointX = x;
//                 float pointY = y;
//
//                 pointX = pointX*this.getWidth()/textureWidth;
//                 pointY = pointY*this.getHeight()/textureHeight;
//
//
//                 canvas.drawText(String.valueOf(i),pointX,pointY,paint);
////
//            }
//
//        }
//
//
//    }
//
//    private  static  final  float ratio=1080/1848;
//
//    private  static  final  float screenWidth=1080;
//
//    private  static  final  float screenHeight=1848;
//
//    /**
//     * Convert x to openGL
//     *
//     * @param x
//     *            Screen x offset top left
//     * @return Screen x offset top left in OpenGL
//     */
//    public static float toGLX(float x) {
//        return -1.0f * ratio + toGLWidth(x);
//    }
//
//    /**
//     * Convert y to openGL y
//     *
//     * @param y
//     *            Screen y offset top left
//     * @return Screen y offset top left in OpenGL
//     */
//    public static float toGLY(float y) {
//        return 1.0f - toGLHeight(y);
//    }
//
//    /**
//     * Convert width to openGL width
//     *
//     * @param width
//     * @return Width in openGL
//     */
//    public static float toGLWidth(float width) {
//        return 2.0f * (width / screenWidth) * ratio;
//    }
//
//    /**
//     * Convert height to openGL height
//     *
//     * @param height
//     * @return Height in openGL
//     */
//    public static float toGLHeight(float height) {
//        return 2.0f * (height / screenHeight);
//    }
//
//    /**
//     * Convert x to screen x
//     *
//     * @param glX
//     *            openGL x
//     * @return screen x
//     */
//    public static float toScreenX(float glX) {
//        return toScreenWidth(glX - (-1 * ratio));
//    }
//
//    /**
//     * Convert y to screent y
//     *
//     * @param glY
//     *            openGL y
//     * @return screen y
//     */
//    public static float toScreenY(float glY) {
//        return toScreenHeight(1.0f - glY);
//    }
//
//    /**
//     * Convert glWidth to screen width
//     *
//     * @param glWidth
//     * @return Width in screen
//     */
//    public static float toScreenWidth(float glWidth) {
//        return (glWidth * screenWidth) / (2.0f * ratio);
//    }
//
//    /**
//     * Convert height to screen height
//     *
//     * @param glHeight
//     * @return Height in screen
//     */
//    public static float toScreenHeight(float glHeight) {
//        return (glHeight * screenHeight) / 2.0f;
//    }
//
//
//}
