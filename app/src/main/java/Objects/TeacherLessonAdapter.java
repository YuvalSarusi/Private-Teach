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

public class TeacherLessonAdapter extends ArrayAdapter<Lesson> {


    public TeacherLessonAdapter(@NonNull Context context, @NonNull List<Lesson> lessons) {
        super(context, 0, lessons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_list_teacher, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Lesson lesson = getItem(position);
        TextView nameTextView = currentItemView.findViewById(R.id.lessonListTeacherName);
        TextView startTextView = currentItemView.findViewById(R.id.lessonListTeacherStart);
        TextView endTextView = currentItemView.findViewById(R.id.lessonListTeacherEnd);
        if (lesson.getStudent()== null){
            nameTextView.setText("Clear Lesson");
        }
        else{
            nameTextView.setText(lesson.getStudent().getUsername());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        startTextView.setText(simpleDateFormat.format(lesson.getStartDate()));
        endTextView.setText(simpleDateFormat.format(lesson.getEndDate()));
        // then return the recyclable view
        return currentItemView;
    }

}
