# VanguardFundHoldingsParser

Project to Extract, Parse, and Load Vanguard Fund Holdings from Vanguard Website.

Steps in Project
1. Read in Fund Numbers from excel file
2. Request Vanguard Holdings Page through Selenium Server using HTMLUnit Driver
3. Parse Page DOM to extract Holding Name, Shares, and Market Value
4. Load data into SQLServer DB
