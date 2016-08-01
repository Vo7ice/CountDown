package com.cn.guojinhu.game2048.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

import static com.cn.guojinhu.game2048.utils.SPUtils.FILE_NAME;


public class GameBackupAgent extends BackupAgentHelper {

    static final String SHARED_KEY = "share_prefs";
    @Override
    public void onCreate() {
        addHelper(SHARED_KEY,new SharedPreferencesBackupHelper(this,FILE_NAME));
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
    }
}
