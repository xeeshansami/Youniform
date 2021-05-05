package com.youniform.android.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Fragment.AccountsFragment;
import com.youniform.android.Fragment.HomeFragment;
import com.youniform.android.Fragment.MenuFragment;
import com.youniform.android.Fragment.SubCategoryFragment;
import com.youniform.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements SubCategoryFragment.OnPositionClick {

    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_menu:
                showFragment(new MenuFragment());
                return true;
            case R.id.navigation_home:
                showFragment(new HomeFragment());
                return true;
            case R.id.navigation_account:
                if (Utils.LOGIN_ID == 0) {
                    loginAlert();
                } else {
                    showFragment(new AccountsFragment());
                }
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        showFragment(new HomeFragment());
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (getIntent().getBooleanExtra("UPDATePROFILE", false)) {
            //navView.setSelectedItemId(R.id.navigation_account);
        }
    }

    private void loginAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Login to see your accounts")
                .setCancelable(false)
                .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction mTransactiont = getSupportFragmentManager().beginTransaction();
        mTransactiont.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        mTransactiont.commit();
    }

    @Override
    public void onsubItemClick(int cat_id, int subId) {
        Utils.CAT_ID = cat_id;
        Utils.SUB_ID = subId;
        navView.setSelectedItemId(R.id.navigation_menu);
        //   187 190
        //      182 183
//177 180
    }


    @Override
    public void onBackPressed() {
    }
}