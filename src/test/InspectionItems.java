package test;

import java.util.HashMap;
import java.util.List;




public class InspectionItems {
    public HashMap<String, String> LargeClassification;
    public HashMap<String, String> MediumClassification;
    public HashMap<String, String> SmallClassification;
    public List<HashMap<String, String>> InspectionItems;
    public HashMap<String, String> getLargeClassification() {
        return LargeClassification;
    }
    public void setLargeClassification(HashMap<String, String> largeClassification) {
        LargeClassification = largeClassification;
    }
    public HashMap<String, String> getMediumClassification() {
        return MediumClassification;
    }
    public void setMediumClassification(HashMap<String, String> mediumClassification) {
        MediumClassification = mediumClassification;
    }
    public HashMap<String, String> getSmallClassification() {
        return SmallClassification;
    }
    public void setSmallClassification(HashMap<String, String> smallClassification) {
        SmallClassification = smallClassification;
    }
    public List<HashMap<String, String>> getInspectionItems() {
        return InspectionItems;
    }
    public void setInspectionItems(List<HashMap<String, String>> inspectionItems) {
        InspectionItems = inspectionItems;
    }
}