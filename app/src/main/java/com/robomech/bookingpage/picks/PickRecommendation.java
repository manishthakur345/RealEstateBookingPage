package com.robomech.bookingpage.picks;

public class PickRecommendation {
    String imagePath;
    String price;
    String description;
    Boolean isWishlisted;
    Boolean isVerified;
    //Add other descriptions


    public PickRecommendation(String imagePath, String price, String description, Boolean isWishlisted, Boolean isVerified) {
        this.imagePath = imagePath;
        this.price = price;
        this.description = description;
        this.isWishlisted = isWishlisted;
        this.isVerified = isVerified;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(Boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
