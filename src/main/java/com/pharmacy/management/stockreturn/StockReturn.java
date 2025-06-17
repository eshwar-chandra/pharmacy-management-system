package com.pharmacy.management.stockreturn; // Updated package

import com.pharmacy.management.stock.Stock; // Updated import
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class StockReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Long returnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp returnDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "quantity_returned")
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
