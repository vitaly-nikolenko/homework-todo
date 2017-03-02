package lv.tsi.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE,
            MySQLiteHelper.COLUMN_CHECKED,
            MySQLiteHelper.COLUMN_DESCRIPTION};

    public ItemDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ToDoItem createItem(String text) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TITLE, text);
        long insertId = database.insert(MySQLiteHelper.TABLE_ITEMS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ToDoItem newComment = cursorToItem(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteItem(ToDoItem toDoItem) {
        long id = toDoItem.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ITEMS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<ToDoItem> getAllToDoItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ToDoItem toDoItem = cursorToItem(cursor);
            toDoItems.add(toDoItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return toDoItems;
    }

    private ToDoItem cursorToItem(Cursor cursor) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setId(cursor.getLong(0));
        toDoItem.setTitle(cursor.getString(1));
        toDoItem.setChecked(cursor.getInt(2) == 1);
        toDoItem.setDescription(cursor.getString(3));
        return toDoItem;
    }
}
