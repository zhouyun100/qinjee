package com.qinjee.model.response;

import com.github.pagehelper.Page;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页查询结果封装类
 * @param <T>
 * @author 高雄
 * @date 2019/10/16
 */
@Data
@ToString
@Setter
@Getter
public class PageResult<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 数据总数
     */
    private long total;

    public PageResult(List<T> list){
        if (list instanceof Page) {
            Page page = (Page)list;
            this.total = page.getTotal();
        }
        this.list = list;
    }

    public PageResult(){
    }

    public void setList(List<T> list) {

    }
}
