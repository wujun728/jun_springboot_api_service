package com.yuyaogc.lowcode.engine.plugin.activerecord;

import com.yuyaogc.lowcode.engine.annotation.APP;
import com.yuyaogc.lowcode.engine.annotation.Service;
import com.yuyaogc.lowcode.engine.annotation.Table;
import com.yuyaogc.lowcode.engine.container.Constants;
import com.yuyaogc.lowcode.engine.container.Container;
import com.yuyaogc.lowcode.engine.context.*;
import com.yuyaogc.lowcode.engine.entity.EntityClass;
import com.yuyaogc.lowcode.engine.entity.EntityField;
import com.yuyaogc.lowcode.engine.entity.datatype.DataType;
import com.yuyaogc.lowcode.engine.exception.EngineException;
import com.yuyaogc.lowcode.engine.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;


/**
 * ActiveRecord Db
 *
 * @param <T>
 */
public class Model<T> extends KvMap implements Serializable {
    public Model() {
    }

    private Logger log = LoggerFactory.getLogger(_getModelClass());
    private Object[] NULL_PARA_ARRAY = new Object[0];


    protected Class<? extends Model> _getModelClass() {
        Class clazz = this.getClass();

        if(clazz.isAnnotationPresent(Table.class)){
            //TODO app name?

            Table table = (Table) clazz.getAnnotation(Table.class);
            getContext().get(table.name()).getEntity();
        }

        //AppClassLoader appClassLoader = (AppClassLoader) entityClass.getApplication().getClassLoader();
        return clazz;
    }

    public Query whereCalc(Config config, EntityClass rec, Criteria criteria, boolean activeTest) {
        // TODO active criteria
        if (!criteria.isEmpty()) {
            return new Expression(config, criteria, rec, null, null).getQuery();
        } else {
            return new Query(_getConfig(), rec.getTableName(), rec.getTableName());
        }
    }

    @Service(displayName = "统计")
    public long count(Criteria criteria) {
        _getModelClass();
        EntityClass rec = _getTable();
        if (Expression.isFalse(rec, criteria)) {
            return 0;
        }
        Query query = whereCalc(_getConfig(), rec, criteria, true);
        Config cr = _getConfig();
        Connection conn;

        try {
            conn = cr.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Query.SelectClause select = query.select("count(1)");

        SqlPara format = cr.mogrify(select.getQuery(), select.getParams());

        try (PreparedStatement pst = conn.prepareStatement(format.getSql())) {
            cr.dialect.fillStatement(pst, format.getParmas());
            ResultSet rs = pst.executeQuery();
            int row = 0;
            if (rs.next()) {
                row = rs.getInt(1);
            }
            DbUtil.close(rs);
            return row;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            cr.close(conn);
        }
        return 0;
    }



    @Service(displayName = "搜索")
    public <T extends Model> T selectOne(Criteria criteria){
       List<T> result = this.search(criteria, 0, 1, null);
        return result.isEmpty()? null: result.get(0);
    }


    private String quote(String identify) {
        return String.format("`%s`", identify);
    }

    private List<String> qualify(Query query,EntityField field){
        List<String> relColumns = new ArrayList<>();

        EntityClass rec =  field.getEntity();
        if(field.getDataType() instanceof DataType.Many2oneField){
            DataType.Many2oneField m2o = (DataType.Many2oneField) field.getDataType();
            String relModel = field.getRelModel();
            EntityClass relClass = Container.me().getEntityClass(relModel);
            String aliasRel = query.leftJoin(rec.getTableName(), field.getColumnName(), relClass.getTableName(), "id", field.getColumnName());

            for(EntityField relField: relClass.getFields()){
                if(relField.getName().equals("id")){
                    continue;
                }
                if(!relField.isStore()){
                    continue;
                }
                String alisColumn = String.format("%s.%s", aliasRel, quote(relField.getColumnName()));
                relColumns.add(String.format("%s as %s", alisColumn, quote(relField.getName())));
            }
            //TODO
            return relColumns;
        }

        return null;
    }




    @Service(displayName = "搜索")
    public <T extends Model> List<T> search(Criteria criteria, Integer offset, Integer limit, String order) {
        try {
            _getModelClass();
            Config config = _getConfig();
            EntityClass rec = _getTable();
            Query query = whereCalc(_getConfig(), rec, criteria, false);
            query.setOrder(generateOrderBy(rec, order, query)).setLimit(limit).setOffset(offset);

            Set<String> columns = new HashSet<>();
            for (EntityField field : rec.getFields()) {
                if(!field.isStore()){
                    continue;
                }

                String alisColumn = String.format("%s.%s", config.quote(rec.getTableName()), config.quote(field.getColumnName()));
                columns.add(String.format("%s as %s", alisColumn, config.quote(field.getName())));
                List<String> relColumns = qualify(query, field);
                if (Objects.isNull(relColumns)) {
                    continue;
                }

                columns.addAll(relColumns);
            }

            Query.SelectClause select = query.select(columns);
            Connection connection = config.getConnection();
            SqlPara format = config.mogrify(select.getQuery(), select.getParams());

            //TODO DEBUG
            log.info(format.getSql());


            try (PreparedStatement pst = connection.prepareStatement(format.getSql())) {
                config.dialect.fillStatement(pst, format.getParmas());
                ResultSet rs = pst.executeQuery();
                List<T> result = config.dialect.buildModelList(rs, _getModelClass());


                List<Object> ids = new ArrayList<>();
                if (!result.isEmpty()) {

                    Map<String,List<Object>> col = ModelUtil.convertListToMap(result);

                    ids = col.get("id");

                    Memory memory = getContext().getCache();
                    for (EntityField field : rec.getFields()) {
                        List<Object> values = col.get(field.getName());
                        if(null == values){
                            continue;
                        }
                        memory.update(ids.toArray(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY), field, values);
                    }

                    for (EntityField field : rec.getFields()) {
                        //TODO Many2many
                        field.getDataType().read(field, query, ids.toArray(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY));
                    }
                }



                // TODO
                List data = new ArrayList<>();
                for(Object id: ids){
                    Model<?> vals = _getModelClass().newInstance();
                    Memory cache = getContext().getCache();
                    //Map<String,Object> vals = new LinkedHashMap<>();
                    vals.put(Constants.ID, id);
                    for (EntityField field : rec.getFields()) {
                        //vals.put(field.getName(), null);
                        Object value = cache.get((Long) id, field, Void.class);
                        if (value == Void.class) {

                        }
                        vals.put(field.getName(), value);
                    }
                    data.add(vals);
                }


                DbUtil.close(rs);
                return data;
            } catch (Exception e) {
                throw new ActiveRecordException(e);
            } finally {
                config.close(connection);
            }

        } catch (Exception e) {
            throw new ActiveRecordException(e);
        }


    }


    private String generateOrderBy(EntityClass rec, String orderSpec, Query query) {
        String orderByClause = "";
        if (StringUtils.isBlank(orderSpec)) {
            orderSpec = rec.getOrder();
        }
        if (StringUtils.isNotBlank(orderSpec)) {
            List<String> orderByElements = generateOrderByInner(rec, rec.getTableName(), orderSpec, query,
                    false, null);
            if (!orderByElements.isEmpty()) {
                orderByClause = StringUtils.join(orderByElements);
            }
        }
        return orderByClause;
    }

    private List<String> generateOrderByInner(EntityClass rec, String alias, String orderSpec, Query query,
                                              boolean reverseDirection, Set<String> seen) {
        if (seen == null) {
            seen = new HashSet<>();
        }
        // TODO
        List<String> orderByElements = new ArrayList<>();
        Config cr = _getConfig();
        for (String orderPart : orderSpec.split(",")) {
            String[] orderSplit = orderPart.trim().split(" ");
            String orderField = orderSplit[0].trim();
            String orderDirection = orderSplit.length == 2 ? orderSplit[1].trim().toLowerCase() : "";
            if (reverseDirection) {
                orderDirection = "DESC".equals(orderDirection) ? "ASC" : "DESC";
            }
            boolean doReverse = "DESC".equals(orderDirection);
            EntityField field = rec.getField(orderField);
            if (Constants.ID.equals(orderField)) {
                orderByElements
                        .add(String.format("%s.%s %s", cr.quote(alias), cr.quote(orderField), orderDirection));
            } else {
                if (field.isStore() && "many2one".equals(field.getDataType())) {
                    DataType.Many2oneField m2o = (DataType.Many2oneField) field.getDataType();
                } else if (field.isStore() && field.getDataType().getType() != ColumnType.None) {
                } else {
                    log.warn("模型{}不能按字段{}排序", rec.getName(), orderField);
                }
            }
        }
        return orderByElements;
    }

    @Service(displayName = "创建")
    public <T extends Model> boolean create(T value) {
        return value.save();
    }

    @Service(displayName = "更新1")
    public <T extends Model> boolean updateById(T value) {
        return value.update();
    }

    @Service(displayName = "校验")
    public <T extends Model> boolean validate(T value) {
        _getModelClass();
        if (!Objects.isNull(value)) {
            for (EntityField entityField : _getTable().getFields()) {
                entityField.getDataType().validate(entityField, value);
            }
        }
        return true;
    }


    public boolean delete() {
        EntityClass table = _getTable();
        String[] pKeys = table.getPrimaryKey();
        if (pKeys.length == 1) {    // 优化：主键大概率只有一个
            Object id = get(pKeys[0]);
            if (id == null)
                throw new ActiveRecordException("Primary key " + pKeys[0] + " can not be null");
            return deleteById(table, id);
        }

        Object[] ids = new Object[pKeys.length];
        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = get(pKeys[i]);
            if (ids[i] == null)
                throw new ActiveRecordException("Primary key " + pKeys[i] + " can not be null");
        }
        return deleteById(table, ids);
    }

    /**
     * Delete model by id.
     *
     * @param idValue the id value of the model
     * @return true if delete succeed otherwise false
     */
    @Service
    public boolean deleteById(Object idValue) {
        if (idValue == null) {
            throw new IllegalArgumentException("idValue can not be null");
        }
        return deleteById(_getTable(), idValue);
    }

    @Service
    public boolean deleteByIds(Object... idValues) {
        EntityClass table = _getTable();
        if (idValues == null || idValues.length != table.getPrimaryKey().length) {
            throw new IllegalArgumentException("Primary key nubmer must equals id value number and can not be null");
        }
        return deleteById(table, idValues);
    }

    public boolean deleteById(EntityClass table, Object... idValues) {
        _getModelClass();
        Config config = this._getConfig();
        Connection conn = null;

        boolean var6;
        try {
            conn = config.getConnection();
            String sql = config.dialect.forModelDeleteById(table);
            var6 = DbUtil.update(config, conn, sql, idValues) >= 1;
        } catch (Exception var10) {
            throw new ActiveRecordException(var10);
        } finally {
            config.close(conn);
        }

        return var6;
    }

    @Service(displayName = "保存")
    public boolean save() {
        _getModelClass();
        Config config = _getConfig();
        EntityClass table = _getTable();

        getContext().call("validate", this);

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList();
        this.set("id", IdWorker.getId());
        config.dialect.forModelSave(table, this, sql, paras);
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
            config.dialect.getModelGeneratedKey(this, pst, table);
            this.clearModifyFlag();
            var8 = result >= 1;
        } catch (Exception var12) {
            throw new EngineException(var12);
        } finally {
            config.close(pst, conn);
        }


        for(EntityField entityField: table.getFields()){
            entityField.getDataType().save(this, entityField);
        }



        return var8;
    }

    @Service(displayName = "更新2")
    public boolean update() {
        _getModelClass();
        Config config = _getConfig();
        EntityClass table = _getTable();

        getContext().call("validate", this);

        String[] pKeys = table.getPrimaryKey();
        for (String pKey : pKeys) {
            Object id = this.get(pKey);
            if (id == null) {
                throw new EngineException("You can't update model without Primary Key, " + pKey + " can not be null.");
            }
        }

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forModelUpdate(table, this, _getModifyFlag(), sql, paras);

        if (paras.size() <= 1) {    // 参数个数为 1 的情况表明只有主键，也无需更新
            return false;
        }

        // --------
        Connection conn = null;
        try {
            conn = config.getConnection();
            int result = DbUtil.update(config, conn, sql.toString(), paras);
            if (result >= 1) {
                clearModifyFlag();
                return true;
            }

            for(EntityField entityField: table.getFields()){
                entityField.getDataType().write(this, entityField);
            }


            return false;
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }


    protected List<T> find(Config config, Connection conn, String sql, Object... paras) throws Exception {
        _getModelClass();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            config.dialect.fillStatement(pst, paras);
            ResultSet rs = pst.executeQuery();
            List<T> result = config.dialect.buildModelList(rs, _getModelClass());
            DbUtil.close(rs);
            return result;
        } finally {
            config.close(conn);
        }
    }

    protected List<T> find(Config config, String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return find(config, conn, sql, paras);
        } catch (Exception e) {
            throw new EngineException(e);
        } finally {
            config.close(conn);
        }
    }


    @Service(displayName = "find")
    public List<T> find(String sql, Object... paras) {
        return find(_getConfig(), sql, paras);
    }

    /**
     * @see #find(String, Object...)
     */
    public List<T> find(String sql) {
        return find(sql, NULL_PARA_ARRAY);
    }

    @Service(displayName = "findAll")
    public List<T> findAll() {
        _getModelClass();
        Config config = _getConfig();
        String sql = config.dialect.forFindAll(_getTable().getTableName());
        return find(config, sql, NULL_PARA_ARRAY);
    }


    @Service(displayName = "findFirst")
    public T findFirst(String sql, Object... paras) {
        List<T> result = find(sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }


    @Service(displayName = "查询通过主键")
    public T findById(Object idValue) {
        return findByIdLoadColumns(new Object[]{idValue}, "*");
    }


    public T findByIds(Object... idValues) {
        return findByIdLoadColumns(idValues, "*");
    }


    public T findByIdLoadColumns(Object idValue, String columns) {
        return findByIdLoadColumns(new Object[]{idValue}, columns);
    }


    public T findByIdLoadColumns(Object[] idValues, String columns) {
        _getModelClass();
        Config config = _getConfig();
        EntityClass table = _getTable();
        if (table.getPrimaryKey().length != idValues.length) {
            throw new IllegalArgumentException("id values error, need " + table.getPrimaryKey().length + " id value");
        }
        String sql = config.dialect.forModelFindById(table, columns);
        List<T> result = find(config, sql, idValues);
        return result.size() > 0 ? result.get(0) : null;
    }


    public Context getContext() {
        return Context.getInstance();
    }

    private EntityClass _getTable() {
        Context context = Context.getInstance();
        return context.getEntity();
    }

    private Config _getConfig() {
        Context context = Context.getInstance();
        return context.getConfig();
    }


    public T findFirst(String sql) {
        return findFirst(sql, NULL_PARA_ARRAY);
    }


    public String getStr(String attr) {
        Object s = this.get(attr);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(String attr) {
        return TypeKit.toInt(this.get(attr));
    }

    public Long getLong(String attr) {
        return TypeKit.toLong(this.get(attr));
    }

    public BigInteger getBigInteger(String attr) {
        Object n = this.get(attr);
        if (n instanceof BigInteger) {
            return (BigInteger) n;
        } else if (n instanceof BigDecimal) {
            return ((BigDecimal) n).toBigInteger();
        } else if (n instanceof Number) {
            return BigInteger.valueOf(((Number) n).longValue());
        } else {
            return n instanceof String ? new BigInteger((String) n) : (BigInteger) n;
        }
    }

    public Date getDate(String attr) {
        return TypeKit.toDate(this.get(attr));
    }

    public LocalDateTime getLocalDateTime(String attr) {
        return TypeKit.toLocalDateTime(this.get(attr));
    }

    public Time getTime(String attr) {
        return (Time) this.get(attr);
    }

    public Timestamp getTimestamp(String attr) {
        return (Timestamp) this.get(attr);
    }

    public Double getDouble(String attr) {
        return TypeKit.toDouble(this.get(attr));
    }

    public Float getFloat(String attr) {
        return TypeKit.toFloat(this.get(attr));
    }

    public Short getShort(String attr) {
        return TypeKit.toShort(this.get(attr));
    }

    public Byte getByte(String attr) {
        return TypeKit.toByte(this.get(attr));
    }

    public Boolean getBoolean(String attr) {
        return TypeKit.toBoolean(this.get(attr));
    }

    public BigDecimal getBigDecimal(String attr) {
        return TypeKit.toBigDecimal(this.get(attr));
    }

    public byte[] getBytes(String attr) {
        return (byte[]) ((byte[]) this.get(attr));
    }

    public Number getNumber(String attr) {
        return TypeKit.toNumber(this.get(attr));
    }
}

