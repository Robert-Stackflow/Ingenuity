package com.cloudchewie.ingenuity.util.bookmark;

import android.content.Context;
import android.util.Log;

import com.cloudchewie.ingenuity.entity.BookmarkGroup;

public class BookmarkExport {
    public static String exportBookmarks(Context context, BookmarkGroup bookmarkGroup, String filePath) {
        Log.d("xuruida", bookmarkGroup.toHtml());
        return filePath;
    }
}
