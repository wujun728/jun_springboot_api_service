package cn.crabc.core.app.config;

import cn.crabc.core.app.service.system.IBaseApiInfoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 加载数据
 */
@Component
public class LoadingData implements InitializingBean {
    @Autowired
    private IBaseApiInfoService iBaseApiInfoService;

    /**
     * 启动加载
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        iBaseApiInfoService.initApi();
    }

    /**
     * 定时加载
     */
    @Scheduled(cron = "${crabc.corn.api:0 0/5 * * * ?}")  // 每5分钟全量加载一次
    public void task() {
        iBaseApiInfoService.initApi();
    }

}
