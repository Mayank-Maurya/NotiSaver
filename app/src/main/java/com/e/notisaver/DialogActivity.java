package com.e.notisaver;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_text;
    private TextView addlead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dialog);
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels);
        int screenheight = (int) (metrics.widthPixels * 0.40);
        setContentView(R.layout.activity_dialog);
        getWindow().setLayout(screenWidth, screenheight);
        Bundle bundle = getIntent().getExtras();
        tv_title = findViewById(R.id.title_dialog_tv);
        tv_text = findViewById(R.id.text_dialog_tv);
        addlead = findViewById(R.id.button_addlead);
        if (bundle != null) {
            String title = bundle.getString("title");
            String text = bundle.getString("text");
            tv_title.setText(title);
            tv_text.setText(text);
        }
        addlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DialogActivity.this, "LEAD ADDED", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });


    }

}