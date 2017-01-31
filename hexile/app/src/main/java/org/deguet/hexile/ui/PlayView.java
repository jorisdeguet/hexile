package org.deguet.hexile.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.opengl.GLES20;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import org.deguet.hexile.PlayActivity;
import org.deguet.hexile.service.Consts;
import org.deguet.hexile.model.Hexile;
import org.deguet.hexile.model.Position;
import org.deguet.hexile.model.Tile;
import org.deguet.hexile.model.Type;
import org.deguet.hexile.service.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.Random;

/**
 * Created by joris on 16-02-07.
 */
public class PlayView extends AbsHexView{

    private PlayActivity playActivity;

    boolean isDone = false;

    double conRatio = 1.0;

    public void setHexile(Hexile hexile) {
        this.hexile = hexile;
        this.hexile.shuffle(new Random());
        conRatio =1.0;
        draw();
    }

    private final float proba = 0.40f;

    public PlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDrawingCacheEnabled(true);
        mode = Mode.Play;
        hexile = new Hexile(numberOfCols,numberOfCols);
        if (mode.equals("Play")) changeHexile();
        draw();
    }

    public void changeHexile(){
        hexile= new Hexile(numberOfCols, numberOfCols).gen(proba, new Random());
        hexile.shuffle(new Random());
        isDone = false;
        conRatio =1.0;
    }




    @Override
    public void drawTile(Canvas c, Tile tile, int col, int row, float sizeCol, float sizeRow){
        float startX = xForCol(col, row, sizeCol);
        float startY = yForRow(col, row, sizeRow);
        //paint.setColor(Service.interpolateColor(Consts.hexFillColor(),Color.WHITE,conRatio));
        paint.setColor(Service.interpolateColor(Color.WHITE,Consts.hexFillColor(),conRatio));
        paint.setStyle(Paint.Style.FILL);
        Position[] positions = positions(startX, startY, sizeCol, sizeRow);
        {
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            for (int i = 0; i < positions.length; i++) {
                Position p = positions[i];
                if (i == 0) path.moveTo((float) p.x, (float) p.y);
                else path.lineTo((float) p.x, (float) p.y);
            }
            path.close();
            c.drawPath(path, paint);
            paint.setStrokeWidth(sizeCol / 20);
            paint.setColor(Service.interpolateColor(Color.WHITE,Consts.hexBorderColor(),conRatio));
            //paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            c.drawPath(path, paint);
        }
        int paintc = Service.interpolateColor(Color.BLACK,Consts.strokeColor(),conRatio);
        //noinspection ResourceAsColor
        paint.setColor(paintc);
        paint.setStrokeWidth(10f);
        if (tile.drawing.equals(Tile.Drawing.Roundy)) {
            for (int pos = 0; pos < 6; pos++) {
                // for each couple, draw a quadratic
                for (int pos2 = pos + 1; pos2 < pos + 6; pos2++) {
                    if (tile.type.positions.length == 1 && Hexile.isOn(tile.type.positions, tile.rotation, pos)){
                        paint.setStyle(Paint.Style.STROKE);
                        float xx = (float) (positions[pos].x + positions[(pos + 1) % 6].x) / 2;
                        float yy = (float) (positions[pos].y + positions[(pos + 1) % 6].y) / 2;
                        c.drawLine(startX + sizeCol / 2, startY + sizeRow / 2, xx, yy, paint);
                    }
                    else if (Hexile.isOn(tile.type.positions, tile.rotation, pos)
                            && Hexile.isOn(tile.type.positions, tile.rotation, pos2%6)) {
                        // draw a quadratic
                        final Path bezier = new Path();
                        paint.setStrokeWidth(10f);
                        paint.setStyle(Paint.Style.STROKE);
                        float x1 = (float) (positions[pos].x + positions[(pos + 1) % 6].x) / 2;
                        float y1 = (float) (positions[pos].y + positions[(pos + 1) % 6].y) / 2;
                        bezier.moveTo(x1, y1);

                        float x2 = (float) (positions[pos2 % 6].x + positions[(pos2 + 1) % 6].x) / 2;
                        float y2 = (float) (positions[pos2 % 6].y + positions[(pos2 + 1) % 6].y) / 2;
                        bezier.quadTo(startX + sizeCol / 2, startY + sizeRow / 2, x2, y2);
                        c.drawPath(bezier, paint);
                        break;
                    }
                }
            }
        }
        else if (tile.drawing.equals(Tile.Drawing.Pointy)) {
            for (int pos = 0; pos < 6; pos++) {
                boolean ison = Hexile.isOn(tile.type.positions, tile.rotation, pos);
                //console.log(" >> pos " + pos + " ::: " + ison);
                if (ison) {
                    paint.setStyle(Paint.Style.STROKE);
                    float xx = (float) (positions[pos].x + positions[(pos + 1) % 6].x) / 2;
                    float yy = (float) (positions[pos].y + positions[(pos + 1) % 6].y) / 2;
                    c.drawLine(startX + sizeCol / 2, startY + sizeRow / 2, xx, yy, paint);
                }
            }
        }
        if (tile.type.positions.length > 0 || tile.type.equals(Type.Dot)){
            paint.setStyle(Paint.Style.FILL);
            if (tile.type.positions.length>1 && tile.drawing.equals(Tile.Drawing.Pointy)){
                c.drawCircle(startX+sizeCol/2, startY+sizeRow/2, sizeCol /20, paint);
            }
            else if (tile.type.positions.length==0 && tile.type.equals(Type.Dot)
                    || tile.type.positions.length==1){
                c.drawCircle(startX+sizeCol/2, startY+sizeRow/2, sizeCol /10, paint);
            }

        }
    }

    private Position getColRowFor(Position touch, Hexile h){
        double min = Double.MAX_VALUE;
        Position result = null;
        for (int col = 0 ; col < hexile.getCols(); col++) {
            for (int row = 0; row < hexile.getRowsForCol(col); row++) {
                Position middle = new Position(xForCol(col, row, sizeCol) + sizeCol / 2, yForRow(col, row, sizeRow) + sizeRow / 2);
                if (middle.distance(touch) < min) {
                    min = middle.distance(touch);
                    result = new Position(col,row);
                }
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("Hexile", "PlayView touch ");
        if(event.getAction() == MotionEvent.ACTION_DOWN && !isDone) {
            if (mode.equals(Mode.Play)){
                float x = event.getX();
                float y = event.getY();
                Position touched = new Position(x,y);
                Position tilePos = getColRowFor(touched, hexile);
                Tile tile = hexile.get((int)tilePos.x,(int)tilePos.y);
                tile.rotation = (tile.rotation+1)%6;
                int con = hexile.connectedOrNot();
                int max = hexile.maxConn();
                isDone = (con ==max);
                //conRatio = 1.0*con/max;
                draw();
                if (isDone){
                    new CountDownTimer(2000, 100) {
                        public void onTick(long millisUntilFinished) {
                            conRatio = millisUntilFinished*1.0 / 2000;
                            draw();
                        }
                        public void onFinish() {
                            Log.i("Hexile","Puzzle is finished, should switch view");
                            draw();
                        }
                    }.start();
                }
            }
        }
        return false;
    }

    public Bitmap grabAScreen(){
        SurfaceView v = this;
        File dir = getContext().getFilesDir();
        int w = getWidth();
        int h = getHeight();
        Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw();
        v.draw(c);
        try {
            b.compress(
                    Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(dir,"image.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void setPlayActivity(PlayActivity playActivity) {
        this.playActivity = playActivity;
    }
}
