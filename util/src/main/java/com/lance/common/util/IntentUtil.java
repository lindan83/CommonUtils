package com.lance.common.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.util.List;

/**
 * @author lindan
 */
public class IntentUtil {
    private IntentUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查系统是否能响应指定的Intent，避免崩溃
     *
     * @param context Context
     * @param intent  Intent
     * @return boolean
     */
    public static boolean isIntentSafe(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        return activities != null && activities.size() > 0;
    }

    /**
     * 打开联系人选择页面
     *
     * @param activity    Activity
     * @param requestCode requestCode
     */
    public static void chooseContacts(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片
     *
     * @param activity     Activity
     * @param srcImageUri  要裁剪的原始图片
     * @param saveImageUri 裁剪后保存的图片
     * @param x            宽比例
     * @param y            高比例
     * @param width        宽度
     * @param height       高度
     * @param requestCode  requestCode
     */
    public static void clipImage(Activity activity, Uri srcImageUri, Uri saveImageUri, int x, int y, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcImageUri, "image/*");
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", x);
        intent.putExtra("aspectY", y);
        //outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveImageUri);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动照相机
     *
     * @param activity    Activity
     * @param path        保存的图片路径
     * @param requestCode requestCode
     */
    public static void startCamera(Activity activity, String path, int requestCode) {
        File outputImage = new File(path);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动相册
     *
     * @param activity    Activity
     * @param requestCode requestCode
     */
    public static void openAlbum(Activity activity, int requestCode) {
        Intent intent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动浏览器
     *
     * @param context Context
     * @param url     Url
     */
    public static void openWebBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 打开拨号界面
     *
     * @param context     Context
     * @param phoneNumber phone number
     */
    public static void startDial(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 直接拨号
     *
     * @param context     Context
     * @param phoneNumber phone number
     */
    public static void startCall(Context context, String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
}
