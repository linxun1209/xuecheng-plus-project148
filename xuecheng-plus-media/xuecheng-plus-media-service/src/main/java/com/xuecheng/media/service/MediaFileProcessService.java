package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件处理业务方法
 * @date 2022/10/16 15:11
 */
public interface MediaFileProcessService {


    /**
     * @param shardIndex 分片序号
     * @param shardTotal 分片总数
     * @param count      获取记录数
     * @return java.util.List<com.xuecheng.media.model.po.MediaProcess>
     * @description 获取待处理任务
     * @author Mr.M
     * @date 2022/9/14 14:49
     */
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);


    /**
     * @param status  处理结果，2:成功3失败
     * @param fileId  文件id
     * @param url 文件访问url
     * @param errorMsg 失败原因
     * @return void
     @description 将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
     */
    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
