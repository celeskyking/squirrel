package com.github.celeskyking.squirrel.instrument;

import com.google.common.collect.Sets;
import com.github.celeskyking.squirrel.instrument.condition.Condition;
import jodd.io.findfile.ClassScanner;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Set;

/**
 * Created by sky on 15/7/25
 * 通过该类可以查找当前ClassLoader之中的各种配置的类
 */
public class ClassFinder {

    private Logger logger = LoggerFactory.getLogger(ClassFinder.class);

    private String basePackage;

    private Set<Class<?>> classes = Sets.newHashSet();

    private ClassScanner scanner;

    private ClassFilter classFilter = ClassFilter.filter(new Condition<Class<?>>() {
        @Override
        public boolean match(Class<?> aClass) {
            return true;
        }
    });

    private ClassFinder(final String basePackage){
        this.basePackage = basePackage;
        this.scanner = new ClassScanner() {
            @Override
            protected void onEntry(EntryData entryData) throws Exception {
                try{
                    if(StringUtil.isEmpty(basePackage)){
                        Class<?> clazz = getClass().getClassLoader().loadClass(entryData.getName());
                        if(clazz!=null&&classFilter.match(clazz)){
                            classes.add(clazz);
                        }
                    }else{
                        Class<?> clazz = getClass().getClassLoader().loadClass(basePackage+"."+entryData.getName());
                        if(clazz!=null&&classFilter.match(clazz)){
                            classes.add(clazz);
                        }
                    }
                }catch (Throwable e){
                    logger.debug("load class error:{}",entryData.getName());
                }
            }
        };
        //this.scanner.setExcludeAllJars(true);
        this.scanner.setIncludeResources(true);
        this.scanner.setIncludeAllEntries(true);
    }

    private ClassFinder() {
        this("");
    }

    public static ClassFinder finder(){
        return new ClassFinder();
    }

    public static ClassFinder finder(String basePackage){
        return new ClassFinder(basePackage);
    }

    public Set<Class<?>> find(){
        if(StringUtil.isEmpty(basePackage)){
            scanner.setIgnoreException(true);
            scanner.scanDefaultClasspath();
        }else{
            scanner.setIgnoreException(true);
            scanner.scanDefaultClasspath();
            String path = "/"+ StringUtil.replace(basePackage, ".", "/");
            scanner.scan(ClassLoaderUtil.getResourceUrl(path));
        }
        return this.classes;
    }

    public ClassFinder filter(ClassFilter filter){
        this.classFilter = filter;
        return this;
    }

    public ClassFinder setExcludedJars(String... excludeJars){
        scanner.setExcludedJars(excludeJars);
        return this;
    }

    public ClassFinder setExcludedEntries(String... excludedEntries){
        scanner.setExcludedEntries(excludedEntries);
        return this;
    }

    public ClassFinder setExcludedAllEntries(boolean bool){
        scanner.setIncludeAllEntries(bool);
        return this;
    }

    public ClassFinder setIncludedJars(String... includedJars){
        scanner.setIncludedJars(includedJars);
        return this;
    }

    public ClassFinder setIncludedAllJar(boolean bool){
        scanner.setIncludeAllJars(bool);
        return this;
    }

    public ClassFinder setIncludedAllEntries(boolean bool){
        scanner.setIncludeAllEntries(bool);
        return this;
    }

    public ClassFinder setIgnoreException(boolean bool){
        scanner.setIgnoreException(bool);
        return this;
    }

}