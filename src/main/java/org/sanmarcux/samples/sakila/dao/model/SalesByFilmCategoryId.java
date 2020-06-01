package org.sanmarcux.samples.sakila.dao.model;

// Generated 20/10/2012 11:23:03 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * SalesByFilmCategoryId generated by hbm2java
 */
@Embeddable
public class SalesByFilmCategoryId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String category;
    private BigDecimal totalSales;

    public SalesByFilmCategoryId() {
    }

    public SalesByFilmCategoryId(String category) {
        this.category = category;
    }

    public SalesByFilmCategoryId(String category, BigDecimal totalSales) {
        this.category = category;
        this.totalSales = totalSales;
    }

    @Column(name = "category", nullable = false, length = 25)
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "total_sales", precision = 27)
    public BigDecimal getTotalSales() {
        return this.totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SalesByFilmCategoryId))
            return false;
        SalesByFilmCategoryId castOther = (SalesByFilmCategoryId) other;

        return ((this.getCategory() == castOther.getCategory()) || (this
                .getCategory() != null && castOther.getCategory() != null && this
                .getCategory().equals(castOther.getCategory())))
                && ((this.getTotalSales() == castOther.getTotalSales()) || (this
                .getTotalSales() != null
                && castOther.getTotalSales() != null && this
                .getTotalSales().equals(castOther.getTotalSales())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getCategory() == null ? 0 : this.getCategory().hashCode());
        result = 37
                * result
                + (getTotalSales() == null ? 0 : this.getTotalSales()
                .hashCode());
        return result;
    }

}