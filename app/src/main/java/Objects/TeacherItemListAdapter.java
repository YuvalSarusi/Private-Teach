package Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.privateteach.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class TeacherItemListAdapter extends ArrayAdapter<Teacher> {


    public TeacherItemListAdapter(@NonNull Context context, @NonNull List<Teacher> teachers) {
        super(context, 0, teachers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.teacher_list_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Teacher teacher = getItem(position);
        TextView nameTextView = currentItemView.findViewById(R.id.teacherListItemName);
        TextView subjectTextView = currentItemView.findViewById(R.id.teacherListItemSubject);
        TextView priceTextView = currentItemView.findViewById(R.id.teacherListItemPrice);
        nameTextView.setText(teacher.getUsername());
        subjectTextView.setText(teacher.getSubject());
        priceTextView.setText(String.valueOf(teacher.getPrice()));
        // then return the recyclable view
        return currentItemView;
    }

}
