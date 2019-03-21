package com.saler.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "Institution_AGNMapping")
public class InstitutionAgnmapping implements Serializable {
    @Column(name = "ID")
    private String id;

    @Column(name = "NEWCODE")
    private String newcode;

    @Column(name = "FORMERCODE")
    private String formercode;

    @Column(name = "MODIFYON")
    private Date modifyon;

    @Column(name = "CREATEON")
    private Date createon;

    private static final long serialVersionUID = 1L;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return NEWCODE
     */
    public String getNewcode() {
        return newcode;
    }

    /**
     * @param newcode
     */
    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    /**
     * @return FORMERCODE
     */
    public String getFormercode() {
        return formercode;
    }

    /**
     * @param formercode
     */
    public void setFormercode(String formercode) {
        this.formercode = formercode;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", newcode=").append(newcode);
        sb.append(", formercode=").append(formercode);
        sb.append(", modifyon=").append(modifyon);
        sb.append(", createon=").append(createon);
        sb.append("]");
        return sb.toString();
    }
}