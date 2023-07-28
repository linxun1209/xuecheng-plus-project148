package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xing'chen
 * @version 1.0
 * @description TODO
 * @date 2022/10/10 11:55
 */

@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;

    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.findTeachplanTree(courseId);
    }

    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto dto) {
        teachplanService.saveTeachplan(dto);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }



    @ApiOperation(value = "课程计划和媒资信解绑")
    @DeleteMapping("/teachplan/association/media/{teachplanId}/{mediaId}")
    public void deleteMedia(@PathVariable Long teachplanId,@PathVariable String  mediaId) {
        teachplanService.delAassociationMedia(teachplanId,mediaId);
    }

    @DeleteMapping("/teachplan/{id}")
    public List<TeachplanDto> deleteTeachplan(@PathVariable Long id){
        return teachplanService.deleteTeachplan(id);
    }


    @PostMapping("/teachplan/moveup/{id}")
    public void moveup(@PathVariable Long id){
        teachplanService.moveup(id);
    }

    @PostMapping("/teachplan/movedown/{id}")
    public void movedown(@PathVariable Long id){
        teachplanService.movedown(id);
    }

}
