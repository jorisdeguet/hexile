package org.deguet.hexile.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by joris on 16-02-07.
 */
public enum Type {

    Dot(),
    Zero(),
    One(0),
    Two1(0,1),
    Two2(0,2),
    Two3(0,3),
    Three(0,1,2),
    Three1(0,1,3),
    Three2(0,2,3),
    Three3(0,2,4),
    Four1(0,1,2,3),
    Four2(0,1,2,4),
    Four3(0,1,3,4),
    Five(0,1,2,3,4),
    Six(0,1,2,3,4,5);

    public int[] positions;

    private Set<Integer> set;

    public Set<Integer> asSet(){
        return set;
    }

    Type(int... positions){
        this.positions = positions;
        set = new HashSet<>();
        for (Integer i : positions) set.add(i);

    }

}
