package group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.group.R;

public class show_present_attend_class extends Activity{
	private MyDatabaseHelper dbHelper;
	private ListView lv;
	private SimpleDateFormat df;
	private String data;
	private ListView lv1;
	private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_present_attent_class);
		 // �õ�ActionBar
     	ActionBar actionBar = getActionBar();
     	// ����ActionBar�ı���
     	actionBar.setTitle("���γ�Ա�������");
     	// ����ActionBar�ı���
     	actionBar.setBackgroundDrawable(getResources().getDrawable(
     				R.drawable.actionbar_background));
     	df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		data=df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
     	//�õ������ݲ�����Ȩ��
     	dbHelper=new MyDatabaseHelper(show_present_attend_class.this,"class.db3", 1);
     	lv=(ListView)this.findViewById(R.id.lv);
     	Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select * from student where date like '"+data+"%'", null);
     	List<Map<String,String>> list=converCursorToList(cursor);
     	SimpleAdapter adapter=new SimpleAdapter(show_present_attend_class.this,list,R.layout.list_show_qiandao_table,new String[] {"student_id","student_name","group_Num","date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
     	//���ListView
     	lv.setAdapter(adapter);
     	//�ұ����Ͽ�δ����Ա������
     	//˼·���ܱ��еģ����Ǳ�С������г�Ա���뱾�ε���ѧ���ĵ�ѧ�ţ����бȽϣ�
     	//������������ܱ��е��ѵ������м�1��������δ�������м�1������¼δ����Ա����Ϣ
     	dbHelper=new MyDatabaseHelper(show_present_attend_class.this,"class.db3", 1);
     	Cursor cursor1=dbHelper.getReadableDatabase().rawQuery("select * from studentAll", null);
         list=converCursorToInt(cursor,cursor1);
     	   SimpleAdapter adapter1=new SimpleAdapter(show_present_attend_class.this,list,R.layout.list_show_qiandao_table,new String[] {"student_id","student_name",null,null},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
     	    //���ListView
     	   lv1=(ListView)this.findViewById(R.id.lv1);
     	    lv1.setAdapter(adapter1);
	}
	//���ListView����
	public  ArrayList<Map<String ,String>> converCursorToList(Cursor cursor)
	{
		    String yidaoInfo="";
			ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
			//����Cursor�����ݼ�
			while(cursor.moveToNext())
			{
				//��������е�������䵽ArrayList��ȥ
				Map<String,String> map=new HashMap<String,String>();
				//ȡ����ѯ��¼�е�2�к͵����е�����
				map.put("student_id",cursor.getString(1));
				map.put("student_name",cursor.getString(2));
				map.put("group_Num", cursor.getString(3));
				map.put("date", cursor.getString(4));
				yidaoInfo=yidaoInfo+cursor.getString(1)+","+cursor.getString(2)+","+cursor.getString(3)+","+cursor.getString(4)+","+cursor.getString(5)+",1;";
				result.add(map);
			}
			preferences=getSharedPreferences("reportInfo",0);
			editor=preferences.edit();
			editor.putString("yidaoInfo",yidaoInfo);
		   editor.commit();
			return result;
	}
	//���δ����Ա������
	protected  ArrayList<Map<String ,String>> converCursorToInt(Cursor cursor,Cursor cursor1)
	{
			String weidaoInfo="";
			ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
			for(int i=cursor1.getCount()-1;i>=0;i--)
			{
				//���α��ƶ�����һλ��
				cursor1.moveToPosition(i);
				int tag=0;
				//��������е�������䵽ArrayList��ȥ
				Map<String,String> map=new HashMap<String,String>();
				String studentId=cursor1.getString(1).trim();
				int  studentyiDaoNum=cursor1.getInt(4);
				int  studentweiDaoNum=cursor1.getInt(5);
				String detail=cursor1.getString(6);
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
				data=df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
				for(int j=cursor.getCount()-1;j>=0;j--)
				{
					cursor.moveToPosition(j);
					String student_id=cursor.getString(1).trim();
					Log.v("WWWW","WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
					if(studentId.equals(student_id)||studentId==student_id)
					{
						tag=1;
						studentyiDaoNum=studentyiDaoNum+1;
						detail=detail+" "+data;
						dbHelper.getWritableDatabase().execSQL("update studentAll set yiDaoNum="+studentyiDaoNum+",detail='"+detail+"' where student_id='"+studentId+"'");
					}
					else
					{
						
					}
				}
				if(tag==0)
				{
				  //ȡ����ѯ��¼�е�2�к͵����е�����
				  map.put("student_id",cursor1.getString(1));
				  map.put("student_name",cursor1.getString(2));
				  studentweiDaoNum=studentweiDaoNum+1;
				  dbHelper.getWritableDatabase().execSQL("update studentAll set weiDaoNum="+studentweiDaoNum+" where student_id='"+studentId+"'");
				  result.add(map);
				  weidaoInfo=weidaoInfo+cursor1.getString(1)+","+cursor1.getString(2)+","+cursor1.getString(3)+","+data+","+cursor1.getString(7)+",0;";
				}
			}
			preferences=getSharedPreferences("reportInfo",0);
			editor=preferences.edit();
			editor.putString("weidaoInfo",weidaoInfo);
		    editor.commit();
			return result;
	}
}
