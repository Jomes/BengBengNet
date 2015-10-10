package com.sohu.focus.framework.upgrade;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.Patterns;

/**
 * Created by miaogao on 13-6-19.
 */
public class Upgrade {

    private Builder builder = null;

    private Upgrade(Builder builder) {
        this.builder = builder;
    }

    public void upgrade(UpgradeDialogFinishListener listener) {
        if (!Patterns.WEB_URL.matcher(builder.url).matches())
            Log.e(getClass().getName(), "Cannot perform upgrade, url: " + builder.url
                    + " is not matching " + Patterns.WEB_URL.toString());
        if (isHigherVersion()) {
            downloadPackage();
        } else if (listener != null)
            listener.onUpgradeDialogFinish(false);
    }

    public void downloadPackage() {
        Intent intent = new Intent();
        intent.setClass(builder.context, UpgradeDownloadService.class);
        intent.putExtra(UpgradeDownloadService.INTENT_EXTRA_URL, builder.url);
        intent.putExtra(UpgradeDownloadService.INTENT_EXTRA_ICONID, builder.iconId);
        PackageManager pm = builder.context.getPackageManager();
        if (pm.queryIntentServices(intent, 0).size() == 0) {
            Log.e(getClass().getName(),
                    "Upgrade download not started, did you declare UpgradeDownloadService in your AndroidManifest.xml?");
            return;
        }
        builder.context.startService(intent);
    }

    // 2.0 - 2.0.3 - 3.0.0
    private boolean isHigherVersion() {
        return true;
        // if (TextUtils.isEmpty(builder.version)) {
        // return false;
        // }
        //
        // PackageManager pm = builder.context.getPackageManager();
        // try {
        // PackageInfo info = pm.getPackageInfo(builder.context.getPackageName(), 0);
        // int currentVersionNameInteger = Integer.parseInt(info.versionName.trim().replace(".",
        // ""));
        // int newVersionNameInteger = Integer.parseInt(builder.version.trim().replace(".", ""));
        // Log.d(builder.context.getPackageName(), "current version name = " +
        // currentVersionNameInteger);
        // Log.d(builder.context.getPackageName(), "new version name = " + newVersionNameInteger);
        // if (newVersionNameInteger > currentVersionNameInteger) {
        // return true;
        // }
        // } catch (PackageManager.NameNotFoundException e) {
        // Log.e(getClass().getName(), "PackageManager.NameNotFoundException for " +
        // builder.context.getPackageName());
        // }
        // return false;
    }

    public interface UpgradeDialogFinishListener {
        public void onUpgradeDialogFinish(boolean isUpgradeStarted);
    }
    /**
     * 用于更新dailog进度条
     * @author jomeslu
     *
     */
    public interface UpgradeDialogListener {
    	public void onUpgradeDialogFinish(boolean isUpgradeStarted);
    	public void getProgress(int num);
    }
    
    

    public static class Builder {
        private int iconId = 0;
        private String title = null;
        private String message = null;
        private String version = null;
        private String url = null;
        private Context context = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder serIconId(int iconId) {
            this.iconId = iconId;
            return this;
        }

        public void upgrade(UpgradeDialogFinishListener listener) {
            if (context == null)
                throw new NullPointerException("Context cannot be NULL");

            // if (TextUtils.isEmpty(version))
            // Log.e(getClass().getName(),
            // "VERSION is EMPTY, in which case the app will not be upgraded anyway");

            // if (iconId == 0)
            // throw new IllegalArgumentException("Must set icon res id for icon drawable");

            Upgrade upgrade = new Upgrade(this);
            upgrade.upgrade(listener);
        }
        
        
//        public void upgrade(UpgradeDialogListener listener){
//        	if (context == null)
//                throw new NullPointerException("Context cannot be NULL");
//        	 Upgrade upgrade = new Upgrade(this);
//             upgrade.upgrade(listener);
//        	
//        }
        
        
    }

}
