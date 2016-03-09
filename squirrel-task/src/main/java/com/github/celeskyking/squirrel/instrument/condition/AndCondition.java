package com.github.celeskyking.squirrel.instrument.condition;

/**
 * Created by Administrator on 2015/2/15
 */
public class AndCondition<T> extends Condition<T>{

    private Condition<T> right;

    private Condition<T> left;

    public AndCondition(Condition<T> right,Condition<T> left){
        this.right=right;
        this.left=left;
    }

    @Override
    public boolean match(T t) {
        return right.match(t) && left.match(t);
    }
}
