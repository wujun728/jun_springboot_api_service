package com.yuyaogc.lowcode.engine.plugin.redis;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.util.SafeEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * FstSerializer.
 *
 * @author mjy
 */
public class FstSerializer implements ISerializer {
    private static Logger logger = LoggerFactory.getLogger(FstSerializer.class);
    public static final ISerializer me = new FstSerializer();

    @Override
    public byte[] keyToBytes(String key) {
        return SafeEncoder.encode(key);
    }

    @Override
    public String keyFromBytes(byte[] bytes) {
        return SafeEncoder.encode(bytes);
    }

    @Override
    public byte[] fieldToBytes(Object field) {
        return valueToBytes(field);
    }

    @Override
    public Object fieldFromBytes(byte[] bytes) {
        return valueFromBytes(bytes);
    }

    @Override
    public byte[] valueToBytes(Object value) {
        FSTObjectOutput fstOut = null;
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            fstOut = new FSTObjectOutput(bytesOut);
            fstOut.writeObject(value);
            fstOut.flush();
            return bytesOut.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fstOut != null)
                try {
                    fstOut.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    @Override
    public Object valueFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;

        FSTObjectInput fstInput = null;
        try {
            fstInput = new FSTObjectInput(new ByteArrayInputStream(bytes));
            return fstInput.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fstInput != null)
                try {
                    fstInput.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }
}
