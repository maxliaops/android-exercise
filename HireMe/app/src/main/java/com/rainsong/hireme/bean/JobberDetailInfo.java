package com.rainsong.hireme.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maxliaops on 17-12-21.
 */

public class JobberDetailInfo implements Serializable {

    /**
     * code : 0
     * msg : ok
     * data : {"status":1,"msg":[{"id":"11535","user_id":"58748","city":"深圳","name":"杨蒴",
     * "age":"24","gender":2,"height":"170","job":"会计","status":"5","price":100,"scopes":",1,2",
     * "photo_1":"http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u
     * .jpg/900","la":"22.653668910354000","lo":"114.022775046590000","fast_rent_status":"3",
     * "introduce":"","receive_audio":"0","receive_video":"0","price_info":[{"price":"100",
     * "name":"约饭"},{"price":"100","name":"聊天"}],"jobber_id":"11535","wx_name":"","rent_num":5,
     * "last_time_desc":"19小时前在线","distance":"14公里内","fan":0,"vip":0,"jdl":"100%",
     * "code":"48683526","identify":0,"lable":[],"one_desc":"","img":["http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u
     * .jpg/900","http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn
     * .jpg/900","http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP
     * .jpg/900"],"rental_range":[{"id":"39764","name":"约饭","price":"100","pic":"",
     * "desc":"不想一个人吃晚餐，毕竟还是想找个人一起品尝美食。"},{"id":"39765","name":"聊天","price":"100","pic":"",
     * "desc":"我想和你看星星看月亮，从诗词歌赋聊到人生理想。"}],"lable_temper":[],
     * "lable_temper_xianshi":[{"lable_name":"每日穿搭","user_id":"58748","id":"3337",
     * "detail":[{"id":"7408","content":"","img_1":"http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP
     * .jpg/900"},{"id":"7407","content":"","img_1":"http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn
     * .jpg/900"},{"id":"7406","content":"","img_1":"http://image.hiremeplz
     * .com/upload/20161012
     * /file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u
     * .jpg/900"}]},{"lable_name":"吃货日记","user_id":"58748","id":"3336","detail":[]}],
     * "lable_temper_video":{"detail":[]},"wanted_thing":[],"wanted_question":[],
     * "promoter_img":[],"base_url":"http://img3.hiremeplz.com/",
     * "signStr":"sMzfhdo
     * +w4F8Dd4kaKvXIuvsPERhPTEwMDc0NzE5JmI9bGFpenV3b2JhJms9QUtJRFZxMzZ5ZXdRWFVGcElmaXB1c3RwWjhYa2xkUUdLODV3JmU9MTUxNDA5ODkxMCZ0PTE1MTM4Mzk3MTAmcj0xMzM2NDg5NjYwJmY9","weibo":"","tags":0,"weibo_id":"","weibo_name":"","weibo_head":"","auth_info":{"weibo":{"weibo_id":"","name":"","head":"","v":""},"visit_card":{"name":""}},"match_pair":0,"audio_chat_price":"1元/分钟","video_chat_price":"3元/分钟","zb":[],"chat_mode":0}],"name_type":"employer"}
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
         * status : 1
         * msg : [{"id":"11535","user_id":"58748","city":"深圳","name":"杨蒴","age":"24","gender":2,
         * "height":"170","job":"会计","status":"5","price":100,"scopes":",1,2",
         * "photo_1":"http://image.hiremeplz
         * .com/upload/20161012
         * /file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900","la":"22.653668910354000","lo":"114.022775046590000","fast_rent_status":"3","introduce":"","receive_audio":"0","receive_video":"0","price_info":[{"price":"100","name":"约饭"},{"price":"100","name":"聊天"}],"jobber_id":"11535","wx_name":"","rent_num":5,"last_time_desc":"19小时前在线","distance":"14公里内","fan":0,"vip":0,"jdl":"100%","code":"48683526","identify":0,"lable":[],"one_desc":"","img":["http://image.hiremeplz.com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900","http://image.hiremeplz.com/upload/20161012/file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn.jpg/900","http://image.hiremeplz.com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900"],"rental_range":[{"id":"39764","name":"约饭","price":"100","pic":"","desc":"不想一个人吃晚餐，毕竟还是想找个人一起品尝美食。"},{"id":"39765","name":"聊天","price":"100","pic":"","desc":"我想和你看星星看月亮，从诗词歌赋聊到人生理想。"}],"lable_temper":[],"lable_temper_xianshi":[{"lable_name":"每日穿搭","user_id":"58748","id":"3337","detail":[{"id":"7408","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900"},{"id":"7407","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn.jpg/900"},{"id":"7406","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900"}]},{"lable_name":"吃货日记","user_id":"58748","id":"3336","detail":[]}],"lable_temper_video":{"detail":[]},"wanted_thing":[],"wanted_question":[],"promoter_img":[],"base_url":"http://img3.hiremeplz.com/","signStr":"sMzfhdo+w4F8Dd4kaKvXIuvsPERhPTEwMDc0NzE5JmI9bGFpenV3b2JhJms9QUtJRFZxMzZ5ZXdRWFVGcElmaXB1c3RwWjhYa2xkUUdLODV3JmU9MTUxNDA5ODkxMCZ0PTE1MTM4Mzk3MTAmcj0xMzM2NDg5NjYwJmY9","weibo":"","tags":0,"weibo_id":"","weibo_name":"","weibo_head":"","auth_info":{"weibo":{"weibo_id":"","name":"","head":"","v":""},"visit_card":{"name":""}},"match_pair":0,"audio_chat_price":"1元/分钟","video_chat_price":"3元/分钟","zb":[],"chat_mode":0}]
         * name_type : employer
         */

        private int status;
        private String name_type;
        private List<MsgEntity> msg;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName_type() {
            return name_type;
        }

        public void setName_type(String name_type) {
            this.name_type = name_type;
        }

        public List<MsgEntity> getMsg() {
            return msg;
        }

        public void setMsg(List<MsgEntity> msg) {
            this.msg = msg;
        }

        public static class MsgEntity {
            /**
             * id : 11535
             * user_id : 58748
             * city : 深圳
             * name : 杨蒴
             * age : 24
             * gender : 2
             * height : 170
             * job : 会计
             * status : 5
             * price : 100
             * scopes : ,1,2
             * photo_1 : http://image.hiremeplz
             * .com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900
             * la : 22.653668910354000
             * lo : 114.022775046590000
             * fast_rent_status : 3
             * introduce :
             * receive_audio : 0
             * receive_video : 0
             * price_info : [{"price":"100","name":"约饭"},{"price":"100","name":"聊天"}]
             * jobber_id : 11535
             * wx_name :
             * rent_num : 5
             * last_time_desc : 19小时前在线
             * distance : 14公里内
             * fan : 0
             * vip : 0
             * jdl : 100%
             * code : 48683526
             * identify : 0
             * lable : []
             * one_desc :
             * img : ["http://image.hiremeplz
             * .com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900","http://image.hiremeplz.com/upload/20161012/file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn.jpg/900","http://image.hiremeplz.com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900"]
             * rental_range : [{"id":"39764","name":"约饭","price":"100","pic":"",
             * "desc":"不想一个人吃晚餐，毕竟还是想找个人一起品尝美食。"},{"id":"39765","name":"聊天","price":"100",
             * "pic":"","desc":"我想和你看星星看月亮，从诗词歌赋聊到人生理想。"}]
             * lable_temper : []
             * lable_temper_xianshi : [{"lable_name":"每日穿搭","user_id":"58748","id":"3337",
             * "detail":[{"id":"7408","content":"","img_1":"http://image.hiremeplz
             * .com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900"},{"id":"7407","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn.jpg/900"},{"id":"7406","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900"}]},{"lable_name":"吃货日记","user_id":"58748","id":"3336","detail":[]}]
             * lable_temper_video : {"detail":[]}
             * wanted_thing : []
             * wanted_question : []
             * promoter_img : []
             * base_url : http://img3.hiremeplz.com/
             * signStr :
             * sMzfhdo+w4F8Dd4kaKvXIuvsPERhPTEwMDc0NzE5JmI9bGFpenV3b2JhJms9QUtJRFZxMzZ5ZXdRWFVGcElmaXB1c3RwWjhYa2xkUUdLODV3JmU9MTUxNDA5ODkxMCZ0PTE1MTM4Mzk3MTAmcj0xMzM2NDg5NjYwJmY9
             * weibo :
             * tags : 0
             * weibo_id :
             * weibo_name :
             * weibo_head :
             * auth_info : {"weibo":{"weibo_id":"","name":"","head":"","v":""},
             * "visit_card":{"name":""}}
             * match_pair : 0
             * audio_chat_price : 1元/分钟
             * video_chat_price : 3元/分钟
             * zb : []
             * chat_mode : 0
             */

            private String id;
            private String user_id;
            private String city;
            private String name;
            private String age;
            private int gender;
            private String height;
            private String job;
            private String status;
            private int price;
            private String scopes;
            private String photo_1;
            private String la;
            private String lo;
            private String fast_rent_status;
            private String introduce;
            private String receive_audio;
            private String receive_video;
            private String jobber_id;
            private String wx_name;
            private int rent_num;
            private String last_time_desc;
            private String distance;
            private int fan;
            private int vip;
            private String jdl;
            private String code;
            private int identify;
            private String one_desc;
            private LableTemperVideoEntity lable_temper_video;
            private String base_url;
            private String signStr;
            private String weibo;
            private int tags;
            private String weibo_id;
            private String weibo_name;
            private String weibo_head;
            private AuthInfoEntity auth_info;
            private int match_pair;
            private String audio_chat_price;
            private String video_chat_price;
            private int chat_mode;
            private List<PriceInfoEntity> price_info;
            private List<?> lable;
            private List<String> img;
            private List<RentalRangeEntity> rental_range;
            private List<?> lable_temper;
            private List<LableTemperXianshiEntity> lable_temper_xianshi;
            private List<?> wanted_thing;
            private List<?> wanted_question;
            private List<?> promoter_img;
            private List<?> zb;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getScopes() {
                return scopes;
            }

            public void setScopes(String scopes) {
                this.scopes = scopes;
            }

            public String getPhoto_1() {
                return photo_1;
            }

            public void setPhoto_1(String photo_1) {
                this.photo_1 = photo_1;
            }

            public String getLa() {
                return la;
            }

            public void setLa(String la) {
                this.la = la;
            }

            public String getLo() {
                return lo;
            }

            public void setLo(String lo) {
                this.lo = lo;
            }

            public String getFast_rent_status() {
                return fast_rent_status;
            }

            public void setFast_rent_status(String fast_rent_status) {
                this.fast_rent_status = fast_rent_status;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getReceive_audio() {
                return receive_audio;
            }

            public void setReceive_audio(String receive_audio) {
                this.receive_audio = receive_audio;
            }

            public String getReceive_video() {
                return receive_video;
            }

            public void setReceive_video(String receive_video) {
                this.receive_video = receive_video;
            }

            public String getJobber_id() {
                return jobber_id;
            }

            public void setJobber_id(String jobber_id) {
                this.jobber_id = jobber_id;
            }

            public String getWx_name() {
                return wx_name;
            }

            public void setWx_name(String wx_name) {
                this.wx_name = wx_name;
            }

            public int getRent_num() {
                return rent_num;
            }

            public void setRent_num(int rent_num) {
                this.rent_num = rent_num;
            }

            public String getLast_time_desc() {
                return last_time_desc;
            }

            public void setLast_time_desc(String last_time_desc) {
                this.last_time_desc = last_time_desc;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public int getFan() {
                return fan;
            }

            public void setFan(int fan) {
                this.fan = fan;
            }

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public String getJdl() {
                return jdl;
            }

            public void setJdl(String jdl) {
                this.jdl = jdl;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getIdentify() {
                return identify;
            }

            public void setIdentify(int identify) {
                this.identify = identify;
            }

            public String getOne_desc() {
                return one_desc;
            }

            public void setOne_desc(String one_desc) {
                this.one_desc = one_desc;
            }

            public LableTemperVideoEntity getLable_temper_video() {
                return lable_temper_video;
            }

            public void setLable_temper_video(LableTemperVideoEntity lable_temper_video) {
                this.lable_temper_video = lable_temper_video;
            }

            public String getBase_url() {
                return base_url;
            }

            public void setBase_url(String base_url) {
                this.base_url = base_url;
            }

            public String getSignStr() {
                return signStr;
            }

            public void setSignStr(String signStr) {
                this.signStr = signStr;
            }

            public String getWeibo() {
                return weibo;
            }

            public void setWeibo(String weibo) {
                this.weibo = weibo;
            }

            public int getTags() {
                return tags;
            }

            public void setTags(int tags) {
                this.tags = tags;
            }

            public String getWeibo_id() {
                return weibo_id;
            }

            public void setWeibo_id(String weibo_id) {
                this.weibo_id = weibo_id;
            }

            public String getWeibo_name() {
                return weibo_name;
            }

            public void setWeibo_name(String weibo_name) {
                this.weibo_name = weibo_name;
            }

            public String getWeibo_head() {
                return weibo_head;
            }

            public void setWeibo_head(String weibo_head) {
                this.weibo_head = weibo_head;
            }

            public AuthInfoEntity getAuth_info() {
                return auth_info;
            }

            public void setAuth_info(AuthInfoEntity auth_info) {
                this.auth_info = auth_info;
            }

            public int getMatch_pair() {
                return match_pair;
            }

            public void setMatch_pair(int match_pair) {
                this.match_pair = match_pair;
            }

            public String getAudio_chat_price() {
                return audio_chat_price;
            }

            public void setAudio_chat_price(String audio_chat_price) {
                this.audio_chat_price = audio_chat_price;
            }

            public String getVideo_chat_price() {
                return video_chat_price;
            }

            public void setVideo_chat_price(String video_chat_price) {
                this.video_chat_price = video_chat_price;
            }

            public int getChat_mode() {
                return chat_mode;
            }

            public void setChat_mode(int chat_mode) {
                this.chat_mode = chat_mode;
            }

            public List<PriceInfoEntity> getPrice_info() {
                return price_info;
            }

            public void setPrice_info(List<PriceInfoEntity> price_info) {
                this.price_info = price_info;
            }

            public List<?> getLable() {
                return lable;
            }

            public void setLable(List<?> lable) {
                this.lable = lable;
            }

            public List<String> getImg() {
                return img;
            }

            public void setImg(List<String> img) {
                this.img = img;
            }

            public List<RentalRangeEntity> getRental_range() {
                return rental_range;
            }

            public void setRental_range(List<RentalRangeEntity> rental_range) {
                this.rental_range = rental_range;
            }

            public List<?> getLable_temper() {
                return lable_temper;
            }

            public void setLable_temper(List<?> lable_temper) {
                this.lable_temper = lable_temper;
            }

            public List<LableTemperXianshiEntity> getLable_temper_xianshi() {
                return lable_temper_xianshi;
            }

            public void setLable_temper_xianshi(List<LableTemperXianshiEntity> lable_temper_xianshi) {
                this.lable_temper_xianshi = lable_temper_xianshi;
            }

            public List<?> getWanted_thing() {
                return wanted_thing;
            }

            public void setWanted_thing(List<?> wanted_thing) {
                this.wanted_thing = wanted_thing;
            }

            public List<?> getWanted_question() {
                return wanted_question;
            }

            public void setWanted_question(List<?> wanted_question) {
                this.wanted_question = wanted_question;
            }

            public List<?> getPromoter_img() {
                return promoter_img;
            }

            public void setPromoter_img(List<?> promoter_img) {
                this.promoter_img = promoter_img;
            }

            public List<?> getZb() {
                return zb;
            }

            public void setZb(List<?> zb) {
                this.zb = zb;
            }

            public static class LableTemperVideoEntity {
                private List<?> detail;

                public List<?> getDetail() {
                    return detail;
                }

                public void setDetail(List<?> detail) {
                    this.detail = detail;
                }
            }

            public static class AuthInfoEntity {
                /**
                 * weibo : {"weibo_id":"","name":"","head":"","v":""}
                 * visit_card : {"name":""}
                 */

                private WeiboEntity weibo;
                private VisitCardEntity visit_card;

                public WeiboEntity getWeibo() {
                    return weibo;
                }

                public void setWeibo(WeiboEntity weibo) {
                    this.weibo = weibo;
                }

                public VisitCardEntity getVisit_card() {
                    return visit_card;
                }

                public void setVisit_card(VisitCardEntity visit_card) {
                    this.visit_card = visit_card;
                }

                public static class WeiboEntity {
                    /**
                     * weibo_id :
                     * name :
                     * head :
                     * v :
                     */

                    private String weibo_id;
                    private String name;
                    private String head;
                    private String v;

                    public String getWeibo_id() {
                        return weibo_id;
                    }

                    public void setWeibo_id(String weibo_id) {
                        this.weibo_id = weibo_id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getHead() {
                        return head;
                    }

                    public void setHead(String head) {
                        this.head = head;
                    }

                    public String getV() {
                        return v;
                    }

                    public void setV(String v) {
                        this.v = v;
                    }
                }

                public static class VisitCardEntity {
                    /**
                     * name :
                     */

                    private String name;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }

            public static class PriceInfoEntity {
                /**
                 * price : 100
                 * name : 约饭
                 */

                private String price;
                private String name;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class RentalRangeEntity {
                /**
                 * id : 39764
                 * name : 约饭
                 * price : 100
                 * pic :
                 * desc : 不想一个人吃晚餐，毕竟还是想找个人一起品尝美食。
                 */

                private String id;
                private String name;
                private String price;
                private String pic;
                private String desc;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }
            }

            public static class LableTemperXianshiEntity {
                /**
                 * lable_name : 每日穿搭
                 * user_id : 58748
                 * id : 3337
                 * detail : [{"id":"7408","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900"},{"id":"7407","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_zJ6TtZ9QAl5nLLSEvyLhiCpSSPOb6i65rDol3XC3J7RUi9Ze8aWRdL9w0n7w5E2XHQIbeKEn.jpg/900"},{"id":"7406","content":"","img_1":"http://image.hiremeplz.com/upload/20161012/file_20161012_6Tjfx8d7L9rGAbFQvchwrLN1j3DJNBFjVoZSWCq8bifcUksPWaMOmZg5tkfGlkqH9Q0vSQ4u.jpg/900"}]
                 */

                private String lable_name;
                private String user_id;
                private String id;
                private List<DetailEntity> detail;

                public String getLable_name() {
                    return lable_name;
                }

                public void setLable_name(String lable_name) {
                    this.lable_name = lable_name;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public List<DetailEntity> getDetail() {
                    return detail;
                }

                public void setDetail(List<DetailEntity> detail) {
                    this.detail = detail;
                }

                public static class DetailEntity {
                    /**
                     * id : 7408
                     * content :
                     * img_1 : http://image.hiremeplz.com/upload/20161012/file_20161012_hVH0v7F3H1klcUhDHPAdT2A1jJCXTHLB3T2Zq8syzdjcy0gFggizJj1ss3QMa1OElh3cPWaP.jpg/900
                     */

                    private String id;
                    private String content;
                    private String img_1;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }

                    public String getImg_1() {
                        return img_1;
                    }

                    public void setImg_1(String img_1) {
                        this.img_1 = img_1;
                    }
                }
            }
        }
    }
}
