package org.k2.processmining.storage;


import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.user.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.*;

public interface LogStorage {
    String getLogLocation(AbstractLog log) throws IOException;
    void makeDirectoryForUser(User user) throws IOException;
    boolean isExist(AbstractLog log) throws IOException;
    boolean isExist(User user) throws IOException;
    void deleteUser(User user) throws IOException;
    void upload(AbstractLog log, InputStream inputStream) throws IOException;
    <T> T upload(AbstractLog log, ProcessOutputStream<T> processOutputStream) throws IOException;
    default void upload(AbstractLog log, File file) throws IOException{
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))){
            upload(log, inputStream);
        }
    }
    @PreAuthorize("#log!=null and T(org.k2.processmining.model.LogState).ACTIVE.value==#log.state " +
            "and ( hasRole('ROLE_ADMIN') or hasPermission(#log, 'download') " +
                    "or T(org.k2.processmining.model.LogShareState).SHARED.value==#log.isShared)")
    void download(AbstractLog log, OutputStream outputStream) throws IOException;

    @PreAuthorize("#log!=null and T(org.k2.processmining.model.LogState).ACTIVE.value==#log.state " +
            "and ( hasRole('ROLE_ADMIN') or hasPermission(#log, 'download') " +
                    "or T(org.k2.processmining.model.LogShareState).SHARED.value==#log.isShared)")
    <T> T download(AbstractLog log, ProcessInputStream<T> processInputStream) throws IOException;
    default void download(AbstractLog log, File outputFile) throws IOException{
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))){
            download(log, outputStream);
        }
    }
    void delete(AbstractLog log) throws IOException;

    interface ProcessInputStream<T> {
        T processInputStream(InputStream inputStream) throws IOException;
    }

    interface ProcessOutputStream<T> {
        T processOutputStream(OutputStream outputStream) throws IOException;
    }
}
