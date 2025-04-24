package com.ests.gestionmodules;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (id == ModuleListActivity.DownloadHandler.getDownloadId()) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);
            
            if (cursor.moveToFirst()) {
                int statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (statusColumnIndex >= 0) {
                    int status = cursor.getInt(statusColumnIndex);
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        ModuleListActivity.DownloadHandler.dismissDialog();
                        Toast.makeText(context, "Téléchargement terminé", Toast.LENGTH_LONG).show();
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        ModuleListActivity.DownloadHandler.dismissDialog();
                        Toast.makeText(context, "Erreur lors du téléchargement", Toast.LENGTH_LONG).show();
                    }
                }
            }
            cursor.close();
        }
    }
} 