package com.lq.blog.model;

public class Type {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column type.id
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column type.name
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    private String name;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column type.id
     *
     * @return the value of type.id
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column type.id
     *
     * @param id the value for type.id
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column type.name
     *
     * @return the value of type.name
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column type.name
     *
     * @param name the value for type.name
     *
     * @mbg.generated Mon Oct 28 15:32:05 CST 2019
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}