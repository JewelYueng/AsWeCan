package org.k2.processmining.storage;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.k2.processmining.model.log.AbstractLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
        logStorage = new HDFS();
        user = new User();
        user.setId("test");
        user.setName("test");
        user.setEmail("test@test.com");
        user.setPassword("123456");
        user.setState(1);
        logStorage.makeDirectoryForUser(user);
        testFile = File.createTempFile("PM-", "-logStorageTest");

        log = new RawLog();
        log.setId("test");
        log.setUserId(user.getId());
    }

    @AfterClass
    public static void destroy() throws Exception {
        logStorage.deleteUser(user);
        testFile.delete();
    }

    @Test
    public void logTest() throws Exception {
        logStorage.upload(log, new FileInputStream(testFile));
        Assert.assertTrue(logStorage.isExist(log));

        logStorage.delete(log);
        Assert.assertFalse(logStorage.isExist(log));
    }
}
