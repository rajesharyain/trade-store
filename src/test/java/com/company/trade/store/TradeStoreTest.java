package com.company.trade.store;

import com.company.trade.constants.ExceptionMessage;
import com.company.trade.model.Trade;
import com.company.trade.utils.DateUtils;
import com.company.trade.utils.TradeBuilderUtils;
import org.junit.jupiter.api.*;

import java.util.Date;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradeStoreTest {

    TradeStore store = new TradeStore();

    @Test
    @Order(1)
    void testIfStoreIsEmpty() {
        boolean tradeEmpty = store.isTradeEmpty();
        Assertions.assertTrue(tradeEmpty);
    }


    @Test
    @Order(2)
    void addTrade() throws Exception{
        Trade t1 = TradeBuilderUtils.buildT1();
        store.addTrade(t1);
        Assertions.assertEquals(1, store.size());
    }


    @Test
    @Order(3)
    void testTradeExceptionIfLowerVersionRecieved() throws Exception{
        Trade t2V2 = TradeBuilderUtils.buildT2V2();
        store.addTrade(t2V2);

        //Create a trade with lower version
        Trade t2 = TradeBuilderUtils.buildT2();
        Exception exception = Assertions.assertThrows(Exception.class, () ->  store.addTrade(t2));
        String expectedMessage = ExceptionMessage.INVALID_MATURITY_DATE.getErrorMessage();
        String actualMessage = exception.getMessage();
        //verify if the exception messages are same
        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @Order(4)
    void testTradeIfSameVersionRecieved() throws Exception{
        Trade t1 = TradeBuilderUtils.buildT1();
        store.addTrade(t1);
        Assertions.assertEquals(1, store.getTrade(t1.getId()).getVersion());
        Assertions.assertEquals("B1", store.getTrade(t1.getId()).getBookId());

        //create a new trade with same version but with different book id
        Trade t2 = TradeBuilderUtils.buildT1();
        t2.setBookId("T1B1");
        store.addTrade(t2);

        Assertions.assertEquals(1, store.getTrade(t1.getId()).getVersion());
        Assertions.assertEquals("T1B1", store.getTrade(t1.getId()).getBookId());

    }

    @Test
    @Order(5)
    void testIfMaturityDateLessThanCurrentDate() throws Exception{
        Trade t3 = TradeBuilderUtils.buildT3();

        Exception exception = Assertions.assertThrows(Exception.class, () ->  store.addTrade(t3));
        String expectedMessage = ExceptionMessage.INVALID_MATURITY_DATE.getErrorMessage();
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @Order(6)
    void testTradeForUpdatingExpiredFlag() throws Exception{
        store.buildFakeTradeStore();
        Assertions.assertEquals(3, store.size());
        Assertions.assertEquals("20/05/2015", DateUtils.toString(store.getTrade("T2").getMaturityDate()));
        Assertions.assertEquals("20/05/2016", DateUtils.toString(store.getTrade("T3").getMaturityDate()));

        //check flags
        Assertions.assertEquals('N', store.getTrade("T2").getExpired());
        Assertions.assertEquals('Y', store.getTrade("T3").getExpired());

        store.updateTrades();

        Assertions.assertEquals('Y', store.getTrade("T2").getExpired());
        Assertions.assertEquals('Y', store.getTrade("T3").getExpired());

       /* Exception exception = Assertions.assertThrows(Exception.class, () ->  store.addTrade(t3));
        String expectedMessage = ExceptionMessage.INVALID_MATURITY_DATE.getErrorMessage();
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        Assertions.assertEquals(actualMessage, expectedMessage);*/

    }
}