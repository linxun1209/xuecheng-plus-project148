package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/10/10 14:51
 */
@Slf4j
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    //新增、修改
    @Override
    public void saveTeachplan(SaveTeachplanDto dto) {

        Long id = dto.getId();

        Teachplan teachplan = teachplanMapper.selectById(id);

        if(teachplan==null){
            teachplan = new Teachplan();
            BeanUtils.copyProperties(dto,teachplan);
            //找到同级课程计划的数量
            int count = getTeachplanCount(dto.getCourseId(), dto.getParentid());
            //新课程计划的值
            teachplan.setOrderby(count+1);

            teachplanMapper.insert(teachplan);

        }else{
            BeanUtils.copyProperties(dto,teachplan);
            //更新
            teachplanMapper.updateById(teachplan);

        }


    }

    @Transactional
    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划的id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        //约束校验
        //教学计划不存在无法绑定
        if(teachplan == null){
            XueChengPlusException.cast("教学计划不存在");
        }
        //只有二级目录才可以绑定视频
        Integer grade = teachplan.getGrade();
        if(grade != 2){
            XueChengPlusException.cast("只有二级目录才可以绑定视频");
        }


        //删除原来的绑定关系
        LambdaQueryWrapper<TeachplanMedia> lambdaQueryWrapper = new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, teachplanId);
        teachplanMediaMapper.delete(lambdaQueryWrapper);

        //添加新的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMediaMapper.insert(teachplanMedia);
        return teachplanMedia;
    }


    /**
     * 教学计划解绑媒资
     * @param teachplanId
     * @param mediaId
     */
    @Override
    public void delAassociationMedia(Long teachplanId, String  mediaId) {


        //教学计划的id
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        //约束校验
        //教学计划不存在无法解绑
        if(teachplan == null){
            XueChengPlusException.cast("教学计划不存在");
        }
        //只有二级目录才可以解绑视频
        Integer grade = teachplan.getGrade();
        if(grade != 2){
            XueChengPlusException.cast("只有二级目录才可以解绑视频");
        }
        //删除绑定关系
        LambdaQueryWrapper<TeachplanMedia> lambdaQueryWrapper = new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, teachplanId);
        teachplanMediaMapper.delete(lambdaQueryWrapper);
    }

    @Transactional
    @Override
    public List<TeachplanDto> deleteTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Long courseId = teachplan.getCourseId();
        if (teachplan == null) {
            throw new XueChengPlusException("无法找到该章节");
        }

        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, id);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new XueChengPlusException("存在子章节，无法删除该章节");
        }

        Long teachplanId = teachplan.getId();
        LambdaQueryWrapper<TeachplanMedia> query = new LambdaQueryWrapper<>();
        query.eq(TeachplanMedia::getTeachplanId, teachplanId);
        Integer mediaCount = teachplanMediaMapper.selectCount(query);
        if (mediaCount > 0) {
            teachplanMediaMapper.delete(query);
        }
        teachplanMapper.deleteById(teachplanId);
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    public void moveup(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Long parentid = teachplan.getParentid();
        Integer targetOrderby = teachplan.getOrderby();
        if (targetOrderby == 1) {
            throw new XueChengPlusException("已经是第一个了，无法继续上移");
        }
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, parentid);
        queryWrapper.lt(Teachplan::getOrderby, targetOrderby);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        if (count >= 1) {
            queryWrapper.eq(Teachplan::getOrderby, targetOrderby - 1);
            Teachplan preTeachplan = teachplanMapper.selectOne(queryWrapper);
            preTeachplan.setOrderby(preTeachplan.getOrderby() + 1);
            teachplanMapper.updateById(preTeachplan);
            teachplan.setOrderby(targetOrderby - 1);
        }
        teachplanMapper.updateById(teachplan);
    }

    @Override
    public void movedown(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Long parentid = teachplan.getParentid();
        Integer targetOrderby = teachplan.getOrderby();
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, parentid);
        queryWrapper.gt(Teachplan::getOrderby, targetOrderby);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        if (count == 0) {
            throw new XueChengPlusException("已经是最后一个了，无法继续下移");
        } else {
            queryWrapper.eq(Teachplan::getOrderby, targetOrderby + 1);
            Teachplan preTeachplan = teachplanMapper.selectOne(queryWrapper);
            preTeachplan.setOrderby(preTeachplan.getOrderby() - 1);
            teachplanMapper.updateById(preTeachplan);
            teachplan.setOrderby(targetOrderby + 1);
        }
        teachplanMapper.updateById(teachplan);
    }

    /**
     * 计算机新课程计划的orderby 找到同级课程计划的数量
     * SELECT count(1) from teachplan where course_id=117 and parentid=268
     * @param courseId
     * @param parentId
     * @return
     */
    public int getTeachplanCount(Long courseId,Long parentId){

        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count.intValue();

    }
}
