package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Bloc implements Serializable {
    private Integer id;

    private Date period;

    @Column(name = "hospital_id")
    private String hospitalId;

    private String segmentation;

    private String chain;

    @Column(name = "chain_en")
    private String chainEn;

    @Column(name = "MODIFYON")
    private Date modifyon;

    @Column(name = "MODIFYBY")
    private String modifyby;

    @Column(name = "CREATEBY")
    private String createby;

    @Column(name = "CREATEON")
    private Date createon;

    @Column(name = "DELETED")
    private Integer deleted;

    @Column(name = "TAID")
    private String taid;

    private String type;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return period
     */
    public Date getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(Date period) {
        this.period = period;
    }

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
     * @return segmentation
     */
    public String getSegmentation() {
        return segmentation;
    }

    /**
     * @param segmentation
     */
    public void setSegmentation(String segmentation) {
        this.segmentation = segmentation;
    }

    /**
     * @return chain
     */
    public String getChain() {
        return chain;
    }

    /**
     * @param chain
     */
    public void setChain(String chain) {
        this.chain = chain;
    }

    /**
     * @return chain_en
     */
    public String getChainEn() {
        return chainEn;
    }

    /**
     * @param chainEn
     */
    public void setChainEn(String chainEn) {
        this.chainEn = chainEn;
    }

    /**
     * @return MODIFYON
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
     * @return MODIFYBY
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
     * @return CREATEBY
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
     * @return CREATEON
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
     * @return TAID
     */
    public String getTaid() {
        return taid;
    }

    /**
     * @param taid
     */
    public void setTaid(String taid) {
        this.taid = taid;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", period=").append(period);
        sb.append(", hospitalId=").append(hospitalId);
        sb.append(", segmentation=").append(segmentation);
        sb.append(", chain=").append(chain);
        sb.append(", chainEn=").append(chainEn);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", modifyby=").append(modifyby);
        sb.append(", createby=").append(createby);
        sb.append(", createon=").append(createon);
        sb.append(", deleted=").append(deleted);
        sb.append(", taid=").append(taid);
        sb.append(", type=").append(type);
        sb.append("]");
        return sb.toString();
    }
}