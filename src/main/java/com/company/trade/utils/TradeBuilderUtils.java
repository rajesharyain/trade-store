package com.company.trade.utils;

import com.company.trade.model.Trade;
import com.company.trade.store.TradeStore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TradeBuilderUtils {

    public static Trade buildTrade(String id, int version, String cp,
                                   String bookId, Date maturityDate, Date createdDate,
                                   boolean expired) throws Exception {
        return Trade.builder()
                .id(id)
                .version(version)
                .counterPartyId(cp)
                .bookId(bookId)
                .maturityDate(maturityDate)
                .createdDate(createdDate)
                .expired(expired)
                .build();
    }

    public static Trade buildT1() throws Exception {
        return Trade.builder()
                .id("T1")
                .version(1)
                .counterPartyId("CP-1")
                .bookId("B1")
                .maturityDate(DateUtils.fromString("20/05/2023"))
                .createdDate(new Date())
                .expired(false)
                .build();
    }

    public static Trade buildT2() throws Exception {
        return Trade.builder()
                .id("T2")
                .version(1)
                .counterPartyId("CP-1")
                .bookId("B1")
                .maturityDate(DateUtils.fromString("20/05/2023"))
                .createdDate(DateUtils.fromString("14/03/2015"))
                .expired(false)
                .build();
    }

    public static Trade buildT2V2() throws Exception {
        return Trade.builder()
                .id("T2")
                .version(2)
                .counterPartyId("CP-2")
                .bookId("B1")
                .maturityDate(DateUtils.fromString("20/05/2023"))
                .createdDate(new Date())
                .expired(false)
                .build();
    }

    public static Trade buildT3() throws Exception {
        return Trade.builder()
                .id("T3")
                .version(3)
                .counterPartyId("CP-3")
                .bookId("B2")
                .maturityDate(DateUtils.fromString("20/05/2014"))
                .createdDate(new Date())
                .expired(true)
                .build();
    }
}
