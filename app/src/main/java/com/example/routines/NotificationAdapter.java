package com.example.routines;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotificationAdapter extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;


    public NotificationAdapter(@NonNull Context context,  @NonNull ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.my_request_list, parent, false);
        }
        Request request = requests.get(position);
        TextView requestUserName = view.findViewById(R.id.my_request_user);
        TextView requestStatus = view.findViewById(R.id.my_request_status);

        requestUserName.setText(request.getRequestUser());
        requestStatus.setText(request.getStatus());

        return view;
    }
}
