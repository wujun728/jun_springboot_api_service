package com.jun.plugin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jun.plugin.system.common.utils.DataResult;
import com.jun.plugin.system.entity.SysFilesEntity;
import com.jun.plugin.system.service.SysFilesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@RestController
@RequestMapping("/sysFiles")
@Api(tags = "文件管理")
@Slf4j
public class SysFilesController {
	@Resource
	private SysFilesService sysFilesService;

	@ApiOperation(value = "新增")
	@PostMapping("/upload")
	@RequiresPermissions(value = { "sysFiles:add", "sysContent:update", "sysContent:add" }, logical = Logical.OR)
	public DataResult add(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "bizid") String bizid,
			@RequestParam(value = "biztype") String biztype) {
		log.info(biztype);
		log.info(bizid);
		// 判断文件是否空
		if (file == null || file.getOriginalFilename() == null
				|| "".equalsIgnoreCase(file.getOriginalFilename().trim())) {
			return DataResult.fail("文件为空");
		}
		return sysFilesService.saveFile(file, biztype, bizid);
	}

	@ApiOperation(value = "删除")
	@DeleteMapping("/delete")
	@RequiresPermissions("sysFiles:delete")
	public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
		sysFilesService.removeByIdsAndFiles(ids);
		return DataResult.success();
	}

	@ApiOperation(value = "查询分页数据")
	@PostMapping("/listByPage")
	@RequiresPermissions("sysFiles:list")
	public DataResult findListByPage(@RequestBody SysFilesEntity sysFiles) {
		Page page = new Page(sysFiles.getPage(), sysFiles.getLimit());
		IPage<SysFilesEntity> iPage = sysFilesService.page(page,
				Wrappers.<SysFilesEntity>lambdaQuery().orderByDesc(SysFilesEntity::getCreateDate));
		return DataResult.success(iPage);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "查询分页数据")
	@PostMapping("/listByPageUser")
	@RequiresPermissions("sysFiles:list")
	public DataResult listByPageUser(@RequestBody SysFilesEntity sysFiles) {
		Page page = new Page(sysFiles.getPage(), sysFiles.getLimit());
		IPage<SysFilesEntity> iPage = sysFilesService.page(page,
				Wrappers.<SysFilesEntity>lambdaQuery()
				.eq(SysFilesEntity::getRefBizid, sysFiles.getRefBizid())
				.eq(SysFilesEntity::getDictBiztype, sysFiles.getDictBiztype())
				.orderByDesc(SysFilesEntity::getCreateDate));
		return DataResult.success(iPage);
	}

}