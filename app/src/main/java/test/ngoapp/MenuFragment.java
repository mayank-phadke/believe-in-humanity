package test.ngoapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        LinearLayout item1, item2, item3, item4, item5, item6;

        item1 = view.findViewById(R.id.item1);
        item2 = view.findViewById(R.id.item2);
        item3 = view.findViewById(R.id.item3);
        item4 = view.findViewById(R.id.item4);
        item5 = view.findViewById(R.id.item5);
        item6 = view.findViewById(R.id.item6);

        item1.setOnClickListener(new OnClick("White T-Shirt", "150"));
        item2.setOnClickListener(new OnClick("Red T-Shirt", "150"));
        item3.setOnClickListener(new OnClick("Black T-Shirt", "150"));
        item4.setOnClickListener(new OnClick("Cap", "50"));
        item5.setOnClickListener(new OnClick("Wrist Band", "30"));
        item6.setOnClickListener(new OnClick("Keychain", "40"));

        return view;
    }

    class OnClick implements View.OnClickListener {

        String title, price;

        public OnClick(String title, String price) {
            this.title = title;
            this.price = price;
        }

        @Override
        public void onClick(View view) {

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage("Price - Rs. " + price)
                    .setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final DatabaseReference ref = FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("cart");

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean found = false;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.child("title").getValue().equals(title)) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) {
                                        Toast.makeText(getActivity(), "Item Already in cart", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ref.push().setValue(new CartItem(title, Integer.valueOf(price), 1));
                                        Toast.makeText(getActivity(), "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    })
                    .

                            setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).

                            create();
            dialog.show();

        }
    }
}
