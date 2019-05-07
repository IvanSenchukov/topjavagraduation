package com.github.ivansenchukov.topjavagraduation.model;

public abstract class AbstractBaseEntity implements HasId {
    public static final int START_SEQ = 100000;

    protected Integer id;


    //<editor-fold desc="Constructors">
    protected AbstractBaseEntity() {
    }

    protected AbstractBaseEntity(Integer id) {
        this.id = id;
    }
    //</editor-fold>


    //<editor-fold desc="Getters and Setters">
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
    //</editor-fold>


    //<editor-fold desc="equals(), hashCode(), toString()">
    @Override
    public String toString() {
        return String.format("Entity %s (%s)", getClass().getName(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
    //</editor-fold>

}