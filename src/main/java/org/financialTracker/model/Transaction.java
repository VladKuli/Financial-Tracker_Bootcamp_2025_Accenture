package org.financialTracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Table(name = "transactions")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal amount;
    private Date date = new Date();
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private TransactionType transactionType;

    @PreRemove
    public void preRemove() {
        if (user != null) {
            // This will ensure that the 'Transaction' is removed from the 'User' collection
            user.getExpens().remove(this);  // Adjust this based on the actual relationship.
            this.user = null;  // Nullify the back-reference to avoid unexpected behavior.
        }
    }
}
