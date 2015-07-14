package com.leo.notes.util;

import java.util.ArrayList;
import java.util.List;

import com.leo.notes.been.Notes;
import com.leo.notes.db.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DB 操作辅助类
 * 
 * @author 师春雷
 * 
 */
public class DBUtils {

	private static final String TAG = "DBUtils";

	private SQLiteHelper helper = null;
	private SQLiteDatabase database = null;
	private Cursor cursor = null;

	private static final String[] COLLECTIONS = new String[] {
			SQLiteHelper._ID, SQLiteHelper.TITLE, SQLiteHelper.CONTENT,
			SQLiteHelper.TIME, SQLiteHelper.GROUP };

	public DBUtils(Context context) {
		super();
		helper = new SQLiteHelper(context);
	}

	/**
	 * 保存
	 * 
	 * @param notes
	 * @return
	 */
	public boolean insert(Notes notes) {
		boolean flag = false;
		long id = -1;

		try {
			database = helper.getReadableDatabase();
			ContentValues values = new ContentValues();

			values.put(SQLiteHelper.TITLE, notes.getTitle());
			values.put(SQLiteHelper.CONTENT, notes.getContent());
			values.put(SQLiteHelper.TIME, notes.getTime());
			values.put(SQLiteHelper.GROUP, notes.getGroup());

			id = database.insert(SQLiteHelper.T_NOTES, null, values);

			flag = (id != -1 ? true : false);

		} catch (Exception e) {
			Log.v(TAG, "insert.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return flag;
	}

	/**
	 * 根据博客ID查询对应信息（收藏博客详情）
	 * 
	 * @param id
	 * @return
	 */
	public Notes queryById(int id) {
		Notes notes = new Notes();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(true, SQLiteHelper.T_NOTES, COLLECTIONS,
					SQLiteHelper._ID + "='" + id + "'", null, null, null, null,
					null);
			while (cursor.moveToNext()) {
				notes.setTitle(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TITLE)));
				notes.setContent(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CONTENT)));
				notes.setTime(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TIME)));
				notes.setGroup(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.GROUP)));
			}
		} catch (SQLException e) {
			Log.e("", "queryByIdAndType.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		Log.d(TAG, "queryById:" + notes.toString());

		return notes;
	}

	/**
	 * 根据博客ID查询对应信息（收藏博客详情）
	 * 
	 * @param id
	 * @return
	 */
	public Notes queryByGroup(int group) {
		Notes notes = new Notes();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(true, SQLiteHelper.T_NOTES, COLLECTIONS,
					SQLiteHelper.GROUP + "='" + group + "'", null, null, null,
					null, null);
			while (cursor.moveToNext()) {
				notes.setTitle(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TITLE)));
				notes.setContent(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CONTENT)));
				notes.setTime(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TIME)));
				notes.setGroup(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.GROUP)));
			}
		} catch (SQLException e) {
			Log.e("", "queryByGroup.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		Log.d(TAG, "queryById:" + notes.toString());

		return notes;
	}

	/**
	 * 查询所有数据（收藏列表）
	 * 
	 * @return（List数据）
	 */
	public List<Notes> queryAll() {
		List<Notes> list = new ArrayList<Notes>();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(SQLiteHelper.T_NOTES, COLLECTIONS, null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				Notes notes = new Notes();
				notes.setId(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper._ID)));

				notes.setTitle(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TITLE)));
				notes.setContent(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.CONTENT)));
				notes.setTime(cursor.getString(cursor
						.getColumnIndexOrThrow(SQLiteHelper.TIME)));
				notes.setGroup(cursor.getInt(cursor
						.getColumnIndexOrThrow(SQLiteHelper.GROUP)));
				list.add(notes);
			}
		} catch (SQLException e) {
			Log.e("", "queryAll.SQLException");
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		Log.d(TAG, "queryAll:" + list.toString());

		return list;
	}

	/**
	 * 删除表内信息
	 * 
	 */
	public boolean deleteAll() {
		boolean flag = false;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLiteHelper.T_NOTES, null, null);

			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			Log.e(TAG, "deleteAll.SQLException");
			Log.e(TAG, e.getMessage());
		} finally {
			closeDatabase();
		}
		return flag;
	}

	/**
	 * 根据 “id” 删除对应信息
	 * 
	 * @param id
	 */
	public boolean deleteById(int id) {
		boolean flag = false;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLiteHelper.T_NOTES, SQLiteHelper._ID
					+ "='" + id + "'", null);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			Log.e(TAG, "deleteById.SQLException");
			Log.e(TAG, e.getMessage());
		} finally {
			closeDatabase();
		}
		return flag;
	}

	// 关闭数据库
	private void closeDatabase() {
		if (database != null) {
			database.close();
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
}
