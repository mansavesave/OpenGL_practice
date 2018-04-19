package drawcanvas.test.com.opengl_practice;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Wells.Chen on 2018/4/9.
 */

public class Ball {
    int mProgram;// 自定義渲染管線著色器程序id
    //    static float[] mMMatrix = new float[16];
    int muMVPMatrixHandle;// 總變換矩陣引用
    int maTexCoorHandle;
    int muMMatrixHandle;//位置、旋轉變換矩陣引用
    int muRHandle;// 立方体的半径属性引用
    int maPositionHandle; // 頂點位置屬性引用
    int maColorHandle;
    int maNormalHandle; //頂點法向量屬性引用
    int maLightLocationHandle;//光源位置屬性引用
    int maCameraHandle; //攝像機位置屬性引用
    FloatBuffer mTexCoorBuffer;//頂點紋理坐標數據緩衝


    String mVertexShader;// 頂點著色器
    String mFragmentShader;// 片元著色器

    FloatBuffer mVertexBuffer;// 頂點坐標數據緩衝
    FloatBuffer mColorBuffer;
    FloatBuffer mNormalBuffer;//頂點法向量數據緩衝
    int vCount = 0;
    float yAngle = 0;// 繞y軸旋轉的角度
    float xAngle = 0;// 繞x軸旋轉的角度
    float zAngle = 0;// 繞z軸旋轉的角度
    float radius = 1;

    Context mContext;
    Practice_GLSurfaceView mPractice_GLSurfaceView;
    public final int angleCountsIn360 = 32;

    public Ball(Practice_GLSurfaceView _GLSurfaceView) {
        mPractice_GLSurfaceView = _GLSurfaceView;
        mContext = _GLSurfaceView.getContext();
        initVertexData();
        initShader();
    }

    ArrayList<Float> getBallVertex() {
        ArrayList<Float> vertices_list = new ArrayList<Float>();
        // first circle: z angle is 0, x, y angle is from 0 -> 360. Get 12 point in the 360 degree
        double x, y, z;
        double X_Y_degree_gap = 360 / angleCountsIn360;
        double Z_Y_degree_gap = 360 / angleCountsIn360;

        ArrayList<ArrayList> allCircleLists = new ArrayList<ArrayList>();

        for (int j = 0; j < angleCountsIn360; j++) {
            ArrayList<Float> eachCircleLists = new ArrayList<Float>();
            double Z_Y_degree = j * Z_Y_degree_gap;
            double Z_Y_radian = Math.toRadians(Z_Y_degree);
            for (int i = 0; i < angleCountsIn360; i++) {
                double X_Y_degree = i * X_Y_degree_gap;
                double X_Y_radian = Math.toRadians(X_Y_degree);
//                Log.d("wellsTest", "X_Y_degree:" + X_Y_degree + " X_Y_radian:" + X_Y_radian);

                double eachCircleRadius = radius * Math.cos(Z_Y_radian);

                x = eachCircleRadius * Math.cos(X_Y_radian);
                y = eachCircleRadius * Math.sin(X_Y_radian);
                z = radius * Math.sin(Z_Y_radian);


                eachCircleLists.add((float) x);
                eachCircleLists.add((float) y);
                eachCircleLists.add((float) z);
//                Log.d("wellsTest", "x:" + x + " y:" + y + " z:" + z);
            }
            allCircleLists.add(eachCircleLists);
        }

        for (int i = 0; i < allCircleLists.size(); i++) {
            ArrayList<Float> eachCircleLists = allCircleLists.get(i);
            ArrayList<Float> eachCircleLists_next;
            if ((i + 1) < allCircleLists.size()) {
                eachCircleLists_next = allCircleLists.get(i + 1);
            } else {
                eachCircleLists_next = allCircleLists.get(0);
            }


            for (int j = 0; j < eachCircleLists.size() / 3; j++) {
                vertices_list.add(eachCircleLists_next.get(j * 3));
                vertices_list.add(eachCircleLists_next.get(j * 3 + 1));
                vertices_list.add(eachCircleLists_next.get(j * 3 + 2));

                vertices_list.add(eachCircleLists.get(j * 3));
                vertices_list.add(eachCircleLists.get(j * 3 + 1));
                vertices_list.add(eachCircleLists.get(j * 3 + 2));

                if ((j + 1) < eachCircleLists.size() / 3) {
                    vertices_list.add(eachCircleLists.get((j + 1) * 3));
                    vertices_list.add(eachCircleLists.get((j + 1) * 3 + 1));
                    vertices_list.add(eachCircleLists.get((j + 1) * 3 + 2));
                } else {
                    vertices_list.add(eachCircleLists.get(0));
                    vertices_list.add(eachCircleLists.get(1));
                    vertices_list.add(eachCircleLists.get(2));
                }

                if (j - 1 >= 0) {
                    vertices_list.add(eachCircleLists_next.get(j * 3));
                    vertices_list.add(eachCircleLists_next.get(j * 3 + 1));
                    vertices_list.add(eachCircleLists_next.get(j * 3 + 2));

                    vertices_list.add(eachCircleLists_next.get((j - 1) * 3));
                    vertices_list.add(eachCircleLists_next.get((j - 1) * 3 + 1));
                    vertices_list.add(eachCircleLists_next.get((j - 1) * 3 + 2));


                    vertices_list.add(eachCircleLists.get(j * 3));
                    vertices_list.add(eachCircleLists.get(j * 3 + 1));
                    vertices_list.add(eachCircleLists.get(j * 3 + 2));
                } else {

                    vertices_list.add(eachCircleLists_next.get(j * 3));
                    vertices_list.add(eachCircleLists_next.get(j * 3 + 1));
                    vertices_list.add(eachCircleLists_next.get(j * 3 + 2));

                    vertices_list.add(eachCircleLists_next.get(eachCircleLists_next.size() - 1 - 2));
                    vertices_list.add(eachCircleLists_next.get(eachCircleLists_next.size() - 1 - 1));
                    vertices_list.add(eachCircleLists_next.get(eachCircleLists_next.size() - 1));

                    vertices_list.add(eachCircleLists.get(j * 3));
                    vertices_list.add(eachCircleLists.get(j * 3 + 1));
                    vertices_list.add(eachCircleLists.get(j * 3 + 2));

                }

            }
        }


        return vertices_list;
    }


    public void initVertexData() {
        ArrayList<Float> vertices_list = getBallVertex();
        vCount = vertices_list.size() / 3;
        float[] vertices = new float[vertices_list.size()];
        for (int i = 0; i < vertices_list.size(); i++) {
            vertices[i] = vertices_list.get(i);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float[] colorArray = new float[vCount * 4];
        for (int i = 0; i < vCount; i++) {
            float r = 0, g = 0, b = 0;
            int mod = i % 3;
//            Log.d("wellsTest", "mod:" + mod);
            if (mod == 0) {
                r = 1;
                g = 0;
                b = 0;
            } else if (mod == 1) {
                r = 1;
                g = 0;
                b = 0;
            } else if (mod == 2) {
                r = 0;
                g = 0;
                b = 1;
            }

            int index_start = i * 4;
            colorArray[index_start] = r;
            colorArray[index_start + 1] = g;
            colorArray[index_start + 2] = b;
            colorArray[index_start + 3] = 0;

        }

//        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asFloatBuffer();
//        mColorBuffer.put(colorArray);
//        mColorBuffer.position(0);


        //頂點紋理坐標數據的初始化================begin============================
        ArrayList<Float> texture_list = new ArrayList();
        for (int i = 0; i < vCount; i++) {
            int mod = i % 6;
            float s = 0;
            float t = 0;
            switch (mod) {
                case 0:
                    s = 0;
                    t = 0;
                    break;
                case 1:
                    s = 1;
                    t = 1;
                    break;
                case 2:
                    s = 1;
                    t = 0;
                    break;
                case 3:
                    s = 0;
                    t = 0;
                    break;
                case 4:
                    s = 0;
                    t = 1;
                    break;
                case 5:
                    s = 1;
                    t = 1;
                    break;
                default:
                    break;

            }

            texture_list.add(s);
            texture_list.add(t);
        }

        float[] texCoor = new float[texture_list.size()];
        for (int i = 0; i < texture_list.size(); i++) {
            texCoor[i] = texture_list.get(i);
        }

//        float texCoor[] = new float[]//頂點顏色值數組，每個頂點4個色彩值RGBA
//                {
//                        1.5f, 0,
//                        0, 3,
//                        3, 3
//                };
        //創建頂點紋理坐標數據緩衝
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length * 4);
        cbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTexCoorBuffer = cbb.asFloatBuffer();//轉換為Float型緩衝
        mTexCoorBuffer.put(texCoor);//向緩衝區中放入頂點著色數據
        mTexCoorBuffer.position(0);//設置緩衝區起始位置


        float[] normals = new float[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            normals[i] = vertices[i];
        }
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
    }

    public void initShader() {
        //加載頂點著色器的腳本內容
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_4_tex_light.sh", mContext.getResources());
        //加載片元著色器的腳本內容
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_4_tex_light.sh", mContext.getResources());
        //基於頂點著色器與片元著色器創建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //獲取程序中頂點位置屬性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //獲取程序中頂點顏色屬性引用id
//        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        //獲取程序中總變換矩陣引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");

        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");

        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
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

//        GLES20.glLineWidth(10);
//        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vCount);
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount);
    }


    public void drawSelf(int texId) {
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
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);

        //為畫筆指定頂點位置數據
        GLES20.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mVertexBuffer
        );

        GLES20.glVertexAttribPointer(
                maTexCoorHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer
        );

        GLES20.glVertexAttribPointer
                (
                        maNormalHandle,
                        4,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );


        //允許頂點位置數據數組
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        //繪製
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);

//        GLES20.glLineWidth(10);
//        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vCount);
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount);
    }
}
