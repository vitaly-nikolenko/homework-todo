package lv.tsi.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lv.tsi.todolist.db.ItemDataSource;
import lv.tsi.todolist.db.ToDoItem;

public class MainActivity extends AppCompatActivity {
    public static final String TODO_ITEM = "lv.tsi.todolist.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String LIST_COLOR = "listColor";

    private List<ToDoItem> items = new ArrayList<>();
    private ArrayAdapter<ToDoItem> itemsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        itemsAdapter = new CustomArrayAdapter(this, R.layout.todo_item, items);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                intent.putExtra(TODO_ITEM, items.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void addNewItem(View view) {
        EditText editText = (EditText) findViewById(R.id.newItemText);
        String str = editText.getText().toString();
        if (str.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
        } else {
            ItemDataSource dataSource = new ItemDataSource(this);
            dataSource.open();
            ToDoItem toDoItem = dataSource.createItem(str);
            dataSource.close();
            itemsAdapter.add(toDoItem);
            editText.setText("");
        }
    }

    public void removeItem(final View view) {
        final int index = (int) view.findViewById(R.id.deleteItem).getTag();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure you want to delete record?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToDoItem toDoItem = items.get(index);
                ItemDataSource dataSource = new ItemDataSource(MainActivity.this);
                dataSource.open();
                dataSource.deleteItem(toDoItem);
                dataSource.close();
                itemsAdapter.remove(toDoItem);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();


    }

    public void checkItem(View view) {
        CheckBox checkBox = (CheckBox) view;
        ToDoItem toDoItem = items.get((int) checkBox.getTag());
        toDoItem.setChecked(checkBox.isChecked());
        ItemDataSource dataSource = new ItemDataSource(MainActivity.this);
        dataSource.open();
        dataSource.updateItem(toDoItem);
        dataSource.close();
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        listView.setBackgroundColor(getColor());
        new ToDoItemLoadingTask().execute();

        super.onResume();
    }

    private int getColor() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt(LIST_COLOR, getResources().getColor(R.color.white, getTheme()));
    }

    private class ToDoItemLoadingTask extends AsyncTask<Void, Void, List<ToDoItem>> {
        ItemDataSource dataSource;
        @Override
        protected List<ToDoItem> doInBackground(Void... params) {
            dataSource = new ItemDataSource(MainActivity.this);
            dataSource.open();
            return dataSource.getAllToDoItems();
        }

        @Override
        protected void onPostExecute(List<ToDoItem> result) {
            super.onPostExecute(result);
            dataSource.close();
            items.clear();
            items.addAll(result);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private class ToDiItemCreationTask extends AsyncTask<String, Void, List<ToDoItem>> {
        ItemDataSource dataSource;
        @Override
        protected List<ToDoItem> doInBackground(String... params) {
            dataSource = new ItemDataSource(MainActivity.this);
            dataSource.open();
            return dataSource.getAllToDoItems();
        }

        @Override
        protected void onPostExecute(List<ToDoItem> result) {
            super.onPostExecute(result);
            dataSource.close();
            items.clear();
            items.addAll(result);
            itemsAdapter.notifyDataSetChanged();
        }
    }

}
