package com.example.kidmonitoring.model.Information;

public interface InformationBuilder {
    InformationBuilder Email(String email);

    InformationBuilder HoTen(String hoTen);

    InformationBuilder NgaySinh(String ngaySinh);

    InformationBuilder GioiTinh(String gioiTinh);

    Information build();
}
