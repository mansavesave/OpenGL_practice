package drawcanvas.test.com.opengl_practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Wells.Chen on 2018/4/9.
 */

public class Practice_GLSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例
    private SceneRenderer mRenderer;//場景渲染器

    private float mPreviousY;//上次的觸控位置Y坐標
    private float mPreviousX;//上次的觸控位置X坐標

    float mZAngle = 0;
    float mXAngle = 0;

    public int mTextureId;

    public Practice_GLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //設置使用OPENGL ES2.0
        mRenderer = new SceneRenderer();    //創建場景渲染器
        setRenderer(mRenderer);                //設置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設置渲染模式為主動渲染
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mZAngle += dx * TOUCH_SCALE_FACTOR;
                mXAngle += dy * TOUCH_SCALE_FACTOR;
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        //        Cylinder cylinder;
        //Triangle_1_color triangle1color;
//        Triangle_2_color_light mTriangle_2_color_light;
//        Triangle_3_texture mTriangle_3_texture;
//        Ball_line mBall_line;
        Ball mBall;

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // 設置屏幕背景色的RGBA

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);// 打開深度檢測
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            MatrixState.setInitStack();

            mTextureId = initTexture(R.drawable.android_robot0);
            // 創建模型物件
//            cylinder = new Cylinder(Practice_GLSurfaceView.this);
//            triangle1color = new Triangle_1_color(Practice_GLSurfaceView.this);
//            mTriangle_3_texture = new Triangle_3_texture(Practice_GLSurfaceView.this);
            mBall = new Ball(Practice_GLSurfaceView.this);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            //設置視窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //計算GLSurfaceView的寬高比
            float ratio = (float) width / height;
            //設置透視投影
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 50);

            //調用此方法產生攝像機9參數位置矩陣
            MatrixState.setCamera(0, 0, 6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            MatrixState.setLightLocation(10, 0, 10);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            MatrixState.pushMatrix();
//            MatrixState.translate(0, 0, 2);
            MatrixState.rotate(mZAngle, 0, 0, 1);
            MatrixState.rotate(mXAngle, 1, 0, 0);
            MatrixState.scale(3, 3, 3);
//            mTriangle_3_texture.drawSelf(mTextureId);
            mBall.drawSelf(mTextureId);

            MatrixState.popMatrix();
        }
    }

    public int initTexture(int drawableId)//textureId
    {
        //生成紋理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(
                1,          //產生的紋理id的數量
                textures,   //紋理id的數組
                0           //偏移量
        );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        //通過輸入流加載圖片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通過輸入流加載圖片===============end=====================

        //實際加載紋理
        GLUtils.texImage2D(
                GLES20.GL_TEXTURE_2D,   //紋理類型，在OpenGL ES中必須為GL10.GL_TEXTURE_2D
                0,                      //紋理的層次，0表示基本圖像層，可以理解為直接貼圖
                bitmapTmp,              //紋理圖像
                0                      //紋理邊框尺寸
        );
        bitmapTmp.recycle();          //紋理加載成功後釋放圖片

        return textureId;
    }
}
