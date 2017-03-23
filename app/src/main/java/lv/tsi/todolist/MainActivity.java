package lv.tsi.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private ItemDataSource dataSource;
    private ToDoItemLoadingTask task;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new ItemDataSource(this);
        listView = (ListView) findViewById(R.id.listView);
        task = new ToDoItemLoadingTask();

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
            ToDoItem toDoItem = dataSource.createItem(str);
            itemsAdapter.add(toDoItem);
            editText.setText("");
        }
    }

    public void removeItem(View view) {
        int index = (int) view.findViewById(R.id.deleteItem).getTag();
        ToDoItem toDoItem = items.get(index);
        dataSource.deleteItem(toDoItem);
        itemsAdapter.remove(toDoItem);
    }

    public void checkItem(View view) {
        CheckBox checkBox = (CheckBox) view;
        ToDoItem toDoItem = items.get((int) checkBox.getTag());
        toDoItem.setChecked(checkBox.isChecked());
        dataSource.updateItem(toDoItem);
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
        dataSource.open();

        listView.setBackgroundColor(getColor());

        task.execute();
        super.onResume();
    }

    private int getColor() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt(LIST_COLOR, getResources().getColor(R.color.white, getTheme()));
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    private class ToDoItemLoadingTask extends AsyncTask<Void, Void, List<ToDoItem>> {

        @Override
        protected List<ToDoItem> doInBackground(Void... params) {
            return dataSource.getAllToDoItems();
        }

        @Override
        protected void onPostExecute(List<ToDoItem> result) {
            super.onPostExecute(result);
            items.clear();
            items.addAll(result);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
