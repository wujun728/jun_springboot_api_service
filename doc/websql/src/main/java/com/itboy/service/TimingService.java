package com.itboy.service;

import com.itboy.model.JobLogs;
import com.itboy.model.Result;
import com.itboy.model.TimingVo;

/**
 * @program: websql
 * @description: 作业服务
 * @author: 超 boy_0214@sina.com
 * @create: 2019-09-16 19:28
 **/
public interface TimingService {
    void addtimingData(TimingVo model) throws Exception;

    void delTiming(String id);

    Result<TimingVo> timingList(TimingVo model);

    void updateTiming(TimingVo vo);

    void updateStatus(TimingVo vo);

    void saveLogs(JobLogs logs);

    Result<JobLogs> jobLogsList(JobLogs model);

    void jobLogDelete();
}
