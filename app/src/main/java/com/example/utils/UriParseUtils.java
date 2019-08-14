package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * Author:${YAN}
 * Time:2019/8/13 0013 下午 4:57
 * Description:
 */
public class UriParseUtils {


    /**
     * 创建文件输出路径的Uri
     * @param context
     * @param file
     * @return   转换后的scheme 为fileProvider的Uri
     */
    private static Uri getUriForFile(Context context, File file){

        return FileProvider.getUriForFile(context,getFileProvider(context),file);
    }

    /**
     * 获取fileProvider 路径，适配6.0+
     * @param context   上下文
     * @return    fileprovider 路径
     */
    private static String getFileProvider(Context context) {
        return context.getApplicationInfo().packageName+".fileprovider";
    }

    /**
     * 安装apk
     */

    public static void install(Activity activity, File apkFile){
        if (!apkFile.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            Uri fileUri =getUriForFile(activity,apkFile);
            intent.setDataAndType(fileUri,"application/vnd.android.package-archive");
        }else {
            intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
        }

        activity.startActivity(intent);
    }


    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Activity context, File file) {
        Log.e("安装中", "--------->" +file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri photoOutputUri = null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {

            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
            photoOutputUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(photoOutputUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
