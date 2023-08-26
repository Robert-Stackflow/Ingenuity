package com.vincent.filepicker.filter;

import static com.vincent.filepicker.filter.callback.FileLoaderCallbacks.TYPE_AUDIO;
import static com.vincent.filepicker.filter.callback.FileLoaderCallbacks.TYPE_FILE;
import static com.vincent.filepicker.filter.callback.FileLoaderCallbacks.TYPE_VIDEO;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.vincent.filepicker.filter.callback.FileLoaderCallbacks;
import com.vincent.filepicker.filter.callback.FilterResultCallback;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.filter.entity.VideoFile;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 10:19
 */

public class FileFilter {
    public static void getVideos(@NonNull FragmentActivity activity, FilterResultCallback<VideoFile> callback) {
        activity.getSupportLoaderManager().initLoader(1, null, new FileLoaderCallbacks(activity, callback, TYPE_VIDEO));
    }

    public static void getAudios(@NonNull FragmentActivity activity, FilterResultCallback<AudioFile> callback) {
        activity.getSupportLoaderManager().initLoader(2, null, new FileLoaderCallbacks(activity, callback, TYPE_AUDIO));
    }

    public static void getFiles(@NonNull FragmentActivity activity, FilterResultCallback<NormalFile> callback, String[] suffix) {
        activity.getSupportLoaderManager().initLoader(3, null, new FileLoaderCallbacks(activity, callback, TYPE_FILE, suffix));
    }
}
