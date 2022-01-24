package ru.job4j.dream.model;

import java.util.Date;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private String cityName;
    private Date createDate;

    public Candidate(int id, String name, String cityName, Date createDate) {
        this.id = id;
        this.name = name;
        this.cityName = cityName;
        this.createDate = createDate;
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String
    toString() {
        return "Candidate{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", cityName='" + cityName + '\''
                + ", create_date=" + createDate
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id && Objects.equals(name, candidate.name)
                && Objects.equals(cityName, candidate.cityName) && Objects.equals(createDate, candidate.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cityName, createDate);
    }
}