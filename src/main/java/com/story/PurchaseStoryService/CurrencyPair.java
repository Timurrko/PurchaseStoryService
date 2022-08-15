package com.story.PurchaseStoryService;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CurrencyPair {
    @Id
    @GeneratedValue
    private long id;
    private String currencyPair;

    public CurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }
}