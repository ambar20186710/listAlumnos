package com.example.listalumnos

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.listalumnos.databinding.ActivityMainNuevoBinding

class MainActivityNuevo : AppCompatActivity() {
    private lateinit var binding: ActivityMainNuevoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //En el onCreate
        binding = ActivityMainNuevoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //creamos la conexion a la DB
        val dbalumnos = DBHelperAlumno(this)
        var db = dbalumnos.readableDatabase

        val consulta = "SELECT * FROM alumnos WHERE id = ?"
        val id = intent.getStringExtra("id")
        val parametros = arrayOf(id.toString())
        val cursor =  db.rawQuery(consulta,parametros)

        if(cursor.count > 0){
            binding.txtNombre.setText("${intent.getStringExtra("nombre").toString()}")
            binding.txtCorreo.setText("${intent.getStringExtra("correo").toString()}")
            binding.txtCuenta.setText("${intent.getStringExtra("cuenta").toString()}")
            binding.txtImage.setText("${intent.getStringExtra("image").toString()}")
            binding.txtDato.setText("Actualización de Alumno")
        }

        //Click en el botón Guardar
        binding.btnGuardar.setOnClickListener {
            // abrir base de datos para escritura
            db = dbalumnos.writableDatabase
            //Pasamos los valores de los editText a variables
            val txtNom = binding.txtNombre.text
            val txtCorr = binding.txtCorreo.text
            val txtImg = binding.txtImage.text
            val txtCue = binding.txtCuenta.text

            if(cursor.count >0) {
                    val valores = ContentValues()
                    valores.put("nombre",txtNom.toString())
                    valores.put("nocuenta",txtCue.toString())
                    valores.put("email",txtCorr.toString())
                    valores.put("imagen",txtImg.toString())
                    val res = db.update("alumnos", valores, "id = ?", parametros)
                    db.close()
                    cursor.close()
                    if(res == -1) {
                        Toast.makeText(this,"No se realizo la actualización", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Se actualizo con éxito",Toast.LENGTH_LONG).show()
                        binding.txtNombre.text.clear()
                        binding.txtCorreo.text.clear()
                        binding.txtCuenta.text.clear()
                        binding.txtImage.text.clear()
                    }
                    //Creamos el Intent para pasarnos al MainActivity
                    val intento2 = Intent(this,MainActivity::class.java)
                    startActivity(intento2)
                }
            else{
            //Pasamos los valores de las cajas a una variable contentValues
            if(txtNom.isNotEmpty() && txtCorr.isNotEmpty() && txtCue.isNotEmpty() && txtImg.isNotEmpty()){
                val newReg = ContentValues()
                newReg.put("nombre",txtNom.toString())
                newReg.put("nocuenta",txtCue.toString())
                newReg.put("email",txtCorr.toString())
                newReg.put("imagen",txtImg.toString())

                //Insetamos el Registro
                val res = db.insert("alumnos",null,newReg)
                db.close()
                cursor.close()

                if(res.toInt() == -1) {
                    Toast.makeText(this,"No se inserto el registro", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Se inserto el registro",Toast.LENGTH_LONG).show()
                    binding.txtNombre.text.clear()
                    binding.txtCorreo.text.clear()
                    binding.txtCuenta.text.clear()
                    binding.txtImage.text.clear()
                }
                //Creamos el Intent para pasarnos al MainActivity
                val intento2 = Intent(this,MainActivity::class.java)
                startActivity(intento2)
            }
            else{
                Toast.makeText(this, "Es necesrio llenar todos los registros", Toast.LENGTH_SHORT).show()
            }

            }

        }
    }
}