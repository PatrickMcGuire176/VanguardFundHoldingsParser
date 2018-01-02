package com.company;

import java.util.ArrayList;



public class Main {

    public static void main(String[] args) {
        ArrayList stockTickerArray = new ArrayList<String>();
        ArrayList<ArrayList<String>> arrayOfArrayOfStockData = new ArrayList<ArrayList<String>>();
        ArrayList<String> arrayOfStockData = new ArrayList<String>();

        ReadFile myFile = new ReadFile();
        stockTickerArray = myFile.ReadFile("C:/Users/McGuirePC/IdeaProjects/VanguardFundHoldingsParser/Middle Files/FundTickers.xlsx");

        RequestData yahooDataArray = new RequestData();
        arrayOfArrayOfStockData = yahooDataArray.getYahooFinanceData(stockTickerArray);

        System.out.println("Stock Data Array sent from main " + arrayOfArrayOfStockData);

        InsertDatabase insertDataToDatabase = new InsertDatabase();
        insertDataToDatabase.insertToDatabase(arrayOfArrayOfStockData);
    }
}

