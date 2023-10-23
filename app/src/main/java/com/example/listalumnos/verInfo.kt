package com.example.listalumnos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.listalumnos.databinding.ActivityMainBinding
import com.example.listalumnos.databinding.ActivityVerInfoBinding

class verInfo : AppCompatActivity() {

    private lateinit var binding: ActivityVerInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_info)

        binding = ActivityVerInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = binding.txtNombre2
        val correo = binding.txtCorr
        val cuenta = binding.txtCuenta2
        val imagen = binding.image

        nombre.setText("Nombre: ${intent.getStringExtra("nombre").toString()}")
        correo.setText("Correo: ${intent.getStringExtra("correo").toString()}")
        cuenta.setText("Número de Cuenta: ${intent.getStringExtra("cuenta").toString()}")
        Glide.with(this).load("${intent.getStringExtra("image").toString()}").into(imagen)

    }
}