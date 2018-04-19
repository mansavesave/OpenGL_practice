package drawcanvas.test.com.opengl_practice;

import java.nio.FloatBuffer;

/**
 * Created by Wells.Chen on 2018/4/9.
 */

public class Cylinder {
    int mProgram;// 自定義渲染管線著色器程序id
    int muMVPMatrixHandle;// 總變換矩陣引用
    int muMMatrixHandle;//位置、旋轉變換矩陣引用
    int muRHandle;// 立方体的半径属性引用
    int maPositionHandle; // 頂點位置屬性引用
    int maNormalHandle; //頂點法向量屬性引用
    int maLightLocationHandle;//光源位置屬性引用
    int maCameraHandle; //攝像機位置屬性引用


    String mVertexShader;// 頂點著色器
    String mFragmentShader;// 片元著色器

    FloatBuffer mVertexBuffer;// 頂點坐標數據緩衝
    FloatBuffer mNormalBuffer;//頂點法向量數據緩衝
    int vCount = 0;
    float yAngle = 0;// 繞y軸旋轉的角度
    float xAngle = 0;// 繞x軸旋轉的角度
    float zAngle = 0;// 繞z軸旋轉的角度
    float r = 1;

    public Cylinder(Practice_GLSurfaceView _GLSurfaceView) {
        initVertexData();
        initShader(_GLSurfaceView);
    }

    public void initVertexData() {

    }

    public void initShader(Practice_GLSurfaceView _GLSurfaceView) {

    }

    public void drawSelf() {

    }
}
