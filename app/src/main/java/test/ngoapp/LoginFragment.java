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

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    EditText email, password;
    Button login;
    TextView register;
    TextInputLayout emailView, passwordView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        emailView = view.findViewById(R.id.emailView);
        passwordView = view.findViewById(R.id.passwordView);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.login_content, new RegisterFragment()).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailView.setError(null);
                passwordView.setError(null);

                String Semail = email.getText().toString();
                String Spass = password.getText().toString();
                boolean cont = true;
                View focus = null;

                if (Spass.trim().equals("")) {
                    passwordView.setError("Field cannot be empty");
                    focus = password;
                    cont = false;
                } else if (Spass.length() < 8) {
                    passwordView.setError("Password cannot be less than 8 characters");
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

                if (!cont)
                    focus.requestFocus();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading, Please Wait...");
                    progressDialog.show();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(Semail, Spass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Invalid Email ID or Password", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }
            }
        });

        return view;
    }

}
