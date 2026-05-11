package com.mytravel;

public class Travel {
    private String destination;
    private String details;
    private String price;
    private String tClass;
    private String services;

    public Travel(String destination, String details, String price) {
        this(destination, details, price, "EXECUTIVE", "None");
    }

    public Travel(String destination, String details, String price, String tClass, String services) {
        this.destination = destination;
        this.details = details;
        this.price = price;
        this.tClass = tClass;
        this.services = services;
    }

    public String getDestination() { return destination; }
    public String getDetails() { return details; }
    public String getPrice() { return price; }
    public String getTClass() { return tClass; }
    public String getServices() { return services; }
}