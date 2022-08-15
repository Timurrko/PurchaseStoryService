package com.story.PurchaseStoryService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Purchase {
    @GeneratedValue
    @Id
    private long purchaseId;
    private String id;
    private String currencyPair;
    private Double price;
    private Date date;

}
