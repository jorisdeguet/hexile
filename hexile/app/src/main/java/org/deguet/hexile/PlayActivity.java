package org.deguet.hexile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.deguet.hexile.service.Consts;
import org.deguet.hexile.ui.PlayView;

public class PlayActivity extends AppCompatActivity {

    PlayView hview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        hview  = (PlayView) this.findViewById(R.id.hexaview);
        hview.mode = PlayView.Mode.Play;
        hview.setPlayActivity(this);

        Intent i = getIntent();
        String serial = i.getStringExtra(Consts.extraHexile());
        if (serial != null){
        }
        else{
            Integer sizeInCols = i.getIntExtra(Consts.extraNumberOfCols(),5);
            hview.numberOfCols = sizeInCols;
            hview.changeHexile();
            hview.draw();
            Log.i("Hexile","onCreate is "+ hview );
        }
    }

    @Override
    protected void onPostResume() {
        hview.draw();
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
        return true;
    }

}
