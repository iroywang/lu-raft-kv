package cn.think.in.java.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import com.alibaba.fastjson.JSON;


import org.junit.Before;
import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sun.misc.BASE64Encoder;

/**
 *
 * @author 莫那·鲁道
 */
public class RocksDBTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStateMachine.class);

    private String dbDir = "./rocksDB-raft/" + System.getProperty("serverPort");
    private String stateMachineDir = dbDir + "/test";

    public RocksDB machineDb;

    static {
        RocksDB.loadLibrary();
    }

    class Inner {
        public String  v1 = "Fake News";
        public String v2 = "Go ahead";
    }


    public  boolean searchMatrix(int[][] nums, int x) {
        // Todo your code goes here...
        int rows = nums.length;
        int colomn = nums[0].length;
        int i = 0, j = colomn - 1;
        while(i < rows && j >= 0){
            while(i < rows && j >= 0 && nums[i][j] < x) ++i;
            while(i < rows && j >= 0 && nums[i][j] > x) --j;
            if(i < rows && j >= 0 && nums[i][j] == x){
                return true;
            }
        }
        return false;
    }

    @Test
    public void main(){

    }



    public byte[] lastIndexKey = "LAST_INDEX_KEY".getBytes();

    public RocksDBTest() {
        try {
            System.setProperty("serverPort", "8078");
            File file = new File(stateMachineDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            Options options = new Options();
            options.setCreateIfMissing(true);
            machineDb = RocksDB.open(options, stateMachineDir);
            //Arrays.stream(new int[0]).max();
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }


    public static RocksDBTest getInstance() {
        return RocksDBTestLazyHolder.INSTANCE;
    }

    private static class RocksDBTestLazyHolder {

        private static final RocksDBTest INSTANCE = new RocksDBTest();
    }

    RocksDBTest instance;

    @Before
    public void before() {
        instance = getInstance();
    }

    @Test
    public void test() throws RocksDBException {
//        System.out.println(getLastIndex());
//        System.out.println(get(getLastIndex()));
        byte[] b = machineDb.get("fuck".getBytes());
        System.out.println(b);
        java.lang.String str = new BASE64Encoder().encode(b);
        System.out.printf(str);
//        write(new Cmd("hello", "value"));
//
//        System.out.println(getLastIndex());
//
//        System.out.println(get(getLastIndex()));
//
//        //deleteOnStartIndex(getLastIndex());
//
//        write(new Cmd("hello", "value"));
//
//        //deleteOnStartIndex(1L);
//
//        System.out.println(getLastIndex());
//
//        System.out.println(get(getLastIndex()));


    }

    public synchronized void write(Cmd cmd) {
        try {
            cmd.setIndex(getLastIndex() + 1);
            machineDb.put(cmd.getIndex().toString().getBytes(), JSON.toJSONBytes(cmd));
        } catch (RocksDBException e) {
            e.printStackTrace();
        } finally {
            updateLastIndex(cmd.getIndex());
        }
    }

    public synchronized void deleteOnStartIndex(Long index) {
        try {
            for (long i = index; i <= getLastIndex(); i++) {
                try {
                    machineDb.delete((i + "").getBytes());
                } catch (RocksDBException e) {
                    e.printStackTrace();
                }
            }

        } finally {
            updateLastIndex(index - 1);
        }
    }

    public Cmd get(Long index) {
        try {
            if (index == null) {
                throw new IllegalArgumentException();
            }
            byte[] cmd = machineDb.get(index.toString().getBytes());
            if (cmd != null) {
                return JSON.parseObject(machineDb.get(index.toString().getBytes()), Cmd.class);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateLastIndex(Long index) {
        try {
            // overWrite
            machineDb.put(this.lastIndexKey, index.toString().getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    public Long  getLastIndex() {
        byte[] lastIndex = new byte[0];
        try {
            lastIndex = machineDb.get(this.lastIndexKey);
            if (lastIndex == null) {
                lastIndex = "0".getBytes();
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return Long.valueOf(new String(lastIndex));
    }

    @Setter
    @Getter
    @ToString
    static class Cmd {

        Long index;
        String key;
        String value;

        public Cmd() {
        }

        public Cmd(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
