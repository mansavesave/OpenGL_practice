precision mediump float;
varying  vec4 aaColor; //接收從頂點著色器過來的參數
void main()                         
{                       
   gl_FragColor = aaColor;//給此片元顏色值
}              