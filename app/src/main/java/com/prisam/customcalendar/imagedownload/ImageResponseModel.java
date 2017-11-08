package com.prisam.customcalendar.imagedownload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vamsi on 10/12/2017.
 */

public class ImageResponseModel {

    private String large;
    private boolean error;
    private String message;
    private boolean checked;
    private boolean downloaded;

    public ImageResponseModel(String name, URL url, String timestamp,boolean checked) {
        this.name = name;
        this.url = url;
        this.timestamp = timestamp;
        this.checked = checked;
    }

    public ImageResponseModel(String name, String large) {
        this.name = name;
        this.large = large;
    }

    public ImageResponseModel(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private URL url;

    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public class URL {

        @SerializedName("small")
        @Expose
        public String small;

        @SerializedName("medium")
        @Expose
        public String medium;

        @SerializedName("large")
        @Expose
        public String large;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
