package cn.think.in.java.impl;

import cn.think.in.java.current.RaftThreadPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.think.in.java.entity.Command;
import cn.think.in.java.entity.LogEntry;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author 莫那·鲁道
 */
public class DefaultLogModuleTest {

    static {
        System.setProperty("serverPort", "8779");
        DefaultLogModule.dbDir = "/Users/cxs/code/lu-raft-revert/rocksDB-raft/" + System.getProperty("serverPort");
        DefaultLogModule.logsDir = DefaultLogModule.dbDir + "/logModule";
    }

    DefaultLogModule defaultLogs;

    @Before
    public void setUp() throws Exception {
        System.setProperty("serverPort", "8777");

        defaultLogs = DefaultLogModule.getInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void write() throws InterruptedException {
//        LogEntry entry = LogEntry.newBuilder().
//            term(1).
//            command(Command.newBuilder().key("hello").value("world").build()).
//            build();
//        defaultLogs.write(entry);
//
//        Assert.assertEquals(entry, defaultLogs.read(entry.getIndex()));
        Thread main = Thread.currentThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    LockSupport.unpark(main);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        boolean flag = true;
        try{

            System.out.println("try");
            LockSupport.park(main);
            flag = false;
        }catch (Exception e){
            System.out.println("exception");
        }
        finally {
            System.out.println("finally");
        }
    }

    @Test
    public void read() {
        System.out.println(defaultLogs.getLastIndex());
    }

    @Test
    public void remove() {
        defaultLogs.removeOnStartIndex(3L);
    }

    @Test
    public void getLast() {

    }

    @Test
    public void getLastIndex() {
    }

    @Test
    public void getDbDir() {
    }

    @Test
    public void getLogsDir() {
    }

    @Test
    public void setDbDir() {
    }
}