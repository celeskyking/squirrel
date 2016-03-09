package com.github.celeskyking.squirrel.instrument.condition;

/**
 * Created by Administrator on 2015/2/15
 */
public abstract class Condition<T> {

    public abstract boolean match(T t);

    public OrCondition<T> or(Condition<T> t){
        return new OrCondition<T>(this,t);
    }

    public AndCondition<T> and(Condition<T> t){
        return new AndCondition<T>(this,t);
    }
}
