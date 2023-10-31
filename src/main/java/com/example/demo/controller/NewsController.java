package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/news")
public class NewsController extends BaseController {

    @Resource
    NewsMapper newsMapper;

    @PostMapping("/save")
    public Result<?> save(@RequestBody News news) {
        news.setCreateTime(new Date());
        news.setCreateBy(getUser().getUsername());
        newsMapper.insert(news);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody News news) {
    	news.setUpdateTime(new Date());
    	news.setUpdateBy(getUser().getUsername());
        newsMapper.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> update(@PathVariable Long id) {
        newsMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(newsMapper.selectById(id));
    }

    @GetMapping("/list")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String title,
                              @RequestParam(defaultValue = "") String status,
                              @RequestParam(defaultValue = "") String type,
                              @RequestParam(defaultValue = "") String productType,
                              @RequestParam(defaultValue = "") String productLevel) {
        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if (StrUtil.isNotBlank(title)) {
            wrapper.like(News::getTitle, title);
        }
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(News::getStatus, status);
        }
        if (StrUtil.isNotBlank(type)) {
            wrapper.eq(News::getType, type);
        }
        if (StrUtil.isNotBlank(productType)) {
            wrapper.eq(News::getProductType, productType);
        }
        if (StrUtil.isNotBlank(productLevel)) {
            wrapper.eq(News::getProductLevel, productLevel);
        }
        Page<News> newsPage = newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(newsPage);
    }
}
