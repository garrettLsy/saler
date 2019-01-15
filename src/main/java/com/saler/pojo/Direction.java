package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

public class Direction implements Serializable {
    private String mon;

    @Column(name = "dist_id")
    private String distId;

    @Column(name = "dist_name")
    private String distName;

    private String style;

    @Column(name = "province_name_cn")
    private String provinceNameCn;

    @Column(name = "pharm_id")
    private String pharmId;

    @Column(name = "pharm_name_ce")
    private String pharmNameCe;

    @Column(name = "hospital_id")
    private String hospitalId;

    private String hospital;

    @Column(name = "sales_date")
    private Date salesDate;

    private String brand;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_name_en")
    private String productNameEn;

    private Integer qty;

    @Column(name = "product_id")
    private String productId;

    private String version;

    private String type;

    @Column(name = "amap_id")
    private String amapId;

    @Column(name = "group_name")
    private String groupName;

    private static final long serialVersionUID = 1L;

    /**
     * @return mon
     */
    public String getMon() {
        return mon;
    }

    /**
     * @param mon
     */
    public void setMon(String mon) {
        this.mon = mon;
    }

    /**
     * @return dist_id
     */
    public String getDistId() {
        return distId;
    }

    /**
     * @param distId
     */
    public void setDistId(String distId) {
        this.distId = distId;
    }

    /**
     * @return dist_name
     */
    public String getDistName() {
        return distName;
    }

    /**
     * @param distName
     */
    public void setDistName(String distName) {
        this.distName = distName;
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
     * @return province_name_cn
     */
    public String getProvinceNameCn() {
        return provinceNameCn;
    }

    /**
     * @param provinceNameCn
     */
    public void setProvinceNameCn(String provinceNameCn) {
        this.provinceNameCn = provinceNameCn;
    }

    /**
     * @return pharm_id
     */
    public String getPharmId() {
        return pharmId;
    }

    /**
     * @param pharmId
     */
    public void setPharmId(String pharmId) {
        this.pharmId = pharmId;
    }

    /**
     * @return pharm_name_ce
     */
    public String getPharmNameCe() {
        return pharmNameCe;
    }

    /**
     * @param pharmNameCe
     */
    public void setPharmNameCe(String pharmNameCe) {
        this.pharmNameCe = pharmNameCe;
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
     * @return hospital
     */
    public String getHospital() {
        return hospital;
    }

    /**
     * @param hospital
     */
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    /**
     * @return sales_date
     */
    public Date getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate
     */
    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return product_name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return product_name_en
     */
    public String getProductNameEn() {
        return productNameEn;
    }

    /**
     * @param productNameEn
     */
    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    /**
     * @return qty
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * @return product_id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
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
     * @return amap_id
     */
    public String getAmapId() {
        return amapId;
    }

    /**
     * @param amapId
     */
    public void setAmapId(String amapId) {
        this.amapId = amapId;
    }

    /**
     * @return group_name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mon=").append(mon);
        sb.append(", distId=").append(distId);
        sb.append(", distName=").append(distName);
        sb.append(", style=").append(style);
        sb.append(", provinceNameCn=").append(provinceNameCn);
        sb.append(", pharmId=").append(pharmId);
        sb.append(", pharmNameCe=").append(pharmNameCe);
        sb.append(", hospitalId=").append(hospitalId);
        sb.append(", hospital=").append(hospital);
        sb.append(", salesDate=").append(salesDate);
        sb.append(", brand=").append(brand);
        sb.append(", productName=").append(productName);
        sb.append(", productNameEn=").append(productNameEn);
        sb.append(", qty=").append(qty);
        sb.append(", productId=").append(productId);
        sb.append(", version=").append(version);
        sb.append(", type=").append(type);
        sb.append(", amapId=").append(amapId);
        sb.append(", groupName=").append(groupName);
        sb.append("]");
        return sb.toString();
    }
}