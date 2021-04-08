package cn.think.in.java.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestForLeet {

    List<List<Integer>> res = new LinkedList<>();
    List<Integer> temp;
    int ft;
    int tempt;

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        ft = target;
        tempt = target;
        rec(candidates, 0);
        return res;
    }

    public boolean rec(int[] candidates, int x){
        if(x >= candidates.length) return false;

        for(int i = x; i < candidates.length; i++){
            if(x == 0) {
                temp = new ArrayList<>();
                tempt = ft;
            }
            if(candidates[i] == tempt){
                temp.add(candidates[i]);
                res.add(temp);
                tempt += candidates[i];
                temp.remove(0);
            }
            if(candidates[i] <= tempt){
                temp.add(candidates[i]);
                tempt -= candidates[i];
                if(!rec(candidates, i + 1)){
                    temp.remove(temp.size()-1);
                    tempt += candidates[i];
                }else{
                    temp.remove(temp.size()-1);
                    tempt += candidates[i];
                }
            }
        }
        return false;
    }

    @Test
    public void invoke(){
        combinationSum2(new int[]{10,1,2,7,6,1,5}, 8);
        List<Integer> l = new ArrayList<>();
        l.add(123);
        l.remove(0);
    }

}
