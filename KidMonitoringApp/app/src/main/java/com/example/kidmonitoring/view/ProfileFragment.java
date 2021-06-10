package com.example.kidmonitoring.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AppController;
import com.example.kidmonitoring.controller.InformationController;
import com.example.kidmonitoring.model.Information.Information;
import com.example.kidmonitoring.model.Information.InformationConcreteBuilder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    EditText edtFullName,edtDoB,edtEmail;
    RadioGroup rgGender;
    RadioButton rdMale, rdFemale;
    String gender = "Nam";
    CardView cvEdit,cvSave;
    ArrayList<String> arrayList;
    String urlUpdateInfo = "https://kid-monitoring.000webhostapp.com/updateDataInfor.php";
    String urlGetData = "https://kid-monitoring.000webhostapp.com/getdataInfor.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    public ProfileFragment() {
        // Required empty public constructor
    }
    View dashboardInflatedView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardInflatedView = inflater.inflate(R.layout.fragment_profile, container, false);
        AnhXa();
        //InformationController.GetData(urlGetData,information,mContext);
        //user=InformationController.findUser(MainActivity.Email,information);
        //Toast.makeText(getActivity(), information.toString(),Toast.LENGTH_LONG).show();
        edtFullName.setText(FormMainActivity.user.getHoTen());
        edtDoB.setText(FormMainActivity.user.getNgaySinh());
        edtEmail.setText(FormMainActivity.user.getEmail());
        EnableEdit(false);

        gender=FormMainActivity.user.getGioiTinh().trim();
        if(gender.equals("Nam")) {
            rdMale.setChecked(true);
        }
        else {
            rdFemale.setChecked(true);
        }
        rdMale.setOnCheckedChangeListener(listenerRadio);
        rdFemale.setOnCheckedChangeListener(listenerRadio);
        edtDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.ChonNgay(getActivity(),edtDoB);
            }
        });
        final boolean[] flag = {true};
        cvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag[0] ==true)
                {
                    EnableEdit(true);
                    flag[0] = false;
                }
                else {
                    EnableEdit(false);
                    flag[0] =true;
                }
            }
        });
        cvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String hoTen = edtFullName.getText().toString().trim();
                String ngaySinh = edtDoB.getText().toString().trim();
                String Gender = gender;
                Information information = new InformationConcreteBuilder().Email(email).HoTen(hoTen)
                                                        .NgaySinh(ngaySinh).GioiTinh(Gender).build();
//                Toast.makeText(mContext,arrayList.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, FormMainActivity.user.getEmail()+"|"+FormMainActivity.user.getHoTen()+"|"+FormMainActivity.user.getNgaySinh()+"|"+FormMainActivity.user.getGioiTinh()+"...", Toast.LENGTH_LONG).show();
                if(InformationController.checkChange(FormMainActivity.user,information)==true)
                {
                    InformationController.Update(urlUpdateInfo,information,getActivity());
                    FormMainActivity.information=new ArrayList<>();
                    InformationController.GetData(urlGetData,FormMainActivity.information,mContext);
                    FormMainActivity.user = InformationController.findUser(FormMainActivity.user.getEmail(),FormMainActivity.information);
                    EnableEdit(false);
                }
                else
                    Toast.makeText(mContext, "Mời bạn chỉnh sửa thông tin trước khi lưu", Toast.LENGTH_SHORT).show();
            }
        });

        return dashboardInflatedView;
    }
    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                gender =  compoundButton.getText().toString();
            }
        }
    };
    private void EnableEdit(boolean bool){
        edtFullName.setEnabled(bool);
        edtDoB.setEnabled(bool);
        rdMale.setEnabled(bool);
        rdFemale.setEnabled(bool);
    }
    private void AnhXa()
    {
        edtFullName = (EditText) dashboardInflatedView.findViewById(R.id.editTextprofileName);
        edtDoB = (EditText) dashboardInflatedView.findViewById(R.id.editTextProfileDOB);
        edtEmail = (EditText) dashboardInflatedView.findViewById(R.id.editTextProfileEmail);
        rdFemale = (RadioButton)dashboardInflatedView.findViewById(R.id.radioProfileFemale);
        rdMale = (RadioButton)dashboardInflatedView.findViewById(R.id.radioProfileMale);
        rgGender = (RadioGroup)dashboardInflatedView.findViewById(R.id.groupProfileGender);
        cvEdit = (CardView)dashboardInflatedView.findViewById(R.id.cardViewEditProfile);
        cvSave = (CardView) dashboardInflatedView.findViewById(R.id.cardViewSaveProfile);
    }
}