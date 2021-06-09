package com.example.kidmonitoring.model;

public class Information {
    private String Email;
    private String HoTen;
    private String NgaySinh;
    private String GioiTinh;
    public Information(String email, String hoTen, String ngaySinh, String gioiTinh){
        super();
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

    public static class InformationBuilder{
        private String Email;
        private String HoTen;
        private String NgaySinh;
        private String GioiTinh;
        public InformationBuilder(String email) {
            this.Email = email;
        }


        public InformationBuilder HoTen(String hoTen) {
            this.HoTen = hoTen;
            return this;
        }

        public InformationBuilder NgaySinh(String ngaySinh) {
            this.NgaySinh = ngaySinh;
            return this;
        }

        public InformationBuilder GioiTinh(String gioiTinh) {
            this.GioiTinh = gioiTinh;
            return this;
        }
        public Information build(){
            Information information = new Information(this.Email,this.HoTen,this.NgaySinh,this.GioiTinh);
            return information;
        }
    }
}
