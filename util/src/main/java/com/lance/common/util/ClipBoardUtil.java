package com.lance.common.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by lindan on 16-10-25.
 * 剪贴板工具
 */
public class ClipBoardUtil {
    private ClipBoardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 实现文本复制功能
     *
     * @param context Context
     * @param input   要复制的文本
     */
    public static void copy(Context context, String input) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, input);
        cmb.setPrimaryClip(clip);
    }

    /**
     * 实现粘贴功能
     *
     * @param context Context
     * @return 文本
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb.hasPrimaryClip()) {
            ClipData clipData = cmb.getPrimaryClip();
            if (clipData.getItemCount() > 0) {
                return clipData.getItemAt(0).getText().toString();
            }
        }
        return null;
    }
}
