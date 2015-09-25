package group;

import wifi_p2p.WiFiServiceDiscoveryActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.group.R;


public class MainActivity extends ActionBarActivity {
	
	//SharedPreferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
	private SharedPreferences preferencesToself;
	private Editor editorToself;
	private MyDatabaseHelper dbHelper;
	//学生到课信息汇报老师
	private SharedPreferences preferencesToteacher;
    private SharedPreferences.Editor editorToteacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_leader_mainui);
        //获取对数据库的操作权限
        dbHelper=new MyDatabaseHelper(MainActivity.this,"class.db3",1);
        //获取只能被本应用程序读写的SharedPreferences对象
		  preferences=getSharedPreferences("tag",0);
		  editor=preferences.edit();
		  editor.putInt("Tag",0);
		  editor.commit();
		  //上报老师的控制信息
		   preferencesToteacher=getSharedPreferences("teacher", 0);
		   editorToteacher=preferencesToteacher.edit();
		   editorToteacher.putInt("reportInfo",0);
		   editorToteacher.commit();
        // 得到ActionBar
     	ActionBar actionBar = getActionBar();
     	// 设置ActionBar的标题
     	actionBar.setTitle("签到");
     	// 设置ActionBar的背景
     	actionBar.setBackgroundDrawable(getResources().getDrawable(
     				R.drawable.actionbar_background));
     	//收集成员到课信息
        ImageButton show_student_attend=(ImageButton)this.findViewById(R.id.show_student_attend);
        show_student_attend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent=new Intent(MainActivity.this,wifi_p2p.WiFiServiceDiscoveryActivity.class);  
	                //启动Intent对应的Activity  
	                startActivity(intent);   
			}
		});
        //查看成员到课历史信息
        ImageButton check_history_redord=(ImageButton)this.findViewById(R.id.check_data_table);
        check_history_redord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(MainActivity.this,Lookup_history_record.class);  
                //启动Intent对应的Activity  
                startActivity(intent);  
			}
		});
       //显示本次成员到课情况
        ImageButton show_class_attend=(ImageButton)this.findViewById(R.id.show_class_attend);
        show_class_attend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,show_present_attend_class.class);  
                //启动Intent对应的Activity  
                startActivity(intent);  	
			}
		});
        //本次上课情况汇报老师
        ImageButton report_people_teacher=(ImageButton)this.findViewById(R.id.report_peopleto_teacher);
        report_people_teacher.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int tagReport=preferencesToteacher.getInt("reportInfo",0);
				if(tagReport==0)
				{
					editorToteacher.putInt("reportInfo",1);
					editorToteacher.commit();
				}
				 Intent intent=new Intent(MainActivity.this,wifi_p2p.WiFiServiceDiscoveryActivity.class);  
	                //启动Intent对应的Activity  
	                startActivity(intent);   
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
        /*if (id == R.id.action_settings) {
        	editor.putInt("Tag",1);
        	editor.commit();
        	 Intent intent=new Intent(MainActivity.this,wifi_p2p.WiFiServiceDiscoveryActivity.class);  
             //启动Intent对应的Activity  
             startActivity(intent);
            return true;
        }*/
        if (id == R.id.action_setting) {
        	editor.putInt("Tag",1);
        	editor.commit();
            preferencesToself=getSharedPreferences("userInfo",0);
   		    editorToself=preferences.edit();
   		    String studentId=preferencesToself.getString("studentId",null);
   		    String studentName=preferencesToself.getString("studentName",null);
   		    String studentGroupId=preferencesToself.getString("GroupId", null);
   		    String studentMac=preferencesToself.getString("macAddress", null);
   		    Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select * from studentAll where student_id='"+studentId+"'", null);
   		    int count=cursor.getCount();
   		    if(count==0)
   		    {
   		     dbHelper.getWritableDatabase().execSQL("insert into studentAll values(NULL,'"+studentId+"','"+studentName+"','"+studentGroupId+"',null,null,null,'"+studentMac+"');");
   		    }
   		    Intent intent=new Intent(MainActivity.this,wifi_p2p.WiFiServiceDiscoveryActivity.class);  
             //启动Intent对应的Activity  
             startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
