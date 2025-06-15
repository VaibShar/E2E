package com.bill.BillGenerator.dto;

public class FuelBillRequest {
    public String header1;
    public String header2;
    public String header3;
    public String carNumber;
    public String phoneNumber;
    public String type;
    public String totalPrice;
    public String price;
    public String totalFuel;
    public String paymentMode; // CASH
    public String quantity;    // e.g., "77"
    public String nozzle;      // e.g., "4"
    public String pumpNo;      // e.g., "4"
    public String product;     // e.g., "Unleaded"
    public String trxId;
    public String receiptId;
}
