package com.qinjee.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import entity.CosStsClient;

import java.io.File;
import java.util.*;
/**
 腾讯云对象存储 COS 中的对象需具有合法的对象键，对象键（ObjectKey）是对象在存储桶中的唯一标识。
 例如：在对象的访问地址examplebucket-1250000000.cos.ap-guangzhou.myqcloud.com/folder/picture.jpg 中，对象键为folder/picture.jpg。

 在相应的操作结束后，需要调用cosclient的shutdown方法进行关闭客户端。

 手动创建的文件夹，如果里面没有内容的话文件夹还存在与存储桶中
 若是通过demo创建的文件夹，里面没有内容会默认删除，这就要求我们对比较重要的目录进行手动创建

 临时签名在删除文件跟获取文件信息会报权限不够的错误。所以这是不能用临时的客户端

 */

/**
 * @author Administrator
 */
public class UpAndDownUtil {
    private static final String SECRET_ID= "AKIDMi3OgwqoGh8YvMu7C6ovseEdy87v3s56";
    private static final String SECRET_KEY = "1pU9icMvJWm1A7wa7SgKrGVbWfgVaVFo";
    private static final String BUCKET = "qinjee-datacenter-1253673776";
    private static final String REGION_NAME= "ap-guangzhou";
    private static final COSClient COS_ClIENT=UpAndDownUtil.initClient();

    /**
     * @return  前端需要调用的方法
     */
    public static  JSONObject getCredential(){
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            config.put("SecretId",SECRET_ID );
            config.put("SecretKey", SECRET_KEY);
            config.put("durationSeconds", 1800);
            config.put("bucket", BUCKET);
            config.put("region", REGION_NAME);
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
            System.out.println(credential.toJSONString());
             return  credential;
            //成功返回临时密钥信息，如下打印密钥信息
        } catch (Exception e) {
            //失败抛出异常
            throw new IllegalArgumentException("no valid secret !");
        }

    }
    /**
     //     * 根据前端要求返回的数据
     //     *
     //     * @return
     //     */
    public static String TransToForward() {
        //获取临时连接所需要的接送对象
        JSONObject credential = UpAndDownUtil.getCredential();
        Map<String,String> map=new HashMap();
        //解析获取临时参数
        String s = credential.getString("credentials");
        JSONObject jsonObject = JSON.parseObject(s);
        String tmpSecretId = jsonObject.getString("tmpSecretId");
        String tmpSecretKey= jsonObject.getString("tmpSecretKey");
        String sessionToken= jsonObject.getString("sessionToken");
        map.put("sessionToken",sessionToken);
        //初始化用户身份信息
        BasicSessionCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);
        //设置bucket的地域信息
        String cosAccessKeyId = cred.getCOSAccessKeyId();
        Region region = new Region(REGION_NAME);
        ClientConfig clientConfig = new ClientConfig(region);
        //生成客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        //根据此对象获取认证信息
        COSSigner cosSigner=new COSSigner();
        //设置过期时间为半小时
        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        //使用post方式上传到”“”“‘/’目录下。
        String authorizationStr = cosSigner.buildAuthorizationStr(HttpMethodName.POST, "/",
                cred, expirationDate);
        map.put("Authorization",authorizationStr);
        System.out.println(authorizationStr);
        cosClient.shutdown();
        return JSON.toJSONString(map);
    }

    /**
     * 初始化客户端
     *
     * @return
     */
    public static COSClient initClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(REGION_NAME);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);

    }
    /**
     * 上传文件到指定存储桶
     *
     * @param path
     * @param
     * @param key        对象键，详情见上方说明
     * @param
     */
    public static void putFile(String path,String key) throws Exception {
        try {
            // 指定要上传的文件
            File localFile = new File(path);

            // 指定要上传到 COS 上对象键
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, key, localFile);
            PutObjectResult putObjectResult = COS_ClIENT.putObject(putObjectRequest);
        } catch (CosServiceException serverException) {
            throw new CosServiceException("服务错误");
        } catch (CosClientException clientException) {
            throw new CosClientException("客户端错误");
        }finally {
            COS_ClIENT.shutdown();
        }

    }

    /**
     * 获取桶中符合的对象集合，可以通过遍历来定位到制定对象
     *
     * @param
     * @param prefix    对象键的前缀
     * @param cosClient
     * @return
     */
    public static List<COSObjectSummary> getFileList( String prefix, COSClient cosClient) {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(BUCKET);
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
     * @param
     * @param key          对象键
     * @param downFilePath 下载到指定的路径
     * @param
     */
    public static void downFile(String key, String downFilePath) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, key);
            File downFile = new File(downFilePath);
            ObjectMetadata downObjectMeta = COS_ClIENT.getObject(getObjectRequest, downFile);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            COS_ClIENT.shutdown();
        }

    }


    /**
     * 说明：在业务场景中，删除应该是改变数据库里面的某个相对于显示的字段，进行逻辑删除，调用此方法是
     * 数据库中将此文件进行抹除
     * @param
     * @param key        对象键
     * @param
     */
    public static void delFile( String key) {
        try {
            COS_ClIENT.deleteObject(BUCKET, key);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }finally {
            COS_ClIENT.shutdown();
        }
    }
    public static String getKey(){
        //TODO 此时对象键的生成，是根据文件桶里面的路径。目前讨论的是根据项目来定义桶，根据公司新建文件夹,在公司文件夹下根据模块再定义文件夹。
        return null;
    }

}
