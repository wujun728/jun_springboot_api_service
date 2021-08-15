package cn.edu.scu.virjarjcd.util;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by virjar on 16/5/15.
 * idea import 排序规则，eclipse一般不会对import进行优化，为了统一idea和eclipse，这里根据idea的规则格式化import部分代码。
 * 这样目标代码在idea和eclipse中使用格式化命令的时候，输出代码将会一致
 */
public class ImportsSorter {

    private List<String> template = new ArrayList<String>();

    public static final String N = "\n";

    private Multimap<String, String> matchingImports= TreeMultimap.create();
    private ArrayList<String> notMatching = new ArrayList<String>();
    private Set<String> allImportOrderItems = new HashSet<String>();

    static List<String> sort(List<String> imports, List<String> importsOrder) {
        ImportsSorter importsSorter = new ImportsSorter(importsOrder);
        return importsSorter.sort(imports);
    }

    public ImportsSorter(List<String> importOrder) {
        if(importOrder == null){
            importOrder = StringUtils.trimToList("java;javax;org;com;");
        }
        List<String> importOrderCopy = new ArrayList<String>(importOrder);
        normalizeStaticOrderItems(importOrderCopy);
        putStaticItemIfNotExists(importOrderCopy);
        template.addAll(importOrderCopy);
        this.allImportOrderItems.addAll(importOrderCopy);
    }


    public List<String> sort(List<String> imports) {
        imports = StringUtils.trimImports(imports);
        filterMatchingImports(imports);
        mergeMatchingItems();
        mergeNotMatchingItems();
        removeNewLines();
        return getResult();
    }

    private void removeNewLines() {
        List<String> temp = new ArrayList<>();

        boolean previousWasNewLine = false;
        boolean anyContent = false;
        for (int i = 0; i < template.size(); i++) {
            String s = template.get(i);
            if (!anyContent && s.equals(N)) {
                continue;
            }
            if (s.equals(N)) {
                if (previousWasNewLine) {
                    continue;
                } else {
                    temp.add(s);
                }
                previousWasNewLine = true;
            } else {
                previousWasNewLine = false;
                anyContent = true;
                temp.add(s);
            }
        }

        Collections.reverse(temp);
        List<String> temp2 = trimNewLines(temp);
        Collections.reverse(temp2);

        template = temp2;
    }

    private List<String> trimNewLines(List<String> temp) {
        List<String> temp2 = new ArrayList<String>();
        boolean anyContent = false;
        for (int i = 0; i < temp.size(); i++) {
            String s = temp.get(i);
            if (!anyContent && s.equals(N)) {
                continue;
            }
            anyContent = true;
            temp2.add(s);
        }
        return temp2;
    }

    private void putStaticItemIfNotExists(List<String> allImportOrderItems) {
        boolean contains = false;
        for (int i = 0; i < allImportOrderItems.size(); i++) {
            String allImportOrderItem = allImportOrderItems.get(i);
            if (allImportOrderItem.equals("static ")) {
                contains = true;
            }
        }
        if (!contains) {
            allImportOrderItems.add(0, "static ");
        }
    }

    private void normalizeStaticOrderItems(List<String> allImportOrderItems) {
        for (int i = 0; i < allImportOrderItems.size(); i++) {
            String s = allImportOrderItems.get(i);
            if (s.startsWith("\\#")) {
                allImportOrderItems.set(i, s.replace("\\#", "static "));
            }
        }
    }

    /**
     * returns not matching items and initializes internal state
     */
    private void filterMatchingImports(List<String> imports) {
        for (String anImport : imports) {
            String orderItem = getBestMatchingImportOrderItem(anImport);
            if (orderItem != null) {
                matchingImports.put(orderItem, anImport);
            } else {
                notMatching.add(anImport);
            }
        }
        notMatching.addAll(allImportOrderItems);
    }

    private String getBestMatchingImportOrderItem(String anImport) {
        String matchingImport = null;
        for (String orderItem : allImportOrderItems) {
            if (anImport.startsWith(orderItem)) {
                if (matchingImport == null) {
                    matchingImport = orderItem;
                } else {
                    matchingImport = StringUtils.betterMatching(matchingImport, orderItem, anImport);
                }
            }
        }
        return matchingImport;
    }

    /**
     * not matching means it does not match any order item, so it will be appended before or after order items
     */
    private void mergeNotMatchingItems() {
        Collections.sort(notMatching);

        template.add(N);
        for (int i = 0; i < notMatching.size(); i++) {
            String notMatchingItem = notMatching.get(i);
            if (!matchesStatic(false, notMatchingItem)) {
                continue;
            }
            boolean isOrderItem = isOrderItem(notMatchingItem, false);
            if (!isOrderItem) {
                template.add(notMatchingItem);
            }
        }
        template.add(N);
    }

    private boolean isOrderItem(String notMatchingItem, boolean staticItems) {
        boolean contains = allImportOrderItems.contains(notMatchingItem);
        return contains && matchesStatic(staticItems, notMatchingItem);
    }

    private boolean matchesStatic(boolean staticItems, String notMatchingItem) {
        boolean isStatic = notMatchingItem.startsWith("static ");
        return (isStatic && staticItems) || (!isStatic && !staticItems);
    }

    private void mergeMatchingItems() {
        for (int i = 0; i < template.size(); i++) {
            String item = template.get(i);
            if (allImportOrderItems.contains(item)) {
                // find matching items for order item
                Collection<String> strings = matchingImports.get(item);
                if (strings == null || strings.isEmpty()) {
                    // if there is none, just remove order item
                    template.remove(i);
                    i--;
                    continue;
                }
                ArrayList<String> matchingItems = new ArrayList<String>(strings);
                Collections.sort(matchingItems);

                // replace order item by matching import statements
                // this is a mess and it is only a luck that it works :-]
                template.remove(i);
                if (i != 0 && !template.get(i - 1).equals(N)) {
                    template.add(i, N);
                    i++;
                }
                if (i + 1 < template.size() && !template.get(i + 1).equals(N) && !template.get(i).equals(N)) {
                    template.add(i, N);
                }
                template.addAll(i, matchingItems);
                if (i != 0 && !template.get(i - 1).equals(N)) {
                    template.add(i, N);
                }

            }
        }
        // if there is \n on the end, remove it
        if (template.size() > 0 && template.get(template.size() - 1).equals(N)) {
            template.remove(template.size() - 1);
        }
    }

    private List<String> getResult() {
        ArrayList<String> strings = new ArrayList<String>();

        for (String s : template) {
            if (s.equals(N)) {
                strings.add(s);
            } else {
                strings.add("import " + s + ";" + N);
            }
        }
        return strings;
    }
}
