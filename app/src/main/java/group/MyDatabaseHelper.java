package group;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper  extends SQLiteOpenHelper{
	
	
	//建表语句
	final String CREATE_TABLE_SQL="create table student(_id integer primary" +
			" Key autoincrement,student_id,student_name,group_Num,date,MAC_address)";
	//建表语句
	final String CREATE_TABLE_SQL1="create table studentAll(_id integer primary" +
				" Key autoincrement,student_id String,student_name String,group_Num String,yiDaoNum integer,weiDaoNum integer,detail String,MAC_address String)";
	public MyDatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);
		db.execSQL(CREATE_TABLE_SQL1);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("-----onUpdate Called--------"+oldVersion+"------>"+newVersion);
		// Add 'MAC_address' column to student table. 
		//db.execSQL("ALTER TABLE student ADD COLUMN MAC_address TEXT");
	}
}
