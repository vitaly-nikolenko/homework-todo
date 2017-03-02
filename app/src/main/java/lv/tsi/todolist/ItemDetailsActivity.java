package lv.tsi.todolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import lv.tsi.todolist.db.ItemDataSource;
import lv.tsi.todolist.db.ToDoItem;

import static android.R.id.message;

public class ItemDetailsActivity extends AppCompatActivity {
    private ItemDataSource dataSource;
    private ToDoItem toDoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_item_details);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        Intent intent = getIntent();
        Long id = intent.getLongExtra(MainActivity.TODO_ITEM, 1L);

        dataSource = new ItemDataSource(this);
        dataSource.open();
        toDoItem = dataSource.getToDoItem(id);

        EditText title = (EditText) findViewById(R.id.itemTitle);
        title.setText(toDoItem.getTitle());

        EditText description = (EditText) findViewById(R.id.description);
        description.setText(toDoItem.getDescription());
    }

    public void saveChanges(View view) {
        EditText titleEdit = (EditText) findViewById(R.id.itemTitle);
        String title = titleEdit.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(ItemDetailsActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
        } else {
            EditText editText = (EditText)findViewById(R.id.description);
            toDoItem.setDescription(editText.getText().toString());
            toDoItem.setTitle(title);

            dataSource.updateItem(toDoItem);
            Toast.makeText(ItemDetailsActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareOnTwitter(View view) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(toDoItem.getTitle());
        builder.show();
    }

    public void shareText(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, toDoItem.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, toDoItem.getDescription());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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