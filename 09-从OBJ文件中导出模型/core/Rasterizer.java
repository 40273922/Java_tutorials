package core;

//只用于三角形的渲染
public class Rasterizer {

  	//顶点缓冲对象数组，设定最多可以处理1000个顶点缓冲对象
  	public static VertexBufferObject[] VBOs = new VertexBufferObject[1000];
  	
  	//记载每一频加载的VBO个数
  	public static int numberOfVBOs;
  	
  	//用来处理着色的线程1
  	public static Shader shader1;
  	
  	//用来处理着色的线程2
  	public static Shader shader2;
  	
  	//用来处理着色的线程3
  	public static Shader shader3;
  	
  //用来处理着色的线程3
  	public static Shader shader4;
  	
    //初始化光栅渲染器
  	public static void init(){
  		
		//初始化着色器线程1并让线程运行起来
		shader1 = new Shader(MainThread.screen, "shader1");
		shader1.start();
		
		//初始化着色器线程2并让线程运行起来
		shader2 = new Shader(null, "shader2");
		shader2.start();
		
		//初始化着色器线程3并让线程运行起来
		shader3 = new Shader(null, "shader3");
		shader3.start();
		
		//初始化着色器线程4并让线程运行起来
		shader4 = new Shader(null, "shader4");
		shader4.start();
	
	
  	}
  	
  	//在每一频的开始，把渲染器上一频的信息清除
  	public static void prepare() {
 
  		numberOfVBOs = 0;
  	}
  	
  	//加载一个VBO
  	public static void addVBO(VertexBufferObject VBO) {
  		VBOs[numberOfVBOs] = VBO;
  		numberOfVBOs++;
  	}
  	
    //渲染器的入口
  	public static void renderScene(){
  		
  		//把场景平均分配给着色器进行渲染
  		shader1.VBOsStart = 0;
  		shader1.VBOsEnd = 1;
  		
		synchronized(shader1) {      //让着色器1开始工作
			shader1.notify();
			shader1.isWorking = true;
		}
		
		shader2.VBOsStart = 1;
  		shader2.VBOsEnd = 2;
  		
		synchronized(shader2) {        //让着色器2开始工作
			shader2.notify();
			shader2.isWorking = true;
		}
		
		shader3.VBOsStart = 2;
  		shader3.VBOsEnd = 3;
  		
		synchronized(shader3) {        //让着色器3开始工作
			shader3.notify();
			shader3.isWorking = true;
		}
		
		shader4.VBOsStart = 3;
  		shader4.VBOsEnd = 4;
  		
		synchronized(shader4) {        //让着色器4开始工作
			shader4.notify();
			shader4.isWorking = true;
		}
	
		//等着色器1完成渲染
		synchronized(shader1.myLock) {
			while(shader1.isWorking){
				try {
					shader1.myLock.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		//等着色器2完成渲染
		synchronized(shader2.myLock) {
			while(shader2.isWorking){
				try {
					shader2.myLock.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		//等着色器3完成渲染
		synchronized(shader3.myLock) {
			while(shader3.isWorking){
				try {
					shader3.myLock.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		//等着色器4完成渲染
		synchronized(shader4.myLock) {
			while(shader4.isWorking){
				try {
					shader4.myLock.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		MainThread.triangleCount = shader1.triangleCount + shader2.triangleCount + shader3.triangleCount + shader4.triangleCount;
  	}
}