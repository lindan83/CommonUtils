package com.lance.common.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * 控件工具
 */
public class ViewUtil {
    private ViewUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 通过反射获取GridView高度
     *
     * @param gridView GridView
     * @return GridView Height
     */
    public static int getGridViewHeight(GridView gridView) {
        //获取对应的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        Class<? extends GridView> tempGridView = gridView.getClass(); // 获得GridView类的class
        int column = -1;
        try {
            Field field = tempGridView.getDeclaredField("mRequestedNumColumns"); // 获得申明的字段
            field.setAccessible(true); // 设置访问权限
            column = Integer.valueOf(field.get(gridView).toString()); // 获取字段的值
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (column == -1)
            return 0;

        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i += column) {
            // 这边因为一排column个，所以i += column
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        return totalHeight;
    }

    /**
     * 根据GridView子项数量及内容设置GridView高度
     *
     * @param gridView GridView
     */
    public static void setGridViewHeight(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int columns;
        columns = gridView.getNumColumns();
        for (int i = 0, count = listAdapter.getCount(); i < count; i += columns) {
            // 这边因为一排column个，所以i += columns
            View gridItem = listAdapter.getView(i, null, gridView);
            gridItem.measure(0, 0);
            totalHeight += gridItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    /**
     * 根据ListView子项数量及内容设置ListView高度
     *
     * @param listView ListView
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, count = listAdapter.getCount(); i < count; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
