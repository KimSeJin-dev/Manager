package com.example.manager;


import com.google.gson.annotations.SerializedName;

public class PostLiveData {

    @SerializedName("name")
    private String name; // 이름

    @SerializedName("major")
    private String major; // 학과

    @SerializedName("phone_num")
    private String phone_num;// 연락처

    @SerializedName("enter_time")
    private String enter_time; // 입장시간

    @SerializedName("student_num")
    private String student_num;

    public void setName(String name) { this.name = name; }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
    public void setEnter_time(String enter_time) {this.enter_time = enter_time;}
    public void setStudent_num(String student_num) {this.student_num = student_num;}

    public PostLiveData(){}

    public PostLiveData(String phone_num, String name, String major, String student_num, String enter_time){
        this.name = name;
        this.major = major;
        this.phone_num = phone_num;
        this.student_num = student_num;
        this.enter_time = enter_time;
    }

    public String getStudent_num(){return student_num;}
    public String getPhone_num(){return phone_num;}
    public String getName() {return name;}
    public String getMajor() { return major;}
    public String getEnter_time(){return enter_time;}
}
