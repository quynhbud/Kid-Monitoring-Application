package com.example.kidmonitoring.model.Information;

public class Information {
    private String Email;
    private String HoTen;
    private String NgaySinh;
    private String GioiTinh;
    public Information(String email, String hoTen, String ngaySinh, String gioiTinh){
        //super();
        this.Email = email;
        this.HoTen = hoTen;
        this.NgaySinh = ngaySinh;
        this.GioiTinh = gioiTinh;
    }

    public String getEmail() {
        return Email;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

}
