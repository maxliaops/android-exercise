package com.rainsong.hireme.bean;

import java.io.Serializable;

/**
 * Created by maxliaops on 17-12-22.
 */

public class WechatInfo implements Serializable {

    /**
     * code : 0
     * msg : ok
     * data : {"status":5,"type":2,"msg":"看微信号：88元/次\n会员每天免费看5次","wechat_account":"http://image
     * .hiremeplz.com/upload/20160107/file_40Km
     * -F73_WEyFiDjwtvR5uv6OoqzpkVKgo_VwaMPueHkZGaw9NvLIE1RlPwulghx568e84bce99b0.jpg/900"}
     * login : 0
     */

    private int code;
    private String msg;
    private DataEntity data;
    private int login;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public static class DataEntity {
        /**
         * status : 5
         * type : 2
         * msg : 看微信号：88元/次
         会员每天免费看5次
         * wechat_account : http://image.hiremeplz
         * .com/upload/20160107/file_40Km
         * -F73_WEyFiDjwtvR5uv6OoqzpkVKgo_VwaMPueHkZGaw9NvLIE1RlPwulghx568e84bce99b0.jpg/900
         */

        private int status;
        private int type;
        private String msg;
        private String wechat_account;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getWechat_account() {
            return wechat_account;
        }

        public void setWechat_account(String wechat_account) {
            this.wechat_account = wechat_account;
        }
    }
}
