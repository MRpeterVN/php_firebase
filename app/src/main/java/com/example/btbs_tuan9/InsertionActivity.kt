package com.example.btbs_tuan9

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.btbs_tuan9.databinding.ActivityInsertionBinding


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class InsertionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertionBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var selectedImageUri: String? = null
    private var selectedImageUrl: Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Product")
        storageRef = FirebaseStorage.getInstance().reference.child("employee_images") // khởi tạo storageRef

        binding.btnSave.setOnClickListener {
            saveEmployeeData()
        }
        binding.btnChoose.setOnClickListener {
            getImage()
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUrl = result.data?.data
            selectedImageUrl = imageUrl
            Glide.with(this)
                .load(selectedImageUrl)
                .into(binding.imageView)
        }

}


    private fun saveEmployeeData() {
        val tenSp = binding.edtTenSP.text.toString()
        val loaiSp = binding.edtLoaiSp.text.toString()
        val giaSp = binding.edtGiaSp.text.toString()

        if (tenSp.isEmpty()) {
            binding.edtTenSP.error = "Hay nhap ten sp"
            return
        }
        if (loaiSp.isEmpty()) {
            binding.edtLoaiSp.error = "Hay nhap loai sp"
            return
        }
        if (giaSp.isEmpty()) {
            binding.edtGiaSp.error = "Hay nhap gia"
            return
        }

        val storageRef = FirebaseStorage.getInstance().getReference("product_images")

        if (selectedImageUrl != null) {
            val filePath = storageRef.child(Calendar.getInstance().time.toString())

            filePath.putFile(selectedImageUrl!!)
                .addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show()

                    // Lấy URL của ảnh được upload lên Firebase Storage
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Lưu URL vào thuộc tính của lớp hoặc truyền nó cho EmployeeModel
                        selectedImageUri = imageUrl

                        val empId = dbRef.push().key!!
                        val employee = EmployeeModel(empId, tenSp, loaiSp, giaSp, imageUrl)

                        // push data to Firebase
                        dbRef.child(empId).setValue(employee)
                            .addOnCompleteListener {
                                Toast.makeText(this, "Them data thanh cong", Toast.LENGTH_SHORT).show()
                                binding.edtTenSP.setText("")
                                binding.edtLoaiSp.setText("")
                                binding.edtGiaSp.setText("")

                                // Xóa ảnh khỏi ImageView
                                Glide.with(this).clear(binding.imageView)
                            }
                            .addOnFailureListener { err ->
                                Toast.makeText(
                                    this,
                                    "Them data that bai ${err.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select an image to upload", Toast.LENGTH_SHORT).show()
        }
    }

}