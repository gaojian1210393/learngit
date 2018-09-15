package com.example.vlposition;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private final static CountDownLatch CD1 = new CountDownLatch(3);

	private SensorManager sensorManager;
	private ImageView imageview;
	private Button takephoto;
	private Button chuli;
	private Button end;
	private Button clear;
	private TextView shuju;
	private TextView areashuju;
	private TextView directionshuju;
	private TextView resultshuju;
	private TextView EastView;
	private TextView WestView;
	private TextView SouthView;
	private TextView NorthView;
	private EditText EditEast;
	private EditText EditWest;
	private EditText EditSouth;
	private EditText EditNorth;
	static String ct = "";//定义一个字符串
	static StringBuffer sb=new StringBuffer();
	 String imgPath;
	private String mPath;
	
//	int hangbg,hangend,liebg,lieend,liemax,hangmax;
	int ctlie1,ctlie2;
	int centerx,centery;
    int  mianji,zhijing;
    int liebg,lieend,hangbg,hangend;
	String arearesult;
	String directionresult;
	String lastresult;
	 float degree;
	 float scaleWidth;      //图片缩放比例  
	 float scaleHeight;
	 int East,West,South,North;
	 int []roy=new int[1440];
	 int []secyq=new int[1440];
	 
	 int realzhijing;
	 int reallightnumber;
	 
	Bitmap cameraBitmap;
	Bitmap newbit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        shuju =(TextView)findViewById(R.id.shuju);
        areashuju =(TextView)findViewById(R.id.areashuju);
        directionshuju =(TextView)findViewById(R.id.directionshuju);
        resultshuju =(TextView)findViewById(R.id.resultshuju);
        
        EastView=(TextView)findViewById(R.id.East);
        WestView=(TextView)findViewById(R.id.West);
        SouthView=(TextView)findViewById(R.id.South);
        NorthView=(TextView)findViewById(R.id.North);
        
        EditEast=(EditText)findViewById(R.id.EditEast);
        EditWest=(EditText)findViewById(R.id.EditWest);
        EditSouth=(EditText)findViewById(R.id.EditSouth);
        EditNorth=(EditText)findViewById(R.id.EditNorth);
        
        
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        
        takephoto=(Button) findViewById (R.id.takephoto);
        takephoto.setOnClickListener(new OnClickListener(){

		

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				
				 imgPath =  Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tencent/MicroMsg/WeiXin/"   ;                                   //"/sdcard/test/img.jpg";

				//必须确保文件夹路径存在，否则拍照后无法完成回调

				File vFile = new File(imgPath,getPhotoFileName());
				
				//要生成总的路径！！！
				 mPath = vFile.getAbsolutePath();

				if(!vFile.exists())

				{
				File vDirPath = vFile.getParentFile(); //new File(vFile.getParent());
				vDirPath.mkdirs();
				}

				Uri uri = Uri.fromFile(vFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent,1);
			}
        	
        });
        
         imageview =(ImageView)findViewById(R.id.imageview);
 //下面四行直接读取照片，方便调试       灯识别和信息识别        
         String testpath=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tencent/MicroMsg/WeiXin/IMG_20180427_225206.jpg"   ;
         Bitmap bitmap = BitmapFactory.decodeFile(testpath);
         
         Matrix matrix = new Matrix(); //旋转图片 动作
	//		matrix.setRotate(-90);//旋转角度
			
			// 计算缩放比例
			   scaleWidth = ((float) 1440) / bitmap.getWidth();
			   scaleHeight = ((float) 2560) / bitmap.getHeight();
		   // 取得想要缩放的matrix参数
			   
			   matrix.postScale(scaleWidth, scaleHeight);
			
//			width = bitmap.getWidth();
//			height = bitmap.getHeight(); // 创建新的图片
		  cameraBitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
         
		
		 
	     imageview.setImageBitmap( cameraBitmap );
         
         chuli =(Button)findViewById(R.id.chuli);
       
         chuli.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				
				if(EditEast.getText().length()==0){
					East=-130;
				}else{
					System.out.println("----"+EditEast.getText().toString());
					 East = Integer.parseInt (EditEast.getText().toString());
				}
				if(EditEast.getText().length()==0){
					West=53;
				}else{
					 West = Integer.parseInt (EditWest.getText().toString());
				}
				if(EditEast.getText().length()==0){
					South=146;
				}else{
					South = Integer.parseInt (EditSouth.getText().toString());
				}
				if(EditEast.getText().length()==0){
					North=-33;
				}else{
					 North = Integer.parseInt (EditNorth.getText().toString());
				}
				
//				 East = Integer.parseInt (EditEast.getText().toString());
//				 West = Integer.parseInt (EditWest.getText().toString());
//				 South = Integer.parseInt (EditSouth.getText().toString());
//				 North = Integer.parseInt (EditNorth.getText().toString());
			
			
				
				
		   //      Bitmap sp=filter(newbit);                     //黑白直方图均衡，会让图片变白，不会突显条纹那部分
			
				           
				      
				         newbit=bitmap2Gray(cameraBitmap);
				 Bitmap sp2=sharpenImageAmeliorate(newbit);         //这个可以让灯分界更分明，但会使条纹变弱，故条纹处理上直接用灰度化的即可
				
			     
			   
	  			    //比例时  直接用实际距离比像素点得比例即可
			        int lie[]=getlie(sp2);
			        int hang[]=gethang(sp2);
			     
			         liebg=lie[0];
			         lieend=lie[1];
		
			        
			         hangbg=hang[0];
			         hangend=hang[1];
			        
	  			    centerx=(lieend+liebg)/2;//从左到右为x方向
	  			    centery=(hangend+hangbg)/2;
	  			    
	  			     zhijing=hangend-hangbg;
	  			    
	  			    arearesult+="灯中心坐标为：("+centerx+","+centery+")," +
	  			    		"           灯直径为： "+zhijing;
	  			    
	  			    areashuju.setText(arearesult);
	  			    
	  			   
	  			    
	  			    
				
 
	  			      roy=rowget1(newbit);
	  			     
					 System.out.println("---------------1----------------");
					 int secy[]=new int[1000];
					 if(centerx<300){
						 int []roya=new int[roy.length-lieend];
					 for(int i=0;i<1000;i++){
						 roya[i]=roy[i+lieend];
					 }
				      secy=nihecut(roya,3);              //二次方拟合
				     
					 }else if(centerx>1140){
						 int []royb=new int[roy.length-liebg];
						 for(int i=0;i<1000;i++){
							 royb[i]=roy[i+liebg-1000];
						 }
					      secy=nihecut(royb,3);              //二次方拟合
					 }else{                                 //在中间时三段拟合
						  ctlie1=centerx-zhijing/2;
						  ctlie2=centerx+zhijing/2;
							System.out.println("-------zhijing   "+zhijing  ) ;
						System.out.println("-------ctlie1  ctlie2   "+ctlie1+"   "+ctlie2  ) ;
						
						
						 
//						 int []royc1=new int[ctlie1];
//						 int []royc2=new int[1440-ctlie2];
//						 int ddd=ctlie2-ctlie1;
//						 int []royc3=new int[ddd+200];
//						 
//						 for(int i=0;i<royc1.length;i++){
//							 royc1[i]=roy[i];
//						 }
//						 for(int i=0;i<royc2.length;i++){
//							 royc2[i]=roy[i+ctlie2];
//						 }
//						 for(int i=0;i<royc3.length;i++){         //中间部分
//							 royc3[i]=roy[i+ctlie1-100];
//						 }
						 
						 
						 Thread tr1 = new Thread(new Runnable() {
						        @Override
						        public void run() {
						        	 int []royc1=new int[ctlie1];
						        	 for(int i=0;i<royc1.length;i++){
										 royc1[i]=roy[i];
									 }
						        	int	 []secy1=nihecut(royc1,3);
						        	for(int i=0;i<ctlie1;i++){
						        		secyq[i]=secy1[i];
						        	}
						        	CD1.countDown();
						        }
						    });	 
						 Thread tr2 = new Thread(new Runnable() {
						        @Override
						        public void run() {
						        	 int []royc2=new int[1440-ctlie2];
						        	 for(int i=0;i<royc2.length;i++){
										 royc2[i]=roy[i+ctlie2];
									 }
						        	 int	 []secy2=nihecut(royc2,3); 
						        	 
						        	 for(int i=ctlie2;i<1440;i++){
						        		 secyq[i]=secy2[i-ctlie2];
							        	}
						        	 CD1.countDown(); 
						        }
						    });	 
						 Thread tr3 = new Thread(new Runnable() {
						        @Override
						        public void run() {
						        	int ddd=ctlie2-ctlie1;
									 int []royc3=new int[ddd+200];
									 for(int i=0;i<royc3.length;i++){         //中间部分
										 royc3[i]=roy[i+ctlie1-100];
									 }
									 int	 []secy3=nihecut(royc3,3);
									 
									 int []secy32=new int[ctlie2-ctlie1];
									    for(int i=0;i<secy32.length;i++){
									    	secy32[i]=secy3[i+100];
									    }
									    for(int i=ctlie1;i<ctlie2;i++){
									    	secyq[i]=secy32[i-ctlie1];
								        	}
									    CD1.countDown();
						        }
						    });	 
						 
						 
//					int	 []secy1=nihecut(royc1,3);
//					int	 []secy2=nihecut(royc2,3); 
//					int	 []secy3=nihecut(royc3,3);
					
//					int	 []secy1;
//					int	 []secy2;
//					int	 []secy3;
					
					
//				    int []secy32=new int[ctlie2-ctlie1];
//				    for(int i=0;i<secy32.length;i++){
//				    	secy32[i]=secy3[i+100];
//				    }
				    
//				    int []secyq=new int[1440];
//				    for(int i=0;i<secyq.length;i++){
//				    	if(i<ctlie1){
//				    		secyq[i]=secy1[i];
//				    	}
//				    	else if(i>=ctlie1&&i<ctlie2){
//				    		secyq[i]=secy32[i-ctlie1];
//				    	}
//				    	else if(i>=ctlie2){
//				    		secyq[i]=secy2[i-ctlie2];
//				    	}  	
//				    }
						 tr1.start();
						 tr2.start();
						 tr3.start();
						 
						 
				    try {
						CD1.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    for(int i=0;i<secy.length;i++){
				    	secy[i]=secyq[i+220];                        //取中间1000个值
				    }
					 
					 }
				     
				     System.out.println("---------------2----------------");     
				     int yzpd[]=yuzhipanduan(secy);
				     System.out.println("---------------3----------------"); 
	  			    
                  
			             messageresult(yzpd,14);
	//	ArrayList<Bit>  list2=message2(yzpd,10);
    //			 int []rstlst=message2(yzpd,13);
			  
			//     int result[]=new int[list2.size()];
			
//			     for(int i=0;i<list2.size();i++){
//				result[i]=list2.get(i).msg;
//				 ct+=String.valueOf(result[i]);
//			 }
			     ct=sb.toString();
			     System.out.println("--------ct:"+ct);
			     shuju.setText(ct);
			     String cct=ct;
			     
			     //解码接触灯的实际直径和灯号
			     
			      realzhijing=jiema(cct)[0];
			     reallightnumber=jiema(cct)[1];
			     
			     System.out.println("--------realzhijing:"+realzhijing);
			     System.out.println("--------realdenghao:"+reallightnumber);
			     
			    
				
			     
			     
			//一共四个点     ，比较四个点朝向灯中心的 角度，与地磁角度即可          注意磁偏角的修正
				 
				
			     double distance=bilijisuan(scaleWidth*realzhijing/zhijing);   //斜边
			    
			     
			     double xresult;
			     double yresult;
			     
			     double xd,yd;
			     
			     if(degree>=East&&degree<North){
			    	xd=distance*Math.cos(((degree-East)*Math.PI/180)*(90/(North-East)));
			    	yd=distance*Math.sin(((degree-East)*Math.PI/180)*(90/(North-East)));
			     }else if(degree>=North&&degree<West){
			    	xd=-distance*Math.sin(((degree-North)*Math.PI/180)*(90/(West-North)));
			        yd=distance*Math.cos(((degree-North)*Math.PI/180)*(90/(West-North)));
			     }else if(degree>=West&&degree<South){
			    	 xd=-distance*Math.cos(((degree-West)*Math.PI/180)*(90/(South-West)));
			    	 yd=-distance*Math.sin(((degree-West)*Math.PI/180)*(90/(South-West)));
			     }else if(degree>=South&&degree<=180){
			    	xd=distance*Math.sin(((degree-North)*Math.PI/180)*(90/(180-South+East-(-180))));
			        yd=-distance*Math.cos(((degree-North)*Math.PI/180)*(90/(180-South+East-(-180))));
			     }else{
			    	 xd=distance*Math.cos(((East-degree)*Math.PI/180)*(90/(180-South+East-(-180))));
				     yd=-distance*Math.sin(((East-degree)*Math.PI/180)*(90/(180-South+East-(-180)))); 
			     }
			     
			     String x=String.format("%.2f", xd);
			     String y=String.format("%.2f", yd);
			     
			     lastresult="灯号:"+reallightnumber+",实际直径"+realzhijing+"mm ;"+"\n"+"手机坐标为 ：("+x+","+y+")";
			     resultshuju.setText(lastresult);
			     
			     
			     
				     
				 
				 imageview.setImageBitmap(newbit);
			}

			private int[] jiema(String a) {
				int []result=new int[2];
				int sum1=0;
				int sum2=0;
				int index1=0;
				for(int i=0;i<a.length();){
					int count=0;
					if(a.charAt(i)=='1'){
						int j=i;
						while(a.charAt(j)=='1'){
							j++;
							count++;
							
						}
					
					if(count==8){
						index1=i+count;
						break;
					}
						i+=count;
					}
					else{
						i+=1;
					}
				}
				System.out.println("----index1:"+index1);
				char []realzhi=new char[8];
				char []reallg=new char[8];
				
				for(int i=0;i<realzhi.length;i++){
					reallg[i]=a.charAt(i+index1);
				}
				for(int i=0;i<realzhi.length;i++){
					realzhi[i]=a.charAt(i+index1+8);
				}	
				
				 for (int i=1;i<=8;i++){
			            //第i位 的数字为：
			            int dt = (reallg[i-1]-'0');
			            sum1+=(int)Math.pow(2,8-i)*dt;
			        }
				 for (int i=1;i<=8;i++){
			            //第i位 的数字为：
			            int dt = (realzhi[i-1]-'0');
			            sum2+=(int)Math.pow(2,8-i)*dt;
			        }
                     result[0]=sum2;
                     result[1]=sum1;
				return result;
			}
        	
        });
        
        clear=(Button)findViewById(R.id.clear);
       
        
        clear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {		
				ct = "";
				shuju.setText(ct);	
				
			}
        	
        });
        end=(Button)findViewById(R.id.end);
        end.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
	
				
				System.exit(0);
			}
        	
        });
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }
    }

    private SensorEventListener listener=new SensorEventListener() {
        float[] accelerometerValues=new float[3];
        float[] magneticValues=new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerometerValues=event.values.clone();
            }else if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticValues=event.values.clone();
            }
            float[] R=new float[9];
            float[] values=new float[3];
            SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticValues);
            SensorManager.getOrientation(R,values);
            degree= -(float) Math.toDegrees(values[0]);//旋转角度
            CharSequence cs=String.valueOf(degree);
            directionresult="地磁场角度:   "+cs;
            directionshuju.setText(directionresult);
        //    myCompassView.setDegree(degree);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
 // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
    	Bitmap bm = null;  
    	  
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口  
      
        ContentResolver resolver = getContentResolver();  
    	 super.onActivityResult(requestCode, resultCode, data);
    	 
    	 if(requestCode==1){
    		 if (resultCode == RESULT_OK)  

    		 {
    			
    	          
    			 Bitmap bitmap = BitmapFactory.decodeFile(mPath);
    			
    			int width=bitmap.getWidth();
    			int height = bitmap.getHeight(); // 创建新的图片
    		
    			int newWidth=1440;
    			int newHeight=2560;
    			
    			if(width==2560 || width==3264 || width==4208 ){
    			
    			Matrix matrix = new Matrix(); //旋转图片 动作
    			matrix.setRotate(-90);//旋转角度
    			
    			// 计算缩放比例
    			    scaleWidth = ((float) newWidth) / width;
    			    scaleHeight = ((float) newHeight) / height;
    			   // 取得想要缩放的matrix参数
    			   
    			   matrix.postScale(scaleWidth, scaleHeight);
    			
//    			width = bitmap.getWidth();
//    			height = bitmap.getHeight(); // 创建新的图片
    		  cameraBitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    			}
    			else if(width==1920 || width==2048 || width==2576){
    				
    				Matrix matrix = new Matrix(); //旋转图片 动作
        			matrix.setRotate(90);//旋转角度

        			// 计算缩放比例
        			   scaleWidth = ((float) newWidth) / width;
        			   scaleHeight = ((float) newHeight) / height;
        			   // 取得想要缩放的matrix参数
        			   
        			   matrix.postScale(scaleWidth, scaleHeight);
//        			width = bitmap.getWidth();
//        			height = bitmap.getHeight(); // 创建新的图片
        		  cameraBitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    				
    				
    				
    			}else if(width==1088 || width==1536 || width==1936) {
    				Matrix matrix = new Matrix(); //旋转图片 动作
        			matrix.setRotate(180);//旋转角度
//        			width = bitmap.getWidth();
//        			height = bitmap.getHeight(); // 创建新的图片

        			// 计算缩放比例
        			    scaleWidth = ((float) newWidth) / width;
        			    scaleHeight = ((float) newHeight) / height;
        			   // 取得想要缩放的matrix参数
        			   
        			   matrix.postScale(scaleWidth, scaleHeight);
        		  cameraBitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    			}
    			else{
    				Matrix matrix = new Matrix(); //旋转图片 动作
        			matrix.setRotate(0);//旋转角度
//        			width = bitmap.getWidth();
//        			height = bitmap.getHeight(); // 创建新的图片

        			// 计算缩放比例
        			    scaleWidth = ((float) newWidth) / width;
        			    scaleHeight = ((float) newHeight) / height;
        			   // 取得想要缩放的matrix参数
        			   
        			   matrix.postScale(scaleWidth, scaleHeight);
        		  cameraBitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    				
    			}
                 
//                 mIvShow[i].setImageBitmap(bitmap);
   		//     cameraBitmap= Bitmap.createBitmap(bitmap,0,0,1440,2560);//这个比列是手机预览比例
    	//		 imageview.setImageURI(Uri.fromFile(new File(imgPath))); 
    		 imageview.setImageBitmap( cameraBitmap );

    		 }
    	 }
    	
    }
    //按比例计算距离                垂直距离固定，可算可不算         AM为灯到手机水平x距离，BM为灯到手机水平y距离，所以一共四个点区分即可
    public double bilijisuan(double a){       //斜边
    	double distance[]=new double[2];
    	
    	double AM=Math.abs(centerx-720)/scaleWidth;
    	double BM=Math.abs(centery-1780)/scaleHeight;
    	
    	double CM=a*Math.sqrt(AM*AM+BM*BM);
    
    	
    	
    	return CM;
    	     
    }
    //处理预览模式相机比例高比长大的问题，否则灰度化会相反，因为高度对信息不影响可做删减,此处直接放在灰度处理内
  
    
    //利用ArrayList判断返回数据               亮度变化开始计次数，用像素点数除以1K单条代表像素个数m，即n个该电平
    
    //判断上升沿或下降沿
    
    public List<Bit> message(int []a,int m){
    List<Bit> list=new ArrayList<Bit>();
    int count=1;
    int j=0;
    	for(int i=0;i<a.length;){
    		j=i;
    		while((j<a.length-1)&&(a[j+1]==a[j])){
    			count++;
    			j++;
    			
    		};
    		
    		for(int q=0;q<(count/m);q++){
    		Bit msg=new Bit();
    		msg.msg=a[i];
    		list.add(msg);
    		}
    		i+=count;
    		count=1;
    		
    	}
    	return list;
    }
    //阈值判断提取数据方法2   从第一个上升沿开始判断，每隔 单条代表像素个数m，进行判断，如果大于m/2则为1，小于则为0
    //先找到上升沿或下降沿的index
    public int findindex(int []a){
    	int index=0;
    	for(int i=0;i<a.length-1;i++){ 
    		if(a[i+1]!=a[i]){
    			index=i+1;
    			 break;
    		}
    	}
    	System.out.println("index:"+index+"    ");            //此处为1000里面的index
    	return index;
    }
public void  messageresult(int []a,int m){
    	
//    	ArrayList<Bit>    list=new ArrayList<Bit>();
       
            int j=0;
            int index=findindex(a);
            System.out.println(a.length);
        	for(int i=index;i<a.length-2;){              //50 可为0,170可为a.length
      
        		int count=1;
             //   int sum=0;
        		j=i;
        	//	System.out.println("j="+j);
        		while((j<a.length-1)&&(a[j]==a[j+1])){
        		//	sum+=a[j];
        			count++;
        			j++;	
        		}	
        			if(count<m){
        				sb.append("");	
        		}else if(count>=m&&count<2*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        		}else if(count>=2*m&&count<3*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=3*m&&count<4*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=4*m&&count<5*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=5*m&&count<6*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=6*m&&count<7*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=7*m&&count<8*m){
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}else if(count>=8*m){                 //最高为8位，此处可变，看传送数据位数
        			String aii=String.valueOf(a[i]);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        			sb.append(aii);
        		}
			
        		i+=count;
		
        	}
        }
    public ArrayList<Bit>  message2(int []a,int m){
    	
    	ArrayList<Bit>    list=new ArrayList<Bit>();
       
            int j=0;
            int index=findindex(a);
            System.out.println(a.length);
        	for(int i=index;i<a.length-2;){              //50 可为0,170可为a.length
      
        		int count=1;
                int sum=0;
        		j=i+1;
        	//	System.out.println("j="+j);
        		while((j<a.length)&&(count<m+1)){
        			sum+=a[j];
        			count++;
        			j++;	
        			
        		};
        		
        	//	System.out.println("count="+count);
        	//	System.out.println("sum="+sum);
        		Bit msg=new Bit();
        		if(sum>m/2){
        		
        		msg.msg=1;
//        		list[numlist++]=1;
        		list.add(msg);
        		}
        		else{
        			
            		msg.msg=0;
//            		list[numlist++]=0;
            		list.add(msg);	                              
        		}
        		i+=count-1;
		
        	}
        	       return list;
        }
 public ArrayList<Bit>  message3(int []a){
    	
    	ArrayList<Bit>    list=new ArrayList<Bit>();
       
            int j=0;
            int index=findindex(a);
            System.out.println(a.length);
        	for(int i=index;i<a.length-2;){              //50 可为0,170可为a.length
        		
               if(a[i]==0){            //4个零为一个零        五个一为一个一
            	   Bit msg=new Bit(); 
            	   msg.msg=1; 
            	   list.add(msg);    //由于三极管或光效应，原为零的实际上是数据1
            	   i+=4;
               }else{
            	   Bit msg=new Bit(); 
            	   msg.msg=0; 
            	   list.add(msg);
            	   i+=5;
               }
        	}   	   
        		
        	       return list;
        }
    //阈值判断处理
    public int[] yuzhipanduan(int []a){
    	for(int i=0;i<a.length;i++){
    		if(a[i]>0){
    			a[i]=1;
    		}
    		else{
    			a[i]=0;
    		}
    		System.out.print(a[i]+" ");
    	}
    	
    	return a;
    	
    }
    //多项式拟合差值处理     a[i]*Math.pow((x-ave),i );
    public int[] nihecut(int []a,int m){
    	int []d=new int [a.length];
    	for(int j=0;j<a.length;j++){
    		d[j]=j;
    	}
    	
  //  	double []x=PolyFit(d,a,a.length,m);
    	double []x=zxec(d,a,10,m);
    	for(int j=0;j<x.length;j++){
    		System.out.print(x[j]+" ");
    	}
    	double []b=new double [a.length];
    	int []c=new int [a.length];
    	for(int i=0;i<a.length;i++){
    //	b[i]=((-0.002)*i*i+0.3206*i+91.4558);
    	b[i]=(x[2]*i*i+x[1]*i+x[0]);            //效果对比  111.06  -0.24  -0.0017    matlab: -0.0017    0.2719  109.0759
    	c[i]=(int)(a[i]-b[i]);                    
    	System.out.print(c[i]+" ");
    	}
    	return c;
    }
    //最小二乘法方法二
    public double[] zxec(int []a,int []b,int s ,int l){              //s为迭代几次，l为几次多项式拟合
    	int n=a.length;
    	int m=b.length;
    	int   i, j, k;
    	 double sumx = 0;
         double sumy = 0;
    	for (i = 0; i < n; i++) {
            sumx += a[i];// x的类和；
            sumy += b[i];// y的类和；
        }
    	 double Sumx[] = new double[s];
         for (i = 0; i < s; i++) {
             double sum = 0;
             for (j = 0; j < n; j++) {
                 double r = 1;
                 for (k = 0; k <= i; k++)
                     r = r * a[j];
                 sum += r;
             }
  
             Sumx[i] = sum;
         }
         /*
          * for(i=0;i<s;i++){ System.out.print(Sumx[i]+"  "); }
          */
         // System.out.println();
         double Sumxy[] = new double[m];
         for (i = 0; i < m; i++) {
             double sumxy = 0;
             for (j = 0; j < n; j++) {
                 double p = 1;
                 double w = 0;
                 for (k = 0; k <= i; k++)
                     p = p * a[j];
                 w = p * b[j];
                 sumxy += w;
             }
             Sumxy[i] = sumxy;
         }
         /*
          * for(i=0;i<m;i++){ System.out.print(Sumxy[i]+"  "); }
          */
         // System.out.println();
         int t = l;
         int q = l+1;
         double A[][] = new double[t][q];
         A[0][0] = n;
         A[0][q - 1] = sumy;
         for (j = 1; j < q - 1; j++)
             A[0][j] = Sumx[j - 1];
         for (i = 1; i < t; i++)
             for (j = 0; j < q - 1; j++)
                 A[i][j] = Sumx[i + j - 1];
         for (i = 1; i < t; i++)
             A[i][q - 1] = Sumxy[i - 1];
         /*
          * for (i = 0; i < t; i++) { int count1 = 0; for (j= 0; j < q;j++) {
          * System.out.print(A[i][j] + "  "); count1++; if (count1 == q)
          * System.out.println(); } }
          */
         for (k = 0; k < t; k++) {
             for (i = k + 1; i < t; i++) {
                 double L = A[i][k] / A[k][k];
                 for (j = 0; j < q; j++)
                     A[i][j] = A[i][j] - L * A[k][j];
             }
         }
         for (k = t - 1; k >= 0; k--) {
             for (i = k - 1; i >= 0; i--) {
                 double L = A[i][k] / A[k][k];
                 for (j = q - 1; j >= k; j--)
                     A[i][j] = A[i][j] - L * A[k][j];
             }
         } // 求多项式的系数
         /*
          * for (i = 0; i < t; i++) { int count1 = 0; for (j= 0; j < q;j++) {
          * System.out.print(A[i][j] + "  "); count1++; if (count1 == q)
          * System.out.println(); } }
          */
         double r[] = new double[t];
         for (i = 0; i < t; i++) {
             r[i] = A[i][q - 1] / A[i][i];
             // System.out.println("x"+i+"="+r[i]);
         }
         return r;
    }
  
   


 //边缘检测-锐化                  此方法效果还好，就是矩阵是否可变有待继续做实验
    public static Bitmap sharpenImageAmeliorate(Bitmap bmp)     
    {       
        // 拉普拉斯矩阵     
        int[] laplacian = new int[] { -1,-2,-1,0,0,0,1,2,1};    //-1,-2,-1,0,0,0,1,2,1
             
        final int width = bmp.getWidth();    
        final int height = bmp.getHeight();   
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);     
             
        int pixR = 0;     
        int pixG = 0;     
        int pixB = 0;     
             
        int pixColor = 0;     
             
        int newR = 0;     
        int newG = 0;     
        int newB = 0;     
             
        int idx = 0;     
        float alpha = 0.5F;     
        int[] pixels = new int[width * height];     
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);   
//      bmp.recycle();  
//      bmp = null;  
        for (int i = 1, length = height - 1; i < length; i++)     
        {     
            for (int k = 1, len = width - 1; k < len; k++)     
            {     
                idx = 0;     
                for (int m = -1; m <= 1; m++)     
                {     
                    for (int n = -1; n <= 1; n++)     
                    {     
                        pixColor = pixels[(i + n) * width + k + m];     
                        pixR = Color.red(pixColor);     
                        pixG = Color.green(pixColor);     
                        pixB = Color.blue(pixColor);     
                             
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);     
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);     
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);     
                        idx++;     
                    }     
                }     
                     
                newR = Math.min(255, Math.max(0, newR));     
                newG = Math.min(255, Math.max(0, newG));     
                newB = Math.min(255, Math.max(0, newB));     
                     
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);     
                newR = 0;     
                newG = 0;     
                newB = 0;     
            }     
        }     
             
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);        
        return bitmap;     
    }  
    //  取每行总值
   public int [] finhang(Bitmap bm){
	   int width = bm.getWidth();  
       int height = bm.getHeight();
       
       int r,g,b;
       int rowy[]=new int[height];
       
       for(int j=0;j<height;j++){
       	     int y=0;
       	for(int i=0;i<width;i++){
       		int localTemp = bm.getPixel(i,j);
     //  		int localTemp = bm.getPixel(i,findJ());
       	    r = Color.red(localTemp);
               g = Color.green(localTemp);
               b = Color.blue(localTemp);
               y+=(int)(0.299* r+ 0.587*g+0.114*b);
     //          y=(int)(0.299* r+ 0.587*g+0.114*b);
       		
       	}
       	   
       	     rowy[j]=y;
//      	     System.out.print(rowy[j]+" "); 
       }
               return rowy;
	   
   }
   public int findJ1(int []a){                   //找两个最亮的有问题，因为可能都是一个灯中心的，最暗的也不行     冒泡排序，排出中间值，匹配那一行
//	   int []b=a;
//	   //int max1=0,max2=1;                      //因为最暗的可能在边上，两灯之间的最好
//	   int min=1;
//	   System.out.println("a[i]的值：");
//	   for(int i=1;i<b.length-1;i++){
//		   System.out.print(b[i]+"  ");
//		   if(a[i]<a[min]){
//			  min=i;
//		   }
//	   }
//	 //  System.out.println("  max1="+max1+"         max2="+max2);
//	   System.out.println("  min="+min);
//	   int findj=min;
	   int []c=new int[a.length];
	   for(int i=0;i<a.length;i++){
			 c[i]=a[i];
		 }
	 int b=maopao(a);
	 int findj=0;
	 for(int i=0;i<a.length;i++){
		 if(c[i]==b)findj=i;
	 }
	  System.out.println("findj="+findj);
	   return findj;
   }
   public int maopao(int []a){
	   System.out.println("冒泡后：   ");
	   for(int i=0;i<a.length;i++){
			  for(int j=i;j<a.length;j++){
				  if(a[j]<a[i]){
					  int temp=a[i];
					  a[i]=a[j];
					  a[j]=temp;
				  }
			  }
		  }
	   for(int i=0;i<a.length;i++){
		   System.out.print(a[i]+" ");
	   }
	   return a[a.length/2];
   }
   
   //对一行进行列向量向量提取                          
     public int[] rowget1(Bitmap bm){
     	int width = bm.getWidth();  
         int height = bm.getHeight();
         
//         int []finghang=finhang(bm); 
//         int finj=findJ1(finghang);
         int r,g,b;
         int rowy[]=new int[width];
         for(int i=0;i<width;i++){                         //取某行值   最左边会出现大 0 大 0 大0  。。。这种现象，取得行多些则会减少此现象
         	     int y=0;                                    //原因   锐化太过了，去掉即可  也可改进
         	for(int j=hangbg-20;j<hangbg+20;j++){
         		int localTemp = bm.getPixel(i,j);           //灯中心点减去灯一半长
         	    
         	    
//         		int localTemp = bm.getPixel(i,width/2);
         	    r = Color.red(localTemp);
                 g = Color.green(localTemp);
                 b = Color.blue(localTemp);
//                 y+=r;
                y+=(int)(0.299* r+ 0.587*g+0.114*b);
 //                y=(int)(0.299* r+ 0.587*g+0.114*b);
         		
       	}
         	     y=(int)(y/40);
         	         rowy[i]=y;
         	     System.out.print(rowy[i]+" "); 
         }
                 return rowy;
     }

   
   //去除灯影响方法2       先遍历取列的均值然后去除大于均值的，然后对剩下的取均值
     //  取每列均值
     public int [] findlie(Bitmap bm){
  	   int width = bm.getWidth();  
         int height = bm.getHeight();
         
         int r,g,b;
         int rowy[]=new int[width];
         for(int i=0;i<width;i++){
         	     int y=0;
         	for(int j=0;j<height;j++){
         		int localTemp = bm.getPixel(i,j);
      
         	    r = Color.red(localTemp);
                 g = Color.green(localTemp);
                 b = Color.blue(localTemp);
                 y+=(int)(0.299* r+ 0.587*g+0.114*b);
   
         	}
         	     y=(int)(y/height);
         	     rowy[i]=y;
//         	     System.out.print(rowy[j]+" "); 
         }
                 return rowy;
  	   
     }
     //过滤强光的点           ,并对余下的取均值
     public int [] rowget2(Bitmap bm){
    	   int width = bm.getWidth();  
           int height = bm.getHeight();
           
           int []eachlie=findlie(bm);
           
           int r,g,b;
           int rowy[]=new int[width];
           for(int i=0;i<width;i++){
           	     int y=0,src=0,count=0;
           	for(int j=0;j<height;j++){
           		int localTemp = bm.getPixel(i,j);
        
           	    r = Color.red(localTemp);
                   g = Color.green(localTemp);
                   b = Color.blue(localTemp);
                  src= (int)(0.299* r+ 0.587*g+0.114*b);
                  if(src<=eachlie[i]){	  
                   y+=src;
                   count++;
                  
                  }
     
           	}
           	     System.out.print(count+" ");
           	     y=(int)(y/count);
           	     rowy[i]=y;
           	  System.out.print(rowy[i]+" "); 
//           	     System.out.print(rowy[j]+" "); 
           }
                   return rowy;
    	   
       }
     
    //直方图均衡
    public Bitmap filter(Bitmap src) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
         
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
       
        int[][] rgbhis = new int[3][256]; // RGB  
        int[][] newrgbhis = new int[3][256]; // after HE  
        for(int i=0; i<3; i++) {  
            for(int j=0; j<256; j++) {  
                rgbhis[i][j] = 0;  
                newrgbhis[i][j] = 0;  
            }  
        }  
        int index = 0;  
        int totalPixelNumber = height * width;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
            	int localTemp = src.getPixel(col,row);   
                ta = (localTemp >> 24) & 0xff;  
                tr = (localTemp >> 16) & 0xff;  
                tg = (localTemp >> 8) & 0xff;  
                tb = localTemp & 0xff;  
  
                // generate original source image RGB histogram  
                rgbhis[0][tr]++;  
                rgbhis[1][tg]++;  
                rgbhis[2][tb]++;  
            }  
        }  
          
        // generate original source image RGB histogram  
        generateHEData(newrgbhis, rgbhis, totalPixelNumber, 256);  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {
            	int localTemp = src.getPixel(col,row);
                 
                ta = (localTemp >> 24) & 0xff;  
                tr = (localTemp >> 16) & 0xff;  
                tg = (localTemp >> 8) & 0xff;  
                tb = localTemp & 0xff;  
  
                // get output pixel now...  
                tr = newrgbhis[0][tr];  
                tg = newrgbhis[1][tg];  
                tb = newrgbhis[2][tb];  
                  
                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb; 
                index++;
            }  
        }  
        //Bitmap.createBitmap (mArrayColor,width,height,Bitmap.Config.RGB_565)
        Bitmap dest=Bitmap.createBitmap(outPixels,width,height,Bitmap.Config.RGB_565);
        
        return dest;  
    }  
    /** 
     *  
     * @param newrgbhis 
     * @param rgbhis 
     * @param totalPixelNumber 
     * @param grayLevel [0 ~ 255] 
     */  
    private void generateHEData(int[][] newrgbhis, int[][] rgbhis, int totalPixelNumber, int grayLevel) {  
        for(int i=0; i<grayLevel; i++) {  
            newrgbhis[0][i] = getNewintensityRate(rgbhis[0], totalPixelNumber, i);  
            newrgbhis[1][i] = getNewintensityRate(rgbhis[1], totalPixelNumber, i);  
            newrgbhis[2][i] = getNewintensityRate(rgbhis[2], totalPixelNumber, i);  
        }  
    }  
      
    private int getNewintensityRate(int[] grayHis, double totalPixelNumber, int index) {  
        double sum = 0;  
        for(int i=0; i<=index; i++) {  
            sum += ((double)grayHis[i])/totalPixelNumber;  
        }  
        return (int)(sum * 255.0);  
    }  
    
  //灰度处理
    public Bitmap bitmap2Gray(Bitmap bmSrc) {  
        // 得到图片的长和宽  
        int width = bmSrc.getWidth();  
        int height = bmSrc.getHeight();  
        
        System.out.println("长："+width);
        System.out.println("高："+height);
        
        int mArrayColorLengh = width * height;  
        int []mArrayColor = new int[mArrayColorLengh]; 
        int y=0;
        int count=0;
        
        
        for(int j=0;j<height;j++){
             for(int i = 0; i<width;i++ ) {
        	
            int localTemp = bmSrc.getPixel(i,j);
            y=(int)(0.299* Color.red(localTemp)+ 0.587*Color.green(localTemp)+0.114*Color.blue(localTemp));
          
          mArrayColor[count]=Color.rgb(y,y,y) ;
          count++;
     
             }
        }
        
        Bitmap bmpGray=   Bitmap.createBitmap (mArrayColor,width,height,Bitmap.Config.RGB_565) ;
        return bmpGray;  
    }  

    //平均列向量提取
    public int[] rowgetlie(Bitmap bm){
    	int width = bm.getWidth();  
        int height = bm.getHeight();
        
        int r,g,b;
      
        int rowy[]=new int[width];
        for(int i=0;i<width;i++){
        	     int y=0;
        	for(int j=0;j<height;j++){
        		int localTemp = bm.getPixel(i,j);
        	    r = Color.red(localTemp);
                g = Color.green(localTemp);
                b = Color.blue(localTemp);
                y+=(int)(0.299* r+ 0.587*g+0.114*b);
        		
        	}
        	     y=(int)(y/(2*height));
        	     rowy[i]=y;
        	     System.out.print(rowy[i]+" "); 
        	    
        }
                return rowy;
    }
    //平均行向量提取
    public int[] rowgethang(Bitmap bm){
    	int width = bm.getWidth();  
        int height = bm.getHeight();
        
        int r,g,b;
      
        int rowy[]=new int[height];
        for(int i=0;i<height;i++){
        	     int y=0;
        	for(int j=0;j<width;j++){
        		int localTemp = bm.getPixel(j,i);
        	    r = Color.red(localTemp);
                g = Color.green(localTemp);
                b = Color.blue(localTemp);
                y+=(int)(0.299* r+ 0.587*g+0.114*b);
        		
        	}
        	     y=(int)(y/(2*width));
        	     rowy[i]=y;
        	     if(i<1800){
        	     System.out.print(rowy[i]+" "); 
        	     }
        }
                return rowy;
    }
    //返回liebg,lieend      即a[0] a[1]
    public int[] getlie(Bitmap bm){
    	int liebg=0;
    	int lieend=0;
    	int liemax=0;
    	int liemaxindex=0;
    	int a[]=new int[3];
    	System.out.println("---------------------------------");
			int []a1=rowgetlie( bm);
			//找阈值  灰度最大值liemax
			for(int i=25;i<a1.length-25;i++){         //从25开始，防止边缘的点影响
				if(a1[i]>liemax){
					liemax=a1[i];
					liemaxindex=i;
				}
			}
			a[2]=liemaxindex;
			ArrayList<Integer> allie=new ArrayList<Integer>();
			for(int i=0;i<a1.length;i++){
				if(a1[i]==liemax){
					allie.add(i);
				}
			}
			//阈值暂停liemax一半可调整，通过这个定位列的灯开始和结束
//			for(int i=0;i<a1.length;i++){
//				if(a1[i]>=liemax/2){
//					liebg=i;
//					break;
//				}
//			}
//			for(int i=liemaxindex;i<a1.length;i++){
//				if(a1[i]<liemax/2){
//					lieend=i;
//					break;
//				}
//			}
			
    	    
			a[0]=allie.get(0);
			a[1]=allie.get(allie.size()-1);
			
			System.out.println("liemax="+liemax+"    liebg="+a[0]+"    lieend="+a[1]);
    	    return a;
    }
  //返回hangbg,hangend      即b[0] b[1]
    public int[] gethang(Bitmap bm){
    	int hangbg=0;
    	int hangend=0;
    	int hangmax=0;
    	int hangmaxindex=0;
    	int b[]=new int[2];
    	System.out.println("---------------------------------");
			int []a2=rowgethang(  bm);
			
			for(int i=25;i<a2.length-25;i++){
				if(a2[i]>hangmax){
					hangmax=a2[i];
					hangmaxindex=i;
				}
			}
			//阈值暂停hangmax一半可调整，通过这个定位列的灯开始和结束
//			for(int i=0;i<a2.length-1;i++){
//				if(a2[i]>=hangmax/2){
//					hangbg=i;
//					break;
//				}
//			}
//			for(int i=hangmaxindex;i<a2.length;i++){
//				if(a2[i]<hangmax/2-1){
//					hangend=i;
//					break;
//				}
//			}
			ArrayList<Integer> alhang=new ArrayList<Integer>();
			for(int i=0;i<a2.length;i++){
				if(a2[i]==hangmax-3){              //这块设置阈值 计算直径，可以为hangmax-n
					alhang.add(i);
				}
			}
			
			
			
			b[0]=alhang.get(0);
			b[1]=alhang.get(alhang.size()-1);
			
System.out.println("hangmax="+hangmax+"    hangbg="+b[0]+"    hangend="+b[1]);
			
			System.out.println("---------------------------------");
			
    	    return b;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
