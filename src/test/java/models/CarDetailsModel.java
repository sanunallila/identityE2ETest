package models;

public class CarDetailsModel {
    public String variantReg;
    public String make;
    public String model;
    public String year;

    public String getVariantReg() {
        return variantReg;
    }

    public CarDetailsModel(String variantReg, String make, String model, String year) {
        this.variantReg = variantReg;
        this.make = make;
        this.model = model;
        this.year = year;
    }
}
