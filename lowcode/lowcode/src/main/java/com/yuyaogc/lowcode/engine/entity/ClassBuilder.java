package com.yuyaogc.lowcode.engine.entity;

import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ClassBuilder {


    public static EntityClass buildEntityClass(EntityClass entityClass, Container container) {
        List<EntityClass> bases = new ArrayList<>();

        String name = entityClass.getName();

        if (Objects.isNull(entityClass.getParent())) {
            return entityClass;
        }

        for (String parent : entityClass.getParent()) {
            EntityClass parentClass = getEntryClass(parent, container);
            if(parentClass == null){
                continue;
            }
            if (StringUtils.equals(name, parent)) {
                parentClass.setInherited(false);
                for (EntityClass clazz : parentClass.getBases()) {
                    bases.remove(clazz);
                    bases.add(clazz);
                }
            } else {
                parentClass.setInherited(true);
                bases.remove(parentClass);
                bases.add(parentClass);
                parentClass.getInheritChildren().add(name);
            }
        }

        entityClass.setBases(bases);

        buildModelAttributes(container, entityClass);

        return entityClass;
    }


    private static void buildModelAttributes(Container container, EntityClass meta) {
        List<EntityClass> mro = meta.getMro();

        for (int i = mro.size() - 1; i > 0; i--) {
            EntityClass base = mro.get(i);

            // merge attribute
            for (EntityField entityField : base.getFields()) {
                meta.addField(entityField.getName(), entityField);
            }

            // merge methods
            for (LinkedList<EntityMethod> entityMethods : base.getMethods()) {
                entityMethods.forEach(entityMethod -> meta.addMethod(entityMethod.getName(), entityMethod));
            }

            // merge pk attribute
            for (EntityField entityField : base.getPkFields()) {
                meta.addPkField(entityField.getName(), entityField);
            }
        }

        // recompute attributes of _inherit_children models
        for (String childName : meta.getInheritChildren()) {
            EntityClass childMeta = getEntryClass(childName, container);
            childMeta.resetMro();
            buildModelAttributes(container, childMeta);
        }
    }


    private static EntityClass getEntryClass(String name, Container container) {
        for (Application application : container.getApps()) {
            EntityClass entityClass = application.getEntity(name);
            if (!Objects.isNull(entityClass)) {
                return entityClass;
            }
        }
        return null;
    }

}
