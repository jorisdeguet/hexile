package org.deguet.hexile.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.deguet.hexile.model.Hexile;
import org.deguet.hexile.model.Position;
import org.deguet.hexile.model.Tile;

/**
 * Created by joris on 16-02-17.
 */
public abstract class AbsHexView extends SurfaceView  implements SurfaceHolder.Callback{

    public enum Mode {Play, Draw, Tuto}

    public Mode mode = Mode.Play;

    protected final SurfaceHolder surfaceHolder;
    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected float spacing = 0;

    public int numberOfCols = 7;
    protected float sizeCol = 110;
    protected float sizeRow = 200;

    public Hexile hexile = new Hexile(numberOfCols,numberOfCols);

    public AbsHexView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    protected float xForCol(int col, int row, float sizeCol)
    {return col* (sizeCol*3/4+spacing) ; }

    protected float yForRow(int col, int row, float sizeRow)
    {return row* (sizeRow+spacing) + (col%2==0?0:sizeRow/2); }


    protected Position[] positions(float startX, float startY, float sizeCol, float sizeRow){
        return new Position[]{
                new Position(startX+(3*sizeCol/4), startY),
                new Position(startX+sizeCol, startY+sizeRow/2),
                new Position(startX+(3*sizeCol/4), startY+sizeRow),
                new Position(startX+sizeCol/4, startY+sizeRow),
                new Position(startX, startY+sizeRow/2),
                new Position(startX+sizeCol/4, startY)
        };
    }

    public void draw(){
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) return;
            this.draw (canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    protected abstract void drawTile(Canvas canvas, Tile tile, int col, int row, float sizeCol, float sizeRow);

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            surfaceHolder.unlockCanvasAndPost(canvas);
            if (hexile.getCols()%2==1){
                int oddCols = (hexile.getCols()+1)/2;
                double divider = (3.0f*oddCols - 1.0f)/2.0;
                this.sizeCol = (int) ( width *1.0 / divider );

                this.sizeRow = height / hexile.getRowsForCol(0);
            }
            else{
                this.sizeCol = (int) ( width *1.0 / hexile.getCols() );
            }
        }
        draw();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        for (int col = 0 ; col < hexile.getCols(); col++){
            for (int row = 0 ; row < hexile.getRowsForCol(col); row++){
                Tile tile = hexile.get(col,row);
                drawTile(
                        canvas, tile,
                        col,row,
                        sizeCol, sizeRow
                );
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        this.surfaceCreated(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}

}
