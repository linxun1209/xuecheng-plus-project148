package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @description TODO
 * @author Mr.M
 * @date 2022/10/10 14:51
 * @version 1.0
 */
public interface TeachplanService {

 public List<TeachplanDto> findTeachplanTree(Long courseId);

 /**
  * @description 保存课程计划(新增/修改)
  * @param dto
  * @return void
  * @author Mr.M
  * @date 2022/10/10 15:07
 */
 public void saveTeachplan(SaveTeachplanDto dto);
 /**
  * @description 教学计划绑定媒资
  * @param bindTeachplanMediaDto

  * @author Mr.M
  * @date 2022/9/14 22:20
  */
 public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

 /**
  * 教学计划解绑媒资
  * @param teachplanId
  * @param mediaId
  */
 void delAassociationMedia(Long teachplanId,String  mediaId);

 /**
  * @description 删除课程计划信息
  * @param id teachplanId
  * @author xiaoming
  * @date 2023/1/30 13:01
  */
 public List<TeachplanDto> deleteTeachplan(Long id);


 /**
  * @description 上移
  * @param id teachplanId
  * @author xiaoming
  * @date 2023/1/30 13:02
  */
 public void moveup(Long id);

 /**
  * @description 下移
  * @param id teachplId
  * @author xiaoming
  * @date 2023/1/30 13:03
  */
 public void movedown(Long id);


}


