package com.omgen.samples.sample1;

import java.util.List;

/**
 *
 */
public class Customer1 {
    private String name;
    private Long id;
    private long primitiveId;
    private String[] aliases;
    private List<Address> addresses;
    private Address[] addressArray;
    private List numbers;
    private List<String> antiAliases;
    private Address address;

    public List<String> getAntiAliases() {
        return antiAliases;
    }

    public void setAntiAliases(List<String> antiAliases) {
        this.antiAliases = antiAliases;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address[] getAddressArray() {
        return addressArray;
    }

    public void setAddressArray(Address[] addressArray) {
        this.addressArray = addressArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPrimitiveId() {
        return primitiveId;
    }

    public void setPrimitiveId(long primitiveId) {
        this.primitiveId = primitiveId;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List getNumbers() {
        return numbers;
    }

    public void setNumbers(List numbers) {
        this.numbers = numbers;
    }
}
