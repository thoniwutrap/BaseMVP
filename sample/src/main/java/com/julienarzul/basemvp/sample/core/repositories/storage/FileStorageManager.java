package com.julienarzul.basemvp.sample.core.repositories.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.julienarzul.basemvp.sample.core.mappers.ListMapper;
import com.julienarzul.basemvp.sample.core.mappers.Mapper;
import com.julienarzul.basemvp.sample.core.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Copyright @ Julien Arzul 2017
 */

public class FileStorageManager implements IStorageManager {

    private static final String STORAGE_DIRECTORY = "storage";

    private final Context context;

    public FileStorageManager(Context context) {
        this.context = context;
    }

    @NonNull
    private static File getStorageFileDir(Context context) {
        File mediasDir = new File(context.getFilesDir(), STORAGE_DIRECTORY);
        if (!mediasDir.exists()) {
            mediasDir.mkdir();
        }
        return mediasDir;
    }

    @NonNull
    private File getFileForKey(String storageKey) {
        return new File(getStorageFileDir(this.context), storageKey + ".json");
    }

    @Override
    public <MODEL, STORAGE> MODEL readObject(String storageKey, Class<STORAGE> storageTypeClass, Mapper<STORAGE, MODEL> mapper) {
        if (TextUtils.isEmpty(storageKey) || storageTypeClass == null || mapper == null) {
            return null;
        }

        File storageFile = this.getFileForKey(storageKey);

        STORAGE storageObject = FileUtils.readJsonObjectFile(storageFile, storageTypeClass);
        return mapper.map(storageObject);
    }

    @Override
    public <MODEL, STORAGE> List<MODEL> readObjectList(String storageKey, Class<STORAGE> storageTypeClass, Mapper<STORAGE, MODEL> mapper) {
        if (TextUtils.isEmpty(storageKey) || storageTypeClass == null || mapper == null) {
            return null;
        }

        File storageFile = this.getFileForKey(storageKey);
        List<STORAGE> storageList = FileUtils.readJsonListFile(storageFile, storageTypeClass);

        ListMapper<STORAGE, MODEL> listMapper = new ListMapper<>(mapper);

        return listMapper.map(storageList);
    }

    @Override
    public <MODEL, STORAGE> void writeObject(String storageKey, MODEL object, Class<STORAGE> storageTypeClass, Mapper<MODEL, STORAGE> mapper) {
        if (TextUtils.isEmpty(storageKey) || storageTypeClass == null || mapper == null) {
            return;
        }

        File storageFile = this.getFileForKey(storageKey);
        STORAGE storageObject = mapper.map(object);

        if (storageObject == null) {
            FileUtils.deleteFile(storageFile);
        } else {
            FileUtils.writeJsonObjectFile(storageFile, storageTypeClass, storageObject);
        }
    }

    @Override
    public <MODEL, STORAGE> void writeObjectList(String storageKey, List<MODEL> objectList, Class<STORAGE> storageTypeClass, Mapper<MODEL, STORAGE> mapper) {
        if (TextUtils.isEmpty(storageKey) || storageTypeClass == null || mapper == null) {
            return;
        }

        File storageFile = this.getFileForKey(storageKey);

        if (objectList == null) {
            FileUtils.deleteFile(storageFile);
        } else {
            ListMapper<MODEL, STORAGE> listMapper = new ListMapper<>(mapper);
            List<STORAGE> storageList = listMapper.map(objectList);

            FileUtils.writeJsonListFile(storageFile, storageTypeClass, storageList);
        }
    }
}
