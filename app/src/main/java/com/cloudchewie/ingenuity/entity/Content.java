package com.cloudchewie.ingenuity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Content extends Serializable {
    int getContentId();

    String getTitle();

    Date getGmtEdit();

    String getContent();

    int getCommentCount();

    User getUser();

    int getThumbupCount();

    int getCollectionCount();

    List<String> getImageUrls();
}
