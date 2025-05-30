package cn.hanabi.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets; // 使用 StandardCharsets 替代 "UTF-8" 字符串
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    /**
     * 获取一个配置了美观打印和禁用 HTML 转义的 Gson 实例。
     *
     * @return 配置好的 Gson 实例。
     */
    public static Gson gson() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    /**
     * 构建一个包含 Chrome 浏览器特征的 RequestConfig。
     *
     * @return 配置好的 RequestConfig 实例。
     */
    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(10000) // 设置连接主机服务超时时间
                .setConnectionRequestTimeout(10000) // 设置连接请求超时时间
                .setSocketTimeout(10000) // 设置读取数据连接超时时间
                .build();
    }

    /**
     * 为 HTTP 请求设置 Chrome 浏览器特征头部。
     *
     * @param request 可以是 HttpPost 或 HttpGet 实例。
     */
    private static void addChromeHeaders(org.apache.http.client.methods.HttpRequestBase request) {
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        request.setHeader("Accept-Encoding", "gzip, deflate, br");
        request.setHeader("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
        request.setHeader("Connection", "keep-alive");
    }

    /**
     * 发送 HTTP POST 请求，请求体为原始字符串。
     * 默认 Content-Type 为 application/json，如果请求体是 JSON 格式。
     *
     * @param url 请求的 URL。
     * @param body 请求体内容，通常为 JSON 字符串。
     * @return 响应体内容。
     * @throws RuntimeException 如果发生网络错误、协议错误或 HTTP 响应状态码非 2xx。
     */
    public static String post(String url, String body) {
        // 使用 try-with-resources 确保 HttpClient 和 HttpResponse 自动关闭
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig()); // 设置请求配置
            addChromeHeaders(httpPost); // 添加 Chrome 头部

            // 假设 body 是 JSON 格式，设置 Content-Type 为 application/json
            httpPost.setHeader("Content-Type", "application/json");

            StringEntity stringEntity = new StringEntity(body, StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                return handleResponse(httpResponse, url);
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("HTTP POST 请求协议错误，URL: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("HTTP POST 请求 IO 错误，URL: " + url, e);
        } catch (ParseException e) {
            throw new RuntimeException("HTTP POST 响应解析错误，URL: " + url, e);
        }
    }

    /**
     * 发送 HTTP POST 请求，请求体为 JsonObject。
     * 默认 Content-Type 为 application/json。
     *
     * @param url 请求的 URL。
     * @param jsonObject 请求体内容，JsonObject 实例。
     * @return 响应体内容。
     * @throws RuntimeException 如果发生网络错误、协议错误或 HTTP 响应状态码非 2xx。
     */
    public static String postURL(String url, JsonObject jsonObject) {
        // 使用 try-with-resources 确保 HttpClient 和 HttpResponse 自动关闭
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig()); // 设置请求配置
            addChromeHeaders(httpPost); // 添加 Chrome 头部

            // 设置 Content-Type 为 application/json，因为是发送 JsonObject
            httpPost.setHeader("Content-Type", "application/json");

            StringEntity stringEntity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                return handleResponse(httpResponse, url);
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("HTTP POST 请求协议错误 (JsonObject)，URL: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("HTTP POST 请求 IO 错误 (JsonObject)，URL: " + url, e);
        } catch (ParseException e) {
            throw new RuntimeException("HTTP POST 响应解析错误 (JsonObject)，URL: " + url, e);
        }
    }

    /**
     * 发送 HTTP POST 请求，请求体为 Map 形式的表单数据。
     * Content-Type 为 application/x-www-form-urlencoded。
     *
     * @param url 请求的 URL。
     * @param param 请求参数的 Map。
     * @return 响应体内容。
     * @throws RuntimeException 如果发生网络错误、协议错误或 HTTP 响应状态码非 2xx。
     */
    public static String postMAP(String url, Map<String, String> param) {
        // 使用 try-with-resources 确保 HttpClient 和 HttpResponse 自动关闭
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig()); // 设置请求配置
            addChromeHeaders(httpPost); // 添加 Chrome 头部

            // Content-Type 默认为 application/x-www-form-urlencoded，因为是表单数据
            // httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded"); // UrlEncodedFormEntity 会自动设置

            // 创建参数列表
            if (param != null && !param.isEmpty()) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                httpPost.setEntity(entity);
            }

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                return handleResponse(httpResponse, url);
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("HTTP POST 请求协议错误 (MAP)，URL: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("HTTP POST 请求 IO 错误 (MAP)，URL: " + url, e);
        } catch (ParseException e) {
            throw new RuntimeException("HTTP POST 响应解析错误 (MAP)，URL: " + url, e);
        }
    }

    /**
     * 发送 HTTP POST 请求，请求体为 JsonObject。
     * Content-Type 为 application/json。
     *
     * @param url 请求的 URL。
     * @param jsonObject 请求体内容，JsonObject 实例。
     * @return 响应体内容。
     * @throws RuntimeException 如果发生网络错误、协议错误或 HTTP 响应状态码非 2xx。
     */
    public static String postJSON(String url, JsonObject jsonObject) {
        // 使用 try-with-resources 确保 HttpClient 和 HttpResponse 自动关闭
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig()); // 设置请求配置
            addChromeHeaders(httpPost); // 添加 Chrome 头部

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json"); // 明确接受 JSON 响应

            StringEntity stringEntity = new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                return handleResponse(httpResponse, url);
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("HTTP POST 请求协议错误 (JSON)，URL: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("HTTP POST 请求 IO 错误 (JSON)，URL: " + url, e);
        } catch (ParseException e) {
            throw new RuntimeException("HTTP POST 响应解析错误 (JSON)，URL: " + url, e);
        }
    }


    /**
     * 根据 Map 构建 URL 查询字符串。
     *
     * @param url 基础 URL。
     * @param map 查询参数 Map。
     * @return 包含查询参数的完整 URL。
     */
    public static String buildUrl(String url, Map<String, String> map) {
        StringBuilder sb = new StringBuilder(url);
        if (map != null && !map.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1); // 删除最后一个 '&'
        }
        return sb.toString();
    }

    /**
     * 发送 HTTP GET 请求。
     *
     * @param url 请求的 URL。
     * @param headers 额外的请求头部信息，可以覆盖默认的 Chrome 头部。
     * @return 响应体内容。
     * @throws RuntimeException 如果发生网络错误、协议错误或 HTTP 响应状态码非 2xx。
     */
    public static String get(String url, Map<String, String> headers) {
        // 使用 try-with-resources 确保 HttpClient 和 HttpResponse 自动关闭
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(buildRequestConfig()); // 设置请求配置
            addChromeHeaders(httpGet); // 添加 Chrome 头部

            // 添加或覆盖用户提供的头部
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(httpGet::addHeader);
            }

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                return handleResponse(httpResponse, url);
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("HTTP GET 请求协议错误，URL: " + url, e);
        } catch (IOException e) {
            throw new RuntimeException("HTTP GET 请求 IO 错误，URL: " + url, e);
        } catch (ParseException e) {
            throw new RuntimeException("HTTP GET 响应解析错误，URL: " + url, e);
        }
    }

    /**
     * 处理 HTTP 响应，检查状态码并提取响应体。
     *
     * @param httpResponse CloseableHttpResponse 实例。
     * @param url 请求的 URL。
     * @return 响应体内容。
     * @throws RuntimeException 如果响应状态码非 2xx 或响应实体为空。
     * @throws IOException 如果读取响应实体时发生 IO 错误。
     * @throws ParseException 如果解析响应实体时发生解析错误。
     */
    private static String handleResponse(CloseableHttpResponse httpResponse, String url) throws IOException, ParseException {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) { // 2xx 表示成功
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            } else {
                throw new RuntimeException("HTTP 请求成功，但响应实体为空。URL: " + url);
            }
        } else {
            String errorResponse = "";
            if (httpResponse.getEntity() != null) {
                try {
                    errorResponse = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
                } catch (IOException | ParseException e) {
                    // 忽略解析错误，只记录原始错误信息
                    errorResponse = "无法解析错误响应体";
                }
            }
            throw new RuntimeException(
                    "HTTP 请求失败，状态码: " + statusCode + ", URL: " + url + ", 响应体: " + errorResponse);
        }
    }
}
