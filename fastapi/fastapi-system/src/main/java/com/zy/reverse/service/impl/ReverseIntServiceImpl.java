package com.zy.reverse.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zy.base.domain.AppManager;
import com.zy.base.domain.DbConfig;
import com.zy.base.service.IAppManagerService;
import com.zy.base.service.IDbConfigService;
import com.zy.common.exception.ServiceException;
import com.zy.common.utils.AntiSqlInjectionUtils;
import com.zy.imanager.domain.SelectIntfaceManager;
import com.zy.imanager.service.ISelectIntfaceManagerService;
import com.zy.iservice.domain.IntfaceAppAuth;
import com.zy.iservice.domain.IntfaceReverseHistory;
import com.zy.iservice.service.IntfaceAppAuthService;
import com.zy.iservice.service.IntfaceReverseHistoryService;
import com.zy.reverse.service.IReverseIntService;
import com.zy.system.service.ISysConfigService;
import com.zy.utils.DBHelper;
import com.zy.utils.ExecuteSqlUtils;
import com.zy.common.utils.ParamUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 查询接口管理Service业务层处理
 *
 * @author javachen
 * @date 2022-09-26
 */
@Service
public class ReverseIntServiceImpl implements IReverseIntService
{

    @Autowired
    private IAppManagerService appManagerService;

    @Autowired
    private IDbConfigService dbConfigService;

    @Autowired
    private ISelectIntfaceManagerService selectIntfaceManagerService;

    @Autowired
    private IntfaceReverseHistoryService intfaceReverseHistoryService;

    @Autowired
    private IntfaceAppAuthService intfaceAppAuthService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 执行调用方法
     * @return
     */
    @Override
    public Object executeMethod(String prefix, String name, Map<String, Object> inputParamMap, IntfaceReverseHistory intfaceReverseHistory){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",reverseInt(prefix,name,inputParamMap,intfaceReverseHistory));
        return resultMap;
    }

    /**
     * 接口执行方法
     * @return
     */
    @Override
    public Object reverseInt(String prefix, String name, Map<String, Object> inputParamMap, IntfaceReverseHistory intfaceReverseHistory){
        //检查是否有sql注入
        boolean flag = checkInputParamMap(inputParamMap);
        if(flag){
            throw new ServiceException("参数不合法,已拒绝本次请求");
        }

        SelectIntfaceManager selectIntfaceManagerTemp = new SelectIntfaceManager();
        selectIntfaceManagerTemp.setName(name);
        selectIntfaceManagerTemp.setAppManagerPrefix(prefix);

        List<SelectIntfaceManager> selectIntfaceManagerList = selectIntfaceManagerService.selectSelectIntfaceManagerList(selectIntfaceManagerTemp);

        if(selectIntfaceManagerList.size()==0){
            throw new ServiceException("接口名称未找到");
        }

        SelectIntfaceManager selectIntfaceManager = selectIntfaceManagerList.get(0);

        //接口授权检查(当intfaceReverseHistory,type为2时,即令牌调用)
        cheHaveIntfaceAppAuth(intfaceReverseHistory,selectIntfaceManager);

        Long appManagerId = selectIntfaceManager.getAppManagerId();

        AppManager appManager = appManagerService.findByIdCache(appManagerId);

        if(null==appManager){
            throw new ServiceException("应用信息信息未找到");
        }

        Long dbConfigId = appManager.getDbConfigId();

        DbConfig dbConfig = dbConfigService.findByIdCache(dbConfigId);

        if(null==dbConfig){
            throw new ServiceException("数据源未找到");
        }

        String paramsStr = ParamUtils.getParamsStrByMap(inputParamMap);

        //缓存key = prefix+name+请求参数
        String redisKey = prefix+":"+name;
        if(StringUtils.isNotEmpty(paramsStr)){
            redisKey = redisKey+","+paramsStr;
        }

        //是否开启缓存 1启用 2关闭
        //如果开启返回缓存值
        if(selectIntfaceManager.getOpenCache()==1){
            Object redisResult = redisTemplate.opsForValue().get(redisKey);;
            if(null!=redisResult){
                return redisResult;
            }
        }

        Map<String, Object> allArgsParamMap=ParamUtils.getAllArgsParamMap(selectIntfaceManager.getSqlStr());

        Object resultObject = getResult(allArgsParamMap, inputParamMap, selectIntfaceManager, appManager, dbConfig,intfaceReverseHistory);

        //是否开启缓存 1启用 2关闭
        //如果开启设置缓存值
        if(selectIntfaceManager.getOpenCache()==1){
            if(null!=resultObject){
                redisTemplate.opsForValue().set(redisKey, resultObject,30, TimeUnit.SECONDS);
            }
        }


        intfaceReverseHistory.setSelectIntfaceManagerId(selectIntfaceManager.getId());
        intfaceReverseHistory.setName(selectIntfaceManager.getName());
        intfaceReverseHistory.setAppManagerName(selectIntfaceManager.getAppManagerName());
        intfaceReverseHistory.setAppManagerPrefix(selectIntfaceManager.getAppManagerPrefix());
        intfaceReverseHistory.setInputParam(JSONObject.toJSON(inputParamMap).toString());
        intfaceReverseHistory.setDepict(selectIntfaceManager.getDepict());

        String openReverseHistory = configService.selectConfigByKey("sys.open.reverse.history");
        if(StringUtils.isEmpty(openReverseHistory)||"on".equals(openReverseHistory)){
            intfaceReverseHistoryService.insertIntfaceReverseHistory(intfaceReverseHistory);
        }

        return resultObject;
    }

    public void cheHaveIntfaceAppAuth(IntfaceReverseHistory intfaceReverseHistory,SelectIntfaceManager selectIntfaceManager){
        if(null!=intfaceReverseHistory.getType()&&2==intfaceReverseHistory.getType()){
            Long reverseAppApplyForId = intfaceReverseHistory.getReverseAppApplyForId();
            Long selectIntfaceId = selectIntfaceManager.getId();

            IntfaceAppAuth intfaceAppAuth = new IntfaceAppAuth(reverseAppApplyForId,selectIntfaceId);
            if(null==intfaceAppAuthService.cheHaveIntfaceAppAuth(intfaceAppAuth)){
                 throw new ServiceException("接口未授权,禁止访问",401);
            };
        }
    }

    public Object getResult(Map<String, Object> allArgsParamMap, Map<String, Object> inputParamMap, SelectIntfaceManager selectIntfaceManager,AppManager appManager, DbConfig dbConfig,IntfaceReverseHistory intfaceReverseHistory) {
        DBHelper dBHelper = new DBHelper();

        String sql = selectIntfaceManager.getSqlStr();

        sql= ExecuteSqlUtils.getExecuteSql(allArgsParamMap,inputParamMap,sql);

        System.out.println("sql==>"+sql);

        List<Map<String,Object>> resultList = (List<Map<String,Object>>)dBHelper.executeSql(dbConfig, sql,intfaceReverseHistory);

        Object resultObject = null;

        //结果类型 1单个对象 2列表对象
        if(selectIntfaceManager.getResultType()==1){
            //如果为空返回空数组
            if(resultList.size()==0){
                resultObject = null;
            }else{
                Map<String, Object> mapTemp = resultList.get(0);
                resultObject = mapTemp;

                if(null!=selectIntfaceManager.getKvConver()){
                    String kvConverStr = selectIntfaceManager.getKvConver().trim();
                    if(StringUtils.isNotEmpty(kvConverStr)){
                        mapTemp = handleKvConver(mapTemp,kvConverStr);
                        resultObject = mapTemp;
                    }
                }

                if(null!=selectIntfaceManager.getRcConver()){
                    String rcConverStr = selectIntfaceManager.getRcConver().trim();
                    if(StringUtils.isNotEmpty(rcConverStr)){
                        mapTemp =  handleRcConver(mapTemp,rcConverStr);
                        resultObject = mapTemp;
                    }
                }

                if(null!=selectIntfaceManager.getKeyExtract()){
                    String keyExtractStr = selectIntfaceManager.getKeyExtract().trim();
                    if(StringUtils.isNotEmpty(keyExtractStr)){
                        resultObject=handleKeyExtract((HashMap) resultObject, keyExtractStr);
                    }
                }

            }
        }

        if(selectIntfaceManager.getResultType()==2){
            //如果为空返回空数组
            if(resultList.size()==0){
                resultObject = new ArrayList<>();
            }else{

                resultObject = resultList;

                List<Map<String,Object>> listMap = new ArrayList<>();

                if(null!=selectIntfaceManager.getKvConver()){
                    String kvConverStr = selectIntfaceManager.getKvConver().trim();
                    if(StringUtils.isNotEmpty(kvConverStr)){
                        resultList = handleKvConver(resultList,kvConverStr);
                        resultObject = resultList;
                    }
                }

                if(null!=selectIntfaceManager.getRcConver()){
                    String rcConverStr = selectIntfaceManager.getRcConver().trim();
                    if(StringUtils.isNotEmpty(rcConverStr)){
                        resultObject = handleRcConver(resultList,rcConverStr);
                    }
                }

                if(resultObject instanceof List){
                    if(null!=selectIntfaceManager.getKeyExtract()){
                        String keyExtractStr = selectIntfaceManager.getKeyExtract().trim();
                        if(StringUtils.isNotEmpty(keyExtractStr)){
                            resultObject = handleKeyExtract(resultList,keyExtractStr);
                        }
                    }
                }

                if(resultObject instanceof HashMap){
                    if(null!=selectIntfaceManager.getKeyExtract()){
                        String keyExtractStr = selectIntfaceManager.getKeyExtract().trim();
                        if(StringUtils.isNotEmpty(keyExtractStr)){
                            resultObject=handleKeyExtract((HashMap) resultObject, keyExtractStr);
                        }
                    }
                }

            }
        }

        return resultObject;
    }

    public Map<String,Object> handleKvConver(Map<String,Object> map,String kvConverStr){
        Map<String,Object> resultMap = new HashMap<>();

        Map<String,String> kvConverMap = JSONObject.parseObject(kvConverStr,Map.class);

        resultMap.put("name",map.get(kvConverMap.get("name")));
        resultMap.put("value",map.get(kvConverMap.get("value")));

        return resultMap;
    }

    public List<Map<String,Object>> handleKvConver(List<Map<String,Object>> list,String kvConverStr){
        List<Map<String,Object>> resultMapList = new ArrayList();

        List<Map<String,Object>> listMap = new ArrayList(list);

        Map<String,String> kvConverMap = JSONObject.parseObject(kvConverStr,Map.class);

        for (int i = 0; i < listMap.size(); i++) {
            Map<String,Object> mapTemp = new HashMap<>();
            mapTemp.put("name",listMap.get(i).get(kvConverMap.get("name")));
            mapTemp.put("value",listMap.get(i).get(kvConverMap.get("value")));
            resultMapList.add(mapTemp);
        }

        return resultMapList;
    }


    public Map<String,Object> handleRcConver(Map<String,Object> map,String rcConverStr){
        Map<String,Object> resultMap = new HashMap();

        Map<String,String> rcConverMap = JSONObject.parseObject(rcConverStr,Map.class);
        resultMap.put(map.get(rcConverMap.get("name"))+"",map.get(rcConverMap.get("value")));

        return resultMap;
    }

    public Map<String,Object> handleRcConver(List<Map<String,Object>> list,String rcConverStr){
        Map<String,Object> resultMap = new HashMap();

        List<Map<String,Object>> listMap = new ArrayList(list);

        Map<String,String> rcConverMap = JSONObject.parseObject(rcConverStr,Map.class);

        for (int i = 0; i < listMap.size(); i++) {
            resultMap.put(listMap.get(i).get(rcConverMap.get("name"))+"",listMap.get(i).get(rcConverMap.get("value")));
        }

        return resultMap;
    }

    public Map<String,Object> handleKeyExtract(Map<String,Object> map,String keyExtractStr){

        Map<String,String> keyExtractMap = JSONObject.parseObject(keyExtractStr,Map.class);

        String keyExtract = keyExtractMap.get("key")+"";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put(((HashMap) map).get(keyExtract)+"",(HashMap) map);

        return resMap;
    }

    public List<Map<String,Object>> handleKeyExtract(List<Map<String,Object>> list,String keyExtractStr){

        List<Map<String,Object>> resultMapList = new ArrayList<>();

        List<Map<String,Object>> listMap = new ArrayList(list);

        Map<String,String> keyExtractMap = JSONObject.parseObject(keyExtractStr,Map.class);

        String keyExtract = keyExtractMap.get("key")+"";

        for (int i = 0; i < listMap.size(); i++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put(listMap.get(i).get(keyExtract)+"",listMap.get(i));
            resultMapList.add(temp);
        }

        return resultMapList;
    }

    public static boolean checkInputParamMap(Map<String, Object> inputParamMap){
        boolean flag = false;

        for(String s:inputParamMap.keySet()){
            String param = inputParamMap.get(s)+"";
            flag = AntiSqlInjectionUtils.check(param);
            if(flag){
                break;
            }
        }

        return flag;
    }

    public static void main(String[] args) {
        Map<String, Object> inputParamMap = new HashMap<>();
        inputParamMap.put("a","123");
        System.out.println(checkInputParamMap(inputParamMap));
    }
}
