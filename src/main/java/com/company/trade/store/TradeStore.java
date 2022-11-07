package com.company.trade.store;

import com.company.trade.constants.ExceptionMessage;
import com.company.trade.model.Trade;
import com.company.trade.utils.DateUtils;
import com.company.trade.utils.TradeBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TradeStore {
    private final Map<String, Trade> tradeStore = new HashMap<>();

    public boolean isTradeEmpty(){
        return tradeStore.isEmpty();
    }

    public int size(){
        return tradeStore.size();
    }

    public void addTrade(Trade newTrade) throws Exception{
        Trade currentTrade = newTrade;
        if(tradeStore.containsKey(newTrade.getId())) {
            currentTrade = tradeStore.get(newTrade.getId());
        }
        verifyVersion(newTrade.getVersion(), currentTrade.getVersion());
        boolean isCrossedMaturityDate = verifyMaturityDate(currentTrade.getMaturityDate());
        if(!isCrossedMaturityDate) {
            tradeStore.put(newTrade.getId(), newTrade);
            log.info("trade {} is created in the store!", newTrade.getId());
        }else{
            log.info("Unable to add {} in the store as maturity date " +
                    "is lower than the current date!", newTrade.getId());
            throw new Exception(ExceptionMessage.INVALID_MATURITY_DATE.getErrorMessage());
        }
    }

    public Trade getTrade(String tradeId) throws Exception{
        if(tradeStore.containsKey(tradeId)){
            return tradeStore.get(tradeId);
        }
        throw new Exception(tradeId + " does not exists in the Trade Store!");
    }

    public void verifyVersion(int newVersion, int currentVersion) throws Exception{
        if(newVersion < currentVersion){
            throw new Exception(ExceptionMessage.INVALID_MATURITY_DATE.getErrorMessage());
        }
    }

    public boolean verifyMaturityDate(Date currentMaturityDate) {
        return currentMaturityDate.before(new Date());
    }

    public void logTradeStore(){
        tradeStore.values().forEach(trade -> log.info(trade.toString()));
    }

    /**
     * Description: Cron job runs every 5 seconds on the trade store
     * Update trade expired flag to "Y" if trade croses maturity date
     */

    @Scheduled(cron="*/5 * * * * ?")
    public void updateTrades(){
        //print all the trades
        log.info("Trades before processing");
        logTradeStore();

        //process trades and set expiry flag to Y if trade croses the maturitydate
        for (String tradeId : tradeStore.keySet()) {
            Trade trade = tradeStore.get(tradeId);
            if (verifyMaturityDate(trade.getMaturityDate())) { //maturity date crossed
                trade.setExpired(true);
                tradeStore.replace(tradeId, trade);
            }
        }

        log.info("Trades after processing");
        logTradeStore();
    }

    /**
     * Build fake tradeStore with crossed maturity date
     */
    public void buildFakeTradeStore() {
        try{
            Trade t1 = TradeBuilderUtils.buildT1();
            tradeStore.put(t1.getId(), t1);
            Trade t2 = TradeBuilderUtils.buildT2();
            t2.setMaturityDate(DateUtils.fromString("20/05/2015"));
            tradeStore.put(t2.getId(), t2);

            Trade t3 = TradeBuilderUtils.buildT3();
            t3.setMaturityDate(DateUtils.fromString("20/05/2016"));
            tradeStore.put(t3.getId(), t3);

        }catch (Exception ex) {
            ex.printStackTrace();
        }




    }
}
