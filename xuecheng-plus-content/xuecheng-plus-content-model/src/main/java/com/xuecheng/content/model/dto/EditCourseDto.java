package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/10/10 10:54
 * @version 1.0
 */
@Data
@ApiModel(value="EditCourseDto", description="修改课程基本信息")
public class EditCourseDto extends AddCourseDto {

 @NotNull(message = "未确定要修改的课程")
 @ApiModelProperty(value = "课程名称", required = true)
 private Long id;

}
