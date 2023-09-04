package com.cloudchewie.ingenuity.util.password;

import android.content.Context;
import android.net.Uri;

import com.cloudchewie.ingenuity.entity.AuthPassword;
import com.cloudchewie.ingenuity.entity.BackupPassword;
import com.cloudchewie.ingenuity.entity.CommonPassword;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExportPasswordUtil {
    public static void exportJsonFile(Context context, PasswordGroup passwordGroup, Uri fileUri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(fileUri, "w");
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(passwordGroup).getAsJsonObject();
            switch (passwordGroup.getType()) {
                case AUTH:
                    List<AuthPassword> authPasswordList = LocalStorage.getAppDatabase().authPasswordDao().get(passwordGroup.getId());
                    jsonObject.add("passwords", gson.toJsonTree(authPasswordList));
                    break;
                case BACKUP:
                    List<BackupPassword> backupPasswordList = LocalStorage.getAppDatabase().backupPasswordDao().get(passwordGroup.getId());
                    jsonObject.add("passwords", gson.toJsonTree(backupPasswordList));
                    break;
                case COMMON:
                    List<CommonPassword> commonPasswordList = LocalStorage.getAppDatabase().commonPasswordDao().get(passwordGroup.getId());
                    jsonObject.add("passwords", gson.toJsonTree(commonPasswordList));
                    break;
            }
            outputStream.write(gson.toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
