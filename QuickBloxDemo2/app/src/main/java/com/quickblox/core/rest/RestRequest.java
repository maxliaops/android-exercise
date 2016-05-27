//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.core.rest;

import android.util.Pair;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.RestMethod;
import com.quickblox.core.helper.CollectionUtils;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.helper.StringUtils;
import com.quickblox.core.helper.ToStringHelper;
import com.quickblox.core.interfaces.QBCancelable;
import com.quickblox.core.request.MultipartEntity;
import com.quickblox.core.rest.HTTPDeleteTask;
import com.quickblox.core.rest.HTTPGetTask;
import com.quickblox.core.rest.HTTPPostTask;
import com.quickblox.core.rest.HTTPPutTask;
import com.quickblox.core.rest.HTTPTask;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.server.HttpRequestTask;
import com.quickblox.core.server.RestRequestCallback;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RestRequest implements QBCancelable {
    private QBProgressCallback progressCallback;
    private UUID uuid = UUID.randomUUID();
    private RestMethod method;
    private URL url;
    private Map<String, String> headers;
    private Map<String, Object> parameters;
    private MultipartEntity multiPartRest;
    private RestRequestCallback callback;
    private HttpRequestTask requestTask;
    private List<Pair<String, Object>> pairParameters;
    private boolean isDownloadFileRequest;

    public RestRequest() {
    }

    public static RestRequest create(String url, Map<String, String> headers, Map<String, Object> parameters, RestMethod method) {
        RestRequest request = new RestRequest();
        URL requestUrl = null;

        try {
            requestUrl = new URL(url);
        } catch (MalformedURLException var7) {
            var7.printStackTrace();
        }

        request.setUrl(requestUrl);
        request.setHeaders(headers);
        request.setParameters(parameters);
        request.setMethod(method);
        return request;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Map<String, Object> getParameters() {
        if(this.parameters == null) {
            this.parameters = new LinkedHashMap();
        }

        return this.parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public List<Pair<String, Object>> getPairParameters() {
        if(this.pairParameters == null) {
            this.pairParameters = new ArrayList();
        }

        return this.pairParameters;
    }

    public void setPairParameters(List<Pair<String, Object>> pairParameters) {
        this.pairParameters = pairParameters;
    }

    public Map<String, String> getHeaders() {
        if(this.headers == null) {
            this.headers = new LinkedHashMap();
        }

        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public RestMethod getMethod() {
        return this.method;
    }

    public void setMethod(RestMethod method) {
        this.method = method;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isDownloadFileRequest() {
        return this.isDownloadFileRequest;
    }

    public void setIsDownloadFileRequest(boolean isDownloadFileRequest) {
        this.isDownloadFileRequest = isDownloadFileRequest;
    }

    public void asyncRequestWithCallback(RestRequestCallback restRequestCallback) {
        this.callback = restRequestCallback;
        HTTPTask restTask = null;

        try {
            restTask = this.getHttpRequestRest();
        } catch (IOException var4) {
            Lo.g(var4);
        }

        this.requestTask = new HttpRequestTask();
        this.requestTask.setProgressCallback(this.progressCallback);
        this.requestTask.executeAsyncRest(this.callback, restTask, this.uuid, this.isDownloadFileRequest());
    }

    public RestResponse syncRequest() {
        HTTPTask restTask = null;

        try {
            restTask = this.getHttpRequestRest();
        } catch (IOException var3) {
            Lo.g(var3);
        }

        this.requestTask = new HttpRequestTask();
        this.requestTask.setProgressCallback(this.progressCallback);
        return this.requestTask.executeSyncRest(restTask, this.uuid, this.isDownloadFileRequest());
    }

    private HTTPTask getHttpRequestRest() throws IOException {
        if(this.method == null) {
            this.method = RestMethod.GET;
        }

        Object restTask = null;
        switch(this.method.ordinal()) {
        case 1:
            restTask = new HTTPGetTask(this.getFinalURL(), this.headers);
            break;
        case 2:
            restTask = new HTTPPostTask(this.getFinalURL(), this.headers);
            if(this.multiPartRest != null) {
                Map contentValues = this.multiPartRest.getPart();
                File fileUpload = this.multiPartRest.getUploadFile();
                String fieldName = this.multiPartRest.getFieldName();
                ((HTTPTask)restTask).setUploadFile(fileUpload, fieldName);
                ((HTTPTask)restTask).setFormBody(contentValues, (List)null);
                if(this.progressCallback != null) {
                    ((HTTPTask)restTask).setProgressCallback(this.progressCallback);
                }
            } else {
                ((HTTPTask)restTask).setFormBody(this.parameters, this.pairParameters);
            }
            break;
        case 3:
            restTask = new HTTPDeleteTask(this.getFinalURL(), this.headers);
            break;
        case 4:
            restTask = new HTTPPutTask(this.getFinalURL(), this.headers);
            ((HTTPTask)restTask).setFormBody(this.parameters, this.pairParameters);
        }

        return (HTTPTask)restTask;
    }

    public URL getFinalURL() {
        if(this.method != RestMethod.GET && this.method != RestMethod.DELETE) {
            return this.getUrl();
        } else {
            String urlWithParams = this.getUrlWithParamsString();

            try {
                return new URL(urlWithParams);
            } catch (MalformedURLException var3) {
                return null;
            }
        }
    }

    public String getParamsOnlyString() {
        return this.getEncodedParamsOnlyString(true);
    }

    public String getParamsOnlyStringNotEncoded() {
        return this.getEncodedParamsOnlyString(false);
    }

    private String getEncodedParamsOnlyString(boolean encoded) {
        StringBuilder paramsOnlyStringBuilder = new StringBuilder();
        if(this.parameters != null && this.parameters.size() > 0) {
            Iterator i$ = this.parameters.keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                String value = this.parameters.get(key).toString();
                if(value != null) {
                    String encodedValue = value;
                    if(encoded) {
                        try {
                            encodedValue = URLEncoder.encode(value, "UTF-8");
                        } catch (UnsupportedEncodingException var8) {
                            var8.printStackTrace();
                            encodedValue = value;
                        }
                    }

                    paramsOnlyStringBuilder.append(String.format("%s=%s&", new Object[]{key, encodedValue}));
                }
            }

            paramsOnlyStringBuilder.deleteCharAt(paramsOnlyStringBuilder.length() - 1);
        }

        this.putPairValuesToUrl(this.pairParameters, paramsOnlyStringBuilder);
        return paramsOnlyStringBuilder.toString();
    }

    private void putPairValuesToUrl(List<Pair<String, Object>> pairParameters, StringBuilder paramsOnlyStringBuilder) {
        if(!CollectionUtils.isEmpty(pairParameters)) {
            if(paramsOnlyStringBuilder.length() > 0) {
                paramsOnlyStringBuilder.append("&");
            }

            Iterator i$ = pairParameters.iterator();

            while(i$.hasNext()) {
                Pair item = (Pair)i$.next();
                String key = item.first.toString();
                String value = item.second.toString();
                if(value != null) {
                    String encodedValue;
                    try {
                        encodedValue = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException var11) {
                        var11.printStackTrace();
                        encodedValue = value;
                    }

                    try {
                        String encodedKey = URLEncoder.encode(key, "UTF-8");
                    } catch (UnsupportedEncodingException var10) {
                        var10.printStackTrace();
                    }

                    paramsOnlyStringBuilder.append(String.format("%s=%s&", new Object[]{key, encodedValue}));
                }
            }

            paramsOnlyStringBuilder.deleteCharAt(paramsOnlyStringBuilder.length() - 1);
        }

    }

    public String getUrlWithParamsString() {
        StringBuilder urlWithParams = new StringBuilder(this.getUrl().toString());
        String paramsOnlyString = this.getParamsOnlyString();
        if(!StringUtils.isEmpty(paramsOnlyString)) {
            urlWithParams.append("?");
        }

        return urlWithParams.append(paramsOnlyString).toString();
    }

    public String toString() {
        String tab = "    ";
        return String.format("=========================================================\n=== REQUEST ==== %s ===\nREQUEST\n    %s %s\nHEADERS\n%s\nPARAMETERS\n%s\nINLINE\n    %s %s", new Object[]{String.valueOf(this.uuid), this.method, this.getUrl().toString(), ToStringHelper.toString(this.getHeaders(), tab), ToStringHelper.toStringBuilder(this.getParameters(), tab, "\n").append(ToStringHelper.toStringPairList(this.getPairParameters(), tab)).toString(), this.method, this.getUrlWithParamsString()});
    }

    public void setMultiPartRest(MultipartEntity multiPartRest) {
        this.multiPartRest = multiPartRest;
    }

    public void cancel() {
        this.requestTask.cancel();
    }

    public void setProgressCallback(QBProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }
}
