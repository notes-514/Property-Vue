package com.guet.property.common.api;

import java.util.List;

/**
 * 通用的分页数据实现类
 *
 * @author dhxstart
 * @date 2021/10/28 21:00
 */
public class CommonPage<T> {
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 分页数据
     */
    private List<T> list;

    ///**
    // * 将PageHelper分页后的list转为分页信息
    // */
    //public static <T> CommonPage<T> restPage(List<T> list) {
    //    CommonPage<T> result = new CommonPage<>();
    //    PageInfo<T> pageInfo = new PageInfo<>(list);
    //    result.setTotalPage(pageInfo.getPages());
    //    result.setPageNum(pageInfo.getPageNum());
    //    result.setPageSize(pageInfo.getPageSize());
    //    result.setTotal(pageInfo.getTotal());
    //    result.setList(pageInfo.getList());
    //    return result;
    //}
    //
    ///**
    // * 将SpringData分页后的list转为分页信息
    // */
    //public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
    //    CommonPage<T> result = new CommonPage<>();
    //    result.setTotalPage(pageInfo.getTotalPages());
    //    result.setPageNum(pageInfo.getNumber());
    //    result.setPageSize(pageInfo.getSize());
    //    result.setTotal(pageInfo.getTotalElements());
    //    result.setList(pageInfo.getContent());
    //    return result;
    //}

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
