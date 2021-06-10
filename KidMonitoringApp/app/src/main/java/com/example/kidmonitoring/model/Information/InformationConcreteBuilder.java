package com.example.kidmonitoring.model.Information;

public class InformationConcreteBuilder implements InformationBuilder{
    private String Email;
    private String HoTen;
    private String NgaySinh;
    private String GioiTinh;
    @Override
    public InformationBuilder Email(String email) {
        this.Email = email;
        return  this;
    }

    @Override
    public InformationBuilder HoTen(String hoTen) {
        this.HoTen = hoTen;
        return this;
    }
    @Override
    public InformationBuilder NgaySinh(String ngaySinh) {
        this.NgaySinh = ngaySinh;
        return this;
    }
    @Override
    public InformationBuilder GioiTinh(String gioiTinh) {
        this.GioiTinh = gioiTinh;
        return this;
    }
    @Override
    public Information build(){
        Information information = new Information(this.Email,this.HoTen,this.NgaySinh,this.GioiTinh);
        return information;
    }
}
