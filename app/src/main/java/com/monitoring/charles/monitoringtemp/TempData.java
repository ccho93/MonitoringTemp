package com.monitoring.charles.monitoringtemp;

/**
 * Created by Charles on 11/20/15.
 */
public class TempData {
    double preheatTemp;
    int preheatTime;
    double temp1temp;
    int temp1time;
    double temp2temp;

    public void setPreheatTemp(double preheatTemp) {
        this.preheatTemp = preheatTemp;
    }

    public void setPreheatTime(int preheatTime) {
        this.preheatTime = preheatTime;
    }

    public void setTemp1temp(double temp1temp) {
        this.temp1temp = temp1temp;
    }

    public void setTemp1time(int temp1time) {
        this.temp1time = temp1time;
    }

    public void setTemp2temp(double temp2temp) {
        this.temp2temp = temp2temp;
    }

    public void setTemp2time(int temp2time) {
        this.temp2time = temp2time;
    }

    public void setTemp3temp(double temp3temp) {
        this.temp3temp = temp3temp;
    }

    public void setTemp3time(int temp3time) {
        this.temp3time = temp3time;
    }

    public void setCoolTemp(double coolTemp) {
        this.coolTemp = coolTemp;
    }

    public void setCoolTempTime(int coolTempTime) {
        this.coolTempTime = coolTempTime;
    }

    public double getPreheatTemp() {
        return preheatTemp;
    }

    public int getPreheatTime() {
        return preheatTime;
    }

    public double getTemp1temp() {
        return temp1temp;
    }

    public int getTemp1time() {
        return temp1time;
    }

    public double getTemp2temp() {
        return temp2temp;
    }

    public int getTemp2time() {
        return temp2time;
    }

    public double getTemp3temp() {
        return temp3temp;
    }

    public int getTemp3time() {
        return temp3time;
    }

    public double getCoolTemp() {
        return coolTemp;
    }

    public int getCoolTempTime() {
        return coolTempTime;
    }

    int temp2time;
    double temp3temp;
    int temp3time;
    double coolTemp;
    int coolTempTime;

    @Override
    public String toString() {
        return "TempData{" +
                "preheatTemp=" + preheatTemp +
                ", preheatTime=" + preheatTime +
                ", temp1temp=" + temp1temp +
                ", temp1time=" + temp1time +
                ", temp2temp=" + temp2temp +
                ", temp2time=" + temp2time +
                ", temp3temp=" + temp3temp +
                ", temp3time=" + temp3time +
                ", coolTemp=" + coolTemp +
                ", coolTempTime=" + coolTempTime +
                '}';
    }
}
