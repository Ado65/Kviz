package com.RMA.kviz;




import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

   private static final String TAG="MainActivity";
    private EditText inputCategoryName;
    private Button btnAddCategory,btnViewListCategory;

    private DatabaseReference databaseCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputCategoryName = findViewById(R.id.inputCategoryName);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnViewListCategory = findViewById(R.id.btnViewListCategory);
        databaseCategory= FirebaseDatabase.getInstance().getReference("category_list");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setTitle("Kviz");
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        btnViewListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void add() {
        String category = inputCategoryName.getText().toString().trim();
        if (!TextUtils.isEmpty(category)) {
           String id = databaseCategory.push().getKey();
           ListCategory listCategory = new ListCategory(id, category);
            databaseCategory.child(id).setValue(listCategory);
            inputCategoryName.setText("");
            Toast.makeText(this, "Kviz je dodan", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Polje je prazno", Toast.LENGTH_LONG).show();
        }
    }
}