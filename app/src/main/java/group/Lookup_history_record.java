package group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.group.R;

/*
 * author:����ǿ
 * date:2015-2-15
 * introduce:�鿴��Ա��ʷ���μ�¼
 */

public class Lookup_history_record extends Activity{
	private MyDatabaseHelper dbHelper;
	private ListView lv;
	private Button btnScort;
	private Button btnSelect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_qiandao_table);
		 // �õ�ActionBar
     	ActionBar actionBar = getActionBar();
     	// ����ActionBar�ı���
     	actionBar.setTitle("�鿴��Ա��ʷ���μ�¼");
     	// ����ActionBar�ı���
     	actionBar.setBackgroundDrawable(getResources().getDrawable(
     				R.drawable.actionbar_background));
		//�õ������ݲ�����Ȩ��
		dbHelper=new MyDatabaseHelper(Lookup_history_record.this,"class.db3", 1);
		lv=(ListView)this.findViewById(R.id.lv);
		Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select * from student", null);
		List<Map<String,String>> list=converCursorToList(cursor);
		SimpleAdapter adapter=new SimpleAdapter(Lookup_history_record.this,list,R.layout.list_show_qiandao_table,new String[] {"student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
		//���ListView
		lv.setAdapter(adapter);
		//����
		btnScort=(Button)this.findViewById(R.id.btnScort);
		btnScort.setOnClickListener(new View.OnClickListener() {
			private TextView Scort_Num_tex;
			private TextView Scort_ChiDaoNum_tex;
			@Override
			public void onClick(View v) {
				//װ��R.layout.textview_list�����ļ�
				LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View pop_view = mLayoutInflater.inflate(R.layout.textview_list, null);
				//����PopupWindow����
				final PopupWindow popup=new PopupWindow(pop_view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);				popup.setFocusable(true);
				
				 //��Ҫ˳����PopUpWindow dimiss�������PopuWindow֮��ĵط��˻���back��PopuWindow����ʧ����
				// PopUpWindow�ı�������Ϊ�ա�������popuWindow.showAsDropDown(v);������������ʾPopuWindow����֮
				// ǰ�������ı�����Ϊ�գ�
				 
				popup.setBackgroundDrawable(new ColorDrawable(0));
				//��������ʽ��ʾ
				popup.showAsDropDown(v);
				//��PopuWindow��ʾ��ָ����λ��
			    popup.showAtLocation(btnScort,Gravity.CENTER, 20, 20);
			    Scort_Num_tex=(TextView)pop_view.findViewById(R.id.tv1);
			    //�����ѧ������ʱ���������¼�
			    Scort_Num_tex.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//insertData(dbHelper.getReadableDatabase(),"Mary","124535067");
						// ��ѧ���������
						Cursor cursor = dbHelper
								.getReadableDatabase()
								.rawQuery(
										"select * from student order by student_id asc  ",
										null);
						List list = null;
						list = converCursorToList(cursor);
						SimpleAdapter adapter = new SimpleAdapter(
								Lookup_history_record.this, list, R.layout.list_show_qiandao_table,
								new String[] { "student_id","student_name","GroupId","qiaodao_date"}, 
								new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
						lv.setAdapter(adapter);
						//Toast.makeText(getActivity(), "�������ѧ������", 1000).show();
						popup.dismiss();	
					}
				});
			    
			    Scort_ChiDaoNum_tex=(TextView)pop_view.findViewById(R.id.tv2);
			    //�����ǩ��ʱ������ʱ���������¼�
			    Scort_ChiDaoNum_tex.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// �������������
						Cursor cursor = dbHelper
								.getReadableDatabase()
								.rawQuery(
										"select * from student order by date desc  ",
										null);
						List list = converCursorToList(cursor);
						SimpleAdapter adapter = new SimpleAdapter(
								Lookup_history_record.this, list,
								R.layout.list_show_qiandao_table, new String[] {
										"student_id", "student_name",
										"GroupId","qiaodao_date"}, new int[] {
									R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4 });
						lv.setAdapter(adapter);
						popup.dismiss();
					}
				});   
			}
			});
		//��ѯ
		btnSelect=(Button)this.findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new View.OnClickListener() {
			private SearchView select_Num_tex;
			private SearchView select_Name_tex;

			@Override
			public void onClick(View v) {
				//װ��R.layout.textview_list�����ļ�
				LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View pop_view = mLayoutInflater.inflate(R.layout.textview_select, null);
				//����PopupWindow����
				final PopupWindow popup2=new PopupWindow(pop_view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				popup2.setFocusable(true);
				popup2.setBackgroundDrawable(new ColorDrawable(0));
				//��������ʽ��ʾ
				popup2.showAsDropDown(v);
				//��PopuWindow��ʾ��ָ����λ��
			    popup2.showAtLocation(btnScort,Gravity.CENTER, 20, 20);
			    //��ѧ�Ž��в�ѯ
			    select_Num_tex=(SearchView)pop_view.findViewById(R.id.tv1);
			    select_Num_tex.setQueryHint("��ѧ�Ž��в�ѯ");
			    select_Num_tex.setIconifiedByDefault(false);
			    //Ϊ��SearchView�����¼�����
			    select_Num_tex.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String query) {
						return false;
					}
					@Override
					public boolean onQueryTextChange(String newText) {
						selectDataNum(newText);
						return false;
					}
				});
			    //���������в�ѯ
			    select_Name_tex=(SearchView)pop_view.findViewById(R.id.tv2);
			    select_Name_tex.setQueryHint("���������в�ѯ");
			    select_Name_tex.setIconifiedByDefault(false);
			    //Ϊ��SearchView�����¼�����
			    select_Name_tex.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String query) {
						return false;
					}
					@Override
					public boolean onQueryTextChange(String newText) {
						// TODO Auto-generated method stub
						selectData(newText);
						return false;
					}
				});
			}
		});
	}
	//�����ݿ���в�ѯ���(��������)
	private void selectData(String name)
	{
		Cursor curous=dbHelper.getReadableDatabase().rawQuery("select * from student where student_name like ?", new String[] {name});
		List<Map<String,String>> list=converCursorToList(curous);
		SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.list_show_qiandao_table,new String[] { "student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
		//���ListView
		lv.setAdapter(adapter);
	}
	//�����ݿ���в�ѯ���(����ѧ��)
		private void selectDataNum(String Num)
		{
			Cursor curous=dbHelper.getReadableDatabase().rawQuery("select * from student where student_id like ?", new String[] {Num});
			List<Map<String,String>> list=converCursorToList(curous);
			SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.list_show_qiandao_table,new String[] { "student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
			//���ListView
			lv.setAdapter(adapter);
		}
	//���ListView����
	protected  ArrayList<Map<String ,String>> converCursorToList(Cursor cursor)
	{
		ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
		//����Cursor�����ݼ�
		while(cursor.moveToNext())
		{
			//��������е�������䵽ArrayList��ȥ
			Map<String,String> map=new HashMap<String,String>();
			//ȡ����ѯ��¼�е�2�к͵����е�����
			map.put("student_id",cursor.getString(1));
			map.put("student_name",cursor.getString(2));
			map.put("GroupId", cursor.getString(3));
			map.put("qiaodao_date", cursor.getString(4));
			result.add(map);
		}
		return result;
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		//�˳�����ʱ�ر�MyDatabaseHelper���	SQLiteDatabase
		if(dbHelper!=null)
		{
			dbHelper.close();
		}
	}
}
