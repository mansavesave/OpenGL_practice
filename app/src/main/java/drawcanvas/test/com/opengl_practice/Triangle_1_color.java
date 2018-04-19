package drawcanvas.test.com.opengl_practice;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Wells.Chen on 2018/4/9.
 */

public class Triangle_1_color {
    int mProgram;// 自定義渲染管線著色器程序id
    //    static float[] mMMatrix = new float[16];
    int muMVPMatrixHandle;// 總變換矩陣引用
    int muMMatrixHandle;//位置、旋轉變換矩陣引用
    int muRHandle;// 立方体的半径属性引用
    int maPositionHandle; // 頂點位置屬性引用
    int maColorHandle;
    int maNormalHandle; //頂點法向量屬性引用
    int maLightLocationHandle;//光源位置屬性引用
    int maCameraHandle; //攝像機位置屬性引用


    String mVertexShader;// 頂點著色器
    String mFragmentShader;// 片元著色器

    FloatBuffer mVertexBuffer;// 頂點坐標數據緩衝
    FloatBuffer mColorBuffer;
    FloatBuffer mNormalBuffer;//頂點法向量數據緩衝
    int vCount = 0;
    float yAngle = 0;// 繞y軸旋轉的角度
    float xAngle = 0;// 繞x軸旋轉的角度
    float zAngle = 0;// 繞z軸旋轉的角度
    float r = 1;

    Context mContext;
    Practice_GLSurfaceView mPractice_GLSurfaceView;

    public Triangle_1_color(Practice_GLSurfaceView _GLSurfaceView) {
        mPractice_GLSurfaceView = _GLSurfaceView;
        mContext = _GLSurfaceView.getContext();
        initVertexData();
        initShader();
    }

    public void initVertexData() {
        vCount = 3;
        float[] vertices = new float[vCount * 3];
        vertices[0] = 0;
        vertices[1] = 1;
        vertices[2] = 0;
        vertices[3] = -1;
        vertices[4] = 0;
        vertices[5] = 0;
        vertices[6] = 1;
        vertices[7] = 0;
        vertices[8] = 0;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float[] colorArray = new float[vCount * 4];
        colorArray[0] = 1;
        colorArray[1] = 0;
        colorArray[2] = 0;
        colorArray[3] = 0;

        colorArray[4] = 0;
        colorArray[5] = 1;
        colorArray[6] = 0;
        colorArray[7] = 0;

        colorArray[8] = 0;
        colorArray[9] = 0;
        colorArray[10] = 1;
        colorArray[11] = 0;

        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);

    }

    public void initShader() {
        //加載頂點著色器的腳本內容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_1_color.sh", mContext.getResources());
        //加載片元著色器的腳本內容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_1_color.sh", mContext.getResources());
        //基於頂點著色器與片元著色器創建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //獲取程序中頂點位置屬性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //獲取程序中頂點顏色屬性引用id
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //獲取程序中總變換矩陣引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    }

    public void drawSelf() {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //初始化變換矩陣
        //Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        //設置沿Z軸正向位移1
        //Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        //設置繞y軸旋轉
        //Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
        //設置繞z軸旋轉
        //Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
        //將最終變換矩陣傳入shader程序
//        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        //為畫筆指定頂點位置數據
        GLES20.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );
        //為畫筆指定頂點著色數據
        GLES20.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES20.GL_FLOAT,
                        false,
                        4 * 4,
                        mColorBuffer
                );
        //允許頂點位置數據數組
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);
        //繪製
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
