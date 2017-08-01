package com.brandon.itunessearch;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String mQuery;
    public final static String QUERY_KEY = "query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_bar:

                AlertDialog.Builder builderObject = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builderObject.setView(input);

                builderObject.setTitle(R.string.dialog_title).setCancelable(false)
                        .setPositiveButton(R.string.pos_butt_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mQuery = input.getText().toString();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container
                                        , createCustomFragment(new wait_screen_fragment(), mQuery)).commit();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                        createCustomFragment(new SearchListFragment(), mQuery)).commit();
                            }
                        })
                        .setNegativeButton(R.string.neg_butt_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builderObject.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Fragment createCustomFragment(Fragment f, String query){
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_KEY, query);
        f.setArguments(bundle);
        return f;
    }
}
