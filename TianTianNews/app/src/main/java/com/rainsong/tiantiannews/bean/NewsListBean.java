package com.rainsong.tiantiannews.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maxliaops on 17-2-13.
 */

public class NewsListBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getErrorCode() {
        return error_code;
    }

    public void setErrorCode(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean implements Serializable {
        private static final long serialVersionUID = 1L;
        private String stat;
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            private static final long serialVersionUID = 1L;
            /**
             * uniquekey : 9b78e8c27ccee4084b4a365aba13a7d4
             * title : 李冰冰现身机场 依偎在男友怀中变小鸟依人
             * date : 2017-02-13 14:47
             * category : 头条
             * author_name : 视觉中国
             * url : http://mini.eastday.com/mobile/170213144747807.html
             * thumbnail_pic_s : http://04.imgmini.eastday
             * .com/mobile/20170213/20170213144747_1ef12c63ed2583218a8a847776ca9241_1_mwpm_03200403.jpeg
             * thumbnail_pic_s02 : http://04.imgmini.eastday
             * .com/mobile/20170213/20170213144747_1ef12c63ed2583218a8a847776ca9241_2_mwpm_03200403.jpeg
             * thumbnail_pic_s03 : http://04.imgmini.eastday
             * .com/mobile/20170213/20170213144747_1ef12c63ed2583218a8a847776ca9241_3_mwpm_03200403.jpeg
             */

            private String uniquekey;
            private String title;
            private String date;
            private String category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthorName() {
                return author_name;
            }

            public void setAuthorName(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }
        }
    }
}
