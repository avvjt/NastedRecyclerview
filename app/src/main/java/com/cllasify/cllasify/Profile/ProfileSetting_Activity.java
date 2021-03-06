package com.cllasify.cllasify.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetting_Activity extends AppCompatActivity {


    LinearLayout ll_UserName,ll_Name,ll_PhoneNo,ll_Instituion,ll_Email,ll_Location,ll_Bio;
    DatabaseReference refUserStatus;
    FirebaseUser currentUser;
    String userID,userName,userEmail;
    TextView tv_UserName,tv_Name,tv_Email,tv_Institution,tv_Phone,tv_Location,tv_Bio,tv_ShowUserName,tv_ChangeProfileImage;

    Button btn_Cancel;

    CircleImageView prof_pic;
    StorageReference storageReference;
    int TAKE_IMAGE_CODE=1001;
    Uri imageUrl,userPhoto;


    DatabaseReference root;
    StorageReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_f_s);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();

        root=FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//        DatabaseReference root=FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference=FirebaseStorage.getInstance().getReference().child(userID);

        tv_ShowUserName=findViewById(R.id.tv_ShowUserName);
        tv_Bio=findViewById(R.id.tv_Bio);
        tv_UserName=findViewById(R.id.tv_UserName);
        tv_Name=findViewById(R.id.tv_Name);
        tv_Location=findViewById(R.id.tv_Location);
        tv_Institution=findViewById(R.id.tv_Institution);
        tv_Phone=findViewById(R.id.tv_Phone);
        tv_Email=findViewById(R.id.tv_Email);
        tv_ChangeProfileImage=findViewById(R.id.tv_ChangeProfileImage);
        prof_pic=findViewById(R.id.prof_pic);

        btn_Cancel=findViewById(R.id.btn_Cancel);

        ll_UserName=findViewById(R.id.ll_UserName);
        ll_Name=findViewById(R.id.ll_Name);
        ll_Location=findViewById(R.id.ll_Location);
        ll_Instituion=findViewById(R.id.ll_Institution);
        ll_PhoneNo=findViewById(R.id.ll_Phone);
        ll_Email=findViewById(R.id.ll_Email);
        ll_Bio=findViewById(R.id.ll_Bio);

        storageReference= FirebaseStorage.getInstance().getReference();


        tv_ChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);

//                Intent openGalleryIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (openGalleryIntent.resolveActivity(getPackageManager())!=null){

//                    startActivityForResult(openGalleryIntent,TAKE_IMAGE_CODE);
//                }
//                startActivityForResult(openGalleryIntent,1000);
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new AccountSettingFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();
                Intent i = new Intent(ProfileSetting_Activity.this, AccountSetting_Activity.class);
                startActivity(i);
                (ProfileSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });

        tv_ShowUserName.setText(userName);
//        tv_ShowUserName.setText(userEmail);

        ll_UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=tv_UserName.getText().toString();
                editSetting("NickName",userName);
            }
        });
        ll_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name=tv_Name.getText().toString();
                editSetting("Name",Name);

            }
        });
        ll_Instituion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Institution=tv_Institution.getText().toString();
                editSetting("Insitution",Institution);

            }
        });
        ll_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Location=tv_Location.getText().toString();
                editSetting("Location",Location);

            }
        });
        ll_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=tv_Email.getText().toString();
                editSetting("Email",Email);
            }
        });
        ll_PhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Phone=tv_Phone.getText().toString();
                editSetting("Phone",Phone);
            }
        });
        ll_Bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Bio=tv_Bio.getText().toString();
                editSetting("Bio",Bio);
            }
        });



        showProfile();
//        return view;
    }

    private void showProfile() {
        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){

                    if (snapshot.child("Bio").exists()){
                        String bio=snapshot.child("Bio").getValue().toString();
                        tv_Bio.setText(bio);
                    }else{
                        tv_Bio.setText("Bio");
                    }
                    if (snapshot.child("Insitution").exists()){
                        String Institute=snapshot.child("Insitution").getValue().toString();
                        tv_Institution.setText(Institute);
                    }else{
                        tv_Institution.setText("Institution");
                    }
                    if (snapshot.child("NickName").exists()){
                        String NickName=snapshot.child("NickName").getValue().toString();
                        tv_UserName.setText(NickName);
                    }else{
                        tv_UserName.setText("Nickname");
                    }
                    if (snapshot.child("Name").exists()){
                        String Name=snapshot.child("Name").getValue().toString();
                        tv_Name.setText(Name);
                    }else{
                        tv_Name.setText("Name");
                    }
                    if (snapshot.child("Phone").exists()){
                        String Phone=snapshot.child("Phone").getValue().toString();
                        tv_Phone.setText(Phone);
                    }else{
                        tv_Phone.setText("Phone");
                    }
                    if (snapshot.child("Location").exists()){
                        String Location=snapshot.child("Location").getValue().toString();
                        tv_Location.setText(Location);
                    }else{
                        tv_Location.setText("Location");
                    }
                    if (snapshot.child("userEmailId").exists()){
                        String email=snapshot.child("userEmailId").getValue().toString();
                        tv_Email.setText(email);
                    }else{
                        tv_Location.setText("Email");
                    }
                    if (snapshot.child("profilePic").exists()){
                        String profilePic=snapshot.child("profilePic").getValue().toString();
                        Glide.with(ProfileSetting_Activity.this).load(profilePic).into(prof_pic);
                    }else{
                        Picasso.get().load(userPhoto).into(prof_pic);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//
//        refUserFollowers= FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(userID);
//        refUserFollowers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//                    long count=snapshot.getChildrenCount();
//                    tv_CountFollowers.setText((int) count+" Followers");
//                    notifyPB.dismiss();
////                        notifyPB.show();
//                }else{
//                    tv_CountFollowers.setText("No Followers");
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(userID);
//        refUserFollowing.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//                    long count=snapshot.getChildrenCount();
//                    tv_CountFollowing.setText((int) count+" Following");
//                    notifyPB.dismiss();
//                }else {
//                    tv_CountFollowing.setText("No Following");
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


    }

    private void editSetting(String title,String userData) {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(this);
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_setting);

        Button btn_Cancel=bottomSheetDialoglogin.findViewById(R.id.btn_Cancel);
        TextView tv_subTitle=bottomSheetDialoglogin.findViewById(R.id.tv_subTitle);
        TextView tv_SettingTitle=bottomSheetDialoglogin.findViewById(R.id.tv_SettingTitle);
        EditText et_NewDetails=bottomSheetDialoglogin.findViewById(R.id.et_NewDetails);
        Button btn_Submit=bottomSheetDialoglogin.findViewById(R.id.btn_Submit);


        tv_subTitle.setText("Enter new "+title);
        et_NewDetails.setText(userData);
        tv_SettingTitle.setText("Please update "+userData);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialoglogin.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("NickName")){
                    String username=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("NickName").setValue(username);

                }else if(title.equals("Name")){
                    String name=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Name").setValue(name);

                }else if(title.equals("Insitution")){
                    String institution=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Insitution").setValue(institution);

                }else if(title.equals("Bio")){
                    String bio=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Bio").setValue(bio);

                }else if(title.equals("Phone")){
                    String phone=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Phone").setValue(phone);

                }else if(title.equals("Email")){
                    String email=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("userEmailId").setValue(email);

                }else if (title.equals("Location")){
                    String location=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Location").setValue(location);

                }

                bottomSheetDialoglogin.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialoglogin.dismiss();
            }
        });

        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialoglogin.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && data!=null && data.getData()!=null){
             imageUrl = data.getData();
//             prof_pic.setImageURI(imageUrl);

             uploadImagetoFirebaseStorage(imageUrl);



        }

        //            refShowUserPrivateGroup.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.getChildrenCount()>0){
//                        notifyPB.show();
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });

//        if (requestCode==TAKE_IMAGE_CODE){
//            if (requestCode== Activity.RESULT_OK){
////                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
////                prof_pic.setImageBitmap(bitmap);
//
//                Uri galleryImageUri=data.getData();
//                prof_pic.setImageURI(galleryImageUri);
//
////                uploadImagetoFirebaseStorage(galleryImageUri);
//
//            }
//
//        }

    }

    private void uploadImagetoFirebaseStorage(Uri ImageUri) {


        StorageReference fileref=reference.child("profilePic"+"."+getFileExtention(ImageUri));
        fileref.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Class_Group class_group=new Class_Group(ImageUri.toString());
//                String classId= root.push().getKey();
                root.child("profilePic").setValue(ImageUri.toString());
                Toast.makeText(ProfileSetting_Activity.this, "Upload successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetting_Activity.this, "Uploading failed to server", Toast.LENGTH_SHORT).show();
            }
        });
//        StorageReference imageRef=storageReference.child(userID).child("ProfilePic.jpg");
//        imageRef.putFile(galleryImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(PFSActivity.this, "Image Successfully updated", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(PFSActivity.this, "Image Upload Failure", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    private String getFileExtention(Uri mUri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(mUri));
    }

}




