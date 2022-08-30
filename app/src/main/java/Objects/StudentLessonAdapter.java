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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
        TextView startDateTextView = currentItemView.findViewById(R.id.lessonListStudentStart);
        TextView endDateTextView = currentItemView.findViewById(R.id.lessonListStudentEnd);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        nameTextView.setText(lesson.getTeacher().getUsername());
        subjectTextView.setText(lesson.getTeacher().getSubject());
        priceTextView.setText(String.valueOf(lesson.getTeacher().getPrice()));
        startDateTextView.setText("Start Date\n"+format.format(lesson.getStartDate()));
        endDateTextView.setText("End Date\n"+format.format(lesson.getEndDate()));
        // then return the recyclable view
        return currentItemView;
    }

}
