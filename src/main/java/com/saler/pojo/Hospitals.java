package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Hospitals implements Serializable {
    @Column(name = "hospital_id")
    private String hospitalId;

    @Column(name = "name_cn")
    private String nameCn;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "complete_name")
    private String completeName;

    private String tier;

    private String grade;

    private String type;

    private String style;

    @Column(name = "city_id")
    private String cityId;

    private String status;

    private String postalcode;

    private Integer noofbeds;

    private String telno;

    private String address;

    private String comments;

    private Integer deleted;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "province_name")
    private String provinceName;

    private Date modifyon;

    private String modifyby;

    private String createby;

    private Date createon;

    private static final long serialVersionUID = 1L;

    /**
     * @return hospital_id
     */
    public String getHospitalId() {
        return hospitalId;
    }

    /**
     * @param hospitalId
     */
    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    /**
     * @return name_cn
     */
    public String getNameCn() {
        return nameCn;
    }

    /**
     * @param nameCn
     */
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
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
     * @return complete_name
     */
    public String getCompleteName() {
        return completeName;
    }

    /**
     * @param completeName
     */
    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    /**
     * @return tier
     */
    public String getTier() {
        return tier;
    }

    /**
     * @param tier
     */
    public void setTier(String tier) {
        this.tier = tier;
    }

    /**
     * @return grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     */
    public void setStyle(String style) {
        this.style = style;
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
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * @param postalcode
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    /**
     * @return noofbeds
     */
    public Integer getNoofbeds() {
        return noofbeds;
    }

    /**
     * @param noofbeds
     */
    public void setNoofbeds(Integer noofbeds) {
        this.noofbeds = noofbeds;
    }

    /**
     * @return telno
     */
    public String getTelno() {
        return telno;
    }

    /**
     * @param telno
     */
    public void setTelno(String telno) {
        this.telno = telno;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
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
        sb.append(", hospitalId=").append(hospitalId);
        sb.append(", nameCn=").append(nameCn);
        sb.append(", nameEn=").append(nameEn);
        sb.append(", completeName=").append(completeName);
        sb.append(", tier=").append(tier);
        sb.append(", grade=").append(grade);
        sb.append(", type=").append(type);
        sb.append(", style=").append(style);
        sb.append(", cityId=").append(cityId);
        sb.append(", status=").append(status);
        sb.append(", postalcode=").append(postalcode);
        sb.append(", noofbeds=").append(noofbeds);
        sb.append(", telno=").append(telno);
        sb.append(", address=").append(address);
        sb.append(", comments=").append(comments);
        sb.append(", deleted=").append(deleted);
        sb.append(", cityName=").append(cityName);
        sb.append(", provinceId=").append(provinceId);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", modifyby=").append(modifyby);
        sb.append(", createby=").append(createby);
        sb.append(", createon=").append(createon);
        sb.append("]");
        return sb.toString();
    }
}