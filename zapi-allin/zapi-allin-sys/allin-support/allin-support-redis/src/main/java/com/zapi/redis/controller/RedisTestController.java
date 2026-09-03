package com.zapi.redis.controller;

import com.zapi.common.Result;
import com.zapi.redis.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis测试控制器
 * 提供Redis操作的测试接口
 * 
 * @author zapi
 * @since 1.0.0
 */
@Api(tags = "Redis测试接口")
@RestController
@RequestMapping("/redis/test")
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试Redis连接
     * 
     * @return 连接状态
     */
    @ApiOperation("测试Redis连接")
    @GetMapping("/status")
    public Result<Map<String, Object>> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 测试设置和获取值
            String testKey = "test:connection";
            String testValue = "Redis连接测试成功";
            
            boolean setResult = redisUtil.set(testKey, testValue, 60);
            Object getResult = redisUtil.get(testKey);
            
            result.put("status", "success");
            result.put("setResult", setResult);
            result.put("getResult", getResult);
            result.put("message", "Redis连接正常");
            
            return Result.success(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Redis连接失败: " + e.getMessage());
            return Result.error("Redis连接测试失败");
        }
    }

    /**
     * 测试String操作
     * 
     * @param key   键
     * @param value 值
     * @param expire 过期时间（秒）
     * @return 操作结果
     */
    @ApiOperation("测试String操作")
    @PostMapping("/string")
    public Result<Map<String, Object>> testString(
            @ApiParam("键") @RequestParam String key,
            @ApiParam("值") @RequestParam String value,
            @ApiParam("过期时间（秒）") @RequestParam(defaultValue = "3600") long expire) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 设置值
            boolean setResult = redisUtil.set(key, value, expire);
            result.put("setResult", setResult);
            
            // 获取值
            Object getResult = redisUtil.get(key);
            result.put("getResult", getResult);
            
            // 检查是否存在
            boolean hasKey = redisUtil.hasKey(key);
            result.put("hasKey", hasKey);
            
            // 获取过期时间
            long ttl = redisUtil.getExpire(key);
            result.put("ttl", ttl);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("String操作失败: " + e.getMessage());
        }
    }

    /**
     * 测试Hash操作
     * 
     * @param key   键
     * @param field 字段
     * @param value 值
     * @return 操作结果
     */
    @ApiOperation("测试Hash操作")
    @PostMapping("/hash")
    public Result<Map<String, Object>> testHash(
            @ApiParam("键") @RequestParam String key,
            @ApiParam("字段") @RequestParam String field,
            @ApiParam("值") @RequestParam String value) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 设置Hash字段
            boolean hsetResult = redisUtil.hset(key, field, value);
            result.put("hsetResult", hsetResult);
            
            // 获取Hash字段
            Object hgetResult = redisUtil.hget(key, field);
            result.put("hgetResult", hgetResult);
            
            // 检查Hash字段是否存在
            boolean hHasKey = redisUtil.hHasKey(key, field);
            result.put("hHasKey", hHasKey);
            
            // 获取所有Hash字段
            Map<Object, Object> hmgetResult = redisUtil.hmget(key);
            result.put("hmgetResult", hmgetResult);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("Hash操作失败: " + e.getMessage());
        }
    }

    /**
     * 测试List操作
     * 
     * @param key   键
     * @param values 值列表
     * @return 操作结果
     */
    @ApiOperation("测试List操作")
    @PostMapping("/list")
    public Result<Map<String, Object>> testList(
            @ApiParam("键") @RequestParam String key,
            @ApiParam("值列表") @RequestParam List<String> values) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 设置List
            boolean lsetResult = redisUtil.lSet(key, values);
            result.put("lsetResult", lsetResult);
            
            // 获取List长度
            long listSize = redisUtil.lGetListSize(key);
            result.put("listSize", listSize);
            
            // 获取List内容
            List<Object> lgetResult = redisUtil.lGet(key, 0, -1);
            result.put("lgetResult", lgetResult);
            
            // 获取第一个元素
            Object firstElement = redisUtil.lGetIndex(key, 0);
            result.put("firstElement", firstElement);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("List操作失败: " + e.getMessage());
        }
    }

    /**
     * 测试Set操作
     * 
     * @param key    键
     * @param values 值数组
     * @return 操作结果
     */
    @ApiOperation("测试Set操作")
    @PostMapping("/set")
    public Result<Map<String, Object>> testSet(
            @ApiParam("键") @RequestParam String key,
            @ApiParam("值数组") @RequestParam String[] values) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 设置Set
            long ssetResult = redisUtil.sSet(key, (Object[]) values);
            result.put("ssetResult", ssetResult);
            
            // 获取Set大小
            long setSize = redisUtil.sGetSetSize(key);
            result.put("setSize", setSize);
            
            // 获取Set内容
            Set<Object> sgetResult = redisUtil.sGet(key);
            result.put("sgetResult", sgetResult);
            
            // 检查某个值是否存在
            if (values.length > 0) {
                boolean sHasKey = redisUtil.sHasKey(key, values[0]);
                result.put("sHasKey", sHasKey);
            }
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("Set操作失败: " + e.getMessage());
        }
    }

    /**
     * 测试计数器操作
     * 
     * @param key   键
     * @param delta 增量
     * @return 操作结果
     */
    @ApiOperation("测试计数器操作")
    @PostMapping("/counter")
    public Result<Map<String, Object>> testCounter(
            @ApiParam("键") @RequestParam String key,
            @ApiParam("增量") @RequestParam(defaultValue = "1") long delta) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 递增
            long incrResult = redisUtil.incr(key, delta);
            result.put("incrResult", incrResult);
            
            // 获取当前值
            Object currentValue = redisUtil.get(key);
            result.put("currentValue", currentValue);
            
            // 递减
            long decrResult = redisUtil.decr(key, delta);
            result.put("decrResult", decrResult);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("计数器操作失败: " + e.getMessage());
        }
    }

    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 操作结果
     */
    @ApiOperation("删除缓存")
    @DeleteMapping("/delete")
    public Result<Map<String, Object>> deleteCache(
            @ApiParam("键") @RequestParam String key) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查是否存在
            boolean hasKey = redisUtil.hasKey(key);
            result.put("beforeDelete", hasKey);
            
            // 删除
            redisUtil.del(key);
            
            // 再次检查是否存在
            boolean afterDelete = redisUtil.hasKey(key);
            result.put("afterDelete", afterDelete);
            
            result.put("message", "删除成功");
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 生成缓存Key
     * 
     * @param prefix 前缀
     * @param params 参数
     * @return 生成的Key
     */
    @ApiOperation("生成缓存Key")
    @GetMapping("/generate-key")
    public Result<Map<String, Object>> generateKey(
            @ApiParam("前缀") @RequestParam String prefix,
            @ApiParam("参数") @RequestParam(required = false) String[] params) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String generatedKey = redisUtil.generateKey(prefix, params);
            result.put("generatedKey", generatedKey);
            result.put("prefix", prefix);
            result.put("params", params);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("生成Key失败: " + e.getMessage());
        }
    }
} 