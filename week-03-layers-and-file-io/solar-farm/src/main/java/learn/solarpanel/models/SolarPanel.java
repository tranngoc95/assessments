package learn.solarpanel.models;

import java.util.Objects;

public class SolarPanel {
    private int solarID;
    private String section;
    private int row;
    private int col;
    private int yearInstalled;
    private MaterialType material;
    private Boolean isTracking;

    public SolarPanel(){

    }

    public SolarPanel(int solarID, String section, int row, int col,
                      int yearInstalled, MaterialType material, Boolean isTracking) {
        this.solarID = solarID;
        this.section = section;
        this.row = row;
        this.col = col;
        this.yearInstalled = yearInstalled;
        this.material = material;
        this.isTracking = isTracking;
    }

    public int getSolarID() {
        return solarID;
    }

    public void setSolarID(int solarID) {
        this.solarID = solarID;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getYearInstalled() {
        return yearInstalled;
    }

    public void setYearInstalled(int yearInstalled) {
        this.yearInstalled = yearInstalled;
    }

    public MaterialType getMaterial() {
        return material;
    }

    public void setMaterial(MaterialType material) {
        this.material = material;
    }

    public Boolean getTracking() {
        return isTracking;
    }

    public void setTracking(Boolean isTracking) {
        this.isTracking = isTracking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolarPanel that = (SolarPanel) o;
        return solarID == that.solarID && row == that.row && col == that.col && yearInstalled == that.yearInstalled && Objects.equals(section, that.section) && material == that.material && Objects.equals(isTracking, that.isTracking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solarID, section, row, col, yearInstalled, material, isTracking);
    }
}
