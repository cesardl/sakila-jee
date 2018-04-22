package org.sanmarcux.samples.sboot.sakila.dao.model;

// Generated 20/10/2012 11:23:03 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.*;

/**
 * SalesByFilmCategory generated by hbm2java
 */
@Entity
@Table(name = "sales_by_film_category", catalog = "sakila")
public class SalesByFilmCategory implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private SalesByFilmCategoryId id;

    public SalesByFilmCategory() {
    }

    public SalesByFilmCategory(SalesByFilmCategoryId id) {
        this.id = id;
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "category", column = @Column(name = "category", nullable = false, length = 25)),
            @AttributeOverride(name = "totalSales", column = @Column(name = "total_sales", precision = 27))})
    public SalesByFilmCategoryId getId() {
        return this.id;
    }

    public void setId(SalesByFilmCategoryId id) {
        this.id = id;
    }

}
