package org.k2.processmining.storage;


import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;

import java.io.*;

public interface LogStorage {
    String getLogLocation(AbstractLog log);
    boolean makeDirectoryForUser(User user);
    boolean deleteUser(User user);
    boolean upload(AbstractLog log, InputStream inputStream);
    boolean upload(AbstractLog log, ProcessOutputStream processOutputStream);
    default boolean upload(AbstractLog log, File file) {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))){
            return upload(log, inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    boolean download(AbstractLog log, OutputStream outputStream);
    boolean download(AbstractLog log, ProcessInputStream processInputStream);
    default boolean download(AbstractLog log, File outputFile) {
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))){
            return download(log, outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    boolean delete(AbstractLog log);

    interface ProcessInputStream {
        boolean processInputStream(InputStream inputStream);
    }

    interface ProcessOutputStream {
        boolean processOutputStream(OutputStream outputStream);
    }
}
