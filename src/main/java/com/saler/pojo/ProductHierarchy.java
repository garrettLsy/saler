package com.saler.pojo;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "product_hierarchy")
public class ProductHierarchy implements Serializable {
    @Column(name = "SKUID")
    private String skuid;

    @Column(name = "NAMECN")
    private String namecn;

    @Column(name = "NAMEEN")
    private String nameen;

    @Column(name = "Prooduct_Name_CN")
    private String prooductNameCn;

    @Column(name = "Prooduct_Name_EN")
    private String prooductNameEn;

    @Column(name = "TA_Name_EN")
    private String taNameEn;

    private static final long serialVersionUID = 1L;

    /**
     * @return SKUID
     */
    public String getSkuid() {
        return skuid;
    }

    /**
     * @param skuid
     */
    public void setSkuid(String skuid) {
        this.skuid = skuid;
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
     * @return Prooduct_Name_CN
     */
    public String getProoductNameCn() {
        return prooductNameCn;
    }

    /**
     * @param prooductNameCn
     */
    public void setProoductNameCn(String prooductNameCn) {
        this.prooductNameCn = prooductNameCn;
    }

    /**
     * @return Prooduct_Name_EN
     */
    public String getProoductNameEn() {
        return prooductNameEn;
    }

    /**
     * @param prooductNameEn
     */
    public void setProoductNameEn(String prooductNameEn) {
        this.prooductNameEn = prooductNameEn;
    }

    /**
     * @return TA_Name_EN
     */
    public String getTaNameEn() {
        return taNameEn;
    }

    /**
     * @param taNameEn
     */
    public void setTaNameEn(String taNameEn) {
        this.taNameEn = taNameEn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", skuid=").append(skuid);
        sb.append(", namecn=").append(namecn);
        sb.append(", nameen=").append(nameen);
        sb.append(", prooductNameCn=").append(prooductNameCn);
        sb.append(", prooductNameEn=").append(prooductNameEn);
        sb.append(", taNameEn=").append(taNameEn);
        sb.append("]");
        return sb.toString();
    }
}