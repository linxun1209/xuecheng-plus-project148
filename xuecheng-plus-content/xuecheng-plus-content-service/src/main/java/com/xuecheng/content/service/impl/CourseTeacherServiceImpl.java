package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getCourseTeacherInfo(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        CourseTeacher teacher = courseTeacherMapper.selectOne(queryWrapper);
        List<CourseTeacher> list = new ArrayList<>();
        if (teacher == null){
            list.add(new CourseTeacher());
        }else {
            list.add(teacher);
        }
        return list;
    }

    @Transactional
    @Override
    public CourseTeacher saveCourseTeacher(CourseTeacher teacher) {
        Long teacherCourseId = teacher.getCourseId();
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, teacherCourseId);
        Integer count = courseTeacherMapper.selectCount(queryWrapper);
        if (count < 1){
            CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(teacher, courseTeacher);
            courseTeacherMapper.insert(courseTeacher);
            return getCourseTeacher(courseTeacher);
        }
        CourseTeacher courseTeacher = courseTeacherMapper.selectOne(queryWrapper);
        Long id = courseTeacher.getId();
        BeanUtils.copyProperties(teacher, courseTeacher);
        courseTeacher.setId(id);
        courseTeacherMapper.updateById(courseTeacher);
        Long courseId = courseTeacher.getCourseId();
        LambdaQueryWrapper<CourseTeacher> query = new LambdaQueryWrapper<>();
        query.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectOne(query);
    }

    private CourseTeacher getCourseTeacher(CourseTeacher courseTeacher) {
        Long courseId = courseTeacher.getCourseId();
        LambdaQueryWrapper<CourseTeacher> query = new LambdaQueryWrapper<>();
        query.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectOne(query);
    }

    @Transactional
    @Override
    public void deleteCourseTeacher(Long courseId, Long id) {
        courseTeacherMapper.deleteById(id);
    }
}