package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Hospitals implements Serializable {
    @Column(name = "HOSPITALID")
    private String hospitalid;

    @Column(name = "ACTUALUSEID")
    private String actualuseid;

    @Column(name = "Name")
    private String name;

    @Column(name = "NAMECN")
    private String namecn;

    @Column(name = "NAMEEN")
    private String nameen;

    @Column(name = "COMPLETENAME")
    private String completename;

    @Column(name = "TIER")
    private String tier;

    private String grade;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STYLE")
    private String style;

    @Column(name = "CITYID")
    private String cityid;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "POSTALCODE")
    private String postalcode;

    @Column(name = "NOOFBEDS")
    private Double noofbeds;

    @Column(name = "TELNO")
    private String telno;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "CITYNAME")
    private String cityname;

    private String provinceid;

    private String provincename;

    private Date modifyon;

    private String modifyby;

    private String createby;

    private Date createon;

    private Integer identifie;

    private static final long serialVersionUID = 1L;

    /**
     * @return HOSPITALID
     */
    public String getHospitalid() {
        return hospitalid;
    }

    /**
     * @param hospitalid
     */
    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }

    /**
     * @return ACTUALUSEID
     */
    public String getActualuseid() {
        return actualuseid;
    }

    /**
     * @param actualuseid
     */
    public void setActualuseid(String actualuseid) {
        this.actualuseid = actualuseid;
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
     * @return NAMECN
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
     * @return NAMEEN
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
     * @return COMPLETENAME
     */
    public String getCompletename() {
        return completename;
    }

    /**
     * @param completename
     */
    public void setCompletename(String completename) {
        this.completename = completename;
    }

    /**
     * @return TIER
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
     * @return TYPE
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
     * @return STYLE
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
     * @return CITYID
     */
    public String getCityid() {
        return cityid;
    }

    /**
     * @param cityid
     */
    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    /**
     * @return STATUS
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
     * @return POSTALCODE
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
     * @return NOOFBEDS
     */
    public Double getNoofbeds() {
        return noofbeds;
    }

    /**
     * @param noofbeds
     */
    public void setNoofbeds(Double noofbeds) {
        this.noofbeds = noofbeds;
    }

    /**
     * @return TELNO
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
     * @return ADDRESS
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
     * @return COMMENTS
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
     * @return DELETED
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
     * @return CITYNAME
     */
    public String getCityname() {
        return cityname;
    }

    /**
     * @param cityname
     */
    public void setCityname(String cityname) {
        this.cityname = cityname;
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
     * @return provincename
     */
    public String getProvincename() {
        return provincename;
    }

    /**
     * @param provincename
     */
    public void setProvincename(String provincename) {
        this.provincename = provincename;
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

    /**
     * @return identifie
     */
    public Integer getIdentifie() {
        return identifie;
    }

    /**
     * @param identifie
     */
    public void setIdentifie(Integer identifie) {
        this.identifie = identifie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", hospitalid=").append(hospitalid);
        sb.append(", actualuseid=").append(actualuseid);
        sb.append(", name=").append(name);
        sb.append(", namecn=").append(namecn);
        sb.append(", nameen=").append(nameen);
        sb.append(", completename=").append(completename);
        sb.append(", tier=").append(tier);
        sb.append(", grade=").append(grade);
        sb.append(", type=").append(type);
        sb.append(", style=").append(style);
        sb.append(", cityid=").append(cityid);
        sb.append(", status=").append(status);
        sb.append(", postalcode=").append(postalcode);
        sb.append(", noofbeds=").append(noofbeds);
        sb.append(", telno=").append(telno);
        sb.append(", address=").append(address);
        sb.append(", comments=").append(comments);
        sb.append(", deleted=").append(deleted);
        sb.append(", cityname=").append(cityname);
        sb.append(", provinceid=").append(provinceid);
        sb.append(", provincename=").append(provincename);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", modifyby=").append(modifyby);
        sb.append(", createby=").append(createby);
        sb.append(", createon=").append(createon);
        sb.append(", identifie=").append(identifie);
        sb.append("]");
        return sb.toString();
    }
}