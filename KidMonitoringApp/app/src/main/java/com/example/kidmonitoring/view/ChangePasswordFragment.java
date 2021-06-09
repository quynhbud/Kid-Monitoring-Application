package com.example.kidmonitoring.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kidmonitoring.R;
import com.example.kidmonitoring.controller.AccountController;
import com.example.kidmonitoring.controller.ChangePasswordDecorator;
import com.example.kidmonitoring.controller.SessionManager;
import com.example.kidmonitoring.model.Accounts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    EditText edtCurrentPassword, edtNewPassword,edtConfirmPassword;
    CardView cvSave;
    SessionManager sessionManager;
    String urlChangePassword ="https://kid-monitoring.000webhostapp.com/updateDataAccount.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    public ChangePasswordFragment() {
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
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        sessionManager = SessionManager.getInstance(mContext);
        dashboardInflatedView = inflater.inflate(R.layout.fragment_change_password, container, false);
        AnhXa();
        cvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = FormMainActivity.acc.getUsername();
                String currentPassword = edtCurrentPassword.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmNewPassword = edtConfirmPassword.getText().toString().trim();
                Accounts accounts = new Accounts.AccountsBuilder(email).Password(newPassword).Build();
                AccountController accountController = AccountController.getInstance();
                ChangePasswordDecorator changePasswordDecorator = new ChangePasswordDecorator(accountController);

                if(changePasswordDecorator.checkChange(FormMainActivity.acc,accounts,currentPassword,confirmNewPassword)==2) {
                    changePasswordDecorator.updatePassword(urlChangePassword, accounts, getActivity());
                    sessionManager.logoutUser();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    getActivity().finish();
                    Toast.makeText(mContext, "Đã đổi mật khẩu. Vui lòng đăng nhập lại!!!", Toast.LENGTH_SHORT).show();
                }
                else if(ChangePasswordDecorator.checkChange(FormMainActivity.acc,accounts,currentPassword,confirmNewPassword)==-2){
                    Toast.makeText(mContext, "Vui lòng nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show();
                }
                else if(ChangePasswordDecorator.checkChange(FormMainActivity.acc,accounts,currentPassword,confirmNewPassword)==1) {
                    Toast.makeText(mContext, "Mật khẩu mới trùng mật khẩu hiện tại!!!", Toast.LENGTH_SHORT).show();
                }
                else if(ChangePasswordDecorator.checkChange(FormMainActivity.acc,accounts,currentPassword,confirmNewPassword)==0){
                    Toast.makeText(mContext, "Mật khẩu hiện tại không đúng!!!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(mContext, "Vui lòng xác nhận lại mật khẩu!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return dashboardInflatedView;
    }

    private void AnhXa() {
        edtCurrentPassword = (EditText) dashboardInflatedView.findViewById(R.id.editTextCurrentPassword);
        edtNewPassword = (EditText) dashboardInflatedView.findViewById(R.id.editTextNewPassword);
        edtConfirmPassword = (EditText) dashboardInflatedView.findViewById(R.id.editTextConfirmNewPassword);
        cvSave = (CardView) dashboardInflatedView.findViewById(R.id.cardViewChangePassword);
    }
}