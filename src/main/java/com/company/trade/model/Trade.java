package com.company.trade.model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
    private String id;
    private int version;
    private String counterPartyId;
    private String bookId;
    private Date maturityDate;
    private Date createdDate;
    private boolean expired;

    public char getExpired(){
        return expired ? 'Y' : 'N';
    }
}
