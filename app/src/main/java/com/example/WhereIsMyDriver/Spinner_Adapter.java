package com.example.WhereIsMyDriver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Spinner_Adapter extends ArrayAdapter<Route_user>
{
    LayoutInflater layoutInflater;

    public Spinner_Adapter(@NonNull Context context, int resource, @NonNull List<Route_user> route_users)
    {
        super(context, resource, route_users);
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.drop_down,null,true);
        Route_user route_user = getItem(position);
        CircleImageView circleImageView = rowView.findViewById(R.id.circleImageView);
        TextView textView3 = rowView.findViewById(R.id.textView3);
        textView3.setText(route_user.getName());
        circleImageView.setImageResource(route_user.getImage());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView= layoutInflater.inflate(R.layout.drop_down,null,false);

        Route_user route_user = getItem(position);
        CircleImageView circleImageView = convertView.findViewById(R.id.circleImageView);
        TextView textView3 = convertView.findViewById(R.id.textView3);
        textView3.setText(route_user.getName());
        circleImageView.setImageResource(route_user.getImage());
        return convertView;
    }
}
