package com.nguyenthithao.thestore;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenthithao.thestore.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private boolean isLoggedIn = false;
    private Uri selectedImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        addEvents();
        showUserData();
        checkLoginStatus();
        return view;
    }

    private void checkLoginStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            isLoggedIn = true;
            binding.btnLogin.setVisibility(View.GONE);
            binding.btnSignup.setVisibility(View.GONE);
            binding.btnLogout.setVisibility(View.VISIBLE);
            binding.imgProfile.setVisibility(View.VISIBLE);
            binding.txtName.setVisibility(View.VISIBLE);
            binding.txtPhone.setVisibility(View.GONE);
            binding.txtEmail.setVisibility(View.VISIBLE);
            binding.imgProfile.setOnClickListener(v -> uploadProfileImage());
        } else {
            isLoggedIn = false;
            binding.btnLogout.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.imgProfile.setVisibility(View.VISIBLE);
            binding.txtName.setVisibility(View.GONE);
            binding.txtPhone.setVisibility(View.GONE);
            binding.txtEmail.setVisibility(View.GONE);
        }
    }

    private void uploadProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            binding.imgProfile.setImageURI(selectedImageUri);
            uploadImageToFirebaseStorage();
        }

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                showUserData();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (selectedImageUri != null) {
            StorageReference imageRef = mStorageRef.child("profile_images/" + mAuth.getCurrentUser().getUid());

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateProfileImageInDatabase(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateProfileImageInDatabase(String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            databaseReference.child("profileImage").setValue(imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                        // Sau khi cập nhật thành công, cập nhật lại UI nếu cần thiết
                        // Ví dụ: gọi phương thức showUserData() để cập nhật thông tin người dùng
                        showUserData();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to update profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showUserData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String profileImage = dataSnapshot.child("profileImage").getValue(String.class); // Lấy đường dẫn ảnh từ Firebase

                        binding.txtName.setText(userName);
                        binding.txtPhone.setText(phoneNumber);
                        binding.txtEmail.setText(email);

                        if (profileImage != null && !profileImage.isEmpty()) {
                            Glide.with(getActivity())
                                    .load(profileImage)
                                    .into(binding.imgProfile);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }

    private void addEvents() {
        binding.btnModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to modify your profile", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ProfileInfoActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        binding.btnModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to view your addresses", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to view your rating", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MyReviewActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnViewWhistlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to view your wishlist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), WishlistActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnViewVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoucherActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to logout
                logout();
            }
        });

        binding.btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to view your order history", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSupport();
            }
        });

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(getContext(), "Please log in to change password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    openChagePassword();
                }
            }
        });
    }

    private void openChagePassword() {
        Intent intent = new Intent(getActivity(), ConfirmPasswordActivity.class);
        startActivity(intent);
    }

    private void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
                        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
                        if (!rememberMe) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("email");
                            editor.remove("password");
                            editor.remove("isLoggedIn");
                            editor.apply();
                        }

                        Toast.makeText(getActivity(), "Sign out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.brown_text));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.brown_text));
    }

    public void openSupport() {
        Intent intent = new Intent(getActivity(), SupportActivity.class);
        startActivity(intent);
    }
}