package com.example.btbs_tuan9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.btbs_tuan9.databinding.ActivityEmployeeDetailsBinding

import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValueToView()

        binding.btnDelete.setOnClickListener {
            deleteRecord(intent.getStringExtra("empId").toString())
        }

        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("TenSp").toString(),
                intent.getStringExtra("LoaiSp").toString(),
                intent.getStringExtra("GiaSp").toString()
            )
        }
    }

    private fun openUpdateDialog(
        empId: String,
        tenSp: String,
        loaiSp: String,
        giaSp: String,



    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)
        mDialog.setView(mDialogView)
        //update data
        val edtTenSp = mDialogView.findViewById<EditText>(R.id.edtTenSp)
        val edtLoaiSp = mDialogView.findViewById<EditText>(R.id.edtLoaiSp)
        val edtGiaSp = mDialogView.findViewById<EditText>(R.id.edtGiaSp)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        edtTenSp.setText(intent.getStringExtra("tenSp").toString())
        edtLoaiSp.setText(intent.getStringExtra("loaiSp").toString())
        edtGiaSp.setText(intent.getStringExtra("giaSp").toString())

        mDialog.setTitle("Updating $tenSp data")
        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmployeeData(
                empId,
                edtTenSp.text.toString(),
                edtLoaiSp.text.toString(),
                edtGiaSp.text.toString()
            )
            Toast.makeText(applicationContext,"Updated",Toast.LENGTH_SHORT).show()
            binding.tvTenSP.text = edtTenSp.text.toString()
            binding.tvLoaiSp.text = edtLoaiSp.text.toString()
            binding.tvGia.text = edtGiaSp.text.toString()
            alertDialog.dismiss()
        }

    }

    private fun updateEmployeeData(
        empId: String,
        tenSp: String,
        loaiSp: String,
        giaSp: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Product").child(empId)
        val empInfo = EmployeeModel(empId, tenSp, loaiSp, giaSp)
        dbRef.setValue(empInfo)
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Product").child(id)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this, "Da xoa data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Xoa data that bai ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }
// view data
    private fun setValueToView() {



    val imageView = findViewById<ImageView>(R.id.imgProduct)
    Glide.with(this)
        .load( intent.getStringExtra("anhSp"))
        .into(imageView)

        binding.tvEmpId.text = intent.getStringExtra("empId")
        binding.tvTenSP.text = intent.getStringExtra("tenSp")
        binding.tvLoaiSp.text = intent.getStringExtra("loaiSp")
        binding.tvGia.text = intent.getStringExtra("giaSp")


    }
}