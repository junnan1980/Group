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
 * author:周炎强
 * date:2015-2-15
 * introduce:查看成员历史到课记录
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
		 // 得到ActionBar
     	ActionBar actionBar = getActionBar();
     	// 设置ActionBar的标题
     	actionBar.setTitle("查看成员历史到课记录");
     	// 设置ActionBar的背景
     	actionBar.setBackgroundDrawable(getResources().getDrawable(
     				R.drawable.actionbar_background));
		//得到对数据操作的权限
		dbHelper=new MyDatabaseHelper(Lookup_history_record.this,"class.db3", 1);
		lv=(ListView)this.findViewById(R.id.lv);
		Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select * from student", null);
		List<Map<String,String>> list=converCursorToList(cursor);
		SimpleAdapter adapter=new SimpleAdapter(Lookup_history_record.this,list,R.layout.list_show_qiandao_table,new String[] {"student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
		//填充ListView
		lv.setAdapter(adapter);
		//排序
		btnScort=(Button)this.findViewById(R.id.btnScort);
		btnScort.setOnClickListener(new View.OnClickListener() {
			private TextView Scort_Num_tex;
			private TextView Scort_ChiDaoNum_tex;
			@Override
			public void onClick(View v) {
				//装载R.layout.textview_list布局文件
				LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View pop_view = mLayoutInflater.inflate(R.layout.textview_list, null);
				//创建PopupWindow对象
				final PopupWindow popup=new PopupWindow(pop_view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);				popup.setFocusable(true);
				
				 //需要顺利让PopUpWindow dimiss（即点击PopuWindow之外的地方此或者back键PopuWindow会消失）；
				// PopUpWindow的背景不能为空。必须在popuWindow.showAsDropDown(v);或者其它的显示PopuWindow方法之
				// 前设置它的背景不为空：
				 
				popup.setBackgroundDrawable(new ColorDrawable(0));
				//以下拉方式显示
				popup.showAsDropDown(v);
				//将PopuWindow显示在指定的位置
			    popup.showAtLocation(btnScort,Gravity.CENTER, 20, 20);
			    Scort_Num_tex=(TextView)pop_view.findViewById(R.id.tv1);
			    //点击按学号排序时，触发该事件
			    Scort_Num_tex.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//insertData(dbHelper.getReadableDatabase(),"Mary","124535067");
						// 按学号排序代码
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
						//Toast.makeText(getActivity(), "您点击了学号排序", 1000).show();
						popup.dismiss();	
					}
				});
			    
			    Scort_ChiDaoNum_tex=(TextView)pop_view.findViewById(R.id.tv2);
			    //点击按签到时间排序时，触发该事件
			    Scort_ChiDaoNum_tex.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 按次数排序代码
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
		//查询
		btnSelect=(Button)this.findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new View.OnClickListener() {
			private SearchView select_Num_tex;
			private SearchView select_Name_tex;

			@Override
			public void onClick(View v) {
				//装载R.layout.textview_list布局文件
				LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View pop_view = mLayoutInflater.inflate(R.layout.textview_select, null);
				//创建PopupWindow对象
				final PopupWindow popup2=new PopupWindow(pop_view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				popup2.setFocusable(true);
				popup2.setBackgroundDrawable(new ColorDrawable(0));
				//以下拉方式显示
				popup2.showAsDropDown(v);
				//将PopuWindow显示在指定的位置
			    popup2.showAtLocation(btnScort,Gravity.CENTER, 20, 20);
			    //按学号进行查询
			    select_Num_tex=(SearchView)pop_view.findViewById(R.id.tv1);
			    select_Num_tex.setQueryHint("按学号进行查询");
			    select_Num_tex.setIconifiedByDefault(false);
			    //为该SearchView设置事件监听
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
			    //按姓名进行查询
			    select_Name_tex=(SearchView)pop_view.findViewById(R.id.tv2);
			    select_Name_tex.setQueryHint("按姓名进行查询");
			    select_Name_tex.setIconifiedByDefault(false);
			    //为该SearchView设置事件监听
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
	//对数据库进行查询语句(根据姓名)
	private void selectData(String name)
	{
		Cursor curous=dbHelper.getReadableDatabase().rawQuery("select * from student where student_name like ?", new String[] {name});
		List<Map<String,String>> list=converCursorToList(curous);
		SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.list_show_qiandao_table,new String[] { "student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
		//填充ListView
		lv.setAdapter(adapter);
	}
	//对数据库进行查询语句(根据学号)
		private void selectDataNum(String Num)
		{
			Cursor curous=dbHelper.getReadableDatabase().rawQuery("select * from student where student_id like ?", new String[] {Num});
			List<Map<String,String>> list=converCursorToList(curous);
			SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.list_show_qiandao_table,new String[] { "student_id","student_name","GroupId","qiaodao_date"},new int[] {R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4});
			//填充ListView
			lv.setAdapter(adapter);
		}
	//填充ListView数据
	protected  ArrayList<Map<String ,String>> converCursorToList(Cursor cursor)
	{
		ArrayList<Map<String ,String>> result=new ArrayList<Map<String ,String>>();
		//遍历Cursor的数据集
		while(cursor.moveToNext())
		{
			//讲结果集中的数据填充到ArrayList中去
			Map<String,String> map=new HashMap<String,String>();
			//取出查询记录中第2列和第三列的数据
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
		//退出程序时关闭MyDatabaseHelper里的	SQLiteDatabase
		if(dbHelper!=null)
		{
			dbHelper.close();
		}
	}
}
