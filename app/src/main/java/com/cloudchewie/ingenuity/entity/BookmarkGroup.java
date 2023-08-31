package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookmarkGroup {
    boolean isRoot;

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    String name;
    int order;
    Date addDate;
    Date lastModified;

    public String getOriginFilePath() {
        return originFilePath;
    }

    public void setOriginFilePath(String originFilePath) {
        this.originFilePath = originFilePath;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    String originFilePath;
    Date importTime;
    List<Bookmark> bookmarks;
    List<BookmarkGroup> bookmarkGroups;

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public BookmarkGroup() {
        bookmarks = new ArrayList<>();
        bookmarkGroups = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
//        return "BookmarkGroup{" + "name='" + name + '\'' + ", bookmarks=" + bookmarks + ", bookmarkGroups=" + bookmarkGroups + '}';
        return "BookmarkGroup{" + "name='" + name + '\'' + ", bookmarks=" + bookmarks.size() + ", bookmarkGroups=" + bookmarkGroups.size() + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void addBookmark(Bookmark bookmark) {
        if (bookmarks == null) {
            bookmarks = new ArrayList<>();
        }
        bookmarks.add(bookmark);
    }

    public void addBookmarkGroup(BookmarkGroup bookmarkGroup) {
        if (bookmarkGroups == null) {
            bookmarkGroups = new ArrayList<>();
        }
        bookmarkGroups.add(bookmarkGroup);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public List<BookmarkGroup> getBookmarkGroups() {
        return bookmarkGroups;
    }

    public void setBookmarkGroups(List<BookmarkGroup> bookmarkGroups) {
        this.bookmarkGroups = bookmarkGroups;
    }

    public void initOrder() {
        for (int i = 0; i < bookmarkGroups.size(); i++) {
            bookmarkGroups.get(i).setOrder(i);
            bookmarkGroups.get(i).initOrder();
        }
        for (int i = 0; i < bookmarks.size(); i++) {
            bookmarks.get(i).setOrder(i);
        }
    }

    public String toHtml() {
        StringBuilder html = new StringBuilder();
        if (isRoot) {
            html.append("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n");
            html.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
            html.append("<TITLE>").append(name).append("</TITLE>\n");
            html.append("<H1>").append(name).append("</H1>\n");
            html.append("<DL><p>\n");
            for (BookmarkGroup child : bookmarkGroups) {
                html.append(child.toHtml());
            }
            for (Bookmark child : bookmarks) {
                html.append(child.toHtml());
            }
            html.append("</DL><p>");
        } else {
            html.append("<DT><H3 ADD_DATE=\"").append(addDate.getTime()).append("\" LAST_MODIFIED=\"").append(lastModified.getTime()).append("\">").append(name).append("</H3>\n");
            html.append("<DL><p>\n");
            for (BookmarkGroup child : bookmarkGroups) {
                html.append(child.toHtml());
            }
            for (Bookmark child : bookmarks) {
                html.append(child.toHtml());
            }
            html.append("</DL><p>\n");
        }
        return html.toString();
    }
}
