package com.youniform.android.Interface;


import com.youniform.android.Model.CategoriesModel;
import com.youniform.android.Model.Filter.FilterModel;
import com.youniform.android.Model.GetProduct.GetProductModel;
import com.youniform.android.Model.LoginModel;
import com.youniform.android.Model.OrderHistory.GetOrderHistory;
import com.youniform.android.Model.ProfileModel.GetProfileModel;
import com.youniform.android.Model.ResponseModel;
import com.youniform.android.Model.ReviewsModel;
import com.youniform.android.Model.ShiipingAddress.GetShippingAddressModel;
import com.youniform.android.Model.UpdateImageModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products/categories?parent=0&hide_empty=true")
    Call<List<CategoriesModel>> Categories();

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products/categories?")
    Call<List<CategoriesModel>> subCategory(@Query("parent") int parent);


    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products")
    Call<List<GetProductModel>> allProducts(@Query("category") int category);

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products")
    Call<List<GetProductModel>> filterProducts(@Query("attribute") String attribute,
                                               @Query("attribute_term") int attribute_term);

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products")
    Call<List<GetProductModel>> searchProducts(
            @Query("search") String search,
            @Query("per_page") String per_page
    );


    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/products/reviews")
    Call<List<ReviewsModel>> allReviews(@Query("product") int productId);


    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/customers/")
    Call<List<GetProfileModel>> getProfile(@Query("include") int include);


    @FormUrlEncoded
    @POST("reset-password")
    Call<ResponseModel> resetpassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("validate-code")
    Call<ResponseModel> validateCode(
            @Field("email") String email,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("set-password")
    Call<ResponseModel> setpassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("addshipping.php")
    Call<ResponseModel> addshipping(
            @Field("userid") int userid,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("address_1") String address_1,
            @Field("address_2") String address_2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postcode") Long postcode,
            @Field("country") String country,
            @Field("category") String category

    );

    @POST("addorder.php")
    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Accept:application/json",
            "Content-Type:application/json;"
    })
    Call<ResponseBody> placeOrder(
            @Body RequestBody data
    );

    @FormUrlEncoded
    @POST("getshipping.php")
    Call<GetShippingAddressModel> getshipping(@Field("user_id") int userid);

    @FormUrlEncoded
    @POST("custom-plugin/login")
    Call<LoginModel> login(
            @Field("username") String username,
            @Field("password") String password);

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("wp/v2/users/register")
    Call<ResponseModel> signup(
            @Field("username") String username,
            @Field("email") String email,
            @Field("number") String number,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("wc/v3/products/reviews")
    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA"})
    Call<ResponseModel> addReviews(
            @Field("product_id") int product_id,
            @Field("review") String review,
            @Field("reviewer") String reviewer,
            @Field("reviewer_email") String reviewer_email,
            @Field("rating") float rating
    );

    @FormUrlEncoded
    @POST("wc/v3/customers/6")
    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    Call<GetProfileModel> updateProfile(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("id") int id
    );


    @Multipart
    @POST("profilepic.php")
    Call<UpdateImageModel> updateImage(@Part MultipartBody.Part file,
                                       @Part("userid") RequestBody userid);

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("wc/v3/orders")
    Call<List<GetOrderHistory>> allOrders(
            @Query("customer") int customer
    );

    @Headers({
            "Authorization: Basic YWRtaW46YWRtaW4xMjMzMjFA",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @GET("iqonic-api/api/v1/woocommerce/get-product-attributes/")
    Call<FilterModel> getFilter();


}
