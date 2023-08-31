package com.cloudchewie.ingenuity.util.bookmark;

import android.content.Context;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.Bookmark;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.util.system.AssetsUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.Objects;

public class BookmarkImport {
    public static BookmarkGroup importBookmarks(Context context, String filePath) {
        String content = AssetsUtil.getTextFileContent(context, filePath);
        BookmarkGroup bookmarkGroup = parseStringToBookmarkGroup(context, content);
        bookmarkGroup.initOrder();
        bookmarkGroup.setRoot(true);
        bookmarkGroup.setImportTime(new Date());
        bookmarkGroup.setOriginFilePath(filePath);
        return bookmarkGroup;
    }

    public static BookmarkGroup parseStringToBookmarkGroup(Context context, String content) {
        Document document = Jsoup.parse(String.valueOf(content));
        Element titleElement = document.selectFirst("title");
        String title;
        if (titleElement != null) {
            title = titleElement.text();
        } else {
            title = context.getString(R.string.bookmark_default_name);
        }
        return parseGroup(title, document.selectFirst("body > dl"));
    }

    /**
     * @param name   文件夹名称
     * @param parent 要遍历的文件夹的父节点
     * @return 得到的文件夹
     */
    public static BookmarkGroup parseGroup(String name, Element parent) {
        BookmarkGroup bookmarkGroup = new BookmarkGroup();
        bookmarkGroup.setName(name);
        if (parent == null) {
            return bookmarkGroup;
        }
        //遍历子节点
        Elements elements = parent.children();
        for (Element element : elements) {
            //如果不是DT标签跳过
            if (!element.tagName().equalsIgnoreCase("dt")) {
                continue;
            }
            if (element.selectFirst("h3") == null) {
                //不包含h3结点说明是具体书签
                //虽然只有一个子节点，但是避免找到所有a标签
                for (Element e : element.children()) {
                    String itemName = e.text();
                    String itemUrl = e.attr("href");
                    String itemIcon = e.attr("icon");
                    String itemAddTime = e.attr("add_date");
                    long timestamp;
                    try {
                        timestamp = Long.parseLong(itemAddTime);
                    } catch (NumberFormatException __) {
                        timestamp = new Date().getTime();
                    }
                    bookmarkGroup.addBookmark(new Bookmark(itemUrl, itemName, "", itemIcon, new Date(timestamp)));
                }
            } else {
                //否则是文件夹，继续遍历
                //遍历时直接选择子节点即可，又变回DL标签
                Element h3 = Objects.requireNonNull(element.selectFirst("h3"));
                String childName = h3.text();
                long timestamp1, timestamp2;
                try {
                    timestamp1 = Long.parseLong(h3.attr("add_date"));
                    timestamp2 = Long.parseLong(h3.attr("last_modified"));
                } catch (NumberFormatException __) {
                    timestamp1 = new Date().getTime();
                    timestamp2 = new Date().getTime();
                }
                BookmarkGroup child = parseGroup(childName, element.selectFirst("dl"));
                child.setAddDate(new Date(timestamp1));
                child.setLastModified(new Date(timestamp2));
                bookmarkGroup.addBookmarkGroup(child);
            }
        }
        return bookmarkGroup;
    }
}
