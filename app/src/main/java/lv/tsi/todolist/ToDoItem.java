package lv.tsi.todolist;

import android.os.Parcel;
import android.os.Parcelable;

public class ToDoItem implements Parcelable {
    String text;
    Boolean checked;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(text);
        out.writeByte((byte) (checked ? 1 : 0));
    }

    public static final Parcelable.Creator<ToDoItem> CREATOR = new Parcelable.Creator<ToDoItem>() {
        public ToDoItem createFromParcel(Parcel in) {
            return new ToDoItem(in);
        }

        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };

    public ToDoItem() {
    }

    private ToDoItem(Parcel in) {
        text = in.readString();
        checked = in.readByte() != 0;
    }
}
