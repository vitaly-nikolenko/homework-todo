package lv.tsi.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    public CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Button deleteButton = (Button) convertView.findViewById(R.id.deleteItem);
        deleteButton.setTag(position);
        vh.textView.setText(getItem(position));

        return convertView;
    }

    private static class ViewHolder {
        TextView textView;

        ViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.textView);
        }
    }
}
