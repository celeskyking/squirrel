package com.github.celeskyking.squirrel.instrument;



import com.github.celeskyking.squirrel.instrument.condition.Condition;
import com.github.celeskyking.squirrel.instrument.condition.AndCondition;
import com.github.celeskyking.squirrel.instrument.condition.OrCondition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

/**
 * Created by sky on 15/8/1
 */
public class ClassFilter {

    private Condition<Class<?>> root ;

    private ClassFilter(Condition<Class<?>> root){
        this.root = root;
    }

    public static ClassFilter filter(Condition<Class<?>> condition){
        return new ClassFilter(condition);
    }

    public boolean match(Class<?> tClass){
        return this.root.match(tClass);
    }

    public ClassFilter and(Condition<Class<?>> rigth){
        this.root = new AndCondition<>(root,rigth);
        return this;
    }

    public ClassFilter or(Condition<Class<?>> rigth){
        this.root = new OrCondition<>(root,rigth);
        return this;
    }

    public static Condition<Class<?>> isInterface(){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.isInterface();
            }
        };
    }

    public static Condition<Class<?>> named(final String name){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.getSimpleName().equals(name);
            }
        };
    }

    public static Condition<Class<?>> contains(final String name){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.getSimpleName().contains(name);
            }
        };
    }

    public static Condition<Class<?>> startWith(final String name){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.getSimpleName().startsWith(name);
            }
        };
    }

    public static Condition<Class<?>> endWith(final String name){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.getSimpleName().endsWith(name);
            }
        };
    }
    public static Condition<Class<?>> isNotInterface(){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return !aClass.isInterface();
            }
        };
    }

    public static Condition<Class<?>> isAbstract(){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return Modifier.isAbstract(aClass.getModifiers());
            }
        };
    }

    public static Condition<Class<?>> isNotAbstract(){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return !Modifier.isAbstract(aClass.getModifiers());
            }
        };
    }

    public static Condition<Class<?>> withAnnotation(final Class<? extends Annotation> annotation){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return aClass.isAnnotationPresent(annotation);
            }
        };
    }

    public static Condition<Class<?>> withInherit(final Class<?> clz){
        return new Condition<Class<?>>() {
            @Override
            public boolean match(Class<?> aClass) {
                return clz.isAssignableFrom(aClass);
            }
        };
    }

}
