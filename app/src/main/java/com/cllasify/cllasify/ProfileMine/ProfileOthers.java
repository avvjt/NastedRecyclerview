package com.cllasify.cllasify.ProfileMine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.cllasify.cllasify.R;



public class ProfileOthers extends Activity {

    Toolbar toolbar;

    @SuppressLint("UseSupportActionBar")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block:
                Toast.makeText(ProfileOthers.this, "Block", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.report:
                Toast.makeText(ProfileOthers.this, "Report", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.restrict:
                Toast.makeText(ProfileOthers.this, "Restrict", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_manu,menu);
        return true;
    }

}