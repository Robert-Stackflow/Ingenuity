/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.service;

import com.cloudchewie.ingenuity.entity.Draft;
import com.cloudchewie.ingenuity.util.system.LocalStorage;

import java.util.ArrayList;
import java.util.List;

public class DraftService {

    public static void insert(Draft draft) {
        LocalStorage.getAppDatabase().draftDao().insert(draft);
    }

    public static void delete(Draft draft) {
        LocalStorage.getAppDatabase().draftDao().delete(draft);
    }

    public static void update(Draft draft) {
        LocalStorage.getAppDatabase().draftDao().update(draft);
    }

    public static List<Draft> getAll() {
        if (LocalStorage.getAppDatabase() != null)
            return LocalStorage.getAppDatabase().draftDao().getAll();
        return new ArrayList<>();
    }
}
