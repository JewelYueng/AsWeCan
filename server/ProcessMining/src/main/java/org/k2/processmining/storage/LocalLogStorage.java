package org.k2.processmining.storage;


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
    public String getLogLocation(AbstractLog log) {
        return ROOT_DIRECTORY + "/" + log.getUserId() + "/" + log.getType() + "/" + log.getId();
    }

    @Override
    public boolean makeDirectoryForUser(User user) {
        String[] dirs = Util.getLogTypeNames();
        for (String dir : dirs) {
            if (! new File(ROOT_DIRECTORY + "/" + user.getId() + "/" + dir).mkdirs()) {
                deleteUser(user);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isExist(AbstractLog log) {
        return new File(getLogLocation(log)).isFile();
    }

    @Override
    public boolean isExist(User user) {
        return new File(ROOT_DIRECTORY + "/" + user.getId()).isDirectory();
    }

    private boolean deleteTempFile(File tempFile) {
        try {
            if(tempFile.isDirectory()) {
                File[] entries = tempFile.listFiles();
                for (File currentFile : entries) {
                    if (!deleteTempFile(currentFile)) {
                        return false;
                    }
                }
            }
            if (! tempFile.delete()) {
                return false;
            }
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteUser(User user) {
        return deleteTempFile(new File(ROOT_DIRECTORY + "/" + user.getId()));
    }

    @Override
    public boolean upload(AbstractLog log, InputStream inputStream) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(getLogLocation(log)))){
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len=inputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
            bufferedOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean download(AbstractLog log, OutputStream outputStream) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(getLogLocation(log)))){
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len=bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
            bufferedOutputStream.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(AbstractLog log) {
        return new File(getLogLocation(log)).delete();
    }

    @Override
    public <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) {
        try (OutputStream outputStream = new FileOutputStream(getLogLocation(log))){
            return processOutputStream.processOutputStream(outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) {
        try (InputStream inputStream = new FileInputStream(getLogLocation(log))){
            return processInputStream.processInputStream(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
