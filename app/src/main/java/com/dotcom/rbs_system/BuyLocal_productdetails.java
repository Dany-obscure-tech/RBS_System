package com.dotcom.rbs_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotcom.rbs_system.Adapter.SliderAdapterExample;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class BuyLocal_productdetails extends AppCompatActivity {

    CardView imageSlider;
    SliderView sliderView;
    List<String> imageUrl;
    TextView report_btn;
    Dialog report_alert_dialog;
    Dialog make_offer_alert_dialog;
    Button alertReportCancel_btn,make_offer_btn,alertMakeOfferCancel_btn;
    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_local_productdetails);
        imageUrl = new ArrayList<>();
        imageUrl.add("https://cdn.pocket-lint.com/r/s/1200x/assets/images/120309-phones-buyer-s-guide-best-smartphones-2020-the-top-mobile-phones-available-to-buy-today-image1-eagx1ykift.jpg");
        imageUrl.add("https://www.savincom.co.uk/data/uploads/2020/02/TJDYpZN3r8B7onh2imuNah.jpg");
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(BuyLocal_productdetails.this,imageUrl);

        imageSlider=(CardView)findViewById(R.id.imageSlider);
        back_btn=(ImageButton)findViewById(R.id.back_btn);
        report_btn=(TextView) findViewById(R.id.report_btn);
        report_alert_dialog = new Dialog(this);
        report_alert_dialog.setContentView(R.layout.alert_report);
        make_offer_alert_dialog = new Dialog(this);
        make_offer_alert_dialog.setContentView(R.layout.alert_make_offer);

        sliderView = findViewById(R.id.imageSliders);
        alertReportCancel_btn = report_alert_dialog.findViewById(R.id.alertReportCancel_btn);
        alertMakeOfferCancel_btn = make_offer_alert_dialog.findViewById(R.id.alertMakeOfferCancel_btn);
        make_offer_btn=(Button)findViewById(R.id.make_offer_btn);
        sliderView.setSliderAdapter(sliderAdapterExample);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#01A0DA"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#F1F1F1"));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        onclicklistners();


    }

    private void onclicklistners() {
        report_btn_listner();
        alertReportCancel_btn_listner();
        make_offer_btn_listner();
        alertMakeOfferCancel_btn_listner();
        back_btn_listner();
    }

    private void back_btn_listner() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void alertMakeOfferCancel_btn_listner() {
        alertMakeOfferCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_offer_alert_dialog.dismiss();
            }
        });
    }

    private void make_offer_btn_listner() {
        make_offer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_offer_alert_dialog.show();
            }
        });
    }

    private void alertReportCancel_btn_listner() {
        alertReportCancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_alert_dialog.dismiss();
            }
        });
    }

    private void report_btn_listner() {
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_alert_dialog.show();
            }
        });
    }
}