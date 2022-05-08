package com.itboy.service.impl;

import com.itboy.dao.JobLogsRepository;
import com.itboy.dao.TimingRepository;
import com.itboy.db.SqlDruidParser;
import com.itboy.model.JobLogs;
import com.itboy.model.Result;
import com.itboy.model.SysUser;
import com.itboy.model.TimingVo;
import com.itboy.service.TimingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 * @program: websql
 * @description: 作业服务业务处理
 * @author: 超 boy_0214@sina.com
 * @create: 2019-09-16 19:28
 **/
@Service
@Slf4j
public class TimingServiceImpl implements TimingService {

    @Resource
    private TimingRepository timingRepository;
    @Resource
    private JobLogsRepository jobLogsRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addtimingData(TimingVo model) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        SysUser user = (SysUser) currentUser.getPrincipal();
        model.setSqlCreateUser(user.getUserName());
        Map<String, Object> sqlParser = SqlDruidParser.sqlParser(model.getTimingName(), model.getSqlText());
        if (sqlParser.get("executeType") == null) {
            throw new NullPointerException("SQL解析异常");
        }
        timingRepository.save(model);
    }

    @Override
    public void delTiming(String id) {
        Long ids = Long.valueOf(id);
        timingRepository.deleteById(ids);
    }


    @Override
    @Cacheable(value = "timing")
    public Result<TimingVo> timingList(TimingVo model) {
        Result<TimingVo> result1 = new Result<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest request = PageRequest.of(model.getPage() - 1, model.getLimit(), sort);
        Specification<TimingVo> spec = (Specification<TimingVo>) (root, query, cb) -> {
            Path<String> timingName = root.get("timingName");
            Path<String> syncName = root.get("syncName");
            Path<String> col1 = root.get("col1");
            Path<String> col2 = root.get("col2");
            String timingName1 = model.getTimingName() == null ? "" : model.getTimingName();
            String syncName1 = model.getSyncName() == null ? "" : model.getSyncName();
            String col1s = model.getCol1() == null ? "" : model.getCol1();
            String col2s = model.getCol2() == null ? "" : model.getCol2();
            Predicate p2 = cb.like(timingName, "%" + timingName1 + "%");
            Predicate p3 = cb.like(syncName, "%" + syncName1 + "%");
            Predicate p4 = cb.like(col1, "%" + col1s + "%");
            Predicate p5 = cb.like(col2, "%" + col2s + "%");
            Predicate p = cb.and(p2, p3, p4, p5);
            return p;
        };
        Page<TimingVo> all = timingRepository.findAll(spec, request);
        result1.setData(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public void updateTiming(TimingVo vo) {
        timingRepository.saveAndFlush(vo);
    }

    @Override
    public void updateStatus(TimingVo vo) {
        timingRepository.saveAndFlush(vo);
    }

    @Override
    public void saveLogs(JobLogs logs) {
        jobLogsRepository.save(logs);
    }

    @Override
    @Cacheable(value = "jobLog")
    public Result<JobLogs> jobLogsList(JobLogs model) {
        Result<JobLogs> result1 = new Result<>();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest request = PageRequest.of(model.getPage() - 1, model.getLimit(), sort);
        Specification<JobLogs> spec = (Specification<JobLogs>) (root, query, cb) -> {
            Path<String> taskName = root.get("taskName");
            Path<String> taskState = root.get("taskState");
            Path<String> executeDate = root.get("executeDate");
            String taskName1 = model.getTaskName() == null ? "" : model.getTaskName();
            String taskState1 = model.getTaskState() == null ? "" : model.getTaskState();
            String executeDate1 = model.getExecuteDate() == null ? "" : model.getExecuteDate();
            Predicate p2 = cb.like(taskName, "%" + taskName1 + "%");
            Predicate p3 = cb.like(taskState, "%" + taskState1 + "%");
            Predicate p4 = cb.like(executeDate, "%" + executeDate1 + "%");
            Predicate p = cb.and(p2, p3, p4);
            return p;
        };
        Page<JobLogs> all = jobLogsRepository.findAll(spec, request);
        result1.setData(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public void jobLogDelete() {
        jobLogsRepository.deleteAll();
    }

}
