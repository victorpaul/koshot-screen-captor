package com.sukinsan.shot.entity;

public class ShotEntity {
    private String shotName;
    private String publicUrl;

    public ShotEntity(String filename, String publicUrl) {
        this.shotName = filename;
        this.publicUrl = publicUrl;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getShotName() {
        return shotName;
    }

    public void setShotName(String shotName) {
        this.shotName = shotName;
    }

    @Override
    public String toString() {
        return "ShotEntity{" +
                "shotName='" + shotName + '\'' +
                '}';
    }
}
