package com.github.ivansenchukov.topjavagraduation.model;

public class Restaurant extends AbstractBaseEntity{


    //<editor-fold desc="Fields">
    String name;
    //</editor-fold>


    //<editor-fold desc="Constructors">
    public Restaurant() {}

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Restaurant(Restaurant prototypeRestaurant) {
        this(prototypeRestaurant.getId(), prototypeRestaurant.getName());
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>


    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
