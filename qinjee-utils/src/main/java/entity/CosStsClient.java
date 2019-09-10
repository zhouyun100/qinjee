/**
 * 文件名：CosStsClient
 * 工程名称：upanddown
 * <p>
 * qinjee
 * 创建日期：2019/8/1
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.cloud.STSPolicy;
import com.tencent.cloud.Scope;
import com.tencent.cloud.cos.util.Request;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CosStsClient {

    private static final int DEFAULT_DURATION_SECONDS = 1800;

    public CosStsClient() {
    }

    public static JSONObject getCredential(TreeMap<String, Object> config) throws IOException {
        TreeMap<String, Object> params = new TreeMap();
        Parameters parameters = new Parameters();
        parameters.parse(config);
        String policy = parameters.policy;
        if (policy != null) {
            params.put("Policy", policy);
        } else {
            params.put("Policy", getPolicy(parameters).toString());
        }

        params.put("DurationSeconds", parameters.duration);
        params.put("Name", "cos-sts-java");
        params.put("Action", "GetFederationToken");
        params.put("Version", "2018-08-13");
        params.put("Region", "ap-guangzhou");
        String host = "sts.tencentcloudapi.com";
        String path = "/";
        String result = null;

        try {
            result = Request.send(params, parameters.secretId, parameters.secretKey, "POST", host, path);
            JSONObject jsonResult = JSON.parseObject(result);
            JSONObject data = (JSONObject)jsonResult.get("Response");
            if (data == null) {
                data = jsonResult;
            }

            long expiredTime = data.getLong("ExpiredTime");
            data.put("startTime", expiredTime - (long)parameters.duration);
            return downCompat(data);
        } catch (Exception var11) {
            throw new IOException("result = " + result, var11);
        }
    }

    public static String getPolicy(List<Scope> scopes) {
        if (scopes != null && scopes.size() != 0) {
            STSPolicy stsPolicy = new STSPolicy();
            stsPolicy.addScope(scopes);
            return stsPolicy.toString();
        } else {
            return null;
        }
    }

    private static JSONObject downCompat(JSONObject resultJson) {
        JSONObject dcJson = new JSONObject();
        Iterator var3 = resultJson.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            Object value = resultJson.get(key);
            if (value instanceof JSONObject) {
                dcJson.put(headerToLowerCase(key), downCompat((JSONObject)value));
            } else {
                String newKey = "Token".equals(key) ? "sessionToken" : headerToLowerCase(key);
                dcJson.put(newKey, resultJson.get(key));
            }
        }

        return dcJson;
    }

    private static String headerToLowerCase(String source) {
        return Character.toLowerCase(source.charAt(0)) + source.substring(1);
    }

    private static JSONObject getPolicy(Parameters parameters) {
        String bucket = parameters.bucket;
        String region = parameters.region;
        String allowPrefix = parameters.allowPrefix;
        if (!allowPrefix.startsWith("/")) {
            allowPrefix = "/" + allowPrefix;
        }

        String[] allowActions = parameters.allowActions;
        JSONObject policy = new JSONObject();
        policy.put("version", "2.0");
        JSONObject statement = new JSONObject();
        statement.put("effect", "allow");
        JSONArray actions = new JSONArray();
        String[] var11 = allowActions;
        int var10 = allowActions.length;

        for(int var9 = 0; var9 < var10; ++var9) {
            String action = var11[var9];
            actions.add(action);
        }

        statement.put("action", actions);
        int lastSplit = bucket.lastIndexOf("-");
        String appId = bucket.substring(lastSplit + 1);
        String resource = String.format("qcs::cos:%s:uid/%s:%s%s", region, appId, bucket, allowPrefix);
        statement.put("resource", resource);
        policy.put("statement", statement);
        return policy;
    }

    private static class Parameters {
        String secretId;
        String secretKey;
        int duration;
        String bucket;
        String region;
        String allowPrefix;
        String[] allowActions;
        String policy;

        private Parameters() {
            this.duration = 1800;
        }

        public void parse(Map<String, Object> config) {
            if (config == null) {
                throw new NullPointerException("config == null");
            } else {
                Iterator var3 = config.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry)var3.next();
                    String key = (String)entry.getKey();
                    if ("SecretId".equalsIgnoreCase(key)) {
                        this.secretId = (String)entry.getValue();
                    } else if ("SecretKey".equalsIgnoreCase(key)) {
                        this.secretKey = (String)entry.getValue();
                    } else if ("durationSeconds".equalsIgnoreCase(key)) {
                        this.duration = (Integer)entry.getValue();
                    } else if ("bucket".equalsIgnoreCase(key)) {
                        this.bucket = (String)entry.getValue();
                    } else if ("region".equalsIgnoreCase(key)) {
                        this.region = (String)entry.getValue();
                    } else if ("allowPrefix".equalsIgnoreCase(key)) {
                        this.allowPrefix = (String)entry.getValue();
                    } else if ("policy".equalsIgnoreCase(key)) {
                        this.policy = (String)entry.getValue();
                    } else if ("allowActions".equalsIgnoreCase(key)) {
                        this.allowActions = (String[])entry.getValue();
                    }
                }

            }
        }
    }
}
