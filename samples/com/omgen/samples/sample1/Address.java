package com.omgen.samples.sample1;

import java.util.List;

/**
 *
 */
public class Address {
    private String street1;
    private String street2;
    private String city;
    private String state;
    private List<String> zips;

    public Address() {}

    public Address(String state, List<String> zips) {
        this.state = state;
        this.zips = zips;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getZips() {
        return zips;
    }

    public void setZips(List<String> zips) {
        this.zips = zips;
    }
}
