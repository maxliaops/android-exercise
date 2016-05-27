//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.core;

import android.content.Context;
import android.text.TextUtils;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBPreferenceSettingsSaver;
import com.quickblox.core.QBSettingsSaver;
import com.quickblox.core.ServiceZone;
import com.quickblox.core.account.model.QBAccountSettings;
import com.quickblox.core.helper.Decorators;
import com.quickblox.core.helper.Lo;
import com.quickblox.core.helper.Utils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class QBSettings {
    private String versionName = "2.5.2";
    private String applicationId;
    private String authorizationKey;
    private String authorizationSecret;
    private LogLevel logLevel;
    private Map<ServiceZone, String> apiEndpointsMap;
    private Map<ServiceZone, String> chatEndpointsMap;
    private ServiceZone zone;
    private String contentBucketName;
    private String restApiVersion;
    static QBSettings qbSettings;
    private String accountKey;
    private QBSettings.AutoUpdateMode updateMode;
    private String chatDefaultResource;

    private QBSettings() {
        this.logLevel = LogLevel.DEBUG;
        this.apiEndpointsMap = new HashMap();
        this.chatEndpointsMap = new HashMap();
        this.zone = ServiceZone.AUTOMATIC;
        this.contentBucketName = "qbprod";
        this.restApiVersion = "0.1.1";
        this.chatDefaultResource = "android";
    }

    public static synchronized QBSettings getInstance() {
        if(qbSettings == null) {
            qbSettings = new QBSettings();
        }

        return qbSettings;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings fastConfigInit(String applicationId, String authorizationKey, String authorizationSecret) {
        Decorators.requireNonNull(applicationId, "applicationId must not be null");
        Decorators.requireNonNull(authorizationKey, "authorizationKey must not be null");
        Decorators.requireNonNull(authorizationSecret, "authorizationSecret must not be null");
        this.applicationId = applicationId;
        this.authorizationKey = authorizationKey;
        this.authorizationSecret = authorizationSecret;
        return this;
    }

    public QBSettings init(Context context, String applicationId, String authorizationKey, String authorizationSecret) {
        Decorators.requireNonNull(context, "context must not be null");
        Decorators.requireNonNull(applicationId, "applicationId must not be null");
        Decorators.requireNonNull(authorizationKey, "authorizationKey must not be null");
        Decorators.requireNonNull(authorizationSecret, "authorizationSecret must not be null");
        this.applicationId = applicationId;
        this.authorizationKey = authorizationKey;
        this.authorizationSecret = authorizationSecret;
        this.setupEndpointsRetrievalTimer(context.getApplicationContext());
        String androidId = Utils.generateDeviceId(context);
        if(!TextUtils.isEmpty(androidId)) {
            this.chatDefaultResource = this.chatDefaultResource + "_" + androidId;
        }

        return this;
    }

    /** @deprecated */
    public String getServerApiDomain() {
        return this.getApiEndpoint();
    }

    public String getApiEndpoint() {
        return (String)this.apiEndpointsMap.get(this.zone);
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setServerApiDomain(String serverDomain) {
        if(!serverDomain.contains("http")) {
            serverDomain = "https://" + serverDomain;
        }

        this.setApiEndpoint(serverDomain, ServiceZone.PRODUCTION);
        this.setZone(ServiceZone.PRODUCTION);
        return this;
    }

    /** @deprecated */
    @Deprecated
    public String getChatServerDomain() {
        return this.getChatEndpoint();
    }

    public String getChatEndpoint() {
        String chatEndpoint = (String)this.chatEndpointsMap.get(this.zone);
        if(TextUtils.isEmpty(chatEndpoint)) {
            Lo.g("There is no information about chat server endpoint, use the default one.");
            chatEndpoint = "chat.quickblox.com";
        }

        return chatEndpoint;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setChatServerDomain(String chatServerDomain) {
        this.setChatEndpoint(chatServerDomain, ServiceZone.PRODUCTION);
        this.setZone(ServiceZone.PRODUCTION);
        return this;
    }

    public QBSettings setEndpoints(String apiEndpoint, String chatEndpoint, ServiceZone zone) {
        if(zone == ServiceZone.AUTOMATIC) {
            throw new IllegalArgumentException("Automatic zone type should not be set manually.");
        } else {
            if(!apiEndpoint.startsWith("http")) {
                apiEndpoint = "https://" + apiEndpoint;
            }

            this.setApiEndpoint(apiEndpoint, zone);
            this.setChatEndpoint(chatEndpoint, zone);
            return this;
        }
    }

    public QBSettings setZone(ServiceZone zone) {
        this.zone = zone;
        this.invalidateEndpointsRetrievalTimer();
        return this;
    }

    public ServiceZone getZone() {
        return this.zone;
    }

    /** @deprecated */
    @Deprecated
    public String getContentBucketName() {
        return this.contentBucketName;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setContentBucketName(String contentBucketDomain) {
        this.contentBucketName = contentBucketDomain;
        return this;
    }

    public String getAuthorizationKey() {
        return this.authorizationKey;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setAuthorizationKey(String authKey) {
        this.authorizationKey = authKey;
        return this;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public String getAuthorizationSecret() {
        return this.authorizationSecret;
    }

    /** @deprecated */
    @Deprecated
    public QBSettings setAuthorizationSecret(String authSecret) {
        this.authorizationSecret = authSecret;
        return this;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public QBSettings setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public String getApiVersion() {
        return this.restApiVersion;
    }

    public QBSettings setApiVersion(String restApiVersion) {
        this.restApiVersion = restApiVersion;
        return this;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public String getAccountKey() {
        return this.accountKey;
    }

    public QBSettings setAccountKey(String accountKey) {
        this.accountKey = accountKey;
        return this;
    }

    public void checkInit() {
        Decorators.requireNonNullInRuntime(this.applicationId, "applicationId is null. You must call QBSettings.getInstance().init(Context, String, String, String) before using the QuickBlox library.");
        Decorators.requireNonNullInRuntime(this.authorizationKey, "authorizationKey is null. You must call QBSettings.getInstance().init(Context, String, String, String) before using the QuickBlox library.");
        Decorators.requireNonNullInRuntime(this.authorizationSecret, "authorizationSecret is null. You must call QBSettings.getInstance().init(Context, String, String, String) before using the QuickBlox library.");
        if(this.zone == ServiceZone.AUTOMATIC) {
            Decorators.requireNonNullInRuntime(this.accountKey, "accountKey is null. You must call QBSettings.getInstance().setAccountKey(String) before using the QuickBlox library. You can find desired value on your app settings page in QuickBlox Admin Panel (https://admin.quickblox.com/account), Settings tab.");
            Decorators.requireNonNullInRuntime(this.updateMode, "context is null. You must call QBSettings.getInstance().init(Context, String, String, String) before using the QuickBlox library.");
        } else {
            Decorators.requireNonNullInRuntime(getInstance().getApiEndpoint(), "There are no endpoints for zone " + getInstance().getZone() + ". You must call QBSettings.getInstance().setEndpoints(String, String, ServiceZone) " + "in a case of custom zone set.");
        }

    }

    public String getChatDefaultResource() {
        return this.chatDefaultResource;
    }

    private void setApiEndpoint(String apiEndpoint, ServiceZone zone) {
        this.apiEndpointsMap.put(zone, apiEndpoint);
    }

    private void setChatEndpoint(String chatEndpoint, ServiceZone zone) {
        this.chatEndpointsMap.put(zone, chatEndpoint);
    }

    private void setupEndpointsRetrievalTimer(Context context) {
        QBSettings.AutoUpdateMode updateMode = new QBSettings.AutoUpdateMode(context, this.getApplicationId());
        this.setUpdateMode(updateMode);
    }

    private void invalidateEndpointsRetrievalTimer() {
        this.setUpdateMode((QBSettings.AutoUpdateMode)null);
    }

    public String toString() {
        return "QBSettings{applicationId=" + this.applicationId + ", authorizationKey=\'" + this.authorizationKey + '\'' + ", authorizationSecret=\'" + this.authorizationSecret + '\'' + ", logLevel=" + this.logLevel + ", zone=\'" + this.zone + '\'' + ", apiEndpointsMap=\'" + this.apiEndpointsMap.toString() + '\'' + ", chatEndpointsMap=\'" + this.chatEndpointsMap.toString() + '\'' + ", restApiVersion=\'" + this.restApiVersion + '\'' + '}';
    }

    private void setUpdateMode(QBSettings.AutoUpdateMode updateMode) {
        this.updateMode = updateMode;
        if(updateMode != null) {
            updateMode.restoreSettings(this);
        }

    }

    public QBSettings.AutoUpdateMode getUpdateMode() {
        return this.updateMode;
    }

    private QBSettings setEndpoints(QBAccountSettings accountSettings) {
        this.setApiEndpoint(accountSettings.getApiEndpoint(), ServiceZone.AUTOMATIC);
        this.setChatEndpoint(accountSettings.getChatEndpoint(), ServiceZone.AUTOMATIC);
        this.setContentBucketName(accountSettings.getBucketName());
        return this;
    }

    public static final class AutoUpdateMode {
        protected long updateTimePeriod;
        protected Date lastUpdateTime;
        private QBSettingsSaver settingsSaver;

        private AutoUpdateMode() {
            this.updateTimePeriod = 0L;
            this.lastUpdateTime = new Date(0L);
            this.updateTimePeriod = TimeUnit.HOURS.toMillis(1L);
        }

        public AutoUpdateMode(Context context, String appIdentifier) {
            this();
            this.settingsSaver = new QBPreferenceSettingsSaver(context.getApplicationContext(), "QBSettings-" + appIdentifier);
        }

        void setLastUpdateTime(Date date) {
            this.lastUpdateTime = date;
        }

        public boolean isUpdatePeriodExpired() {
            return (new Date(this.lastUpdateTime.getTime() + this.updateTimePeriod)).before(new Date());
        }

        public void restoreSettings(QBSettings settings) {
            QBAccountSettings accountSettings = new QBAccountSettings();
            this.setLastUpdateTime(this.settingsSaver.restore(accountSettings));
            settings.setApiEndpoint(accountSettings.getApiEndpoint(), ServiceZone.AUTOMATIC);
            settings.setChatEndpoint(accountSettings.getChatEndpoint(), ServiceZone.AUTOMATIC);
            Lo.g("Restored custom endpoints. ApiEndpoint: " + accountSettings.getApiEndpoint() + ", ChatEndpoint: " + accountSettings.getChatEndpoint() + ". lastUpdateTime: " + this.lastUpdateTime);
        }

        void updateSettings(QBAccountSettings accountSettings, QBSettings settings) {
            this.setLastUpdateTime(new Date());
            settings.setEndpoints(accountSettings);
            this.settingsSaver.save(accountSettings, this.lastUpdateTime);
        }
    }
}
