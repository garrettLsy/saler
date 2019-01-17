package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Distributors implements Serializable {
    private String distributorid;

    @Column(name = "Name")
    private String name;

    private String namecn;

    private String nameen;

    @Column(name = "city_id")
    private String cityId;

    private String provinceid;

    private String comments;

    private Integer deleted;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "province_name")
    private String provinceName;

    private Date modifyon;

    private String modifyby;

    private String createby;

    private Date createon;

    private static final long serialVersionUID = 1L;

    /**
     * @return distributorid
     */
    public String getDistributorid() {
        return distributorid;
    }

    /**
     * @param distributorid
     */
    public void setDistributorid(String distributorid) {
        this.distributorid = distributorid;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return namecn
     */
    public String getNamecn() {
        return namecn;
    }

    /**
     * @param namecn
     */
    public void setNamecn(String namecn) {
        this.namecn = namecn;
    }

    /**
     * @return nameen
     */
    public String getNameen() {
        return nameen;
    }

    /**
     * @param nameen
     */
    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    /**
     * @return city_id
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * @param cityId
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * @return provinceid
     */
    public String getProvinceid() {
        return provinceid;
    }

    /**
     * @param provinceid
     */
    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    /**
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return deleted
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * @param deleted
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * @return city_name
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return province_name
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * @param provinceName
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * @return modifyon
     */
    public Date getModifyon() {
        return modifyon;
    }

    /**
     * @param modifyon
     */
    public void setModifyon(Date modifyon) {
        this.modifyon = modifyon;
    }

    /**
     * @return modifyby
     */
    public String getModifyby() {
        return modifyby;
    }

    /**
     * @param modifyby
     */
    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    /**
     * @return createby
     */
    public String getCreateby() {
        return createby;
    }

    /**
     * @param createby
     */
    public void setCreateby(String createby) {
        this.createby = createby;
    }

    /**
     * @return createon
     */
    public Date getCreateon() {
        return createon;
    }

    /**
     * @param createon
     */
    public void setCreateon(Date createon) {
        this.createon = createon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", distributorid=").append(distributorid);
        sb.append(", name=").append(name);
        sb.append(", namecn=").append(namecn);
        sb.append(", nameen=").append(nameen);
        sb.append(", cityId=").append(cityId);
        sb.append(", provinceid=").append(provinceid);
        sb.append(", comments=").append(comments);
        sb.append(", deleted=").append(deleted);
        sb.append(", cityName=").append(cityName);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", modifyby=").append(modifyby);
        sb.append(", createby=").append(createby);
        sb.append(", createon=").append(createon);
        sb.append("]");
        return sb.toString();
    }
}