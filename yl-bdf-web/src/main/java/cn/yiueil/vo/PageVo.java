package cn.yiueil.vo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Author:YIueil
 * Date:2022/7/3 20:05
 * Description: 分页包装类
 */
@Getter
@Setter
@ApiModel(value = "分页视图对象")
public class PageVo implements Serializable {
    @NotNull
    @Min(value = 1, message = "页码必须是正整数")
    @ApiModelProperty(value = "页码")
    private int pageIndex; // 页码

    @ApiModelProperty(value = "每页数量")
    @Min(value = 1, message = "每页数量必须是正整数")
    private int pageSize; // 每页数量

    @ApiModelProperty(value = "页面总数")
    private int pageTotal; // 页面总数

    @ApiModelProperty(value = "元素总数")
    private int itemCounts; //元素总数

    @ApiModelProperty(value = "数据体")
    private Object body; // 页面内容
}
