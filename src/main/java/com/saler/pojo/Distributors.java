package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Distributors implements Serializable {
    @Column(name = "distributor_id")
    private String distributorId;

    @Column(name = "name_ce")
    private String nameCe;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "city_id")
    private String cityId;

    @Column(name = "province_id")
    private String provinceId;

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
     * @return distributor_id
     */
    public String getDistributorId() {
        return distributorId;
    }

    /**
     * @param distributorId
     */
    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    /**
     * @return name_ce
     */
    public String getNameCe() {
        return nameCe;
    }

    /**
     * @param nameCe
     */
    public void setNameCe(String nameCe) {
        this.nameCe = nameCe;
    }

    /**
     * @return name_en
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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
     * @return province_id
     */
    public String getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId
     */
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
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
        sb.append(", distributorId=").append(distributorId);
        sb.append(", nameCe=").append(nameCe);
        sb.append(", nameEn=").append(nameEn);
        sb.append(", cityId=").append(cityId);
        sb.append(", provinceId=").append(provinceId);
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