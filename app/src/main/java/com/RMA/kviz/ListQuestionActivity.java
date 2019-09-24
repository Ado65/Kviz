package com.RMA.kviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListQuestionActivity extends AppCompatActivity {

    private static final String TAG="ListQuestionActivity";
    private List<Question> questions;
    private ListView listViewQuestion;
    private DatabaseReference databaseQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_question);
        listViewQuestion = findViewById(R.id.listViewQuestion);
        questions=new ArrayList<>();


        Intent intent =getIntent();
        final String id = intent.getStringExtra("categoryId");
        final String name = intent.getStringExtra("category");
        databaseQuestionList= FirebaseDatabase.getInstance().getReference("qustion_list").child(id);

        getSupportActionBar().setTitle("Popis pitanja");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String categoryId = String.valueOf(id);

        Log.d(TAG, "categoryId: " + id);
        Log.d(TAG, "categoryId: " + categoryId);



        databaseQuestionList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions.clear();
                for(DataSnapshot questionSnapshot : dataSnapshot.getChildren()){
                    Question question = questionSnapshot.getValue(Question.class);
                    questions.add(question);
                }
                ListQuestionsAdapter adapter = new ListQuestionsAdapter(ListQuestionActivity.this,questions);
                listViewQuestion.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question=questions.get(position);
                showQuestionDialog(categoryId,question.getQustionId(),question.getQustionName(),question.getQustionAnswer());
            }
        });
    }

    private void showQuestionDialog(final String categoryId, final String qustionId,String qustionName,String qustionAnswer){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.qustion_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText inputUpdateQuestion = dialogView.findViewById(R.id.inputUpdateQuestion);
        final EditText inputUpdateAnswer = dialogView.findViewById(R.id.inputUpdateAnswer);
        final Button btnApplayUpdateQuestion = dialogView.findViewById(R.id.btnApplayUpdateQuestion);
        final Button btnApplayDeleteQustion = dialogView.findViewById(R.id.btnApplayDeleteQustion);
        inputUpdateQuestion.setText(qustionName);
        inputUpdateAnswer.setText(qustionAnswer);



        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();

        btnApplayUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qustion = inputUpdateQuestion.getText().toString().trim();
                String answer = inputUpdateAnswer.getText().toString().trim();

                if(TextUtils.isEmpty(qustion)){
                    inputUpdateQuestion.setError("Polje je prazno");
                    return;
                }
                if(TextUtils.isEmpty(answer)){
                    inputUpdateAnswer.setError("Polje je prazno");
                    return;
                }
                updateCategory(categoryId,qustionId,qustion,answer);
                alertDialog.dismiss();
            }
        });

        btnApplayDeleteQustion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(categoryId,qustionId);
                alertDialog.dismiss();
            }
        });


    }

    private void deleteCategory(String categoryId, String qustionId) {
        DatabaseReference dtQuestions= FirebaseDatabase.getInstance().getReference("qustion_list").child(categoryId).child(qustionId);
        dtQuestions.removeValue();
        Toast.makeText(this, "Podaci su obrisani", Toast.LENGTH_LONG).show();

    }

    private void updateCategory(String categoryId, String qustionId,String qustion, String answer){
        databaseQuestionList= FirebaseDatabase.getInstance().getReference("qustion_list").child(categoryId).child(qustionId);
        Question question= new Question(qustionId, qustion,answer);
        databaseQuestionList.setValue(question);
        Toast.makeText(this, "Podaci su pohranjeni", Toast.LENGTH_LONG).show();
    }

}
