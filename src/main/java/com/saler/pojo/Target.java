package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;

public class Target implements Serializable {
    private Integer id;

    private Date period;

    private String hospitalid;

    private String productid;

    private Double targetunit;

    private Date modifyon;

    private String modifyby;

    private String createby;

    private Date createon;

    private Integer deleted;

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
     * @return hospitalid
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
     * @return productid
     */
    public String getProductid() {
        return productid;
    }

    /**
     * @param productid
     */
    public void setProductid(String productid) {
        this.productid = productid;
    }

    /**
     * @return targetunit
     */
    public Double getTargetunit() {
        return targetunit;
    }

    /**
     * @param targetunit
     */
    public void setTargetunit(Double targetunit) {
        this.targetunit = targetunit;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", period=").append(period);
        sb.append(", hospitalid=").append(hospitalid);
        sb.append(", productid=").append(productid);
        sb.append(", targetunit=").append(targetunit);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", modifyby=").append(modifyby);
        sb.append(", createby=").append(createby);
        sb.append(", createon=").append(createon);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }
}