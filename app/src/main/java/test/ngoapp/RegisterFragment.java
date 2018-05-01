package test.ngoapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    EditText email, password, confirmPassword, name, address, phoneNo;
    Button register;
    TextView login;
    TextInputLayout emailView, passwordView, confirmPasswordView, nameView, addressView, phoneNoView;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        phoneNo = view.findViewById(R.id.phoneNe);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        nameView = view.findViewById(R.id.nameView);
        addressView = view.findViewById(R.id.addressView);
        phoneNoView = view.findViewById(R.id.phonenNView);
        emailView = view.findViewById(R.id.emailView);
        passwordView = view.findViewById(R.id.passwordView);
        confirmPasswordView = view.findViewById(R.id.confirmPasswordView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.login_content, new LoginFragment()).commit();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailView.setError(null);
                passwordView.setError(null);

                final String Sname = name.getText().toString();
                final String Saddress = address.getText().toString();
                final String SPhoneNo = phoneNo.getText().toString();
                String Semail = email.getText().toString();
                String Spass = password.getText().toString();
                String SconirmPass = confirmPassword.getText().toString();
                boolean cont = true;
                View focus = null;

                if (!Spass.equals(SconirmPass)) {
                    passwordView.setError("Passwords do not match");
                    confirmPasswordView.setError("Passwords do not match");
                    focus = confirmPassword;
                    cont = false;
                }

                if (SconirmPass.trim().equals("")) {
                    confirmPasswordView.setError("Field cannot be empty");
                    focus = confirmPassword;
                    cont = false;
                } else if (SconirmPass.length() < 8) {
                    confirmPasswordView.setError("Invalid Password");
                    focus = confirmPassword;
                    cont = false;
                }

                if (Spass.trim().equals("")) {
                    passwordView.setError("Field cannot be empty");
                    focus = password;
                    cont = false;
                } else if (Spass.length() < 8) {
                    passwordView.setError("Invalid Password");
                    focus = password;
                    cont = false;
                }

                if (Semail.trim().equals("")) {
                    emailView.setError("Field cannot be empty");
                    focus = email;
                    cont = false;
                } else if (!Semail.contains("@")) {
                    emailView.setError("Invalid Email");
                    focus = email;
                    cont = false;
                }

                if (SPhoneNo.trim().equals("")) {
                    phoneNoView.setError("Field cannot be empty");
                    focus = phoneNo;
                    cont = false;
                } else if ((SPhoneNo.contains("+") && SPhoneNo.length() != 13) || (!SPhoneNo.contains("+") && SPhoneNo.length() != 10)) {
                    phoneNoView.setError("Please enter valid phone number");
                    focus = phoneNo;
                    cont = false;
                }

                if (Saddress.trim().equals("")) {
                    addressView.setError("Field cannot be empty");
                    focus = address;
                    cont = false;
                }

                if (Sname.trim().equals("")) {
                    nameView.setError("Field cannot be empty");
                    focus = name;
                    cont = false;
                }

                if (!cont)
                    focus.requestFocus();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading, Please Wait...");
                    progressDialog.show();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(Semail, Spass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseDatabase.getInstance().getReference("/users").child(authResult.getUser().getUid()).setValue(new User(Sname, Saddress, SPhoneNo)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "User Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }

            }
        });

        return view;
    }

}

class User {
    public String name, address, phoneNo;

    public User(String name, String address, String phoneNo) {
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
    }
}