package com.cloudchewie.ingenuity.util.authenticator;

import android.content.Context;
import android.net.Uri;

import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.entity.ExportOtpTokens;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ImportExportTokenUtil {
    public static void importJsonFile(Context context, Uri fileUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ExportOtpTokens exportOtpTokens = new Gson().fromJson(bufferedReader.readLine(), ExportOtpTokens.class);
            bufferedReader.close();
            inputStream.close();
            LocalStorage.getAppDatabase().otpTokenDao().insertAll(exportOtpTokens.getTokens());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportJsonFile(Context context, Uri fileUri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(fileUri, "w");
            List<OtpToken> tokens = LocalStorage.getAppDatabase().otpTokenDao().getAll();
            List<String> tokenOrder = new ArrayList<>();
            for (OtpToken token : tokens) {
                if (token.getIssuer() != null) {
                    tokenOrder.add(token.getIssuer() + ":" + token.getAccount());
                } else {
                    tokenOrder.add(token.getAccount());
                }
            }
            outputStream.write(new Gson().toJson(new ExportOtpTokens(tokens, tokenOrder)).getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importKeyUriFile(Context context, Uri fileUri) {
        long currentLastOrdinal = LocalStorage.getAppDatabase().otpTokenDao().getLastOrdinal() != null ? LocalStorage.getAppDatabase().otpTokenDao().getLastOrdinal() : 0L;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int index = 0;
            List<OtpToken> otpTokens = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                OtpToken token = OtpTokenParser.createFromUri(Uri.parse((line.trim())));
                token.setOrdinal(currentLastOrdinal + index + 1);
                otpTokens.add(token);
                index++;
            }
            bufferedReader.close();
            inputStream.close();
            LocalStorage.getAppDatabase().otpTokenDao().insertAll(otpTokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportKeyUriFile(Context context, Uri fileUri) {
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(fileUri, "w");
            PrintWriter printWriter = new PrintWriter(outputStream);
            List<OtpToken> tokens = LocalStorage.getAppDatabase().otpTokenDao().getAll();
            for (OtpToken token : tokens)
                printWriter.println(OtpTokenParser.toUri(token).toString());
            printWriter.flush();
            printWriter.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
