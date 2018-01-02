package com.company;

import javax.xml.ws.http.HTTPException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by McGuirePC on 6/20/2017.
 */
public class InsertDatabase {
    //String connectionString = "jdbc:sqlserver://localhost:13175; user=test2; password=test; databaseName=StockData;";
    String connectionString = "jdbc:sqlserver://localhost:3579; user=test2; password=test; databaseName=StockData;";
    Date sysDate = new Date();
    String modifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sysDate);

    public void insertToDatabase(ArrayList<ArrayList<String>> arrayOfArrayOfStockData) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e)
        {
            System.out.println(e);
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            Statement stmt = connection.createStatement();
            // Insert string for Company Info tbl
            for (ArrayList myArray : arrayOfArrayOfStockData) {
                System.out.println(myArray);
                try {
                    String fundSQL =
                            "IF not exists(select a.fund_ID, b.fund_Holding_ID, c.companyInfoPK " +
                            "from tbl_Fund a  " +
                            "join dbo.tbl_fund_holding b  " +
                                    "on a.fund_id = b.fund_id  " +
                            "join dbo.tbl_company_info c  " +
                                    "on b.companyInfoPK = c.companyInfoPK " +
                            "where " +
                                    "fund_Number =" + myArray.get(0) + " " +
                                    "and companyDescription1='" + myArray.get(1) + "'" +
                                    "and shares =" + myArray.get(2) + " " +
                                    "and  market_value =" + myArray.get(3) + ")" +

                            "BEGIN " +
                            "BEGIN " +

                            "update dbo.tbl_fund_holding " +
                            "set end_dt = (select sysdatetime()) " +
                            "where fund_holding_id = (" +
                                    "select top 1 fund_holding_id " +
                                    "from dbo.tbl_fund_holding a  " +
                                    "join dbo.tbl_fund b " +
                                        "on a.fund_id = b.fund_id  " +
                                    "join dbo.tbl_company_info c  " +
                                        "on a.companyInfoPK = c.companyInfoPK " +
                                    "where " +
                                        "fund_number=" + myArray.get(0) + " " +
                                        "and companyDescription1='" + myArray.get(1) + "'" +
                                        "order by start_dt desc) " +

                            "END " +
                            "BEGIN " +

                            "insert into dbo.tbl_Fund_Holding (fund_Holding_ID, fund_Id, start_dt, shares, market_value, companyInfoPK) " +
                            "VALUES(newid(), (select fund_ID from tbl_Fund where fund_Number =" + myArray.get(0) + "  ), (select SYSDATETIME()), " + myArray.get(2) + "  ,   " + myArray.get(3) + " , (select companyInfoPK from dbo.tbl_Company_Info a where companydescription1 =  '" + myArray.get(1) + "')) " +
                            "END " +
                            "END";
                    stmt.executeUpdate(fundSQL);
                    System.out.println("Successful fundSQL insert");
                } catch (HTTPException | NullPointerException e) {
                    System.out.println("Catch 8");
                    System.out.println(e.getMessage());
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Index was out of bounds " + e);
                    System.out.print("The stock symbol was " + myArray.get(0));
                    continue;
                } catch (SQLException err1) {
                    continue;
                }
            }
            stmt.close();

        } catch (SQLException err1) {
            System.out.println("Catch 10");
            System.out.println(err1.getMessage());

        } finally {

        }
    }
}

