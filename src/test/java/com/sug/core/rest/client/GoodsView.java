package com.sug.core.rest.client;

/**
 * Created by Administrator on 2014/10/19.
 */
public class GoodsView {

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getMgrade_year() {
        return mgrade_year;
    }

    public void setMgrade_year(String mgrade_year) {
        this.mgrade_year = mgrade_year;
    }

    public int getMgrade_norm() {
        return mgrade_norm;
    }

    public void setMgrade_norm(int mgrade_norm) {
        this.mgrade_norm = mgrade_norm;
    }

    public String getMgrade_ingredients() {
        return mgrade_ingredients;
    }

    public void setMgrade_ingredients(String mgrade_ingredients) {
        this.mgrade_ingredients = mgrade_ingredients;
    }

    public String getMgrade_grade() {
        return mgrade_grade;
    }

    public void setMgrade_grade(String mgrade_grade) {
        this.mgrade_grade = mgrade_grade;
    }

    public String getMgrade_dis() {
        return mgrade_dis;
    }

    public void setMgrade_dis(String mgrade_dis) {
        this.mgrade_dis = mgrade_dis;
    }

    public String getMgrade_areas() {
        return mgrade_areas;
    }

    public void setMgrade_areas(String mgrade_areas) {
        this.mgrade_areas = mgrade_areas;
    }

    public String getMgrade_alcohol() {
        return mgrade_alcohol;
    }

    public void setMgrade_alcohol(String mgrade_alcohol) {
        this.mgrade_alcohol = mgrade_alcohol;
    }

    public float getMarket_price() {
        return market_price;
    }

    public void setMarket_price(float market_price) {
        this.market_price = market_price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefault_spec() {
        return default_spec;
    }

    public void setDefault_spec(int default_spec) {
        this.default_spec = default_spec;
    }

    public String getDefault_image2() {
        return default_image2;
    }

    public void setDefault_image2(String default_image2) {
        this.default_image2 = default_image2;
    }

    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    private float price;
    private String mgrade_year;
    private int mgrade_norm;
    private String mgrade_ingredients;
    private String mgrade_grade;
    private String mgrade_dis;
    private String mgrade_areas;
    private String mgrade_alcohol;
    private float market_price;
    private String goods_name;
    private int goods_id;
    private String description;
    private int default_spec;
    private String default_image2;
    private String default_image;
    private String brand;
    private String recipe;
    private String origin;

    @Override
    public String toString() {
        return "GoodsView{" +
                "price=" + price +
                ", mgrade_year='" + mgrade_year + '\'' +
                ", mgrade_norm=" + mgrade_norm +
                ", mgrade_ingredients='" + mgrade_ingredients + '\'' +
                ", mgrade_grade='" + mgrade_grade + '\'' +
                ", mgrade_dis='" + mgrade_dis + '\'' +
                ", mgrade_areas='" + mgrade_areas + '\'' +
                ", mgrade_alcohol='" + mgrade_alcohol + '\'' +
                ", market_price=" + market_price +
                ", goods_name='" + goods_name + '\'' +
                ", goods_id=" + goods_id +
                ", description='" + description + '\'' +
                ", default_spec=" + default_spec +
                ", default_image2='" + default_image2 + '\'' +
                ", default_image='" + default_image + '\'' +
                ", brand='" + brand + '\'' +
                ", recipe='" + recipe + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}
