package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface IRow<T> {

    /**
     * Convert Model or Record to a Map.
     * <p>
     * Danger! The update method will ignore the attribute if you change it directly.
     * You must use set method to change attribute that update method can handle it.
     */
    public Map<String, Object> toMap();

    /**
     * Put map to the model without check attribute name.
     */
    public T put(Map<String, Object> map);

    /**
     * Put key value pair to the model without check attribute name.
     */
    public T put(String key, Object value);

    /**
     * Set column value.
     *
     * @param column the column name
     * @param value  the value of the column
     */
    public T set(String column, Object value);

    /**
     * Get column value of any mysql type
     */
    public <T> T get(String column);

    /**
     * Get column of any mysql type. Returns defaultValue if null.
     */
    public <T> T get(String column, Object defaultValue);

    /**
     * Get column of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
     */
    public String getStr(String column);

    /**
     * Get column of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
     */
    public Integer getInt(String column);

    /**
     * Get column of mysql type: bigint, unsigned int
     */
    public Long getLong(String column);

    /**
     * Get column of mysql type: unsigned bigint
     */
    public java.math.BigInteger getBigInteger(String column);

    /**
     * Get column of mysql type: date, year
     */
    public java.util.Date getDate(String column);

    public LocalDateTime getLocalDateTime(String column);

    /**
     * Get column of mysql type: time
     */
    public java.sql.Time getTime(String column);

    /**
     * Get column of mysql type: timestamp, datetime
     */
    public java.sql.Timestamp getTimestamp(String column);

    /**
     * Get column of mysql type: real, double
     */
    public Double getDouble(String column);

    /**
     * Get column of mysql type: float
     */
    public Float getFloat(String column);

    public Short getShort(String column);

    public Byte getByte(String column);

    /**
     * Get column of mysql type: bit, tinyint(1)
     */
    public Boolean getBoolean(String column);

    /**
     * Get column of mysql type: decimal, numeric
     */
    public BigDecimal getBigDecimal(String column);

    /**
     * Get column of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob
     * I have not finished the test.
     */
    public byte[] getBytes(String column);

    /**
     * Get column of any type that extends from Number
     */
    public Number getNumber(String column);

    /**
     * Convert to json string.
     */
    public String toJson();

    // isEmpty() 方法导致 Model、Record 在使用 fastjson 转化 json 时多出一个 empty 字段，改为 size() 方法
    public int size();
}
