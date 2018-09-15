package com.example.androidpay;


import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import android.app.Activity;

import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.os.RemoteException;
import android.util.Log;
import android.hardware.Camera.Parameters;
import android.hardware.Camera;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;  
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends Activity  {
	
	
	private Button sent;
	private Button end;
	
	boolean Enable;
	 private EditText  edit_id    = null;
	 private EditText  edit_password    = null;
	 private EditText  edit_cost    = null;
	 private String    sentms         = null;
	 private String    stms         = null;
	 private Camera camera = null;  
	 private Parameters parameters = null;
	 
	 TextView tvShow;  
	 private static  int i=0 ;
	 boolean  bgsent=false;
	
	 
	 
	
	
         
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		 camera = Camera.open();
			final	Parameters   p = camera.getParameters(); 
		
		
		Context context = this;
		PackageManager pm = context.getPackageManager();
		
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
			}
		//jishi
		edit_id = (EditText) findViewById(R.id.in_id);
		edit_password = (EditText) findViewById(R.id.in_password);
		edit_cost = (EditText) findViewById(R.id.in_cost);
	        
	       
		
		 
		 
		 //数据发送
		 sent=(Button)findViewById(R.id.sent);
		 sent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 
				
					// bgsent=true;
						
				
				 stms = edit_id.getText().toString()+edit_password.getText().toString()+edit_cost.getText().toString();
				sentms= addjiaoyan(stms);
				
				
//				
//			ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(1);  
			   camera.startPreview();
				
				
//	               TimerTask task1 = new TimerTask() {
//					public void run() {
			   for(int j=0;j<1000;j++){
						//帧头
						p.setFlashMode(Parameters.FLASH_MODE_TORCH);
						camera.setParameters(p);
						

			               try {
			                  Thread.sleep(500);                        //可调，单位ms
			               } catch (InterruptedException e) {
			            	   e.printStackTrace();
			               }  
			           	p.setFlashMode(Parameters.FLASH_MODE_OFF);
						camera.setParameters(p);
			               
						//信息
							char []ms=new char[sentms.length()];
							
							for(int aa=0;aa<sentms.length();aa++){
								ms[aa]=sentms.charAt(aa);
							}
							  for(int w=0; w<ms.length;w++){
								  System.out.println(ms[w]);
					                     bianma(ms[w]);	
					   //                     bianmaooK(ms[w]);
					                        
		               }
								
						//帧尾
								p.setFlashMode(Parameters.FLASH_MODE_TORCH);
								camera.setParameters(p);
							  
								
								 try {
					                  Thread.sleep(300);
					               } catch (InterruptedException e) {
					            	   e.printStackTrace();
					               }   
									p.setFlashMode(Parameters.FLASH_MODE_OFF);
									camera.setParameters(p);
			   }
							
//					}
//					};   
//				newScheduledThreadPool.scheduleAtFixedRate(task1, 0,5000, TimeUnit.MILLISECONDS);
	              
				
				 p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					camera.stopPreview(); 
					
					
				}
			
				
			
			 
		 });
		 
		 
			
				
			
			 
		
		
		
		 
		 
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

	
	
	
	//鍙戦�佹椂闂撮棿闅�
	/*public void sentmes(){
		

		Parameters   p = camera.getParameters();
            	
            
			if(i%2==0){
				  
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				camera.startPreview();
			}else{
				
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(p);
				camera.stopPreview();
		    }
          
            
	 }*/
	//校验函数（将来可搞纠错）      最后一位1  代表基数个1  0代表偶数个1    （奇校验方式）
	String addjiaoyan(String m){
		int count=0;
		String res="";
		for(int p=0;p<m.length();p++){
			if(m.charAt(i)==1)count++;
		}
		if(count%2==0)res=m+'0';
		else{
			res=m+'1';
		}
		return res;
	}
	
	//编码方式  F-PWM
	public void bianma(char ms) {
		Parameters   p = camera.getParameters();
		String bit="";
		switch(ms){
		case '0':
			bit = "00";
		    break;	
		case '1':
		     bit = "01";
		     break;
		case '2':
			 bit = "02";
			 break;
		case '3':
			 bit = "03";
			 break;
		case '4':
			 bit = "10";
			 break;
		case '5':
			 bit = "11";
			 break;
		case '6':
			 bit = "12";
			 break;
		case '7':
			 bit = "13";
			 break;
		case '8':
			 bit = "20";
			 break;
		default:
			 bit = "21";
			 break;
		
		}
		
		char  []stringArr =new char[2];
		for(int j=0;j<stringArr.length;j++){
			stringArr[j]=bit.charAt(j);
			System.out.print(stringArr[j]+" ");
		}
		for(int j=0;j<stringArr.length;j++){
			System.out.println("----------------------------------------------");
			
			
			if(stringArr[j]=='0'){
				
				
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(p);
				
				
			
				
			}else if(stringArr[j]=='1'){
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				
				try {
					Thread.sleep(0) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
				
			}else if(stringArr[j]=='2'){
				
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				try {
					Thread.sleep(40) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 
				
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
				
				
			}else if(stringArr[j]=='3'){
				
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				
				try {
					Thread.sleep(80) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				
				p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
				
			}
		}
		 
		
	}
	//编码方式  OOK
		public void bianmaooK(char ms) {
			
			Parameters   p = camera.getParameters();
			String bit="";
			switch(ms){
			case '0':
				bit = "0000";
			    break;	
			case '1':
			     bit = "0001";
			     break;
			case '2':
				 bit = "0010";
				 break;
			case '3':
				 bit = "0011";
				 break;
			case '4':
				 bit = "0100";
				 break;
			case '5':
				 bit = "0101";
				 break;
			case '6':
				 bit = "0110";
				 break;
			case '7':
				 bit = "0111";
				 break;
			case '8':
				 bit = "1000";
				 break;
			default:
				 bit = "1001";
				 break;
			
			}
			
			char  []stringArr =new char[4];
			for(int j=0;j<stringArr.length;j++){
				stringArr[j]=bit.charAt(j);
				System.out.print(stringArr[j]+" ");
			}
			for(int j=0;j<stringArr.length;j++){
				System.out.println("----------------------------------------------");
				
				
				if(stringArr[j]=='0'){
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					
					
				
					
				}else if(stringArr[j]=='1'){
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(p);
					
					 
//					p.setFlashMode(Parameters.FLASH_MODE_OFF);
//						camera.setParameters(p);
					
			     
				}
				
			              
				
			}
			
		}
	
	// handler绫绘帴鏀舵暟鎹�  
   /* Handler handler = new Handler() {  
    	
    	
    	
        public void handleMessage(Message msg) {  
            if (msg.what == 1) {  
            //	sentmes();
                tvShow.setText(Integer.toString(i++));  
                System.out.println("receive....");  
            }  
        };  
    };  
	
	// 瀹氭椂鍣�
	class ThreadShow implements Runnable {  
		  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while (true) {  
                try {  
                    Thread.sleep(1000);  
                    Message msg = new Message();  
                    msg.what = 1;  
                    handler.sendMessage(msg);  
                    System.out.println("send...");  
                } catch (Exception e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                    System.out.println("thread error...");  
                }  
            }  
        }  
    }  
	
	 */
}



	

