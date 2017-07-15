package org.k2.processmining.storage;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.util.Util;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by nyq on 2017/6/11.
 */
//@Component
public class LocalLogStorage implements LogStorage {
    private static final String ROOT_DIRECTORY = "E:/ppmm";

    public LocalLogStorage() {
        new File(ROOT_DIRECTORY).mkdirs();
    }

    @Override
    public String getLogLocation(AbstractLog log) throws IOException {
        if (log == null || log.getUserId() == null || log.getId() == null) {
            throw new URLJoinException("Fail to join URL for AbstractLog:" + log);
        }
        return ROOT_DIRECTORY + "/" + log.getUserId() + "/" + log.getType() + "/" + log.getId();
    }

    @Override
    public void makeDirectoryForUser(User user) throws IOException {
        String[] dirs = Util.getLogTypeNames();
        for (String dir : dirs) {
            if (! new File(ROOT_DIRECTORY + "/" + user.getId() + "/" + dir).mkdirs()) {
                deleteUser(user);
            }
        }
    }

    @Override
    public boolean isExist(AbstractLog log) throws IOException {
        return new File(getLogLocation(log)).isFile();
    }

    @Override
    public boolean isExist(User user) {
        return new File(ROOT_DIRECTORY + "/" + user.getId()).isDirectory();
    }

    @Override
    public void deleteUser(User user) throws IOException {
        FileUtils.deleteDirectory(new File(ROOT_DIRECTORY + "/" + user.getId()));
    }

    @Override
    public void upload(AbstractLog log, InputStream inputStream) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getLogLocation(log)))){
            IOUtils.copyLarge(inputStream, bufferedOutputStream);
        }
    }

    @Override
    public void download(AbstractLog log, OutputStream outputStream) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(getLogLocation(log)))){
            IOUtils.copyLarge(bufferedInputStream, outputStream);
        }
    }

    @Override
    public void delete(AbstractLog log) throws IOException {
        if (! new File(getLogLocation(log)).delete()) {
            throw new IOException("Fail to delete log: " + log);
        }
    }

    @Override
    public <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(getLogLocation(log))){
            return processOutputStream.processOutputStream(outputStream);
        }
    }

    @Override
    public <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) throws IOException{
        try (InputStream inputStream = new FileInputStream(getLogLocation(log))){
            return processInputStream.processInputStream(inputStream);
        }
    }
}
