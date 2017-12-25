package com.rainsong.hireme.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maxliaops on 17-12-20.
 */

public class HireFragmentInfo implements Serializable {
    private static final long serialVersionUID = 10L;
    /**
     * code : 0
     * msg : ok
     * data : {"status":1,"data":{"notification":"1","member_count":"4103","join_price":199,
     * "member":[{"user_id":"7222","name":"单只蝴蝶","image":"http://image.hiremeplz
     * .com/upload/20160413
     * /file_20160413_zeTbEsLEgXXXjyVFwSrfmuNv0fJQYIVYmfzq8LvP8TdSRyXNQPtCJGyamHqLPM9Cs9t0kofT_compress.jpeg/80","code":"40082149"},{"user_id":"2005","name":"Sherry","image":"http://image.hiremeplz.com/upload/20160822/file_20160822_aRbPNNNpBlQiuD5EHl7pjDKDcSsrnbOXtqdGDqwFcNn6QSbXEIMoMXrofkg2VuqOl4VoUSux.jpg/80","code":0},{"user_id":"7719","name":"莯晓寒","image":"http://image.hiremeplz.com/upload/20150921/file_bOPBAOGMx_CnRZeyAcHl5LY3EiFFv61uKQR_FFINVO_DFvFHtnp7iMf0n3h9zvmO56001941cbdf9.jpg/80","code":"82764546"}],"role":"0","group_list":[{"id":"224","name":"深圳出租人快速接单群","photo":"http://image.hiremeplz.com/20170930135732SovGJU38rbUPJAJ6qOeo0mDcrMjs47lm/80"}]}}
     * login : 0
     */

    private int code;
    private String msg;
    private DataEntityX data;
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

    public DataEntityX getData() {
        return data;
    }

    public void setData(DataEntityX data) {
        this.data = data;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public static class DataEntityX {
        /**
         * status : 1
         * data : {"notification":"1","member_count":"4103","join_price":199,
         * "member":[{"user_id":"7222","name":"单只蝴蝶","image":"http://image.hiremeplz
         * .com/upload/20160413
         * /file_20160413_zeTbEsLEgXXXjyVFwSrfmuNv0fJQYIVYmfzq8LvP8TdSRyXNQPtCJGyamHqLPM9Cs9t0kofT_compress.jpeg/80","code":"40082149"},{"user_id":"2005","name":"Sherry","image":"http://image.hiremeplz.com/upload/20160822/file_20160822_aRbPNNNpBlQiuD5EHl7pjDKDcSsrnbOXtqdGDqwFcNn6QSbXEIMoMXrofkg2VuqOl4VoUSux.jpg/80","code":0},{"user_id":"7719","name":"莯晓寒","image":"http://image.hiremeplz.com/upload/20150921/file_bOPBAOGMx_CnRZeyAcHl5LY3EiFFv61uKQR_FFINVO_DFvFHtnp7iMf0n3h9zvmO56001941cbdf9.jpg/80","code":"82764546"}],"role":"0","group_list":[{"id":"224","name":"深圳出租人快速接单群","photo":"http://image.hiremeplz.com/20170930135732SovGJU38rbUPJAJ6qOeo0mDcrMjs47lm/80"}]}
         */

        private int status;
        private DataEntity data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public DataEntity getData() {
            return data;
        }

        public void setData(DataEntity data) {
            this.data = data;
        }

        public static class DataEntity {
            /**
             * notification : 1
             * member_count : 4103
             * join_price : 199
             * member : [{"user_id":"7222","name":"单只蝴蝶","image":"http://image.hiremeplz
             * .com/upload/20160413/file_20160413_zeTbEsLEgXXXjyVFwSrfmuNv0fJQYIVYmfzq8LvP8TdSRyXNQPtCJGyamHqLPM9Cs9t0kofT_compress.jpeg/80","code":"40082149"},{"user_id":"2005","name":"Sherry","image":"http://image.hiremeplz.com/upload/20160822/file_20160822_aRbPNNNpBlQiuD5EHl7pjDKDcSsrnbOXtqdGDqwFcNn6QSbXEIMoMXrofkg2VuqOl4VoUSux.jpg/80","code":0},{"user_id":"7719","name":"莯晓寒","image":"http://image.hiremeplz.com/upload/20150921/file_bOPBAOGMx_CnRZeyAcHl5LY3EiFFv61uKQR_FFINVO_DFvFHtnp7iMf0n3h9zvmO56001941cbdf9.jpg/80","code":"82764546"}]
             * role : 0
             * group_list : [{"id":"224","name":"深圳出租快速接单群","photo":"http://image.hiremeplz
             * .com/20170930135732SovGJU38rbUPJAJ6qOeo0mDcrMjs47lm/80"}]
             */

            private String notification;
            private String member_count;
            private int join_price;
            private String role;
            private List<MemberEntity> member;
            private List<GroupListEntity> group_list;

            public String getNotification() {
                return notification;
            }

            public void setNotification(String notification) {
                this.notification = notification;
            }

            public String getMember_count() {
                return member_count;
            }

            public void setMember_count(String member_count) {
                this.member_count = member_count;
            }

            public int getJoin_price() {
                return join_price;
            }

            public void setJoin_price(int join_price) {
                this.join_price = join_price;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public List<MemberEntity> getMember() {
                return member;
            }

            public void setMember(List<MemberEntity> member) {
                this.member = member;
            }

            public List<GroupListEntity> getGroup_list() {
                return group_list;
            }

            public void setGroup_list(List<GroupListEntity> group_list) {
                this.group_list = group_list;
            }

            public static class MemberEntity {
                /**
                 * user_id : 7222
                 * name : 单只蝴蝶
                 * image : http://image.hiremeplz
                 * .com/upload/20160413/file_20160413_zeTbEsLEgXXXjyVFwSrfmuNv0fJQYIVYmfzq8LvP8TdSRyXNQPtCJGyamHqLPM9Cs9t0kofT_compress.jpeg/80
                 * code : 40082149
                 */

                private String user_id;
                private String name;
                private String image;
                private String code;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }
            }

            public static class GroupListEntity {
                /**
                 * id : 224
                 * name : 深圳出租快速接单群
                 * photo : http://image.hiremeplz.com/20170930135732SovGJU38rbUPJAJ6qOeo0mDcrMjs47lm/80
                 */

                private String id;
                private String name;
                private String photo;

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

                public String getPhoto() {
                    return photo;
                }

                public void setPhoto(String photo) {
                    this.photo = photo;
                }
            }
        }
    }
}
