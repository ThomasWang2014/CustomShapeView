package com.thomaswang.tujia.customshapeview.com.thomaswang.tujia.customshapeview.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.thomaswang.tujia.customshapeview.R;
import com.thomaswang.tujia.customshapeview.com.thomaswang.tujia.customshapeview.view.CustomShapeView;


public class MainActivity extends ActionBarActivity {
    private CustomShapeView mCustomShapeView;
    private Button btn_switch;
    private boolean showCircleView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomShapeView = (CustomShapeView) findViewById(R.id.customShapeView);
        btn_switch = (Button) findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCircleView = !showCircleView;
                btn_switch.setText(showCircleView ? "切换到圆角矩形" : "切换到圆形");
                mCustomShapeView.setType(showCircleView ? CustomShapeView.TYPE_CIRCLE : CustomShapeView.TYPE_ROUND);
                mCustomShapeView.invalidate();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
