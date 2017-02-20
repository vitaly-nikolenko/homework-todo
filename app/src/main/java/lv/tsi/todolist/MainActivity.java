package lv.tsi.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private CustomArrayAdapter adapter;
    private EditText textEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile"};


        adapter = new CustomArrayAdapter(this, R.layout.todo_item, Arrays.asList(values));

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        textEntry = (EditText) findViewById(R.id.editText);
    }

    public void addNewItem(View view) {
        String str = textEntry.getText().toString();
        if (str.length() < 1) {
            Toast.makeText(this, R.string.enter_text_label, Toast.LENGTH_LONG).show();
        } else {
            adapter.add(str);
            textEntry.setText("");
            runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
