package org.harmony.toddler.mybatis.groovy;

import groovy.lang.Binding;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Objects;

import static org.apache.ibatis.reflection.ParamNameResolver.GENERIC_NAME_PREFIX;

@Slf4j
public abstract class GroovySqlScriptBase extends Script {

    protected Object param(String name) {
        Binding binding = getBinding();
        if (binding.hasVariable(GroovySqlSource.PARAMETER_OBJECT_KEY)) {
            Object paramMap = binding.getVariable(GroovySqlSource.PARAMETER_OBJECT_KEY);
            if (Objects.nonNull(paramMap)) {
                return InvokerHelper.getProperty(paramMap, name);
            }
        }
        return null;
    }

    /**
     * begin with 1
     *
     * Reference:
     * {@link org.apache.ibatis.reflection.ParamNameResolver#getNamedParams(Object[])}
     *
     * @param i param index begin with 1
     * @return
     */
    protected Object param(Integer i) {
        return param(GENERIC_NAME_PREFIX + i);
    }

    public <V> GroovySqlScriptBase addParam(String name, V value) {
        setParam(name, value);
        return this;
    }

    public <V> GroovySqlScriptBase setParam(String name, V value) {
        getBinding().setProperty(name, value);
        return this;
    }
}
