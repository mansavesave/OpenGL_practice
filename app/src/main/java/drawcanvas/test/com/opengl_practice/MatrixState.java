package drawcanvas.test.com.opengl_practice;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//存儲系統矩陣狀態的類
public class MatrixState {
    private static float[] mProjMatrix = new float[16];//矩陣投影用
    private static float[] mVMatrix = new float[16];//攝像機位置朝向9參數矩陣
    private static float[] currMatrix;//當前變換矩陣
    public static float[] lightLocation = new float[]{0, 0, 0};//定位光光源位置
    public static FloatBuffer lightPositionFB;
    public static FloatBuffer cameraFB;
    //保護變換矩陣的棧
    static float[][] mStack = new float[10][16];
    static int stackTop = -1;

    public static void setInitStack()//獲取不變換初始矩陣
    {
        currMatrix = new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix()//保護變換矩陣
    {
        stackTop++;
        for (int i = 0; i < 16; i++) {
            mStack[stackTop][i] = currMatrix[i];
        }
    }

    public static void popMatrix()//恢復變換矩陣
    {
        for (int i = 0; i < 16; i++) {
            currMatrix[i] = mStack[stackTop][i];
        }
        stackTop--;
    }

    public static void translate(float x, float y, float z)//設置沿XYZ軸移動
    {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z)//設置繞xyz軸移動
    {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }

    //獲取投影矩陣
    public static float[] getProjMatrix()
    {
        return mProjMatrix;
    }

    //獲取攝像機朝向的矩陣
    public static float[] getCaMatrix()
    {
        return mVMatrix;
    }

    //設置攝像機
    static ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
    static float[] cameraLocation = new float[3];//攝像機位置

    public static void setCamera
            (
                    float cx,    //攝像機位置x
                    float cy,   //攝像機位置y
                    float cz,   //攝像機位置z
                    float tx,   //攝像機目标点x
                    float ty,   //攝像機目标点y
                    float tz,   //攝像機目标点z
                    float upx,  //攝像機UP向量X分量
                    float upy,  //攝像機UP向量Y分量
                    float upz   //攝像機UP向量Z分量
            ) {
        Matrix.setLookAtM
                (
                        mVMatrix,
                        0,
                        cx,
                        cy,
                        cz,
                        tx,
                        ty,
                        tz,
                        upx,
                        upy,
                        upz
                );

        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;

        llbb.clear();
        llbb.order(ByteOrder.nativeOrder());//設置字節順序
        cameraFB = llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }

    //設置正交投影參數
    public static void setProjectOrtho
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距離
            float far       //far面距離
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //設置透視投影參數
    public static void setProjectFrustum
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距離
            float far        //far面距離
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //獲取具體物體的總變換矩陣
    static float[] mMVPMatrix = new float[16];

    public static float[] getFinalMatrix() {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    //獲取具體物體的變換矩陣
    public static float[] getMMatrix() {
        return currMatrix;
    }

    //設置燈光位置的方法
    static ByteBuffer llbbL = ByteBuffer.allocateDirect(3 * 4);

    public static void setLightLocation(float x, float y, float z) {
        llbbL.clear();

        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;

        llbbL.order(ByteOrder.nativeOrder());//設置字節順序
        lightPositionFB = llbbL.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }
}
