package carsharing.model;

public class Customer {
    private int ID;
    private String NAME;
    private Integer RENTED_CAR_ID;

    public Customer(int ID, String NAME, Integer RENTED_CAR_ID) {
        this.ID = ID;
        this.NAME = NAME;
        this.RENTED_CAR_ID = RENTED_CAR_ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getRENTED_CAR_ID() {
        return RENTED_CAR_ID;
    }

    public void setRENTED_CAR_ID(Integer RENTED_CAR_ID) {
        this.RENTED_CAR_ID = RENTED_CAR_ID;
    }
}
