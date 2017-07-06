package com.cc.customview.progress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cc.customview.R;

import butterknife.ButterKnife;

public class CircleProgressActivity extends AppCompatActivity {
    CircleProgressView progressView;
    CircleProgressView progressView2;
    CircleProgressView progressView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        ButterKnife.bind(this);

        progressView = (CircleProgressView) findViewById(R.id.progressView);
        progressView2 = (CircleProgressView) findViewById(R.id.progressView2);
        progressView3 = (CircleProgressView) findViewById(R.id.progressView3);
        progressView.setProgress(80);
        progressView2.setProgress(80);
        progressView3.setProgress(80);
//        progressView.setCircleColor(getResources().getColor(R.color.colorPrimary));
//        progressView.setAbove(false);
//        progressView.setScroll(true);
//        progressView.setShow(true);
//        progressView.setProgressColor(getResources().getColor(R.color.colorOrange));
//        progressView.setProTextColor(getResources().getColor(R.color.colorWhite));
//        progressView.setTipTextColor(getResources().getColor(R.color.colorPrimary));
//        progressView.setSectorColor(getResources().getColor(R.color.colorAccent));
//        progressView.setTipText("当前进度");
//        progressView.setTipTextColor(getResources().getColor(R.color.colorWhite));
//        progressView.setProgressWidth(8);
//        progressView.setSectorWidth(5);
    }

    public void start(View view) {
        progressView.setProgress(80);
        progressView2.setProgress(80);
        progressView3.setProgress(80);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
