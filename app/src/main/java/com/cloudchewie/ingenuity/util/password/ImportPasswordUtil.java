package com.cloudchewie.ingenuity.util.password;

import android.content.Context;
import android.net.Uri;

import com.cloudchewie.ingenuity.entity.AuthPassword;
import com.cloudchewie.ingenuity.entity.BackupPassword;
import com.cloudchewie.ingenuity.entity.CommonPassword;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ImportPasswordUtil {
    public static void importJsonFile(Context context, Uri fileUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                builder.append(line);
            bufferedReader.close();
            inputStream.close();
            String content = builder.toString();
            Gson gson = new Gson();
            JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
            PasswordGroup passwordGroup = gson.fromJson(jsonObject, PasswordGroup.class);
            passwordGroup.setId(null);
            JsonElement jsonElement = jsonObject.getAsJsonArray("passwords");
            Integer id = Math.toIntExact(LocalStorage.getAppDatabase().passwordGroupDao().insert(passwordGroup));
            switch (passwordGroup.getType()) {
                case COMMON:
                    List<CommonPassword> commonPasswordList = Arrays.asList(gson.fromJson(jsonElement, CommonPassword[].class));
                    for (CommonPassword commonPassword : commonPasswordList)
                        commonPassword.setGroupId(id);
                    LocalStorage.getAppDatabase().commonPasswordDao().insertAll(commonPasswordList);
                case BACKUP:
                    List<BackupPassword> backupPasswordList = Arrays.asList(gson.fromJson(jsonElement, BackupPassword[].class));
                    for (BackupPassword backupPassword : backupPasswordList)
                        backupPassword.setGroupId(id);
                    LocalStorage.getAppDatabase().backupPasswordDao().insertAll(backupPasswordList);
                case AUTH:
                    List<AuthPassword> authPasswordList = Arrays.asList(gson.fromJson(jsonElement, AuthPassword[].class));
                    for (AuthPassword authPassword : authPasswordList)
                        authPassword.setGroupId(id);
                    LocalStorage.getAppDatabase().authPasswordDao().insertAll(authPasswordList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
