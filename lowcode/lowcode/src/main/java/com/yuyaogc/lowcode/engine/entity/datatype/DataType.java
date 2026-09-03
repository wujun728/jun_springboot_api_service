package com.yuyaogc.lowcode.engine.entity.datatype;

import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.Context;
import com.yuyaogc.lowcode.engine.context.Query;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.Validate;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.plugin.activerecord.*;
import com.yuyaogc.lowcode.engine.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataType {
    private Logger log = LoggerFactory.getLogger(DataType.class);
    static Map<String, Class> typeFields = new HashMap<>();

    static {
        registerField(Constants.BINARY, ByteField.class);
        registerField(Constants.STRING, StringField.class);
        registerField(Constants.BIG_DECIMAL, BigDecimalField.class);
        registerField(Constants.DOUBLE, DoubleField.class);
        registerField(Constants.LONG, LongField.class);
        registerField(Constants.BOOLEAN, BooleanField.class);
        registerField(Constants.DATE, DateField.class);
        registerField(Constants.DATETIME, DateTimeField.class);
        registerField(Constants.TIMESTAMP, TimestampField.class);
        registerField(Constants.FLOAT, FloatField.class);
        registerField(Constants.INTEGER, IntegerField.class);
        registerField(Constants.INT, IntegerField.class);
        registerField(Constants.TEXT, TextField.class);
        registerField(Constants.MANY2MANY, Many2manyField.class);
        registerField(Constants.MANY2ONE, Many2oneField.class);
        registerField(Constants.ONE2MANY, One2manyField.class);
        registerField(Constants.DICT, DictField.class);
        registerField(Constants.FILE, FileField.class);
        registerField(Constants.CLASS, ClassField.class);
        registerField(Constants.SELECTION, SelectionField.class);
    }


    public Object convertToRead(Object value,Context context){return  value;}

    public static Set<String> getFieldTypes() {
        return typeFields.keySet();
    }

    public static String getFieldName(String type) {
        return typeFields.get(type).getName();
    }

    public void read(EntityField field, Query query, Object[] ids){
    }

    public String quote(String identify) {
        return String.format("`%s`", identify);
    }


    public String getName() {
        Optional<Map.Entry<String, Class>> optional = typeFields.entrySet().stream().filter(d -> d.getValue().equals(this.getClass())).findFirst();
        return optional.isPresent() ? optional.get().getKey() : Constants.CLASS;
    }



    public void write(Model v, EntityField field){
    }


    public void save(Model v, EntityField field){

    }



    public static DataType create(String type) {
        Class<?> clazz = typeFields.get(type);
        if (clazz == null) {
            clazz = typeFields.get("Class");
        }
        try {
            DataType dataType = (DataType) clazz.getDeclaredConstructors()[0].newInstance(DataType.class.newInstance());
            return dataType;
        } catch (Exception e) {
            throw new EngineException(String.format("创建字段%s失败,class=%s", type, clazz.getName()));
        }
    }

    public static void registerField(String type, Class<?> fieldClass) {
        typeFields.put(type, fieldClass);
    }

    public ColumnType getType() {
        return null;
    }


    public boolean validate(EntityField entityField, Model value) {
        return true;
    }

    public Integer getSize(EntityField field) {
        return null;
    }


    public Integer getScale(EntityField field) {
        return null;
    }

    public Object convertToColumn(Object value, EntityClass record, boolean validate) {
        return value;
    }

    LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof String) {
            if (StringUtils.isEmpty((String) value)) {
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate result = LocalDate.parse((String) value, formatter);
            return result;
        }
        if (value instanceof java.sql.Date) {
            java.sql.Date dt = (Date) value;
            LocalDate result = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return result;
        }
        if (value instanceof LocalDateTime) {
            LocalDateTime local = (LocalDateTime) value;
            return local.toLocalDate();
        }
        throw new EngineException(String.format("%s不支持转换成LocalDate", value));
    }
    public class BigDecimalField extends DataType {

        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBigDecimal(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Float;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }


    public class BooleanField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBoolean(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Boolean;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 1;
        }
    }

    public class ByteField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getByte(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Binary;
        }
    }

    public class CharField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getShort(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }
    }


    public class DateField extends DataType {

        @Override
        public Object convertToColumn(Object value, EntityClass record, boolean validate) {
            if (value instanceof LocalDateTime) {
                LocalDateTime local = (LocalDateTime) value;
                local = local.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return formatter.format(local);
            }
            if (value instanceof java.util.Date) {
                java.util.Date d = (java.util.Date) value;
                Instant ins = d.toInstant();
                LocalDateTime local = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return formatter.format(local);
            }
            return value;
        }

        public Object convertToRead2(Object value, Context context) {
            if(value == null)
            {
                return null;
            }

            if (value instanceof java.sql.Date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                return format.format((java.sql.Date) value);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return formatter.format(toLocalDate(value));
        }

        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getDate(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Date;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }
    public class TimestampField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getTimestamp(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.DateTime;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }
    public class DateTimeField extends DataType {




        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getLocalDateTime(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.DateTime;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class DoubleField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getDouble(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Decimal;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 10;
        }
    }

    public class FloatField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getFloat(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Float;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class IntegerField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getBigInteger(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Integer;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class LongField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (Objects.isNull(value.getLong(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class Many2manyField extends DataType {


        @Override
        public void read(EntityField field, Query query, Object[] ids) {
            Context context = Context.getInstance();
            Config config = context.getConfig();
            EntityClass table = Container.me().getEntityClass(field.getRelModel());

            String sql = "select * from "+table.getTableName()+" where "+field.getJoinColumnName()+" in %s";

            List<Object> params = new ArrayList<>();
            params.add(Arrays.asList(ids));
            SqlPara format = config.mogrify(sql, params);
            log.info(format.getSql());
            System.out.println(1);


            Connection connection = null;
            try {
                connection = config.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            try (PreparedStatement pst = connection.prepareStatement(format.getSql())) {
                config.dialect.fillStatement(pst, format.getParmas());
                ResultSet rs = pst.executeQuery();
                List<Model> result =  config.dialect.buildModelList(rs, Model.class);
                Map<Long, List<Long>> group = new HashMap<>();

                for(Model model: result){
                    List<Long> list = group.get(model.getLong(field.getJoinColumnName()));
                    if (list == null) {
                        list = new ArrayList<>();
                        group.put(model.getLong(field.getJoinColumnName()), list);
                    }
                    list.add(model.getLong(field.getInverseName()));
                }


                Memory cache = context.getCache();
                for(Object rec: ids){
                    cache.set((Long) rec, field, group.get(rec));
                }
            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(connection);
            }


        }

        @Override
        public void write(Model v, EntityField field) {
            Config config = Context.getInstance().getConfig();
            EntityClass table = Container.me().getEntityClass(field.getRelModel());


        }

        @Override
        public void save(Model v, EntityField field) {
            Object value =  v.get(field.getName());
            String valueString =   Objects.toString(value);
            Context context = Context.getInstance();
            Config config = context.getConfig();

            EntityClass table = Container.me().getEntityClass(field.getRelModel());


            if(StringUtils.isNotEmpty(valueString)){
                String[] ids =  valueString.split(",");
                for(String id: ids){
                    StringBuilder sql = new StringBuilder();
                    List<Object> paras = new ArrayList();
                    Map<String, Object> attrs = new LinkedHashMap<>();
                    attrs.put("id", IdWorker.getId());
                    attrs.put(field.getJoinColumnName(), v.get("id"));
                    attrs.put(field.getInverseName(), Long.valueOf(id));

                    config.dialect.forModelSave(table, attrs, sql, paras);

                    Connection conn = null;
                    PreparedStatement pst = null;
                    boolean var8;
                    try {
                        conn = config.getConnection();
                        if (config.getSqlDialect().isOracle()) {
                            pst = conn.prepareStatement(sql.toString(), table.getPrimaryKey());
                        } else {
                            pst = conn.prepareStatement(sql.toString(), 1);
                        }
                        config.dialect.fillStatement(pst, paras);
                        int result = pst.executeUpdate();
                        //config.dialect.getModelGeneratedKey(this, pst, table);
                        var8 = result >= 1;
                    } catch (Exception var12) {
                        throw new EngineException(var12);
                    } finally {
                        config.close(pst, conn);
                    }

                }
            }
        }

        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }
        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

    public class Many2oneField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }




    }


    public class One2manyField extends DataType {

        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }
        @Override
        public ColumnType getType() {
            return ColumnType.Long;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }


    public class DictField extends DataType {
        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }
        @Override
        public Integer getSize(EntityField field) {
            return 255;
        }
    }

    public class SelectionField extends DataType {
        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }
        @Override
        public Integer getSize(EntityField field) {
            return 255;
        }
    }


    public class FileField extends DataType {
        @Override
        public Object convertToRead(Object value, Context context) {
            if(null == value){
                return null;
            }

            if(value instanceof List){
                List<Map> files = (List<Map>) value;
               Map<String,Object> file = files.get(0);
               return file.get("name");
            }

            return value;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }
        @Override
        public Integer getSize(EntityField field) {
            return 255;
        }
    }


    public class StringField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {
            for (Validate validate : entityField.getValidates().values()) {
                if (!validate.isEmpty()) {
                    continue;
                }
                if (StringUtils.isEmpty(value.getStr(entityField.getName()))) {
                    throw new EngineException(String.format("EntityClass %s EntityField %s", entityField.getEntity().getName(), entityField.getName()));
                }
            }
            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.VarChar;
        }


        @Override
        public Integer getSize(EntityField field) {
            return 255;
        }
    }

    public class TextField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public Integer getSize(EntityField field) {
            return null;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Text;
        }
    }

    public class ClassField extends DataType {
        @Override
        public boolean validate(EntityField entityField, Model value) {

            return true;
        }

        @Override
        public ColumnType getType() {
            return ColumnType.Text;
        }

        @Override
        public Integer getSize(EntityField field) {
            return 0;
        }
    }

}
