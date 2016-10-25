package org.deguet.hexile.model;

/**
 * Created by joris on 16-02-07.
 */
public class Position {

    public double x,y;

    public Position(double x,double y){
        this.x = x; this.y = y;
    }

    public double distance(Position p){
        return Math.sqrt( Math.pow(p.x-x,2.0) + Math.pow(p.y-y,2.0) );
    }

    public static Position middle(Position... positions){
        double x = 0 ,y = 0;
        for (Position p : positions){
            x += p.x; y += p.y;
        }
        return new Position(x/positions.length, y/positions.length);
    }

    public static Position[] middles(Position[] p){
        Position[] result  = new Position[p.length];
        for (int i  = 0 ; i < p.length ; i++){
            result[i] = middle(p[i], p[(i+1)%p.length]);
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
