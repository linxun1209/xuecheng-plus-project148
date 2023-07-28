package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.MinioException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio上传文件、删除文件、查询文件
 * @date 2022/10/13 14:42
 */
public class MinIOTest {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();


    @Test
    public void upload() {

        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("testbucket")
                    .object("1.mp4")//同一个桶内对象名不能重复
                    .filename("D:\\develop\\upload\\1.mp4")
                    .build();
            //上传
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功了");
        } catch (Exception e) {
            System.out.println("上传失败");
        }
    }
    //上传文件
    public static void upload1()throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            boolean found =minioClient.bucketExists(BucketExistsArgs.builder().bucket("testbucket").build());
            //检查testbucket桶是否创建，没有创建自动创建
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("testbucket").build());
            } else {
                System.out.println("Bucket 'testbucket' already exists.");
            }
            //上传1.mp4
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("testbucket")
                            .object("1.mp4")
                            .filename("D:\\develop\\upload\\1.mp4")
                            .build());
            //上传1.avi,上传到avi子目录
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("testbucket")
                            .object("avi/1.avi")
                            .filename("D:\\develop\\upload\\1.avi")
                            .build());
            System.out.println("上传成功");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

    }

    //删除文件
    public static void delete(String bucket,String filepath)throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {

            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(filepath).build());
            System.out.println("删除成功");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

    }

    //下载文件
    public static void getFile(String bucket,String filepath,String outFile)throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(filepath)
                        .build());
                  FileOutputStream fileOutputStream = new FileOutputStream(new File(outFile));
        ) {

            // Read data from stream
            IOUtils.copy(stream,fileOutputStream);
            System.out.println("下载成功");
        }

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

    }


    public static void main(String[] args)throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        upload1();
//  delete("testbucket","1.mp4");
//  delete("testbucket","avi/1.avi");
        getFile("testbucket","avi/1.avi","D:\\develop\\minio_data\\1.avi");
    }

    //指定桶内的子目录
    @Test
    public void upload2() {

        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("testbucket")
                    .object("test/1.avi")//同一个桶内对象名不能重复
                    .filename("D:\\develop\\upload\\1.avi")
                    .build();
            //上传
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功了");
        } catch (Exception e) {
            System.out.println("上传失败");
        }


    }
    //删除文件
    @Test
    public void delete() {

        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("testbucket").object("test/1.avi").build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
        }

    }
    //查询文件
    @Test
    public void getFile() {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("1.mp4").build();
        try(
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("D:\\develop\\upload\\1_1.mp4"));
                ) {

            if(inputStream!=null){
                IOUtils.copy(inputStream,outputStream);
            }
        } catch (Exception e) {
        }

    }

}
