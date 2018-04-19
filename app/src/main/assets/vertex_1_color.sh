uniform mat4 uMVPMatrix; //總變換矩陣
attribute vec3 aPosition;  //頂點位置
attribute vec4 aColor;    //頂點顏色
varying  vec4 aaColor;  //用於傳遞給片元著色器的變量
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根據總變換矩陣計算此次繪製此頂點位置
   aaColor = aColor;//將接收的顏色傳遞給片元著色器
   gl_PointSize = 10.0;
}                      