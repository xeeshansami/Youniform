package com.youniform.android.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.vinay.stepview.VerticalStepView;
import com.vinay.stepview.models.Step;
import com.youniform.android.R;

import java.util.ArrayList;
import java.util.List;

public class SingleOrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_status);
        VerticalStepView vertical_step_view = findViewById(R.id.vertical_step_view);
        String ORDER_STATUS = getIntent().getStringExtra("ORDER_STATUS");
        List<String> stringList = new ArrayList<>();
        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step("Order Signed", Step.State.COMPLETED));
        if (ORDER_STATUS.equals("pending")) {
            stringList.add("Order Processed");
            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
        }
//        if (ORDER_STATUS.equals("on-hold")){
//            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//        }
//        if (ORDER_STATUS.equals("cancelled")){
//            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//        }
//        if (ORDER_STATUS.equals("refunded")){
//            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//        }
//        if (ORDER_STATUS.equals("failed")){
//            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
//            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
//        }
//        if (ORDER_STATUS.equals("processing")){
//            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
//            stringList.add("Processing");
//            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
//            stepList.add(new Step("Processing", Step.State.COMPLETED));
//        }
        if (ORDER_STATUS.equals("completed")) {
            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
            //  stringList.add("Completed");
            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
            // stepList.add(new Step("Processing", Step.State.COMPLETED));
            //     stepList.add(new Step("Completed", Step.State.COMPLETED));
        }
        if (ORDER_STATUS.equals("shipped")) {
            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
            //        stringList.add("Completed");
            stringList.add("Shipped");
            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
            //  stepList.add(new Step("Processing", Step.State.COMPLETED));
            //    stepList.add(new Step("Completed", Step.State.COMPLETED));
            stepList.add(new Step("Shipped", Step.State.COMPLETED));
        }
        if (ORDER_STATUS.equals("on the way")) {
            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
            //     stringList.add("Completed");
            stringList.add("Shipped");
            stringList.add("Out For Delivery");
            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
            //    stepList.add(new Step("Processing", Step.State.COMPLETED));
            //     stepList.add(new Step("Completed", Step.State.COMPLETED));
            stepList.add(new Step("Shipped", Step.State.COMPLETED));
            stepList.add(new Step("Out For Delivery", Step.State.COMPLETED));
        }
        if (ORDER_STATUS.equals("delivered")) {
            stringList.add("Order Processed");
//            stringList.add("On hold");
//            stringList.add("Cancelled");
//            stringList.add("Refunded");
//            stringList.add("Failed");
            //  stringList.add("Completed");
            stringList.add("Shipped");
            stringList.add("Out For Delivery");
            stringList.add("Delivered");
            stepList.add(new Step("Order Processed", Step.State.COMPLETED));
//            stepList.add(new Step("On hold", Step.State.COMPLETED));
//            stepList.add(new Step("Cancelled", Step.State.COMPLETED));
//            stepList.add(new Step("Refunded", Step.State.COMPLETED));
//            stepList.add(new Step("Failed", Step.State.COMPLETED));
            //  stepList.add(new Step("Processing", Step.State.COMPLETED));
            // stepList.add(new Step("Completed", Step.State.COMPLETED));
            stepList.add(new Step("Shipped", Step.State.COMPLETED));
            stepList.add(new Step("Out For Delivery", Step.State.COMPLETED));
            stepList.add(new Step("Delivered", Step.State.COMPLETED));
        } else {
            if (!stringList.contains("Order Processed")) {
                stepList.add(new Step("Order Processed"));
            }
//            if (!stringList.contains("On hold")){
//                stepList.add(new Step("On hold"));
//            }
//            if (!stringList.contains("Cancelled")){
//                stepList.add(new Step("Cancelled"));
//            }
//            if (!stringList.contains("Refunded")){
//                stepList.add(new Step("Refunded"));
//            }
//            if (!stringList.contains("Failed")){
//                stepList.add(new Step("Failed"));
//            }
//            if (!stringList.contains("Processing")){
//                stepList.add(new Step("Processing"));
//            }
            if (!stringList.contains("Shipped")) {
                stepList.add(new Step("Shipped"));
            }
            if (!stringList.contains("Out For Delivery")) {
                stepList.add(new Step("Out For Delivery"));
            }
            if (!stringList.contains("Delivered")) {
                stepList.add(new Step("Delivered"));
            }
        }
        vertical_step_view.setSteps(stepList);
        vertical_step_view
                .setCompletedStepIcon(getResources().getDrawable(R.drawable.ic_custom_completed))
                .setNotCompletedStepIcon(getResources().getDrawable(R.drawable.ic_custom_not_completed))
                .setCompletedStepTextColor(Color.DKGRAY)
                .setNotCompletedStepTextColor(Color.DKGRAY)
                .setCurrentStepTextColor(Color.BLACK)
                .setCompletedLineColor(Color.parseColor("#000000"))
                .setNotCompletedLineColor(Color.parseColor("#b7b7b7"))
                .setTextSize(15)
                .setCircleRadius(15)
                .setLineLength(50);

        vertical_step_view.setReverse(false); // Default: true
        ImageButton ib_Back = findViewById(R.id.ib_Back);
        ib_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}