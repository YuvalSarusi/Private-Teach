package Objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.privateteach.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherSelectDialog extends AppCompatDialogFragment {

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View view;
    private PassInfoListener listener;

    private EditText usernameEditText;
    private EditText priceEditText;
    private Spinner subjectSpinner;

    private String selectedSubject;
    private String username;
    private String price;



    public interface PassInfoListener{
        void applyInfo(String username, String subject, String price);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PassInfoListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()  +
                    "must implement PassInfoListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.teacher_search_layout,null);
        setSpinner();
        setEditTexts();
        builder.setView(view)
                .setTitle("Search Teacher")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        username = usernameEditText.getText().toString();
                        price = priceEditText.getText().toString();
                        listener.applyInfo(username,selectedSubject,price);
                    }
                });
        return builder.create();
    }

    private void setSpinner(){
        subjectSpinner = view.findViewById(R.id.filterTeacherSpinner);
        List<String> options = new ArrayList<>();
        options.add("All");
        options.addAll(Arrays.asList(getResources().getStringArray(R.array.subjects)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(arrayAdapter);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = arrayAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //there is a default value
                selectedSubject = "";
            }
        });
    }

    private void setEditTexts(){
        usernameEditText = view.findViewById(R.id.filterTeacherUsername);
        priceEditText = view.findViewById(R.id.filterTeacherPrice);
    }
}
