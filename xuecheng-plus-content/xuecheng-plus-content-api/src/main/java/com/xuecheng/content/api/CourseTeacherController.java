package com.xuecheng.content.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xingchen
 * @version V1.0
 * @Package com.xuecheng.content.api
 * @date 2023/2/14 21:12
 */
@Slf4j
@RestController
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;


    /**
     * @description 查询课程老师
     * @param courseId 课程id
     * @return com.xuecheng.contentModel.po.CourseTeacher
     * @author xiaoming
     * @date 2023/1/30 15:50
     */
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> selectCourseTeacher(@PathVariable Long courseId){
        return courseTeacherService.getCourseTeacherInfo(courseId);
    }

    /**
     * @description 添加/修改课程老师
     * @param  teacher 教师信息
     * @return com.xuecheng.contentModel.po.CourseTeacher
     * @author xiaoming
     * @date 2023/1/30 15:51
     */
    @PostMapping("/courseTeacher")
    public CourseTeacher saveCourseTeacher(@RequestBody @Validated CourseTeacher teacher){
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        return courseTeacherService.saveCourseTeacher(teacher);
    }


    /**
     * @description 删除课程老师
     * @param courseId 课程id
     * @param id 教师id
     * @author xiaoming
     * @date 2023/1/30 16:02
     */
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long id){
        courseTeacherService.deleteCourseTeacher(courseId, id);
    }
}