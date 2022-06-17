package ru.n_korotkov.oop.dsl.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyShell;
import groovy.lang.MetaProperty;
import groovy.util.DelegatingScript;
import ru.n_korotkov.oop.dsl.Main;

public class GroovyConfigurable extends GroovyObjectSupport {

    private Object configureCollectionItem(Class itemClass, Object value) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchFieldException {
        if (value instanceof Closure) {
            Object newValue = itemClass.getConstructor().newInstance();
            Closure c = (Closure) value;
            c.setDelegate(newValue);
            c.setResolveStrategy(Closure.DELEGATE_FIRST);
            c.setDelegate(newValue);
            c.call();
            ((GroovyConfigurable) newValue).configureProperties();
            return newValue;
        }
        return value;
    }

    private Object configureCollection(MetaProperty prop, Object value) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchFieldException {
        ParameterizedType collectionType = (ParameterizedType)
                getClass().getDeclaredField(prop.getName()).getGenericType();
        Class itemClass = (Class) collectionType.getActualTypeArguments()[0];
        if (GroovyConfigurable.class.isAssignableFrom(itemClass)) {
            Collection collection = (Collection) value;
            Collection newValue = collection.getClass().getConstructor().newInstance();
            for (Object item : collection) {
                newValue.add(configureCollectionItem(itemClass, item));
            }
            return newValue;
        }
        return value;
    }

    private void configureProperties() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException, NoSuchFieldException {
        for (MetaProperty prop : getMetaClass().getProperties()) {
            Object value = getProperty(prop.getName());
            if (Collection.class.isAssignableFrom(prop.getType()) && value instanceof Collection) {
                setProperty(prop.getName(), configureCollection(prop, value));
            }
        }
    }

    public void configureFromFile(File file) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            CompilationFailedException, IOException, NoSuchFieldException {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell sh = new GroovyShell(Main.class.getClassLoader(), new Binding(), cc);
        DelegatingScript script = (DelegatingScript) sh.parse(file);
        script.setDelegate(this);
        script.run();
        configureProperties();
    }

}
