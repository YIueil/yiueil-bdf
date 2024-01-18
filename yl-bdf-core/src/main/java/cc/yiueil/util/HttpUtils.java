package cc.yiueil.util;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * HttpUtils 网络请求工具类
 *
 * @author 弋孓 YIueil@163.com
 * @version 1.0
 * @date 2024/1/18 22:14
 */
public class HttpUtils {
    private static volatile OkHttpClient client = null;
    private static final Object LOCK = new Object();

    private HttpUtils() {
    }

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (LOCK) {
                if (client == null) {
                    client = new OkHttpClient().newBuilder()
                            // 连接超时时间 60秒
                            .connectTimeout(60, TimeUnit.SECONDS)
                            // 读取超时时间 5分钟
                            .readTimeout(5 * 60, TimeUnit.SECONDS)
                            // 读取超时时间 5分钟
                            .writeTimeout(5 * 60, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return client;
    }

    /**
     * 基于OkHttp封装的get请求方法
     *
     * @param url    请求地址
     * @param params params参数
     * @param header header请求头参数
     * @return 执行结果
     * @throws IOException IO异常
     */
    public static String doGet(String url, Map<String, String> params, Map<String, String> header) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = buildHeader(requestBuilder, header)
                .url(buildUrl(url, params))
                .build();
        return getResultBodyString(request);
    }

    /**
     * 基于OkHttp封装的post方法 适用于RequestBody为formData
     *
     * @param url      请求地址
     * @param params   params参数
     * @param header   header请求头参数
     * @param formData formData 表单数据
     * @return 请求结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, String> params, Map<String, Object> formData, Map<String, String> header) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = buildHeader(requestBuilder, header)
                .url(buildUrl(url, params))
                .post(buildRequestBody(formData))
                .build();
        return getResultBodyString(request);
    }

    /**
     * 基于OkHttp封装的post方法 适用于RequestBody为raw并且请求体application/json
     *
     * @param url        请求地址
     * @param params     params参数
     * @param header     header请求头参数
     * @param jsonString JsonString字符串
     * @return 请求结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, String> params, String jsonString, Map<String, String> header) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Request request = buildHeader(requestBuilder, header)
                .url(buildUrl(url, params))
                .post(RequestBody.create(jsonString, JSON))
                .build();
        return getResultBodyString(request);
    }

    @NotNull
    private static String getResultBodyString(Request request) throws IOException {
        try (Response response = getInstance().newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    return "";
                }
                return body.string();

            }
            throw new IOException("Unexpected code " + response);
        }
    }

    private static RequestBody buildRequestBody(Map<String, Object> formData) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (formData != null) {
            for (Map.Entry<String, Object> entry : formData.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof File) {
                    File file = (File) value;
                    RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/png"));
                    requestBodyBuilder.addFormDataPart(entry.getKey(), file.getName(), requestBody);
                } else if (value instanceof byte[]) {
                    byte[] bytes = (byte[]) value;
                    RequestBody requestBody = RequestBody.create(bytes, MediaType.parse("image/png"));
                    requestBodyBuilder.addFormDataPart(entry.getKey(), UUID.randomUUID().toString().replaceAll("-", ""), requestBody);
                } else {
                    requestBodyBuilder.addFormDataPart(entry.getKey(), String.valueOf(value));
                }
            }
        }
        return requestBodyBuilder.build();
    }

    private static Request.Builder buildHeader(Request.Builder requestBuilder, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return requestBuilder;
    }

    private static HttpUrl buildUrl(String url, Map<String, String> params) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new RuntimeException("异常URL: " + url);
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return urlBuilder.build();
    }
}
