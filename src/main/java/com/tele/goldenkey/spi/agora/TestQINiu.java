//package com.tele.goldenkey.spi.agora;
//
//
//import com.google.gson.Gson;
//import com.qiniu.common.QiniuException;
//import com.qiniu.http.Response;
//import com.qiniu.storage.Configuration;
//import com.qiniu.storage.Region;
//import com.qiniu.storage.UploadManager;
//import com.qiniu.storage.model.DefaultPutRet;
//import com.qiniu.util.Auth;
//
//public class TestQINiu {
//    public static void main(String[] args) {
//        Configuration cfg = new Configuration(Region.regionNa0());
//        UploadManager uploadManager = new UploadManager(cfg);
//
//
//        String accessKey = "IDWmE1z9uVy0Svg8PyrW9w8ebshSTzMU40QXIdVk";
//        String secretKey = "-sBKxJdD-t1jq7qEtZdfX2pbvLOfnvORJ5MQXJGl";
//        String bucket = "tele-live";
//        String localFilePath = "D:\\u=1282019946,1074044209&fm=26&gp=0.jpg";
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(localFilePath, null, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }
//}
