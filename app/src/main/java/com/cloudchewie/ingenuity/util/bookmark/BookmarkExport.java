package com.cloudchewie.ingenuity.util.bookmark;

import android.content.Context;
import android.net.Uri;

import com.cloudchewie.ingenuity.entity.BookmarkGroup;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class BookmarkExport {
    public static void exportBookmarks(Context context, BookmarkGroup bookmarkGroup, Uri fileUri) {
        if (bookmarkGroup == null)
            bookmarkGroup = new BookmarkGroup();
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(fileUri, "w");
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            writer.append(bookmarkGroup.toHtml());
            writer.flush();
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
