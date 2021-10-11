package com.jun.plugin.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jun.plugin.system.common.utils.DataResult;
import com.jun.plugin.system.service.HomeService;
import com.jun.plugin.system.service.HttpSessionService;
import com.jun.plugin.system.service.PermissionService;
import com.jun.plugin.system.vo.resp.PermissionRespNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * 首页
 *
 * @author wujun
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Resource
    private HomeService homeService;
    @Resource
    private HttpSessionService httpSessionService;
    
    @Resource
    private PermissionService permissionService;

    @GetMapping("/home")
    @ApiOperation(value = "获取首页数据接口")
    public DataResult getHomeInfo() {
        //通过access_token拿userId
        String userId = httpSessionService.getCurrentUserId();
        DataResult result = DataResult.success();
        result.setData(homeService.getHomeInfo(userId));
        return result;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/init")
    @ApiOperation(value = "获取首页数据接口")
    public Map getHomeInitInfo() {
    	String userId = httpSessionService.getCurrentUserId();
    	List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);
    	Map result = new HashMap<>();
    	Map homeInfo = new HashMap<>();
    	homeInfo.put("title", "首页");
    	homeInfo.put("href", "welcome1.html");
    	Map logoInfo = new HashMap<>();
    	logoInfo.put("title", "齐兴会计师事务所");
//    	logoInfo.put("image", "images/logo.png");
    	logoInfo.put("href", "");
    	result.put("homeInfo", homeInfo);
    	result.put("logoInfo", logoInfo);
    	result.put("menuInfo", menus);
    	return result;
    }
}
