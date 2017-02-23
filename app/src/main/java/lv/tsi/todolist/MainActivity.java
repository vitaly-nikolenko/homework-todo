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

public class MainActivity extends AppCompatActivity {
    private static final String KEY_TEXT_VALUE = "textValue";

    private ArrayList<ToDoItem> items = new ArrayList<>();
    private ArrayAdapter<ToDoItem> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            items = savedInstanceState.getParcelableArrayList(KEY_TEXT_VALUE);
        } else {
            items.add(createToDo("Item 1", false));
            items.add(createToDo("Item 2", true));
        }
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
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.text = str;
            toDoItem.checked = false;
            itemsAdapter.add(toDoItem);
            editText.setText("");
        }
    }

    public void removeItem(View view) {
        items.remove((int)view.findViewById(R.id.deleteItem).getTag());
        itemsAdapter.notifyDataSetChanged();
    }

    public void checkItem(View view) {
        CheckBox checkBox = (CheckBox)view;
        ToDoItem toDoItem = items.get((int) checkBox.getTag());
        toDoItem.checked = checkBox.isChecked();
        itemsAdapter.notifyDataSetChanged();
    }

    private ToDoItem createToDo(String text, Boolean checked) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.text = text;
        toDoItem.checked = checked;
        return toDoItem;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TEXT_VALUE, items);
    }
}
