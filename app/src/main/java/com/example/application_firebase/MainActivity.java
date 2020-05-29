package com.example.application_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText txtID, txtName, txtAddress, txtContact;
    Button btnSave, btnShow, btnUpdate, btnDelete;

    DatabaseReference dbRef;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtID = findViewById(R.id.txtID);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtContact = findViewById(R.id.txtContact);

        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        student = new Student();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRef = FirebaseDatabase.getInstance().getReference().child("Student");
                try{
                    if(TextUtils.isEmpty(txtID.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Please enter ID", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(txtName.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(txtAddress.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Please enter address", Toast.LENGTH_SHORT).show();
                    }else{
                        student.setID(txtID.getText().toString().trim());
                        student.setName(txtName.getText().toString().trim());
                        student.setAddress(txtAddress.getText().toString().trim());
                        student.setConNo(Integer.parseInt(txtContact.getText().toString().trim()));

                        dbRef.child("test").setValue(student);

                        Toast.makeText(getApplicationContext(), "Data Saved Succussfully", Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference readDef = FirebaseDatabase.getInstance().getReference().child("Student").child("test");
                readDef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            txtID.setText(dataSnapshot.child("id").getValue().toString());
                            txtName.setText(dataSnapshot.child("name").getValue().toString());
                            txtAddress.setText(dataSnapshot.child("address").getValue().toString());
                            txtContact.setText(dataSnapshot.child("conNo").getValue().toString());
                        }else{
                            Toast.makeText(getApplicationContext(), "No Source to Display", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference upRef = FirebaseDatabase.getInstance().getReference().child("Student");
                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("test")){
                            try{
                                student.setID(txtID.getText().toString().trim());
                                student.setName(txtName.getText().toString().trim());
                                student.setAddress(txtAddress.getText().toString().trim());
                                student.setConNo(Integer.parseInt(txtContact.getText().toString().trim()));

                                dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("test");
                                dbRef.setValue(student);

                                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                clearControls();

                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "No source to update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Student");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("test")){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Student").child("test");
                            delRef.removeValue();

                            Toast.makeText(getApplicationContext(), "Data removed Successfully", Toast.LENGTH_SHORT).show();
                            clearControls();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void clearControls(){
        txtID.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        txtName.setText("");
    }
}