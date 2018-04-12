package com.cmu.nuts.coffee9.model;

public class Review_Image {
    public static String tag = "Review_Image";
    private String rid;
    private String imgid;
    private String image_url;

    public Review_Image() {

    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public static String getTag() {
        return tag;
    }

    public static void setTag(String tag) {
        Review_Image.tag = tag;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Review_Image(String rid, String imgid, String image_url) {
        this.rid = rid;
        this.imgid = imgid;
        this.image_url = image_url;
    }
}
