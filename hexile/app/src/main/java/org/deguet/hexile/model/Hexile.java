package org.deguet.hexile.model;

import android.util.Log;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by joris on 16-02-07.
 */
public class Hexile {

    public String name = "default";

    private Tile[][] tiles;

    public int getCols() {
        return cols;
    }

    public int getRowsForCol(int col) {
        return col % 2 == 0 ? rows : rows - 1;
    }

    private int cols, rows;

    public Hexile(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.tiles = new Tile[cols][rows];
        for (int col = 0; col < this.getCols(); col++) {
            for (int row = 0; row < this.getRowsForCol(col); row++) {
                tiles[col][row] = new Tile();
            }
        }

    }

    public String toString() {
        return name;
    }

    public Tile get(int col, int row) {
        return tiles[col][row];
    }

    public static int[] rotate(int[] tile, int rotation) {
        int[] res = new int[tile.length];
        System.arraycopy(tile, 0, res, 0, tile.length);
        for (int r = 0; r < rotation; r++) {
            for (int elt = 0; elt < res.length; elt++) {
                res[elt] = (res[elt] + 1) % 6;
            }
        }
        return res;
    }

    public static Set<Integer> rotate(Set<Integer> tile, int rotation) {
        Set<Integer> res = new HashSet<Integer>();
        for (Integer i : tile) {
            res.add((i+rotation)%6);
        }
        return res;
    }

    public static boolean isOn(int[] tile, int rotation, int position) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i : tile) set.add(i);
        Set<Integer> rotated = rotate(set, rotation);
        boolean res = rotated.contains(position);
        return res;
    }

    public boolean isValid(int col, int row) {
        if (col >= 0 && col < getCols()) {
            return row >= 0 && row < getRowsForCol(col);
        }
        return false;
    }

    private Set<Integer> showCon(Tile tile) {
        Set<Integer> res = new HashSet<Integer>();
        for (int pos = 0; pos < 6; pos++) {
            if (isOn(tile.type.positions, tile.rotation + 1, pos)) {
                res.add(pos);
            }
        }
        return res;
    }

    public boolean isFinished() {
        return connectedOrNot() == maxConn();
    }

    public int maxConn(){
        int result=0;
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {
                Set<Integer> conns = showCon(tiles[col][row]);
                result += conns.size();
            }
        }
        return result;
    }

    public int connectedOrNot() {
        int on = 0 ;
        int off = 0;
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {
                Set<Integer> conns = showCon(tiles[col][row]);
                // bottom
                if (isValid(col, row + 1)) {
                    boolean me = conns.contains(3);
                    boolean other = showCon(tiles[col][row + 1]).contains(0);
                    // return false if one is on and the other if off   XOR
                    if (me ? !other : other) off++;
                    if (me && other) on+=2;
                }
                // the tile on the right bottom for even cols
                if (col % 2 == 0 && isValid(col + 1, row)) {
                    boolean me = conns.contains(2);
                    boolean other = showCon(tiles[col + 1][row]).contains(5);
                    // return false if one is on and the other if off   XOR
                    if (me ? !other : other) off++;
                    if (me && other) on+=2;
                }
                // the tile on the right bottom for odd cols
                if (col % 2 == 1 && isValid(col + 1, row + 1)) {
                    boolean me = conns.contains(2);
                    boolean other = showCon(tiles[col + 1][row + 1]).contains(5);
                    // return false if one is on and the other if off   XOR
                    if (me ? !other : other) off++;
                    if (me && other) on+=2;
                }
                // the tile on the right top for even cols
                if (col % 2 == 0 && isValid(col + 1, row - 1)) {
                    boolean me = conns.contains(1);
                    boolean other = showCon(tiles[col + 1][row - 1]).contains(4);
                    // return false if one is on and the other if off   XOR
                    if (me ? !other : other) off++;
                    if (me && other) on+=2;
                }
                // the tile on the right top for odd cols
                if (col % 2 == 1 && isValid(col + 1, row)) {
                    boolean me = conns.contains(1);
                    boolean other = showCon(tiles[col + 1][row]).contains(4);
                    // return false if one is on and the other if off   XOR
                    if (me ^ other) off++;
                    if (me && other) on+=2;
                }
                // if there is a pos but no neighbour
                if (!isValid(col, row - 1) && conns.contains(0))
                    off++;
                if (!isValid(col, row + 1) && conns.contains(3))
                    off++;
                // on the left only for even columns
                if (col==0){
                    if (conns.contains(5)|| conns.contains(4))
                        off++;
                }
                if (col % 2 == 0 && !isValid(col + 1, row - 1) && conns.contains(1))
                    off++;
                if (col % 2 == 1 && !isValid(col + 1, row) && conns.contains(1))
                    off++;
                if (col % 2 == 0 && !isValid(col + 1, row) && conns.contains(2))
                    off++;
                if (col % 2 == 1 && !isValid(col + 1, row + 1) && conns.contains(2))
                    off++;
                if (col % 2 == 0 && !isValid(col - 1, row) && conns.contains(4))
                    off++;
                if (col % 2 == 1 && !isValid(col - 1, row + 1) && conns.contains(4))
                    off++;
                if (col % 2 == 0 && !isValid(col - 1, row - 1) && conns.contains(5))
                    off++;
                if (col % 2 == 1 && !isValid(col - 1, row) && conns.contains(5))
                    off++;
                //console.log("No trouble for  col " + col + " row " + row);
            }
        }
        Log.d("OnOff", "Off "+ off+" on : "+ on+"    total " + (on+off)+"   " +maxConn());
        return on;
    }

    public Hexile gen(float proba, Random random) {
        Set<Integer>[][] connectors = genConnectors(proba, random);
        return this.update(connectors, random);
    }


    private Set<Integer>[][] genConnectors(float proba, Random r) {
        Set<Integer>[][] connectors = new Set[cols][rows];
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < tiles[col].length; row++) {
                connectors[col][row] = new HashSet<Integer>();
            }
        }
        //showConnectors(connectors);
        // randomly choose if two tiles are connected

        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {

                // the tile under
                if (isValid(col, row + 1) && r.nextFloat() < proba) {
                    connectors[col][row].add(3);
                    connectors[col][row + 1].add(0);
                }
                // the tile on the right bottom for even cols
                if (col % 2 == 0 && isValid(col + 1, row) && r.nextFloat() < proba) {
                    connectors[col][row].add(2);
                    connectors[col + 1][row].add(5);
                }
                // the tile on the right bottom for odd cols
                if (col % 2 == 1 && isValid(col + 1, row + 1) && r.nextFloat() < proba) {
                    connectors[col][row].add(2);
                    connectors[col + 1][row + 1].add(5);
                }
                // the tile on the right top for even cols
                if (col % 2 == 0 && isValid(col + 1, row - 1) && r.nextFloat() < proba) {
                    connectors[col][row].add(1);
                    connectors[col + 1][row - 1].add(4);
                }
                // the tile on the right top for odd cols
                if (col % 2 == 1 && isValid(col + 1, row) && r.nextFloat() < proba) {
                    connectors[col][row].add(1);
                    connectors[col + 1][row].add(4);
                }
            }
        }
        showConnectors(connectors);
        return connectors;
    }

    private void showConnectors(Set<Integer>[][] connectors){
        String res = "\n";
        for (int row = 0 ; row < connectors[0].length+2; row++){
            String line = "";
            for (int col = 0 ; col < getCols(); col++){
                line += " ";
                for (int pos = 0 ; pos < 6 ; pos++){
                    if (isValid(col, row)){
                        if (connectors[col][row].contains(pos)){
                            line+= pos;
                        }
                        else{
                            line+= ".";
                        }
                    }
                    else {line += " ";}
                }

            }
            Log.i("Hexile","row "+row+" :: " + line);
            res += line+"\n";
        }
        Log.i("Hexile", res);
    }

    // replace tiles with the good type and fixes rotation to give actual solution
    private Hexile update(Set<Integer>[][] connectors, Random r) {
        Hexile res = new Hexile(this.cols, this.rows);
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {
                res.tiles[col][row].drawing = get(col,row).drawing;
                if (connectors[col][row].size() == 0 && get(col,row).type.positions.length == 0){
                    res.tiles[col][row].type = get(col,row).type;
                    res.tiles[col][row].rotation = 0;
                    continue;
                }
                for (int rot = 0; rot < 6; rot++) {
                    Set<Integer> rotated = rotate(connectors[col][row], rot);
                    for (Type t : Type.values()){
                        if (rotated.equals(t.asSet())){
                            res.tiles[col][row].type = t;
                            res.tiles[col][row].rotation = ( 11-rot) % 6;
                        }
                    }
                }
            }
        }
        return res;
    }

    public void shuffle(Random random) {
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {
                this.get(col,row).rotation = random.nextInt(6);
            }
        }
    }

    public Hexile swap(int colA, int rowA, int colB, int rowB) {
        if (!isFinished())
            throw new IllegalArgumentException("Can only swap if everything is aligned");
        Set<Integer>[][] connectors = this.connectors();
        showConnectors(connectors);
        // first case on top of each other
        if (colA == colB){
            if (connectors[colA][rowA].contains(3)){
                connectors[colA][rowA].remove(3);
                connectors[colB][rowB].remove(0);
            }
            else {
                connectors[colA][rowA].add(3);
                connectors[colB][rowB].add(0);
            }
        }
        // then colA is inferior to colB
        else{
            if (colA%2==0 ){
                if (rowA == rowB){
                    if (connectors[colA][rowA].contains(2)){
                        connectors[colA][rowA].remove(2);
                        connectors[colB][rowB].remove(5);
                    }
                    else {
                        connectors[colA][rowA].add(2);
                        connectors[colB][rowB].add(5);
                    }
                }
                else {
                    if (connectors[colA][rowA].contains(1)){
                        connectors[colA][rowA].remove(1);
                        connectors[colB][rowB].remove(4);
                    }
                    else {
                        connectors[colA][rowA].add(1);
                        connectors[colB][rowB].add(4);
                    }
                }
            }
            else{
                if (rowA != rowB){
                    if (connectors[colA][rowA].contains(2)){
                        connectors[colA][rowA].remove(2);
                        connectors[colB][rowB].remove(5);
                    }
                    else {
                        connectors[colA][rowA].add(2);
                        connectors[colB][rowB].add(5);
                    }
                }
                else {
                    if (connectors[colA][rowA].contains(1)){
                        connectors[colA][rowA].remove(1);
                        connectors[colB][rowB].remove(4);
                    }
                    else {
                        connectors[colA][rowA].add(1);
                        connectors[colB][rowB].add(4);
                    }
                }
            }
        }
        Log.i("Hexile", "==========================");
        showConnectors(connectors);
        return this.update(connectors, new Random());
    }

    private Set<Integer>[][] connectors(){
        Set<Integer>[][] connectors = new Set[cols][rows];
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRowsForCol(col); row++) {
                connectors[col][row] = new HashSet<Integer>();
                for (int p : get(col,row).type.positions){
                    connectors[col][row].add(p);
                }
                connectors[col][row] = rotate(connectors[col][row], (get(col,row).rotation+1)%6);
            }
        }
        return connectors;
    }

    public void swapDot(int col, int row) {
        if (get(col,row).type.equals(Type.Zero)) get(col,row).type = Type.Dot;
        else if (get(col,row).type.equals(Type.Dot)) get(col,row).type = Type.Zero;
    }

}
