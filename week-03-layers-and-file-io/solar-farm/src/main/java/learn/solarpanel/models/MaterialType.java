package learn.solarpanel.models;

public enum MaterialType {
    CDTE("CdTe"), CIGS("CIGS"), GIGS("GIGS"), POLYSI("PolySi"), ASI("a-Si");

    private final String name;

    MaterialType(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
