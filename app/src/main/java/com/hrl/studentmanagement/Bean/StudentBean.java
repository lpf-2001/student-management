package com.hrl.studentmanagement.Bean;

public class StudentBean {
    private String name,sex,aihao,date,age,phone;
    private int id;

    public StudentBean() {

    }

    public StudentBean(int id, String name, String age, String sex, String aihao, String phone, String date) {
        this.name = name;
        this.sex = sex;
        this.aihao = aihao;
        this.date = date;
        this.id = id;
        this.age = age;
        this.phone = phone;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAihao() {
        return aihao;
    }

    public void setAihao(String aihao) {
        this.aihao = aihao;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
