package com.example.itunessearchls;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.itunessearchls.ui.CercaFragment;
import com.example.itunessearchls.ui.EsbrinaFragment;
import com.example.itunessearchls.ui.FavoritsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_cerca) {
                loadFragment(new CercaFragment());
                return true;
            } else if (id == R.id.nav_esbrina) {
                loadFragment(new EsbrinaFragment());
                return true;
            } else if (id == R.id.nav_favorits) {
                loadFragment(new FavoritsFragment());
                return true;
            }

            return false;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_cerca);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof FavoritsFragment) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
