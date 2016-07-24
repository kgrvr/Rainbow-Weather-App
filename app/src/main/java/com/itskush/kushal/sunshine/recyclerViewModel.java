package com.itskush.kushal.sunshine;

/**
 * Created by Kush on 17-07-2016.
 */
public class recyclerViewModel {

    private String headingText, subHeadingText, baseText, low, humidity;
    private int forecastImageView;

    recyclerViewModel() {}

    recyclerViewModel(int forecastImageView, String headingText, String subHeadingText, String baseText, String low, String humidity) {
        this.forecastImageView = forecastImageView;
        this.headingText = headingText;
        this.subHeadingText = subHeadingText;
        this.baseText = baseText;
        this.low = low;
        this.humidity = humidity;
    }

    public void setForecastImageView(int forecastImageView) {
        this.forecastImageView = forecastImageView;
    }

    public int getForecastImageView() {
        return forecastImageView;
    }

    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }

    public String getHeadingText() {
        return headingText;
    }

    public String getSubHeadingText() {
        return subHeadingText;
    }

    public void setSubHeadingText(String subHeadingText) {
        this.subHeadingText = subHeadingText;
    }

    public String getBaseText() {
        return baseText;
    }

    public void setBaseText(String baseText) {
        this.baseText = baseText;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
