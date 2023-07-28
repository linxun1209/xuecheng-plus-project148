package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author xioaming
 * @version 1.0
 * @description 课程教师信息业务类
 * @date 2023/1/30 15:34
 */
public interface CourseTeacherService {

    /**
     * @description 查询课程老师信息
     * @param courseId 课程id
     * @return com.xuecheng.contentModel.po.CourseTeacher
     * @author xiaoming
     * @date 2023/1/30 21:10
     */
    public List<CourseTeacher> getCourseTeacherInfo(Long courseId);


    /**
     * @description 新增、修改教师信息
     * @param teacher 教师信息传输类
     * @return com.xuecheng.contentModel.po.CourseTeacher
     * @author xiaoming
     * @date 2023/1/30 21:20
     */
    public CourseTeacher saveCourseTeacher(CourseTeacher teacher);

    /**
     * @description 删除教师信息
     * @param courseId 课程id
     * @param id 教师信息表id
     * @author xiaoming
     * @date 2023/1/30 21:57
     */
    public void deleteCourseTeacher(Long courseId, Long id);
}