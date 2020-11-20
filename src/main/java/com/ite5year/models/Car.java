package com.ite5year.models;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private int seatsNumber;
    private LocalDateTime dateOfSale;
    private double priceOfSale;

    public Car() {
    }

    public Car(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Car(String name, double price, int seatsNumber, LocalDateTime dateOfSale, double priceOfSale) {
        this.name = name;
        this.price = price;
        this.seatsNumber = seatsNumber;
        this.dateOfSale = dateOfSale;
        this.priceOfSale = priceOfSale;
    }

    public Car(Long id, String name, double price, int seatsNumber, LocalDateTime dateOfSale, double priceOfSale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.seatsNumber = seatsNumber;
        this.dateOfSale = dateOfSale;
        this.priceOfSale = priceOfSale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(int seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public LocalDateTime getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDateTime dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public double getPriceOfSale() {
        return priceOfSale;
    }

    public void setPriceOfSale(double priceOfSale) {
        this.priceOfSale = priceOfSale;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", seatsNumber=" + seatsNumber +
                ", dateOfSale=" + dateOfSale +
                ", priceOfSale=" + priceOfSale +
                '}';
    }
}
