package lv.tsi.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<ToDoItem> {
    private int resource;

    public CustomArrayAdapter(Context context, int resource, List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ToDoItem toDoItem = getItem(position);
        if (toDoItem != null) {
            vh.deleteButton.setTag(position);
            vh.textView.setText(toDoItem.text);
            vh.checkBox.setTag(position);
            vh.checkBox.setChecked(toDoItem.checked);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
        Button deleteButton;
        CheckBox checkBox;

        ViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.textView);
            deleteButton = (Button) convertView.findViewById(R.id.deleteItem);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        }
    }
}
