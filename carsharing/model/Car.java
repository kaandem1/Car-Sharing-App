package carsharing.model;

public class Car {
    private int ID;
    private String NAME;
    private int COMPANY_ID;

    public Car(int ID, String NAME, int COMPANY_ID) {
        this.ID = ID;
        this.NAME = NAME;
        this.COMPANY_ID = COMPANY_ID;
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

    public int getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(int COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }
}