package com.yuyaogc.lowcode.engine.container;

import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.entity.EntityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Method Resolve Order (MRO) - 类对象属性的解析顺序
 */
public class C3 {
    public static List<EntityClass> calculate(EntityClass startingType, List<EntityClass> bases) {
        if (bases.contains(startingType)) {
            throw new EngineException(
                    String.format("a __bases__ item causes an inheritance cycle (%s)", startingType.getName()));
        }
        List<EntityClass> mro = new ArrayList<>();
        mro.add(startingType);
        if (bases.size() != 0) {
            List<List<EntityClass>> mroList = new ArrayList<>();
            for (EntityClass dt : bases) {
                // mroList.add(new ArrayList<MetaModel>(Arrays.asList(dt)));
                mroList.add(new ArrayList<EntityClass>(dt.getMro()));
            }
            mroList.add(new ArrayList<EntityClass>(bases));
            for (; ; ) {
                boolean removed = false;
                boolean sawNonZero = false;
                EntityClass lastHead = null;
                for (int i = 0; i < mroList.size(); i++) {
                    if (mroList.get(i).size() == 0) {
                        continue;
                    }
                    sawNonZero = true;
                    EntityClass head = lastHead = mroList.get(i).get(0);
                    boolean inTail = false;
                    for (int j = 0; j < mroList.size(); j++) {
                        List<?> list = mroList.get(j);
                        if (list.size() != 0 && !list.get(0).equals(head) && list.contains(head)) {
                            inTail = true;
                            break;
                        }
                    }

                    if (!inTail) {
                        if (mro.contains(head)) {
                            throw new EngineException("a __bases__ item causes an inheritance cycle");
                        }
                        mro.add(head);
                        for (int j = 0; j < mroList.size(); j++) {
                            mroList.get(j).remove(head);
                        }
                        removed = true;
                        break;
                    }
                }
                if (!sawNonZero) {
                    break;
                }
                if (!removed) {
                    EntityClass other = null;
                    String error = String.format(
                            "Cannot create a consistent method resolution\norder (MRO) for bases %s", lastHead.getName());
                    for (int i = 0; i < mroList.size(); i++) {
                        List<EntityClass> list = mroList.get(i);
                        if (list.size() != 0 && !list.get(0).equals(lastHead)) {
                            other = list.get(0);
                            error += ", ";
                            error += other.getName();
                        }
                    }
                    throw new EngineException(error);
                }
            }
        }
        return mro;
    }
}
