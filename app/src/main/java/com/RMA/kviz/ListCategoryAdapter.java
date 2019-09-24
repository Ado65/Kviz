package com.RMA.kviz;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListCategoryAdapter extends ArrayAdapter<ListCategory> {

    private static final String TAG="ListCategoryAdapter";
    private Activity context;
    private List<ListCategory> categoryList;

    public ListCategoryAdapter(Activity context, List<ListCategory> categoryList){
        super(context,R.layout.list_category_layout,categoryList);
        this.context=context;
        this.categoryList=categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_category_layout,null,true);

        TextView textView1= listViewItem.findViewById(R.id.textView1);


        ListCategory listCategory = categoryList.get(position);


        textView1.setText(listCategory.getCategory());
        return listViewItem;
    }
}
