package com.youniform.android.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youniform.android.Activities.AddShippingAddressActivity;
import com.youniform.android.Activities.OrderHistoryActivity;
import com.youniform.android.Activities.SplashActivity;
import com.youniform.android.Activities.UpdateProfileActivity;
import com.youniform.android.Activities.WishListActivity;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.PrefManager;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class AccountsFragment extends Fragment {

    @BindView(R.id.iv_profile)
    CircleImageView profileImage;
    @BindView(R.id.tv_Name)
    TextView tv_Name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    View view;
    private PrefManager prefManager;
    private Dialog progressDialog;
    private CartDataBase helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accounts, container, false);

        return view;
    }

    @Override
    public void onStart() {
        setupClients(view);
        super.onStart();
    }

    private void setupClients(View view) {
        ButterKnife.bind(this, view);
        prefManager = new PrefManager(getContext());
        progressDialog = Utils.progressDialog(getContext());
        helper = new CartDataBase(getContext());
        Glide.with(getContext()).load(prefManager.getString("PROFILE_IMAGE")).placeholder(R.drawable.account).into(profileImage);
        String fname = prefManager.getString("FIRST_NAME");
        String lname = prefManager.getString("LAST_NAME");
        String uname = prefManager.getString("USER_NAME");
        if (fname.isEmpty() || lname.isEmpty()) {
            tv_Name.setText(uname);
        } else {
            tv_Name.setText(fname + " " + lname);
        }
        tv_email.setText(prefManager.getString("EMAIL"));
    }

    @OnClick(R.id.tv_editProfile)
    void moveToEditPRofile() {
        Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_shippingAddress)
    void moveToShippingAddress() {

        Intent intent = new Intent(getContext(), AddShippingAddressActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.tv_wishlist)
    void moveToWishList() {
        startActivity(new Intent(getContext(), WishListActivity.class));
    }

    @OnClick(R.id.tv_orderHistory)
    void moveToOrderHistory() {
        startActivity(new Intent(getContext(), OrderHistoryActivity.class));
    }

    @OnClick(R.id.tv_logout)
    void logoutClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to Logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utils.showDialog(progressDialog);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Utils.dismissDialog(progressDialog);
                                Utils.LOGIN_ID = 0;
                                helper.deleteAll();
                                prefManager.setInt("LOGIN_ID", 0);

                                prefManager.remove();

                                Intent mainIntent = new Intent(getContext(), SplashActivity.class);
                                startActivity(mainIntent);
                            }
                        }, 2000);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}