package com.itboy.controller;

import com.alibaba.fastjson.JSON;
import com.itboy.model.JobLogs;
import com.itboy.model.Result;
import com.itboy.model.TimingVo;
import com.itboy.service.TimingService;
import com.itboy.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: websql
 * @description: 定时业务
 * @Author 超 boy_0214@sina.com
 * @create: 2019-09-16 18:28
 **/
@Controller
@Slf4j
public class TimingController {

    @Resource
    private TimingService timingService;

    @RequestMapping("timing/addPage")
    public String timingAddPage() {
        return "timingAddPage";
    }

    @RequestMapping("timing/timList")
    public String timListPage() {
        return "timingListPage";
    }

    @RequestMapping("timing/history")
    public String historyPage() {
        return "timingHistoryPage";
    }

    /**
     * 新增执行计划
     *
     * @param model
     * @return
     */
    @RequestMapping("timing/addtimingData")
    @ResponseBody
    public Map addtimingData(TimingVo model) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("msg", "新增成功！");
        try {
            timingService.addtimingData(model);
            createJob(model.getExecuteTime(), model.getCol2(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 2);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /*加载执行计划列表*/
    @RequestMapping("timing/timingList")
    @ResponseBody
    public Map timingList(TimingVo model) {
        Result<TimingVo> p = timingService.timingList(model);
        Map result = new HashMap();
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }

    @RequestMapping("timing/executeOnce/{type}/{id}")
    @ResponseBody
    public Map executeOnce(@PathVariable("type") int type, @PathVariable("id") Long id, String cron) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "执行成功！");
        try {
            TimingVo models = new TimingVo();
            models.setId(id);
            TimingVo vo = timingService.timingList(models).getData().get(0);
            ScheduleUtils.Job job = createJob(vo.getExecuteTime(), vo.getCol2(), 2);
            switch (type) {
                case 1:
                    ScheduleUtils.cancel(job);
                    vo.setCol1("停用");
                    timingService.updateTiming(vo);
                    break;
                case 2://启用
                    createJob(vo.getExecuteTime(), vo.getCol2(), 1);
                    break;
                case 3://修改cron
                    vo.setExecuteTime(cron);
                    timingService.updateTiming(vo);
                    createJob(cron, vo.getCol2(), 1);
                    break;
                case 4://删除
                    timingService.delTiming(String.valueOf(id));
                    ScheduleUtils.cancel(job);
                    break;
                case 5://手动执行
                    createJob(vo.getExecuteTime(), vo.getCol2(), 4);
                    break;
            }
        } catch (Exception e) {
            result.put("code", 2);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    private ScheduleUtils.Job createJob(String Corn, String JobName, int status) throws Exception {
        ScheduleUtils.Job job2 = new ScheduleUtils.Job();
        job2.setClassName("com.itboy.db.JobExecuteFactory");
        job2.setMethodName("executeSql");
        job2.setCron(Corn);
        job2.setJobName(JobName);
        job2.setStatus(1);
        if (status == 1) {
            ScheduleUtils.cancel(job2);
            ScheduleUtils.add(job2);
        } else if (status == 4) {
            ScheduleUtils.cancel(job2);
            ScheduleUtils.add(job2);
            Thread.sleep(1000);
            ScheduleUtils.cancel(job2);
        }
        return job2;
    }

    /*加载执行日志列表*/
    @RequestMapping("timing/jobLogList")
    @ResponseBody
    public Map jobLogList(JobLogs model) {
        Result<JobLogs> p = timingService.jobLogsList(model);
        Map result = new HashMap();
        Map reuslt2 = new HashMap();
        reuslt2.put("totalCount", p.getCount());
        reuslt2.put("pageSize", model.getLimit());
        reuslt2.put("list", p.getData());
        result.put("code", 0);
        result.put("page", reuslt2);
        return result;
    }


    //删除日志
    @RequestMapping("timing/jobLogDelete")
    @ResponseBody
    public String jobLogDelete() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        try {
            timingService.jobLogDelete();
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "删除失败！error：" + e.getMessage());
        }
        return JSON.toJSONString(result);
    }
}
