package com.company;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.logging.Level;

public class RequestData {
    WebDriver driver;

    ArrayList stockTickerArray = new ArrayList<String>();
    ArrayList<ArrayList<String>> arrayOfArrayOfStockData = new ArrayList<ArrayList<String>>();
    ArrayList<String> arrayOfStockData = new ArrayList<String>();

    // Database - Company Info Variables
    String holdingName = null;
    String holdingShares = null;
    String holdingMarketValue = null;

    String sharesInFund = null;
    int sharesInFundInt;
    int sharesOnPage;


    public ArrayList<ArrayList<String>> getYahooFinanceData(ArrayList<String> stockTickerArray) {
        this.stockTickerArray = stockTickerArray;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setBrowserName("htmlunit");
        driver = new HtmlUnitDriver(caps);

        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        WebDriver holdingsDriver = new HtmlUnitDriver(caps);

        for (String fundNumber : stockTickerArray) {
            try {
                holdingsDriver.quit();
                holdingsDriver = new HtmlUnitDriver(caps);
                holdingsDriver.get("https://personal.vanguard.com/us/FundsAllHoldings?FundId=" + fundNumber + "&FundIntExt=INT&tableName=Equity&tableIndex=0&sort=marketValue&sortOrder=desc&ps_disable_redirect=true");
            } catch (NoSuchSessionException e) {
                e.printStackTrace();
            }
            try {
                String sharesInFund = holdingsDriver.findElement(By.xpath("//*[@id=\"colMcontent\"]/table[1]/tbody/tr/th[1]/div/small/b")).getText();
                String start = StringUtils.substringBefore(sharesInFund, " ");
                sharesInFundInt = Integer.valueOf(start);
            } catch (NumberFormatException e) {
            }
            if (sharesInFundInt < 50) {
                sharesOnPage = sharesInFundInt;
            } else {
                sharesOnPage = 50;
            }
            for (int pageLoop = 0; pageLoop <= (int) Math.ceil(sharesInFundInt / 50); pageLoop++) {
                try {
                    driver.quit();
                    driver = new HtmlUnitDriver(caps);
                    driver.get("https://personal.vanguard.com/us/FundsAllHoldings?FundId=" + fundNumber + "&FundIntExt=INT&tableName=Equity&tableIndex=" + pageLoop + "&ps_disable_redirect=true");
                } catch (NoSuchSessionException e) {
                    e.printStackTrace();
                }

                if (pageLoop != 0 && pageLoop == (int) Math.floor(sharesInFundInt / 50)) {
                    sharesOnPage = sharesInFundInt - ((pageLoop) * 50);
                }

                for (int rowLoop = 2; rowLoop <= sharesOnPage + 1; rowLoop++) {
                    for (int columnLoop = 1; columnLoop < 3; columnLoop++) {
                        switch (columnLoop) {
                            case 1:
                                holdingName = driver.findElement(By.xpath("//*[@id=\"colMcontent\"]/table[2]/tbody/tr[" + rowLoop + "]/td[1]")).getText();
                            case 2:
                                holdingShares = driver.findElement(By.xpath("//*[@id=\"colMcontent\"]/table[2]/tbody/tr[" + rowLoop + "]/td[2]")).getText();
                            case 3:
                                holdingMarketValue = driver.findElement(By.xpath("//*[@id=\"colMcontent\"]/table[2]/tbody/tr[" + rowLoop + "]/td[3]")).getText();
                        }
                    }
                    holdingShares = holdingShares.replaceAll(",", "");
                    holdingMarketValue = holdingMarketValue.replaceAll("\\$", "");
                    holdingMarketValue = holdingMarketValue.replaceAll(",", "");

                    arrayOfStockData.add(fundNumber);
                    arrayOfStockData.add(holdingName);
                    arrayOfStockData.add(holdingShares);
                    arrayOfStockData.add(holdingMarketValue);

                    arrayOfArrayOfStockData.add(new ArrayList(arrayOfStockData));
                    arrayOfStockData.clear();

                    System.out.println(arrayOfStockData);
                    System.out.println(arrayOfArrayOfStockData);
                }
                System.out.println("Value of H is " + pageLoop);
            }

        }return arrayOfArrayOfStockData;
    }
}




