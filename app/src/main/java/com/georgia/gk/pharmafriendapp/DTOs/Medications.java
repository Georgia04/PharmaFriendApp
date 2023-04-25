package com.georgia.gk.pharmafriendapp.DTOs;

import java.io.Serializable;

public class
Medications implements Serializable {
    public String medication_Name;
    public String medication_Price;
    public String medication_Image;
    public String medication_Description;
    public String medication_Restrictions;
    public String medication_Availability;
    public String category;

    public Medications(){}

    public Medications(String medication_Name, String medication_Price, String medication_Image,
                       String medication_Description, String medication_Restrictions,
                       String medication_Availability, String category)
    {
        this.medication_Name = medication_Name;
        this.medication_Price = medication_Price;
        this.medication_Image = medication_Image;
        this.medication_Description = medication_Description;
        this.medication_Restrictions = medication_Restrictions;
        this.medication_Availability = medication_Availability;
        this.category = category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setMedication_Restrictions(String medication_Restrictions) {
        this.medication_Restrictions = medication_Restrictions;
    }

    public void setMedication_Price(String medication_Price) {
        this.medication_Price = medication_Price;
    }

    public void setMedication_Name(String medication_Name) {
        this.medication_Name = medication_Name;
    }

    public void setMedication_Image(String medication_Image) {
        this.medication_Image = medication_Image;
    }

    public void setMedication_Description(String medication_Description) {
        this.medication_Description = medication_Description;
    }

    public void setMedication_Availability(String medication_Availability) {
        this.medication_Availability = medication_Availability;
    }

    public String getMedication_Restrictions() {
        return medication_Restrictions;
    }

    public String getMedication_Price() {
        return medication_Price;
    }

    public String getMedication_Name() {
        return medication_Name;
    }

    public String getMedication_Image() {
        return medication_Image;
    }

    public String getMedication_Description() {
        return medication_Description;
    }

    public String getMedication_Availability() {
        return medication_Availability;
    }
}
