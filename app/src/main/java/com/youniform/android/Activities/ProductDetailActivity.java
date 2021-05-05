package com.youniform.android.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.youniform.android.Adapters.OptionAdapter;
import com.youniform.android.Adapters.ReviewsAdapter;
import com.youniform.android.Adapters.SuggestionProductsAdapter;
import com.youniform.android.BaseClasses.APIClient;
import com.youniform.android.BaseClasses.CartDataBase;
import com.youniform.android.BaseClasses.Utils;
import com.youniform.android.Fragment.AttributesListDialogFragment;
import com.youniform.android.Interface.APIInterface;
import com.youniform.android.Interface.OptionClickListener;
import com.youniform.android.Model.CartModel;
import com.youniform.android.Model.GetProduct.Attribute;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.Model.OrderHistory.GetOrderHistory;
import com.youniform.android.Model.ReviewsModel;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NonConstantResourceId")
public class ProductDetailActivity extends AppCompatActivity implements OptionClickListener {

    private final List<GetProductModel> getProductModelList = new ArrayList<>();
    private final List<ReviewsModel> reviewsModelList = new ArrayList<>();
    private final List<GetOrderHistory> getOrderHistoryList = new ArrayList<>();
    int id = 0;
    @BindView(R.id.iv_ProductImage)
    ImageView iv_ProductImage;
    @BindView(R.id.tv_productName)
    TextView productName;
    @BindView(R.id.textView70)
    TextView reviewsHeading;
    @BindView(R.id.tv_productDetail)
    TextView productDetail;
    @BindView(R.id.tv_stockStatus)
    TextView stockStatus;
    @BindView(R.id.tv_Price)
    TextView price;
    @BindView(R.id.tv_size)
    TextView size;
    @BindView(R.id.tv_pname)
    TextView tv_pname;
    @BindView(R.id.tv_detail)
    TextView tv_detail;
    @BindView(R.id.btnWriteReviews)
    TextView btnWriteReviews;
    @BindView(R.id.tv_colors)
    TextView color;
    @BindView(R.id.textView68)
    TextView tvDetails;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rv_suggestions)
    RecyclerView rv_suggestion;
    @BindView(R.id.sv_main)
    ScrollView sv_main;
    @BindView(R.id.sv_info)
    ScrollView sv_info;
    @BindView(R.id.tvCartCount)
    TextView tvCartCount;
    @BindView(R.id.cont_suggestion_product)
    LinearLayout llSuggestion;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.constraintLayout2)
    LinearLayout constraintLayout2;
    @BindView(R.id.btnAddToBag)
    Button btn;
    String from = "";
    @BindView(R.id.ib_add)
    ImageButton btnAdd;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.ib_sub)
    ImageButton btnSub;
    TextView sizeChart;
    int count = 1;
    String ProductSize, ProductColor;
    List<String> colorList = new ArrayList<>();
    List<String> sizeList = new ArrayList<>();
    OkHttpClient client;
    BottomSheetDialog bottomSheetDialog, bottomsheetOption;
    Boolean contains = false;
    private GetProductModel productModel;
    private ReviewsAdapter reviewsAdapter;
    private SuggestionProductsAdapter productAdapter;
    private OptionAdapter optionAdapter;
    private APIInterface apiInterface;
    private Dialog progressDialog;
    private CartDataBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        id = getIntent().getIntExtra("ID", 0);
        setupClients();
        client = new OkHttpClient();
        setData();
        setAdapter();


        if (!productModel.getStockStatus().equals("instock")) {
            Toast.makeText(getApplicationContext(), "Item not in Stock", Toast.LENGTH_SHORT).show();
            btn.setEnabled(false);
        } else {
            btn.setEnabled(true);
        }

        //      new inBackground(this, client).execute();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 1;
                tvCount.setText("" + count);
            }
        });


        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count = count - 1;
                    tvCount.setText("" + count);
                } else if (count == 1) {
                    count = 1;
                    tvCount.setText("" + count);
                }
            }
        });

    }

    private void setData() {
        String details = "";
        if (productModel.getImages() != null) {
            Picasso.get().load(productModel.getImages().get(0).getSrc()).into(iv_ProductImage);
        }
        productName.setText(productModel.getName());
        tv_pname.setText(productModel.getName());
        productDetail.setText(productModel.getDescription());
        details += productModel.getDescription();
        price.setText("RS: " + productModel.getPrice());
        details += "\n  • Price   Rs: " + productModel.getPrice();
        stockStatus.setText(productModel.getStockStatus());
        details += "\n  • Stock Status   " + productModel.getStockStatus();
        if (productModel.getAttributes() != null) {
            for (Attribute pm : productModel.getAttributes()) {
                if (pm.getName().equals("Color")) {
                    details += "\n  • " + pm.getName() + "   ";

                    colorList.addAll(pm.getOptions());
                    for (String c : colorList) {
                        details += c + " ";
                    }
                    ProductColor = pm.getOptions().get(0);
                    color.setText(pm.getOptions().get(0));
//                    color.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AttributesListDialogFragment dialogFragment = AttributesListDialogFragment.newInstance();
//                            Bundle bundle = new Bundle();
//                            bundle.putStringArrayList("ATTRIBUTES_LIST", (ArrayList<String>) colorList);
//                            dialogFragment.setArguments(bundle);
//                            // dialogFragment.show(getSupportFragmentManager(),"dialog");
//                        }
//                    });
                }
                if (pm.getName().equals("Size")) {
                    details += "\n  • " + pm.getName() + "   ";
                    sizeList.addAll(pm.getOptions());
                    for (String c : sizeList) {
                        details += c + " ";
                    }
                    ProductSize = pm.getOptions().get(0);
                    size.setText(pm.getOptions().get(0));
//                    size.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AttributesListDialogFragment dialogFragment = AttributesListDialogFragment.newInstance();
//                            Bundle bundle = new Bundle();
//                            bundle.putStringArrayList("ATTRIBUTES_LIST", (ArrayList<String>) sizeList);
//                            dialogFragment.setArguments(bundle);
//                            //  dialogFragment.show(getSupportFragmentManager(),"dialog");
//                        }
//                    });
                }
                if (pm.getName().equals("Brand")) {
                    details += "\n  • " + pm.getName() + "   ";
                    for (String c : pm.getOptions()) {
                        details += c + " ";
                    }
                }
            }
        }
        // size.setText(productModel.getAttributes().get(0));

        tv_detail.setText(details);

        sizeChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog settingsDialog = new Dialog(ProductDetailActivity.this);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.sizelayout
                        , null));
                Window window = settingsDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                ImageView ivSize = settingsDialog.findViewById(R.id.ivImage);
                Glide.with(settingsDialog.getContext()).load(getString(R.string.sizeURL)).into(ivSize);
                Button btndismiss = settingsDialog.findViewById(R.id.btn_dismiss);
                btndismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        settingsDialog.dismiss();
                    }
                });
                settingsDialog.show();
            }
        });
    }

    private void setupClients() {
        ButterKnife.bind(this);
        sizeChart = findViewById(R.id.tv_sizechart);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        helper = new CartDataBase(this);

        updateCount();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = Utils.progressDialog(ProductDetailActivity.this);
        productModel = (GetProductModel) getIntent().getSerializableExtra("PRODUCT_DETAIL");

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomsheetOption = new BottomSheetDialog(this);
        getproductReviews(productModel.getId());
        recyclerview.setNestedScrollingEnabled(false);


        String udata = "Size Chart";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        sizeChart.setText(content);

        String udata2 = "Reviews";
        SpannableString content2 = new SpannableString(udata2);
        content2.setSpan(new UnderlineSpan(), 0, udata2.length(), 0);
        reviewsHeading.setText(content2);


        String udata3 = "Write your Reviews";
        SpannableString content3 = new SpannableString(udata3);
        content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
        btnWriteReviews.setText(content3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getproductReviews(productModel.getId());
    }

    private void updateCount() {
        List<CartModel> cartModelList = helper.getData();
//        productList.addAll(cartModelList);
        if (cartModelList.size() > 9) {
            tvCartCount.setText("9+");
        } else {
            tvCartCount.setText(String.valueOf(cartModelList.size()));
        }
    }

    private void getproductReviews(int id) {
        Utils.showDialog(progressDialog);
        Call<List<ReviewsModel>> call1 = apiInterface.allReviews(id);
        call1.enqueue(new Callback<List<ReviewsModel>>() {
            @Override
            public void onResponse(Call<List<ReviewsModel>> call, Response<List<ReviewsModel>> response) {
                Utils.dismissDialog(progressDialog);
                reviewsModelList.clear();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        reviewsModelList.addAll(response.body());
                        reviewsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReviewsModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), ProductDetailActivity.this);
            }
        });
    }

    private void setAdapter() {
        reviewsAdapter = new ReviewsAdapter(reviewsModelList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(reviewsAdapter);
    }

    @OnClick(R.id.ib_back)
    public void back() {
        onBackPressed();
        contains = true;
    }

    @OnClick(R.id.btncompositionCare)
    public void composition() {
        Intent intent = new Intent(ProductDetailActivity.this, WebsiteActivity.class);
        intent.putExtra("URL", "https://ndapak.org/uniform/composition-and-care/");
        startActivity(intent);
    }

    @OnClick(R.id.btnSizeGuide)
    public void sizeguide() {
        Intent intent = new Intent(ProductDetailActivity.this, WebsiteActivity.class);
        intent.putExtra("URL", "https://ndapak.org/uniform/size-guide/");
        startActivity(intent);
    }

    @OnClick(R.id.btnBuyingGuide)
    public void buyingguide() {
        Intent intent = new Intent(ProductDetailActivity.this, WebsiteActivity.class);
        intent.putExtra("URL", "https://ndapak.org/uniform/buying-guide/");
        startActivity(intent);
    }


    @OnClick(R.id.instoreAvailabe)
    public void storeavailable() {
        Intent intent = new Intent(ProductDetailActivity.this, WebsiteActivity.class);
        intent.putExtra("URL", "https://ndapak.org/uniform/in-store-availabilty/");
        startActivity(intent);
    }

    @OnClick(R.id.tv_insideInfo)
    public void insideInfo() {
        sv_main.setEnabled(false);
        sv_info.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_outsideInfo)
    public void outsideInfo() {
        sv_main.setEnabled(true);
        sv_info.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnAddToBag)
    public void checkOut() {
        if (!productModel.getStockStatus().equals("instock")) {
            Toast.makeText(getApplicationContext(), "Item not in Stock", Toast.LENGTH_SHORT).show();
        } else {

            for (CartModel cartModel : helper.getData()) {

                if (cartModel.getProducdid() == productModel.getId() && cartModel.getSize().equals(size.getText().toString()) && cartModel.getColor().equals(color.getText().toString())) {
                    Log.d("TAG", "checkOut: " + cartModel.getProducdid() + "\n" + productModel.getId());
                    Utils.showToastMessage("Cart Updated", ProductDetailActivity.this);
                    helper.Update(String.valueOf(productModel.getId()), count, size.getText().toString());
                    contains = true;
                }
            }
            if (!contains) {

                helper.insertData(productModel.getId(), id, productModel.getName(), productModel.getPrice(), productModel.getImages().get(0).getSrc(), count, ProductSize, ProductColor);
                displaySuggestionCont(false);
            }
            if (helper.getData().size() == 0) {
                helper.insertData(productModel.getId(), id, productModel.getName(), productModel.getPrice(), productModel.getImages().get(0).getSrc(), count, ProductSize, ProductColor);
                displaySuggestionCont(false);

            }
            updateCount();
            onStart();
        }

    }

    private void displaySuggestionCont(Boolean check) {
        if (!check) {
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_suggestion_product);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            productAdapter = new SuggestionProductsAdapter(this, getProductModelList);
            RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.rv_suggestions);
            recyclerView.setLayoutManager(layoutManager2);
            recyclerView.setAdapter(productAdapter);
            RelativeLayout rlBack = bottomSheetDialog.findViewById(R.id.btnBack);
            RelativeLayout rlProcess = bottomSheetDialog.findViewById(R.id.btnprocess);
            rlBack.setOnClickListener(v -> {
                if (bottomSheetDialog.isShowing())
                    bottomSheetDialog.dismiss();
            });

            rlProcess.setOnClickListener(v -> {

                if (Utils.LOGIN_ID == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Login to Proceed")
                            .setCancelable(false)
                            .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                                    intent.putExtra("check", true);
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
                } else {
                    Intent intent = new Intent(ProductDetailActivity.this, CheckOutTabActivity.class);
                    intent.putExtra("check", true);
                    startActivity(intent);
                }

                /*    startActivity(new Intent(ProductDetailActivity.this, CheckOutTabActivity.class));
                 */
                if (bottomSheetDialog.isShowing())
                    bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();

            getProducts();

        }

    }

    public void addtobagToast() {
        Toast toast = Toast.makeText(ProductDetailActivity.this, "Product added successfully", Toast.LENGTH_LONG);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.BLACK);
        toastView.setBackgroundColor(Color.WHITE);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check, 0, 0);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(16);
        toast.show();
    }

    private void getAllOrders() {
        Utils.showDialog(progressDialog);
        Call<List<GetOrderHistory>> call1 = apiInterface.allOrders(Utils.LOGIN_ID);
        call1.enqueue(new Callback<List<GetOrderHistory>>() {
            @Override
            public void onResponse(Call<List<GetOrderHistory>> call, Response<List<GetOrderHistory>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        getOrderHistoryList.addAll(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetOrderHistory>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), ProductDetailActivity.this);
            }
        });
    }

    @OnClick(R.id.btnWriteReviews)
    public void writeReviews() {
        if (Utils.LOGIN_ID == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Login to add reviews")
                    .setCancelable(false)
                    .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            intent.putExtra("check", true);
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
        } else {

            Intent intent = new Intent(ProductDetailActivity.this, WriteReviewActivity.class);
            intent.putExtra("PRODUCT_NAME", productModel.getName());
            intent.putExtra("check", true);
            intent.putExtra("PRODUCT_ID", productModel.getId());
            startActivity(intent);
        }
    }


    @OnClick(R.id.tv_Search)
    public void moveToSearch() {
        startActivity(new Intent(ProductDetailActivity.this, SearchAllActivty.class));
    }

    @OnClick(R.id.iv_home)
    public void moveToHome() {
        startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));
    }

    @OnClick(R.id.ib_bag)
    public void moveToBag() {
        startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
    }

    @OnClick(R.id.constraintLayout)
    public void openBottomsheetColor() {

        from = "color";

        bottomsheetOption.setContentView(R.layout.bottom_sheet_options);

        TextView tvHeading = bottomsheetOption.findViewById(R.id.tv_heading);
        tvHeading.setText("Select Color");

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        optionAdapter = new OptionAdapter(this, colorList, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        RecyclerView recyclerView = bottomsheetOption.findViewById(R.id.rv_options);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(optionAdapter);

        bottomsheetOption.show();
    }

    @OnClick(R.id.textView66)
    public void openBottomsheetSize() {
        from = "size";

        bottomsheetOption.setContentView(R.layout.bottom_sheet_options);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        optionAdapter = new OptionAdapter(this, sizeList, this);

        TextView tvHeading = bottomsheetOption.findViewById(R.id.tv_heading);
        tvHeading.setText("Select Size");


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        RecyclerView recyclerView = bottomsheetOption.findViewById(R.id.rv_options);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(optionAdapter);

        bottomsheetOption.show();

    }

    private void getProducts() {
        Utils.showDialog(progressDialog);
        Call<List<GetProductModel>> call1 = apiInterface.searchProducts("", "100");
        call1.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(Call<List<GetProductModel>> call, Response<List<GetProductModel>> response) {
                Utils.dismissDialog(progressDialog);
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        getProductModelList.clear();
//                        noProduct.setVisibility(View.GONE);
                        getProductModelList.addAll(response.body());
                        productAdapter.notifyDataSetChanged();
                    } else {
//                        noProduct.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetProductModel>> call, Throwable t) {
                Utils.dismissDialog(progressDialog);
                Utils.showToastMessage(t.getLocalizedMessage(), ProductDetailActivity.this);
            }
        });
    }

    @Override
    public void onOptionItemClick(int position) {
        if (from.equals("color")) {
            color.setText(colorList.get(position));
            ProductColor = colorList.get(position);
        } else {
            size.setText(sizeList.get(position));
            ProductSize = sizeList.get(position);
        }

        if (bottomsheetOption.isShowing()) {
            bottomsheetOption.dismiss();
        }
    }



  /*  class inBackground extends AsyncTask<Void, Void, String> {
        Context context;
        OkHttpClient client;

        inBackground(Context context, OkHttpClient client) {
            this.context = context;
            this.client = client;

        }

        String resp;

        @Override
        protected String doInBackground(Void... voids) {
            RequestBody formBody = new FormBody.Builder().add("customer", String.valueOf(Utils.LOGIN_ID))
                    .build();

            Request request = new Request.Builder()

                    .header("Authorization", "Basic YWRtaW46YWRtaW4xMjMzMjFA")
                    .post(formBody)
                    .url("https://ndapak.org/uniform/wp-json/wc/v3/orders")
                    .build();
            try {
                resp = client.newCall(request).execute().body().string();
            } catch (IOException e) {
                resp = e.getLocalizedMessage().toString();
            }

            return resp;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s.toString().substring(1, s.length() - 2));
                JSONObject object1 = object.getJSONObject("line_items");
                JSONArray array = new JSONArray(object1);
                for (int i = 0; i < array.length(); i++) {
                    Log.d("BLACK", "onPostExecute: " + array.getJSONArray(i).getJSONObject(i).toString());
                    Toast.makeText(context, array.getJSONArray(i).getJSONObject(i).toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }*/
}