package com.example.demo.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.CustomInfo;
import com.example.demo.entity.News;
import com.example.demo.mapper.CustomInfoMapper;

import cn.hutool.core.util.StrUtil;

@RestController
@RequestMapping("/custom")
public class CustomInfoController extends BaseController {


	@Resource
	CustomInfoMapper customInfoMapper;

	@PostMapping("/save")
	public Result<?> save(@RequestBody CustomInfo customInfo) {
		customInfo.setCreateTime(new Date());
		customInfo.setCreateBy("admin");
		customInfoMapper.insert(customInfo);
		return Result.success();
	}

	@GetMapping("/list")
	public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
							  @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String name,
							  @RequestParam(defaultValue = "") String phone) {
		LambdaQueryWrapper<CustomInfo> wrapper = Wrappers.<CustomInfo>lambdaQuery();
		if (StrUtil.isNotBlank(name)) {
			wrapper.like(CustomInfo::getName, name);
		}
		if (StrUtil.isNotBlank(phone)) {
			wrapper.like(CustomInfo::getPhone, phone);
		}
		Page<CustomInfo> newsPage = customInfoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
		return Result.success(newsPage);
	}

}
