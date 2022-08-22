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

import java.util.List;

public class StudentLessonAdapter extends ArrayAdapter<Lesson> {


    public StudentLessonAdapter(@NonNull Context context, @NonNull List<Lesson> lessons) {
        super(context, 0, lessons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_list_student, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Lesson lesson = getItem(position);
        TextView nameTextView = currentItemView.findViewById(R.id.lessonListStudentName);
        TextView subjectTextView = currentItemView.findViewById(R.id.lessonListStudentSubject);
        TextView priceTextView = currentItemView.findViewById(R.id.lessonListStudentPrice);

        nameTextView.setText(lesson.getTeacher().getUsername());
        subjectTextView.setText(lesson.getTeacher().getSubject());
        priceTextView.setText(lesson.getTeacher().getPrice());
        // then return the recyclable view
        return currentItemView;
    }

}
