package com.rainsong.blockexplorer.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(indexes = {
        @Index(value = "blockDate DESC", unique = true)
})
public class Block {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String blockDate;
    private String content;

    @Generated(hash = 1005137608)
    public Block(Long id, @NotNull String blockDate, String content) {
        this.id = id;
        this.blockDate = blockDate;
        this.content = content;
    }

    @Generated(hash = 1192728677)
    public Block() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockDate() {
        return this.blockDate;
    }

    public void setBlockDate(String blockDate) {
        this.blockDate = blockDate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
