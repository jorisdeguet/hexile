package org.deguet.hexile;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;


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

        ImageButton bPlay = (ImageButton) findViewById(R.id.bPlay);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Hexile","Will switch view to ad while playing");
                hview.changeHexile();
                hview.draw();
                ViewSwitcher vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
                vs.showPrevious();
            }
        });

    }

    @Override
    protected void onPostResume() {
        hview.draw();
        super.onPostResume();
    }

    public void switchToCaptureOrNext(String message){
        //Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_LONG).show();
        Log.i("Hexile","Will switch view to show menu");
        ViewSwitcher vs = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        vs.showNext();
    }

}
