package com.omgen.generator.builtin;

/**
 * Summary class containing all the info needed to build the typical method block in an OM.
 */
public class SetterMethod {
    private String name;
    private String shortName; // i.e., without the "set" in front
    private String argType;

    public SetterMethod() {
    }

    public SetterMethod(String name, String shortName, String argType) {
        this.name = name;
        this.shortName = shortName;
        this.argType = argType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType = argType;
    }
}
