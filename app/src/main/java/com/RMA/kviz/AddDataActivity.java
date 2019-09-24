package com.RMA.kviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddDataActivity extends AppCompatActivity {
    private static final String TAG="AddDataActivity";

    private TextView textView2,textView10,textView11,textView12;
    private EditText inputQustionName,inputQustionAnswer,imputAnswer;
    private Button btnAddQustion,btnViewListQuestion,btnAnswer;
    private List<Question> questions;
    private DatabaseReference databaseQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data_layout);
        textView2 = findViewById(R.id.textView2);
        inputQustionName = findViewById(R.id.inputQustionName);
        inputQustionAnswer = findViewById(R.id.inputQustionAnswer);
        imputAnswer = findViewById(R.id.imputAnswer);
        btnAddQustion = findViewById(R.id.btnAddQustion);
        btnViewListQuestion = findViewById(R.id.btnViewListQuestion);
        btnAnswer = findViewById(R.id.btnAnswer);
        textView10 = findViewById(R.id.textView10);
        textView11 = findViewById(R.id.textView11);
        textView12 = findViewById(R.id.textView12);
        questions=new ArrayList<>();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent =getIntent();
        final String id = intent.getStringExtra("categoryId");
        Log.d(TAG, "readRow is : "+ intent.getStringExtra("categoryId"));
        final String name = intent.getStringExtra("category");
        textView2.setText(name);
        databaseQuestionList= FirebaseDatabase.getInstance().getReference("qustion_list").child(id);


        getSupportActionBar().setTitle("Kviz " + name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseQuestionList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions.clear();
                for(DataSnapshot questionSnapshot : dataSnapshot.getChildren()){
                    Question question = questionSnapshot.getValue(Question.class);
                    questions.add(question);
              }
                Log.d(TAG, "sizeOfList " + questions.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddQustion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestionList();
            }
        });

        btnViewListQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDataActivity.this, ListQuestionActivity.class);
                intent.putExtra("categoryId",id);
                intent.putExtra("category",name);
                startActivity(intent);
            }
        });
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            int i=0, counter=0;
            @Override
            public void onClick(View v) {
                if(i<questions.size()) {
                    Log.d(TAG ,"Tu sam " + i + "  " + questions.size());
                    btnAnswer.setText("Odgovori");
                    Question question = questions.get(i);
                    counter = checkAnswer(question, i, counter);
                    i++;
                    textView12.setText("Odgovorili ste točno na " + counter + " pitanja od " + questions.size() + " pitanja." + '\n' + " Trenutno se nalazite na " + i + ". pitanju.");
                    imputAnswer.setText("");
                }else if(i==questions.size()){
                    Question question = questions.get(i-1);
                    counter = checkAnswer(question, i, counter);
                    textView12.setText("Odgovorili ste točno na " + counter + " pitanja od " + questions.size() + " pitanja.");
                    imputAnswer.setText("");
                    textView10.setText("Nema više pitanja");
                    i++;
                }
            }
        });



    }

    private int checkAnswer(Question question,int i , int counter ) {

        textView10.setText("Pitanje: "+question.getQustionName());
        String answer = imputAnswer.getText().toString();
        String corectAnswer = question.getQustionAnswer();
        if (corectAnswer.equals(answer)){
            textView11.setTextColor(Color.parseColor("#10D15D"));
          if(i>0){  textView11.setText("Odgovorili ste točno na prošlo pitanje.");
          counter++;
          return counter;
          }return counter;
        }else{
            textView11.setTextColor(Color.RED);
            if(i>0) textView11.setText("Odgovorili ste pogrešno na prošlo pitanje.");
            return counter;
        }
    }

    private void saveQuestionList() {
        String qustionName = inputQustionName.getText().toString().trim();
        String qustionAnswer = inputQustionAnswer.getText().toString().trim();

        if (!TextUtils.isEmpty(qustionName)&&!TextUtils.isEmpty(qustionAnswer)) {
            String id = databaseQuestionList.push().getKey();
            Question question = new Question(id, qustionName,qustionAnswer);
            databaseQuestionList.child(id).setValue(question);
            inputQustionName.setText("");
            inputQustionAnswer.setText("");
            Toast.makeText(this, "Pitanje je dodano", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Polja su prazna ", Toast.LENGTH_LONG).show();
        }
    }

}
