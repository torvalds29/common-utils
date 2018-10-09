package com.po;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiaoqian on 2015/5/21.
 */
@MappedSuperclass
abstract public class BaseDomain implements Serializable {

/*    @Id
    @GenericGenerator(name = "PKUUID", strategy = "uuid")
    @GeneratedValue(generator = "PKUUID")
    @Column(length = 32)
    protected String id;*/

    /**
     * 创建日期
     */
    @Column(name = "date_created")
    private Date dateCreated;

    /**
     * 最后更新日期
     */
    @Column(name = "last_updated")
    private Date lastUpdated;

    @Column(name = "version")
    private Integer version;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    private Date deleteDate;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        if (isDelete != null && isDelete)
            this.deleteDate = new Date();
        this.isDelete = isDelete;
    }

    @PrePersist
    protected void prePersist() {
        dateCreated = new Date();
        version = 1;
    }

    @PreUpdate
    private void preUpdate() {
        lastUpdated = new Date();
        if (version == null) {
            version = 1;
        } else {
            version++;
        }
    }
}
