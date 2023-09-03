package com.cloudchewie.ingenuity.entity;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "bookmark")
public class BookmarkGroup implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    String name;
    int order;
    Date addDate;
    boolean isRoot;

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    @Ignore
    boolean isAll = false;

    Date lastModified;
    @Ignore
    Uri originUri;
    Date importTime;
    List<Bookmark> bookmarks;
    List<BookmarkGroup> bookmarkGroups;

    public Integer getId() {
        return id;
    }


    public int size() {
        return sizeOfBookmarkGroups() + sizeOfBookmarks();
    }

    public int sizeOfBookmarks() {
        return (bookmarks != null ? bookmarks.size() : 0);
    }

    public int sizeOfBookmarkGroups() {
        return (bookmarkGroups != null ? bookmarkGroups.size() : 0);
    }

    public Object get(int index) {
        if (index < 0 || index > size())
            return null;
        else if (index >= 0 && index < sizeOfBookmarkGroups())
            return bookmarkGroups.get(index);
        else return bookmarks.get(index - sizeOfBookmarkGroups());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public Uri getOriginUri() {
        return originUri;
    }

    public void setOriginUri(Uri originUri) {
        this.originUri = originUri;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }


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

    public void deleteGroup(BookmarkGroup delete) {
        bookmarkGroups.remove(delete);
    }

    public void deleteItem(Bookmark delete) {
        bookmarks.remove(delete);
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

    public List<Bookmark> find(String key) {
        List<Bookmark> found = new ArrayList<>();
        for (Bookmark bookmark : bookmarks)
            if (bookmark.name.contains(key) || bookmark.url.contains(key))
                found.add(bookmark);
        for (BookmarkGroup bookmarkGroup : bookmarkGroups)
            found.addAll(bookmarkGroup.find(key));
        return found;
    }

    @NonNull
    @Override
    public BookmarkGroup clone() {
        try {
            BookmarkGroup group = (BookmarkGroup) super.clone();
            List<BookmarkGroup> clonedBookmarkGroups = new ArrayList<>();
            for (BookmarkGroup bookmarkGroup : bookmarkGroups) {
                clonedBookmarkGroups.add(bookmarkGroup.clone());
            }
            group.setBookmarkGroups(clonedBookmarkGroups);
            List<Bookmark> clonedBookmarks = new ArrayList<>();
            for (Bookmark bookmark : bookmarks) {
                clonedBookmarks.add(bookmark.clone());
            }
            group.setBookmarks(clonedBookmarks);
            return group;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
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
