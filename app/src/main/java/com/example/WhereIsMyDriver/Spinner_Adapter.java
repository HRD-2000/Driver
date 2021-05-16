package com.example.WhereIsMyDriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Spinner_Adapter extends ArrayAdapter<dropdown_pojo>
{
    LayoutInflater layoutInflater;

    public Spinner_Adapter(@NonNull Context context, int resource, @NonNull List<dropdown_pojo> dropdown_pojos)
    {
        super(context, resource, dropdown_pojos);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.drop_down,null,true);
        dropdown_pojo dropdown_pojo = getItem(position);
        CircleImageView circleImageView = rowView.findViewById(R.id.circleImageView);
        TextView textView3 = rowView.findViewById(R.id.textView3);
        textView3.setText(dropdown_pojo.getDriver_id());
        Picasso.get().load(dropdown_pojo.getDriver_profile_pic()).into(circleImageView);
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView= layoutInflater.inflate(R.layout.drop_down,null,false);

        dropdown_pojo dropdown_pojo = getItem(position);
        CircleImageView circleImageView = convertView.findViewById(R.id.circleImageView);
        TextView textView3 = convertView.findViewById(R.id.textView3);
        textView3.setText(dropdown_pojo.getDriver_id());
        Picasso.get().load(dropdown_pojo.getDriver_profile_pic()).into(circleImageView);
        return convertView;
    }
}
