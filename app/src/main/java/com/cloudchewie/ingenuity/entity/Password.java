package com.cloudchewie.ingenuity.entity;

import java.io.Serializable;
import java.util.Date;

public interface Password extends Serializable {
    Integer getId();

    String getIssuer();

    String getUsername();

    String getRemark();

    String getPassword();

    Date getAddDate();

    Date getEditDate();
}
