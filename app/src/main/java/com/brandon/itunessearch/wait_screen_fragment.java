package com.brandon.itunessearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static com.brandon.itunessearch.MainActivity.QUERY_KEY;

public class wait_screen_fragment extends Fragment {

    TextView mQueryField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.wait_screen, container, false);
        mQueryField = (TextView)v.findViewById(R.id.edit_query);
        Bundle bundle = getArguments();
        mQueryField.append(bundle.getString(QUERY_KEY));
        return v;
    }
}
