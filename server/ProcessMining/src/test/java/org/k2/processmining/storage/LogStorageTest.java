package org.k2.processmining.storage;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;

import java.io.*;

/**
 * Created by nyq on 2017/6/21.
 */
public class LogStorageTest {

    private static LogStorage logStorage;
    private static User user;
    private static File testFile;
    private static AbstractLog log;


    @BeforeClass
    public static void init() throws Exception {
        logStorage = new LocalLogStorage();

        testFile = File.createTempFile("PM-", "-logStorageTest");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))){
            writer.write("This is a file!\n");
        }
        user = new User();
        user.setId("test");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setPassword("123456");
        user.setState(1);
        log = new RawLog();
        log.setId("test");
        log.setUserId(user.getId());

        logStorage.makeDirectoryForUser(user);
    }

    @Test
    public void uploadTest() throws Exception {
        logStorage.upload(log, new FileInputStream(testFile));
        Assert.assertTrue(logStorage.isExist(log));
    }

    @Test
    public void downloadTest() throws Exception {
        logStorage.download(log, inputStream -> {
            int i;
            try {
                while ((i=inputStream.read()) != -1) {
                    System.out.print((char)i);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
    }

    @Test
    public void deleteTest() throws Exception {
        logStorage.delete(log);
        Assert.assertFalse(logStorage.isExist(log));
    }

    @Test
    public void deleteUserTest() throws Exception {
        logStorage.deleteUser(user);
    }
}
