package com.zsr.sample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zsr.starratingview.databinding.ActivityMainBinding;

import com.zsr.starratingview.R;
import com.zsr.view.StarRatingView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.star.setRate(3);
        binding.star.setOnRateChangeListener(new StarRatingView.OnRateChangeListener() {
            @Override
            public void onRateChange(float rate) {
                //do some thing~~~.
            }
        });
    }
}
