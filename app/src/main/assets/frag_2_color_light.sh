precision mediump float;
varying  vec4 aaColor; //接收從頂點著色器過來的參數
varying vec3 vPosition;//接收從頂點著色器過來的頂點位置
varying vec4 vAmbient;//接收從頂點著色器過來的環境光分量
varying vec4 vDiffuse;//接收從頂點著色器過來的散射光分量
varying vec4 vSpecular;//接收從頂點著色器過來的鏡面反射光分量
void main()
{
   gl_FragColor = aaColor;
   //gl_FragColor=aaColor*vAmbient + aaColor*vDiffuse + aaColor*vSpecular;
}