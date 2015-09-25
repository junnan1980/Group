package group;

import com.example.group.R;

import android.app.Activity;  
import android.content.Context;
import android.content.Intent;  
import android.net.wifi.WifiManager;
import android.os.Bundle;  
import android.view.animation.AlphaAnimation;  
import android.view.animation.Animation;  
import android.view.animation.Animation.AnimationListener;  
import android.widget.ImageView;  
  
public class Loading extends Activity {  
    private ImageView welcomeImg = null;
	private WifiManager mWifiManager;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.login_tupian);  
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);  
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);  
        anima.setDuration(3000);// 设置动画显示时间  
        welcomeImg.startAnimation(anima);  
        anima.setAnimationListener(new AnimationImpl());  
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
    }  
  
  //当程序运行时，打开wifi
  	 private void openWifi() {  
  	       if (!mWifiManager.isWifiEnabled()) {  
  	    	   mWifiManager.setWifiEnabled(true);  
  	       }    
  	    } 
    private class AnimationImpl implements AnimationListener {  
  
        @Override  
        public void onAnimationStart(Animation animation) {  
            welcomeImg.setBackgroundResource(R.drawable.welcome_pic);  
        }  
  
        @Override  
        public void onAnimationEnd(Animation animation) {  
            skip(); // 动画结束后跳转到别的页面  
        }  
  
        @Override  
        public void onAnimationRepeat(Animation animation) {  
  
        }  
  
    }  
  
    private void skip() {  
        startActivity(new Intent(this, login.class));  
        finish();  
    }  
}  
