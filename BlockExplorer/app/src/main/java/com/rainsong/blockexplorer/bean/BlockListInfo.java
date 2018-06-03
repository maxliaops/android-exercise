package com.rainsong.blockexplorer.bean;

import java.util.List;

/**
 * Created by maxliaops on 18-6-1.
 */

public class BlockListInfo {
    private int length;
    private Pagination pagination;
    private List<BlockInfo> blocks;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<BlockInfo> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockInfo> blocks) {
        this.blocks = blocks;
    }

    public static class Pagination {
        /**
         * next : 2018-06-02
         * prev : 2018-05-31
         * currentTs : 1527897599
         * current : 2018-06-01
         * isToday : true
         * more : false
         */

        private String next;
        private String prev;
        private int currentTs;
        private String current;
        private boolean isToday;
        private boolean more;

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getPrev() {
            return prev;
        }

        public void setPrev(String prev) {
            this.prev = prev;
        }

        public int getCurrentTs() {
            return currentTs;
        }

        public void setCurrentTs(int currentTs) {
            this.currentTs = currentTs;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public boolean isIsToday() {
            return isToday;
        }

        public void setIsToday(boolean isToday) {
            this.isToday = isToday;
        }

        public boolean isMore() {
            return more;
        }

        public void setMore(boolean more) {
            this.more = more;
        }
    }

    public static class BlockInfo {
        /**
         * height : 525382
         * size : 480319
         * hash : 000000000000000000327425c20590914d02ec2769eb2f25562d9e407d189c42
         * time : 1527821040
         * txlength : 1125
         * poolInfo : {"poolName":"AntMiner","url":"https://bitmaintech.com/"}
         */

        private int height;
        private int size;
        private String hash;
        private long time;
        private int txlength;
        private PoolInfo poolInfo;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTxlength() {
            return txlength;
        }

        public void setTxlength(int txlength) {
            this.txlength = txlength;
        }

        public PoolInfo getPoolInfo() {
            return poolInfo;
        }

        public void setPoolInfo(PoolInfo poolInfo) {
            this.poolInfo = poolInfo;
        }

        public static class PoolInfo {
            /**
             * poolName : AntMiner
             * url : https://bitmaintech.com/
             */

            private String poolName;
            private String url;

            public String getPoolName() {
                return poolName;
            }

            public void setPoolName(String poolName) {
                this.poolName = poolName;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
