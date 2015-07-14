package com.leo.notes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = "SQLiteHelper";

	/** 数据库名 */
	public final static String DB_NAME = "notes.db";
	/** 数据库版本号 */
	private final static int VERSION = 1;
	/** 数据库表 */
	public final static String T_NOTES = "t_notes";

	public static final String _ID = "id";// ID
	public static final String TITLE = "title";// 标题
	public static final String CONTENT = "content";// 内容
	public static final String TIME = "time";// 创建时间
	public static final String GROUP = "group";// 组别
	public static final String GROUP_ID = "group_id";// 组别

	public static final String COVER = "cover";// 封面
	public static final String NAME = "name";

	/** 创建数据库表SQL语句 */
	String sql_notes = "create table if not exists " + T_NOTES + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + TITLE
			+ " varchar(100) , " + CONTENT + " TEXT , " + TIME
			+ " varchar(20) , " + GROUP + " varchar(20))";

	/**
	 * 在SQLiteOpenHelper的子类中，必须有该构造方法
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, version);
	}

	public SQLiteHelper(Context context) {
		this(context, DB_NAME, null, VERSION);
	}

	/**
	 * 数据库的构造方法，用来定义数据库的名称，数据库的查询的结果集，数据库版本。
	 * 
	 * @param context
	 * @param name
	 * @param version
	 * 
	 */
	public SQLiteHelper(Context context, String name, int version) {
		// factory ，游标工厂，设置位null，version数据库版本。
		this(context, DB_NAME, null, version);
	}

	// 该方法是在第一次创建数据库时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Create a Database: " + db.getPath());
		// 创建表
		db.execSQL(sql_notes);
	}

	// 该方法在升级数据库时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrade a Database from version " + oldVersion
				+ " to version " + newVersion);
	}
}
