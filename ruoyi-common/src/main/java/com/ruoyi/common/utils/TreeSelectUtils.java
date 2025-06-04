package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.TreeSelect;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TreeSelectUtils {

    public static <T> List<TreeSelect> build(Iterable<T> iterable, Function<T, TreeSelect> function) {
        if (IterableUtils.isEmpty(iterable)) {
            return Collections.emptyList();
        }

        Map<Object, List<TreeSelect>> childrenMap = new HashMap<>();
        Map<Object, TreeSelect> nodeMap = new HashMap<>();
        List<TreeSelect> treeList = new ArrayList<>();

        iterable.forEach(item -> {
            TreeSelect ts = function.apply(item);

            if (childrenMap.containsKey(ts.getParentId())) {
                childrenMap.get(ts.getParentId()).add(ts);
            } else {
                childrenMap.put(ts.getParentId(), new ArrayList<>() {{
                    add(ts);
                }});
            }
            nodeMap.put(ts.getId(), ts);
        });

        iterable.forEach(item -> {
            TreeSelect ts = function.apply(item);

            if (nodeMap.containsKey(ts.getParentId())) {
                return;
            }
            treeList.add(ts);
        });

        treeList.forEach(item -> adaptToChildrenList(item, childrenMap));

        return treeList;
    }

    private static void adaptToChildrenList(TreeSelect item, Map<Object, List<TreeSelect>> childrenListMap) {
        if (childrenListMap.containsKey(item.getId())) {
            item.setChildren(childrenListMap.get(item.getId()));
        }

        for (TreeSelect child : item.getChildren()) {
            adaptToChildrenList(child, childrenListMap);
        }
    }

    public static <T> List<T> build(List<T> list, Function<T, Object> getId, Function<T, Object> getParentId, BiConsumer<T, List<T>> setChildren) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Map<Object, List<T>> childrenMap = new HashMap<>();
        Map<Object, T> nodeMap = new HashMap<>();
        List<T> treeList = new ArrayList<>();

        list.forEach(item -> {
            Object id = getId.apply(item);
            Object parentId = getParentId.apply(item);

            if (childrenMap.containsKey(parentId)) {
                childrenMap.get(parentId).add(item);
            } else {
                childrenMap.put(parentId, new ArrayList<>() {{
                    add(item);
                }});
            }
            nodeMap.put(id, item);
        });

        list.forEach(item -> {
            Object parentId = getParentId.apply(item);

            if (nodeMap.containsKey(parentId)) {
                return;
            }
            treeList.add(item);
        });

        treeList.forEach(item -> adaptToChildrenList(item, childrenMap, getId, setChildren));

        return treeList;
    }

    private static <T> void adaptToChildrenList(T item, Map<Object, List<T>> childrenMap, Function<T, Object> getId, BiConsumer<T, List<T>> setChildren) {
        Object id = getId.apply(item);
        List<T> children = childrenMap.get(id);

        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        setChildren.accept(item, children);
        children.forEach(child -> adaptToChildrenList(child, childrenMap, getId, setChildren));
    }
}
