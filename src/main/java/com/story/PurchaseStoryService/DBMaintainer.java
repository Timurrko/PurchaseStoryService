package com.story.PurchaseStoryService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

interface PurchaseRepository extends CrudRepository<Purchase, String> {
}

interface CurrencyPairRepository extends CrudRepository<CurrencyPair, String> {
}

@EnableScheduling
@Component
public class DBMaintainer {
    private final PurchaseRepository purchaseRepository;
    @Autowired
    private final CurrencyServiceClient client;
    @Autowired
    private final CurrencyPairRepository currencyPairRepository;

    @Scheduled(fixedRate = 50000)
    public void AccessServiceAndUpdateDB() {
        System.out.println(1);
        Iterable<CurrencyPair> allStories = currencyPairRepository.findAll();
        System.out.println(allStories);
        List<Purchase> allPurchases = (List<Purchase>) purchaseRepository.findAll();
        List<String> allCurrencyServiceIds = new ArrayList<>();
        for (Purchase purchase: allPurchases) {
            allCurrencyServiceIds.add(purchase.getId());
        }

        for (CurrencyPair currencyPair : allStories) {
            for (Purchase purchase: client.getPurchaseStoryByCurrencyPair(currencyPair.getCurrencyPair())){
                if (!allCurrencyServiceIds.contains(purchase.getId())){
                    purchaseRepository.save(purchase);
                }
            }
        }
    }

    public List<Purchase> getPurchasesByCurrencyPairName(String currencyPairName){
        List<Purchase> purchases = (List<Purchase>) this.purchaseRepository.findAll();
        List<Purchase> fittingPurchases = new ArrayList<Purchase>();
        for (Purchase purchase: purchases) {
            String name = purchase.getCurrencyPair();
            if (Objects.equals(name, currencyPairName)){
                fittingPurchases.add(purchase);
            }
        }
        return fittingPurchases;
    }

    public void clearAll(){
        this.purchaseRepository.deleteAll();
        this.currencyPairRepository.deleteAll();
    }
    public List<Purchase> getPurchasesByCurrencyPairNameAndTimePeriodInSeconds(String currencyPairName, Long seconds){
        Date currentDate = new Date();
        long pastTime = (currentDate.getTime() / 1000 - seconds) * 1000;

        Date pastDate = new Date(pastTime);
        List<Purchase> purchases = this.getPurchasesByCurrencyPairName(currencyPairName);
        List<Purchase> fittingPurchases = new ArrayList<Purchase>() {};
        for (Purchase purchase: purchases){
            Date purchaseDate = purchase.getDate();
            if (purchaseDate.after(pastDate) && purchaseDate.before(currentDate)){
                fittingPurchases.add(purchase);
            }
        }
        return fittingPurchases;

    }

    public DBMaintainer(PurchaseRepository purchaseRepository, CurrencyPairRepository currencyPairRepository, CurrencyServiceClient client, @Value("${currencyPairsFile}") String currencyPairsFile, @Value("${spareCurrencyPairsFile}") String spareCurrencyPairsFile) {
        this.purchaseRepository = purchaseRepository;
        this.client = client;
        this.currencyPairRepository = currencyPairRepository;
        try {
            this.loadCurrencyPairNamesFromJSON(currencyPairsFile);
        } catch (FileNotFoundException e1) {
            try {
                this.loadCurrencyPairNamesFromJSON(spareCurrencyPairsFile);
            } catch (FileNotFoundException e2) {
                currencyPairRepository.save(new CurrencyPair("FNFETN"));
            }
        }
    }


    public void loadCurrencyPairNamesFromJSON(String currencyPairNamesFileName) throws FileNotFoundException {
        try (FileReader namesFile = new FileReader(currencyPairNamesFileName)) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Map<String, String>>> typeRef = new TypeReference<>() {
            };
            List<Map<String, String>> nameslist = mapper.readValue(namesFile, typeRef);
            for (Map<String, String> map : nameslist) {
                String name = map.get("currencyPair");
                this.addCurrencyPair(name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCurrencyPair(String currencyPairName) {
        List<CurrencyPair> currencyPairs = (List<CurrencyPair>) this.currencyPairRepository.findAll();
        List<String> names = new ArrayList<>();
        for (CurrencyPair currencyPair:currencyPairs){
            names.add(currencyPair.getCurrencyPair());
        }
        if (!names.contains(currencyPairName)){
            this.currencyPairRepository.save(new CurrencyPair(currencyPairName));
        }
    }
}
