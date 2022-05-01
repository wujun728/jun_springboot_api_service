package com.itboy.service.impl;

import com.itboy.dao.*;
import com.itboy.model.*;
import com.itboy.service.DbSourceService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName DbSourceServiceImpl
 * @Description 业务处理
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 17:34
 **/
@Service
public class DbSourceServiceImpl implements DbSourceService {

    @Resource
    private DbSourceRepository dbSourceRepository;

    @Resource
    private DbSqlTextRepository dbSqlTextRepository;

    @Resource
    private SysSetUpRepository sysSetUpRepository;

    @Resource
    private SysLogRepository sysLogRepository;

    @Resource
    private SysUserLogRepository sysUserLogRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Result<DbSourceModel> selectDbSourceList(DbSourceModel result) {
        Result<DbSourceModel> result1 = new Result<DbSourceModel>();
        PageRequest pageRequest = PageRequest.of(result.getPage() - 1, result.getLimit());
        Specification<DbSourceModel> spec = (Specification<DbSourceModel>) (root, query, cb) -> {
            Path<String> dbname = root.get("dbname");
            Path<String> dbaccount = root.get("dbaccount");
            Path<String> dburl = root.get("dburl");
            String dbUrl1 = result.getDburl() == null ? "" : result.getDburl();
            String account1 = result.getDbaccount() == null ? "" : result.getDbaccount();
            String dbname2 = result.getDbname() == null ? "" : result.getDbname();
            Predicate p1 = cb.like(dbname, "%" + dbname2 + "%");
            Predicate p2 = cb.like(dbaccount, "%" + account1 + "%");
            Predicate p3 = cb.like(dburl, "%" + dbUrl1 + "%");
            Predicate p = cb.and(p1, p2, p3);
            return p;
        };
        Page<DbSourceModel> all = dbSourceRepository.findAll(spec, pageRequest);
        result1.setData(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public Result<DbSqlText> getDbSqlText(DbSqlText model) {
        Result<DbSqlText> result1 = new Result<DbSqlText>();
        PageRequest request = PageRequest.of(model.getPage() - 1, model.getLimit());
        Specification<DbSqlText> spec = (Specification<DbSqlText>) (root, query, cb) -> {
            Path<String> title = root.get("title");
            Path<String> sqlText = root.get("sqlText");
            String sqlText1 = model.getSqlText() == null ? "" : model.getSqlText();
            String title1 = model.getTitle() == null ? "" : model.getTitle();
            Predicate p2 = cb.like(sqlText, "%" + sqlText1 + "%");
            Predicate p3 = cb.like(title, "%" + title1 + "%");
            Predicate p = cb.and(p2, p3);
            return p;
        };
        Page<DbSqlText> all = dbSqlTextRepository.findAll(spec, request);
        result1.setData(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public DbSourceModel delDbSource(String id) {
        Long ids = Long.valueOf(id);
        Optional<DbSourceModel> model = dbSourceRepository.findById(ids);
        dbSourceRepository.deleteById(ids);
        return model.get();
    }

    @Override
    public SysSetup initSysSetup() {
        List<SysSetup> sysList = sysSetUpRepository.findAll();
        if (sysList.size() > 0) {
            return sysList.get(0);
        } else {
            return SysSetup.getInstance();
        }
    }

    @Override
    public void delsqlText(String id) {
        dbSqlTextRepository.delsqlText(Integer.valueOf(id));
    }

    @Override
    public void insertLog(SysLog logs) {
        sysLogRepository.save(logs);
    }

    @Override
    public Result<SysLog> getLogList(SysLog model) {
        Result<SysLog> result1 = new Result<SysLog>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest request = PageRequest.of(model.getPage() - 1, model.getLimit(), sort);
        Specification<SysLog> spec = new Specification<SysLog>() {
            @Override
            public Predicate toPredicate(Root<SysLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> logName = root.get("logName");
                Path<String> logContent = root.get("logContent");
                Path<String> logDbSource = root.get("logDbSource");
                String logContent1 = model.getLogContent() == null ? "" : model.getLogContent();
                String logName1 = model.getLogName() == null ? "" : model.getLogName();
                String logDbSource1 = model.getLogDbSource() == null ? "" : model.getLogDbSource();
                Predicate p2 = cb.like(logContent, "%" + logContent1 + "%");
                Predicate p3 = cb.like(logName, "%" + logName1 + "%");
                Predicate p1 = cb.like(logDbSource, "%" + logDbSource1 + "%");
                Predicate p = cb.and(p1, p2, p3);
                return p;
            }
        };
        Page<SysLog> all = sysLogRepository.findAll(spec, request);
        result1.setData(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public void delSysLog() {
        sysLogRepository.deleteAll();
    }

    @Override
    public void delUserLog() {
        sysUserLogRepository.deleteAll();
    }

    @Override
    public void addDbSource(DbSourceModel model) {
        dbSourceRepository.save(model);
    }

    @Override
    public List<DbSourceModel> dbsourceSqlList(DbSourceModel model) {
        List dbList = em.createNativeQuery("select dbname as code,dbname as value  from sql_dbcource where dbstate='有效' ").unwrap(Query.class)
                .setResultTransformer(
                        AliasToEntityMapResultTransformer.INSTANCE
                ).list();
        return dbList;
    }

    @Override
    public void saveSqlText(DbSqlText model) {
        Subject currentUser = SecurityUtils.getSubject();
        SysUser user = (SysUser) currentUser.getPrincipal();
        model.setSqlCreateUser(user.getUserName());
        dbSqlTextRepository.save(model);
    }

    @Override
    public List<DbSourceModel> sqlTextList(DbSourceModel model) {
        List dbList = em.createNativeQuery("select title as value ,to_char(sql_text)  as code  from sql_text ").unwrap(SQLQuery.class)
                .setResultTransformer(
                        Transformers.ALIAS_TO_ENTITY_MAP
                ).getResultList();
        return dbList;
    }


}
