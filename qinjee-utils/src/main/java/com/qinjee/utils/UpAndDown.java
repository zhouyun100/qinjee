package com.qinjee.utils;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Administrator
 */
public class UpAndDown {

    /**
     腾讯云对象存储 COS 中的对象需具有合法的对象键，对象键（ObjectKey）是对象在存储桶中的唯一标识。
     例如：在对象的访问地址examplebucket-1250000000.cos.ap-guangzhou.myqcloud.com/folder/picture.jpg 中，对象键为folder/picture.jpg。

     在相应的操作结束后，需要调用cosclient的shutdown方法进行关闭客户端。

     手动创建的文件夹，如果里面没有内容的话文件夹还存在与存储桶中
     若是通过demo创建的文件夹，里面没有内容会默认删除，这就要求我们对比较重要的目录进行手动创建

     临时签名在删除文件跟获取文件信息会报权限不够的错误。所以这是不能用临时的客户端

     鉴于后期需要对异常捕获，这里异常很多，影响性能比较严重
     */


    /**
     * 获取临时对象
     * @param secretId
     * @param secretKey
     * @param bucketName
     * @param regionName
     */
    public static  JSONObject getCredential(String secretId,String secretKey,String bucketName,String regionName){
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            config.put("SecretId",secretId );
            config.put("SecretKey", secretKey);
            config.put("durationSeconds", 1800);
            config.put("bucket", bucketName);
            config.put("region", regionName);
            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的目录，例子：* 或者 doc/* 或者 picture.jpg
            config.put("allowPrefix", "*");
            // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[] {
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            JSONObject credential = CosStsClient.getCredential(config);
             return  credential;
            //成功返回临时密钥信息，如下打印密钥信息
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }

    }

    /**
     * 初始化客户端
     *
     * @return
     */
    public static   COSClient InitClient(String secretId,String secretKey,String regionName) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);

        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }


//    /**
//     * 初始化客户端
//     *
//     * @return
//     */
//    public static COSClient InitClient(String secretId,String secretKey,String bucketName,String regionName) {
//        //获取临时连接所需要的接送对象
//        JSONObject credential = UpAndDown.getCredential(secretId, secretKey, bucketName, regionName);
//        //解析获取临时参数
//        String s = credential.getString("credentials");
//        JSONObject jsonObject = JSON.parseObject(s);
//        String tmpSecretId = jsonObject.getString("tmpSecretId");
//        String tmpSecretKey= jsonObject.getString("tmpSecretKey");
//        String sessionToken= jsonObject.getString("sessionToken");
//        //初始化用户身份信息
//        BasicSessionCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);
//        //设置bucket的地域信息
//        Region region = new Region(regionName);
//        ClientConfig clientConfig = new ClientConfig(region);
//        //生成客户端
//        return new COSClient(cred, clientConfig);
//
//    }




    /**
     * 上传文件到指定存储桶
     *
     * @param path
     * @param bucketName
     * @param key        对象键，详情见上方说明
     * @param cosClient
     */
    public static void putFile(String path, String bucketName, String key, COSClient cosClient) {
        try {
            // 指定要上传的文件
            File localFile = new File(path);
            // 指定要上传到 COS 上对象键
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }

    }

    /**
     * 获取桶中符合的对象集合，可以通过遍历来定位到制定对象
     *
     * @param bucket
     * @param prefix    对象键的前缀
     * @param cosClient
     * @return
     */
    public static List<COSObjectSummary> getFileList(String bucket, String prefix, COSClient cosClient) {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(bucket);
            // prefix 表示列出的 object 的 key 以 prefix 开始
            listObjectsRequest.setPrefix(prefix);
            // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            listObjectsRequest.setDelimiter("/");
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            return objectListing.getObjectSummaries();
//            for (COSObjectSummary cosObjectSummary : objectListing.getObjectSummaries()) {
//                // 对象的路径 key
//                String key = cosObjectSummary.getKey();
//                // 对象的 etag
//                String etag = cosObjectSummary.getETag();
//                // 对象的长度
//                long fileSize = cosObjectSummary.getSize();
//                // 对象的存储类型
//                String storageClass = cosObjectSummary.getStorageClass();
//                System.out.println("key:" + key + "; etag:" + etag + "; fileSize:" + fileSize + "; storageClass:" + storageClass);
//            }
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * 下载文件到指定路径
     *
     * @param bucketName
     * @param key          对象键
     * @param downFilePath 下载到指定的路径
     * @param cosClient
     */
    public static void downFile(String bucketName, String key, String downFilePath, COSClient cosClient) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            File downFile = new File(downFilePath);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }

    }


    /**
     * 说明：在业务场景中，删除应该是改变数据库里面的某个相对于显示的字段，进行逻辑删除，调用此方法是
     * 数据库中将此文件进行抹除
     * @param bucketName
     * @param key        对象键
     * @param cosClient
     */
    public static void delFile(String bucketName, String key, COSClient cosClient) {
        try {
            cosClient.deleteObject(bucketName, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            cosClient.shutdown();
        }
    }
}
