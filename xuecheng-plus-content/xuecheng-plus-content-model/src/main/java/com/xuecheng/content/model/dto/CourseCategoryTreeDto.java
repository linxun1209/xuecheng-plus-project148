package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 课程分类树型结点dto
 * @author Mr.M
 * @date 2022/10/8 11:29
 * @version 1.0
 */
 @Data
public class CourseCategoryTreeDto extends CourseCategory  implements Serializable {
     //子分类
     List<CourseCategoryTreeDto> childrenTreeNodes;
}
