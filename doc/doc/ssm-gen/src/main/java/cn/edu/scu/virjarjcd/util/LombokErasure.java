package cn.edu.scu.virjarjcd.util;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by virjar on 16/3/25.
 */
public class LombokErasure {

    private TopLevelClass topLevelClass;
    private boolean hasImport = false;

    private boolean allArgConstruction = false;
    private boolean nonArgConstruction = false;
    private boolean data = false;


    public LombokErasure(TopLevelClass topLevelClass) {
        this.topLevelClass = topLevelClass;
    }

    public void erasure() {
        //擦除import
        //Set<FullyQualifiedJavaType> importedTypes = topLevelClass.getImportedTypes();
        //需要通过反射机制，结果集被unmodify了
        Set<FullyQualifiedJavaType> importedTypes = null ;
        try {
            importedTypes = ReflectUtil.getField(topLevelClass, "importedTypes");
        } catch (Exception e) {//异常不会发生
            e.printStackTrace();
        }
        erasurePackage(importedTypes);

        erasureClassModifier();

        if (allArgConstruction) {
            Method method = IntrospectedTableUtil.genParameterizedConstructor(topLevelClass);
            topLevelClass.addMethod(method);
        }

        if (nonArgConstruction) {
            topLevelClass.addMethod(IntrospectedTableUtil.genNoneParameterizedConstructor(topLevelClass));
        }

        Set<Field> shouldGenGetter = new HashSet<>();
        Set<Field> shouldGenSetter = new HashSet<>();
        collectGetterSetterAndErasure(shouldGenGetter, shouldGenSetter);
        rebuildGetterSetter(shouldGenGetter, shouldGenSetter);

    }

    private void rebuildGetterSetter(Set<Field> shouldGenGetter, Set<Field> shouldGenSetter) {
        for (Field field : topLevelClass.getFields()) {
            if (shouldGenGetter.contains(field)) {
                topLevelClass.addMethod(FieldMethodBuilder.getGetter(field));
            }
            if (shouldGenSetter.contains(field)) {
                topLevelClass.addMethod(FieldMethodBuilder.getSetter(field));
            }
        }
    }

    private void collectGetterSetterAndErasure(Set<Field> shouldGenGetter, Set<Field> shouldGenSetter) {
        if (data) {
            for (Field filed : topLevelClass.getFields()) {
                shouldGenGetter.add(filed);
                shouldGenSetter.add(filed);
            }
        }

        for (Field field : topLevelClass.getFields()) {
            Iterator<String> iterator = field.getAnnotations().iterator();
            while(iterator.hasNext()){
                String annotation = iterator.next();
                if (hasImport && annotation.trim().equals("@Getter")) {
                    shouldGenGetter.add(field);
                    iterator.remove();
                } else if (annotation.trim().equals("@lombok.Getter")) {
                    shouldGenGetter.add(field);
                    iterator.remove();
                }

                if (hasImport && annotation.trim().equals("@Setter")) {
                    shouldGenSetter.add(field);
                    iterator.remove();
                } else if (annotation.trim().equals("@lombok.Setter")) {
                    shouldGenSetter.add(field);
                    iterator.remove();
                }
            }
        }
    }

    private void erasurePackage(Set<FullyQualifiedJavaType> imports) {
        Iterator<FullyQualifiedJavaType> iterator = imports.iterator();
        while (iterator.hasNext()) {
            FullyQualifiedJavaType javaType = iterator.next();
            if (javaType.getFullyQualifiedName().trim().startsWith("lombok.")) {
                iterator.remove();
                hasImport = true;
            }
        }
    }

    private void erasureClassModifier() {
        Iterator<String> iterator = topLevelClass.getAnnotations().iterator();
        while (iterator.hasNext()) {
            String annotation = iterator.next();
            if (hasImport) {
                if (annotation.trim().equals("@Data")) {
                    iterator.remove();
                    data = true;
                } else if (annotation.trim().equals("@AllArgsConstructor")) {
                    iterator.remove();
                    allArgConstruction = true;
                } else if (annotation.trim().equals("@NoArgsConstructor")) {
                    iterator.remove();
                    nonArgConstruction = true;
                }
            } else {
                if (annotation.trim().equals("@lombok.Data")) {
                    iterator.remove();
                    data = true;
                } else if (annotation.trim().equals("@lombok.AllArgsConstructor")) {
                    iterator.remove();
                    allArgConstruction = true;
                } else if (annotation.trim().equals("@lombok.NoArgsConstructor")) {
                    iterator.remove();
                    nonArgConstruction = true;
                }
            }

        }
    }

}
