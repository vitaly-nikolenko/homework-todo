package lv.tsi.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private List<ToDoItem> items = new ArrayList<>();
    private ArrayAdapter<ToDoItem> itemsAdapter;
    private ItemDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new ItemDataSource(this);
        dataSource.open();

        items = dataSource.getAllToDoItems();

        itemsAdapter = new CustomArrayAdapter(this, R.layout.todo_item, items);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        "ListItem number " + position + " clicked",
                        Toast.LENGTH_SHORT).show();
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
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
