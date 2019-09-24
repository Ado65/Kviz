package com.RMA.kviz;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG="ListActivity";
    private ListView listViewCategory;
    private DatabaseReference databaseCategory;
    private List<ListCategory> categoryList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listViewCategory=findViewById(R.id.listViewQuestion);
        databaseCategory= FirebaseDatabase.getInstance().getReference("category_list");
        categoryList = new ArrayList<>();


        getSupportActionBar().setTitle("Popis kvizova");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for(DataSnapshot listSnapshot : dataSnapshot.getChildren()){
                    ListCategory listCategory = listSnapshot.getValue(ListCategory.class);

                    categoryList.add(listCategory);
                }
                ListCategoryAdapter adapter = new ListCategoryAdapter(ListActivity.this,categoryList);
                listViewCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListCategory listCategory=categoryList.get(position);
                Intent intent= new Intent(getApplicationContext(),AddDataActivity.class);
                intent.putExtra("categoryId",listCategory.getCategoryId());
                intent.putExtra("category",listCategory.getCategory());

                startActivity(intent);
            }
        });
        listViewCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListCategory listCategory=categoryList.get(position);
                showCategoryDialog(listCategory.getCategoryId(),listCategory.getCategory());
                return true;
            }
        });



    }

    private void showCategoryDialog(final String categoryId, String categoryName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.category_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText inputUpdateCategory = dialogView.findViewById(R.id.inputUpdateCategory);
        final Button button5 = dialogView.findViewById(R.id.btnApplayUpdate);
        final Button button6 = dialogView.findViewById(R.id.btnApplayDelete);

        dialogBuilder.setTitle(categoryName);
        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = inputUpdateCategory.getText().toString().trim();
                Log.d(TAG, "updateCategory: " + category);

                if(TextUtils.isEmpty(category)){
                    inputUpdateCategory.setError("Polje je prazno");
                    return;
                }
                updateCategory(categoryId,category);
                alertDialog.dismiss();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(categoryId);
                alertDialog.dismiss();
            }
        });


    }

    private void deleteCategory(String categoryId) {
        DatabaseReference dtCategory= FirebaseDatabase.getInstance().getReference("category_list").child(categoryId);
        DatabaseReference dtQuestions= FirebaseDatabase.getInstance().getReference("qustion_list").child(categoryId);
        dtCategory.removeValue();
        dtQuestions.removeValue();
        Toast.makeText(this, "Kviz je obrisan", Toast.LENGTH_LONG).show();
            }

    private void updateCategory(String id, String category){
        databaseCategory= FirebaseDatabase.getInstance().getReference("category_list").child(id);
        ListCategory listCategory = new ListCategory(id, category);
        databaseCategory.setValue(listCategory);
        Toast.makeText(this, "Ime kviza je promijenjeno", Toast.LENGTH_LONG).show();
    }

}
