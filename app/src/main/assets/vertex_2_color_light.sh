uniform mat4 uMVPMatrix; //總變換矩陣
uniform mat4 uMMatrix; 			//變換矩陣
uniform vec3 uLightLocation;		//光源位置
uniform vec3 uCamera;			//攝像機位置
attribute vec3 aPosition;  //頂點位置
attribute vec3 aNormal;    		//法向量
varying vec3 vPosition;			//用於傳遞給片元著色器的頂點位置
varying vec4 vAmbient;			//用於傳遞給片元著色器的環境光最終強度
varying vec4 vDiffuse;			//用於傳遞給片元著色器的散射光最終強度
varying vec4 vSpecular;			//用於傳遞給片元著色器的鏡面光最終強度
attribute vec4 aColor;    //頂點顏色
varying  vec4 aaColor;  //用於傳遞給片元著色器的變量
void pointLight(				//定位光光照计算的方法
  in vec3 normal,				//法向量
  inout vec4 ambient,			//環境光最終強度
  inout vec4 diffuse,			//散射光最終強度
  inout vec4 specular,			//鏡面光最終強度
  in vec3 lightLocation,		//光源位置
  in vec4 lightAmbient,			//環境光強度
  in vec4 lightDiffuse,			//散射光強度
  in vec4 lightSpecular			//鏡面光強度
){
  ambient=lightAmbient;			//直接得出環境光的最終強度
  vec3 normalTarget=aPosition+normal;	//計算變換後的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//對法向量規格化
  //計算從表面點到攝像機的向量
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);
  //計算從表面點到光源位置的向量vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);
  vp=normalize(vp);//格式化vp
  vec3 halfVector=normalize(vp+eye);	//求視線與光線的半向量
  float shininess=50.0;				//粗糙度，越小越光滑
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量與vp的點積與0的最大值
  diffuse=lightDiffuse*nDotViewPosition;				//計算散射光的最終強度
  float nDotViewHalfVector=dot(newNormal,halfVector);	//法線與半向量的點積
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//鏡面反射光強度因子
  specular=lightSpecular*powerFactor;    			//計算鏡面光的最終強度
}


void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根據總變換矩陣計算此次繪製此頂點位置
   vec4 ambientTemp,diffuseTemp,specularTemp;	  //用来接收三个通道最终强度的变量
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,
       vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));

   vAmbient=ambientTemp; 		//將環境光最終強度傳給片元著色器
   vDiffuse=diffuseTemp; 		//將散射光最終強度傳給片元著色器
   vSpecular=specularTemp; 		//將鏡面光最終強度傳給片元著色器
   vPosition = aPosition;  //将顶点的位置传给片元着色器

    aaColor = aColor;//將接收的顏色傳遞給片元著色器
}                      