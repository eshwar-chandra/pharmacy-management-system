package com.pharmacy.management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "stock_returns")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "soft_delete = false")
public class StockReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Long returnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock; // The specific stock entry being returned

    @Column(name = "return_date") // Defaults to CURRENT_TIMESTAMP in DB schema
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp returnDate;

    // Adding reason and quantityReturned as they are common for returns
    @Column(name = "reason")
    private String reason;

    @Column(name = "quantity_returned") // If returning a portion of the stock entry
    private Integer quantityReturned;


    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "soft_delete")
    private boolean softDelete = false;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
        if (returnDate == null) {
            returnDate = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
