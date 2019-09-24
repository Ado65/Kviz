package com.RMA.kviz;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListQuestionsAdapter extends ArrayAdapter<Question> {

    private Activity context;
    private List<Question> questions;


    public ListQuestionsAdapter(Activity context, List<Question> questions){
        super(context, R.layout.list_question_layout, questions);
        this.context=context;
        this.questions=questions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_question_layout,null,true);

        TextView textView5= listViewItem.findViewById(R.id.textView5);
        TextView textView6= listViewItem.findViewById(R.id.textView6);

        Question question = questions.get(position);

        textView5.setText("Pitanje:   "+question.getQustionName());
        textView6.setText("Odgovor: "+question.getQustionAnswer());
        return listViewItem;
    }
}
