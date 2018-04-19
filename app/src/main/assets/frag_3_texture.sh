precision mediump float;
varying  vec4 aaColor; //接收從頂點著色器過來的參數
varying vec2 vTextureCoord;  //用於傳遞給片元著色器的變量
uniform sampler2D sTexture;//紋理內容數據
void main()                         
{                       
   //gl_FragColor = aaColor;//給此片元顏色值
   gl_FragColor = texture2D(sTexture, vTextureCoord);
}              