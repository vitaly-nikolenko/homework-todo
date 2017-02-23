package lv.tsi.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ToDoItem> items = new ArrayList<>();
    private ArrayAdapter<ToDoItem> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items.add(createToDo("Item 1", false));
        items.add(createToDo("Item 2", true));

        itemsAdapter = new CustomArrayAdapter(this, R.layout.todo_item, items);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        "ListItem number "+position+" clicked",
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
            toDoItem.setText(str);
            toDoItem.setChecked(false);
            itemsAdapter.add(toDoItem);
            editText.setText("");
        }
    }

    public void removeItem(View view) {
        items.remove((int) view.findViewById(R.id.deleteItem).getTag());
        itemsAdapter.notifyDataSetChanged();
    }

    public void checkItem(View view) {
        CheckBox checkBox = (CheckBox)view;
        ToDoItem toDoItem = items.get((int) checkBox.getTag());
        toDoItem.setChecked(checkBox.isChecked());
        itemsAdapter.notifyDataSetChanged();
    }

    private ToDoItem createToDo(String text, Boolean checked) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setText(text);
        toDoItem.setChecked(checked);
        return toDoItem;
    }
}
