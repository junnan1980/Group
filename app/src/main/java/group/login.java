package group;

import java.text.SimpleDateFormat;
import java.util.Date;

import wifi_p2p.WiFiServiceDiscoveryActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.group.R;

public class login extends Activity {

	private Button LoginButton;
	private EditText LoginNum;
	private EditText LoginPwd;
	private Spinner spinner_group;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public MyDatabaseHelper dbHelper;
    private SimpleDateFormat df;
	private String data;
	private WifiManager mWifiManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		//����һ�����ݿ⣬ʹÿ��ѧ��ǩ���������¼������
		dbHelper=new MyDatabaseHelper(login.this,"class.db3",1);
		LoginButton = (Button) this.findViewById(R.id.qqLoginButton);
		LoginNum=(EditText)this.findViewById(R.id.qqNum);
		LoginPwd=(EditText)this.findViewById(R.id.qqPassword);
		spinner_group=(Spinner)findViewById(R.id.GroupId);
		  String[] arr={"��һ��","�ڶ���","������","������","������","������","������","�ڰ���","�ھ���","��ʮ��"};
		  //����ArrayAdapt����
		  ArrayAdapter<String> adapt=new ArrayAdapter<String>(this,R.layout.info_list,arr);
		  //ΪSpinner����Adapter
		  spinner_group.setAdapter(adapt);
		  //��ȡֻ�ܱ���Ӧ�ó����д��SharedPreferences����
		  preferences=getSharedPreferences("userInfo",0);
		  editor=preferences.edit();
		  //�����¼��ť
		 LoginButton.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				//�����û���Ϣ
				String studentId=LoginNum.getText().toString();
				String Name=LoginPwd.getText().toString();
				String GroupId=spinner_group.getSelectedItem().toString();
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
				data=df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
				String  mac=getLocalMacAddress();
				dbHelper.getWritableDatabase().execSQL("insert into student values(NULL,'"+studentId+"','"+Name+"','"+GroupId+"','"+data+"','"+mac+"');");
				//����ѧ����Ϣ
				editor.putString("studentId", studentId);
				editor.putString("studentName", Name);
				editor.putString("GroupId", GroupId);
				editor.putString("macAddress", mac);
				editor.commit();
				Intent it = new Intent(login.this, MainActivity.class);
				startActivity(it);
				finish();
			}
		});
	}
	 public String getLocalMacAddress()
	    {
	        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	        WifiInfo info = wifi.getConnectionInfo();
	        return info.getMacAddress();
	    }

}