package com.example.bsdiffdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.utils.UriParseUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Activity activity;

    // 用于在应用程序启动时，加载本地lib库
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.version);
        tv.setText(BuildConfig.VERSION_NAME);

        //运行时权限的申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    /**
     * 合成安装包
     * @param oldApk 就得安装包 现在的版本v1.0
     * @param patch 差分包（补丁）
     * @param outputApk 合成后，新版本的文件 v2.0的apk
     *  差分包的命令 bsdiff old.apk new.apk patch   (说明：bsdiff固定的，old.apk是旧版本 new.apk是新版本  patch表示差分包，名称随意，但是要和代码中的对应)
     */
    public native void bsPatch(String oldApk, String patch, String outputApk);


    @SuppressLint("StaticFieldLeak")
    public void updata(View view) {

        new AsyncTask<Void, Void, File>() {
            /**
             * 做耗时操作，完成后返回file
             * @param voids
             * @return
             */
            @Override
            protected File doInBackground(Void... voids) {
                //获取现在手机上安装的apk的版本
                String oldApk = getApplicationInfo().sourceDir;
                //不模拟下载过程，直接放在sdcard中
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                Log.e("pathc", "--------->" + patch);
                //合成后的新文件放入SDCard中
                String outputApk = createNewApk().getAbsolutePath();
                Log.e("outputApk", "--------->" + outputApk);

                bsPatch(oldApk, patch, outputApk);

                return new File(outputApk);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                Log.e("安装前", "--------->" );
                //安装合成后的apk （v2.0）
                UriParseUtils.installApk(activity, file);
                Log.e("安装后", "--------->" );
            }
        }.execute();


    }

    /**
     * 创建合成后的新版本 V2.0
     *
     * @return
     */
    private File createNewApk() {
//占坑 最后合成的apk文件
        File newFile = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");
        Log.e("newFile", "--------->" + newFile.getAbsolutePath());
        try {
            if (!newFile.exists()) {
                newFile.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }
}
