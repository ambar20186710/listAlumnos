package com.example.listalumnos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    //Vinculacion de las vistas con MainActivity
    private lateinit var binding: ActivityMainBinding
    private var data =  ArrayList<Alumno>()
    private lateinit var rvAdapter: AlumnoAdapter
    private var idAlumno  = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        //Creamos la conexion con la BD
        val dbalummnos = DBHelperAlumno(this)
        //Abrimos la base de datos solo para leer
        val db = dbalummnos.readableDatabase
        //Declaramos un cursor para recorrer los registros en la tabla
        val cursor = db.rawQuery("SELECT * FROM alumnos", null)
        //evaluar if si el cursor de la base de datos se  puede mover
        if(cursor.moveToFirst())
        {
            do {
                //pasar los valores de la tabla a variables locales
                idAlumno = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                var itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                var itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                //agregarlos a la lista data
                data.add(Alumno("${itemNom}",
                    "${itemCue}",
                    "${itemCorr}",
                    "${itemImg}"
                )
                )
            }  while(cursor.moveToNext())
            //cerramos base de datos y cursor
            db.close()
            cursor.close()
            //pasar la informacion del Array por el adaptador
            rvAdapter = AlumnoAdapter(this,data, object : AlumnoAdapter.OptionsMenuClickListener{
                override fun onOptionsMenuClicked(position: Int) {
                    TODO("Not yet implemented")
                    //funcion para llamar menu opciones
                    itemOptionsMenu(position)
                }
            })

            binding.recyclerview.adapter = rvAdapter
        }// fin del if movetofirst



        rvAdapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener{
            override fun onOptionsMenuClicked(position: Int) {
                //Toast.makeText(this@MainActivity, "onItemClick ${position}", Toast.LENGTH_SHORT).show()
                itemOptionsMenu(position)
            }
        })

        // Setting the Adapter with the recyclerview
        binding.recyclerview.adapter = rvAdapter

        binding.faButton.setOnClickListener {
            val intento1 = Intent(this,MainActivityNuevo::class.java)
            //intento1.putExtra("valor1","Hola mundo")
            startActivity(intento1)
        }

        //Variable para recibir extras
        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")

        //Preguntamos se el mensaje es para nuevo alumno
        if (msje=="nuevo"){
            //Sacamos en una variable el total de elementos en nuestra lista
            val insertIndex: Int = data.count()
            //Usamos la variable insertIndex para indicar la posición del nuevo alumno
            data.add(insertIndex,
                Alumno(
                    "${nombre}",
                    "${cuenta}",
                    "${correo}",
                    "${image}"
                )
            )
            //Notificamos que se inserto un nuevo elemento en la lista
            rvAdapter.notifyItemInserted(insertIndex)

        }

    }


    private fun itemOptionsMenu( position: Int) {
        val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)
        //Para cambiarnos de activity
        val intento2 = Intent(this,MainActivityNuevo::class.java)
        //Implementar el click en el item
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.borrar -> {
                        val dbalummnos = DBHelperAlumno(this@MainActivity)
                        val db = dbalummnos.writableDatabase
                        val id = arrayOf(idAlumno.toString())
                        val res = db.delete("alumnos", "id = ?",id)
                        db.close()

                        if(res > 0){
                            val tmpAlum = data[position]
                            data.remove(tmpAlum)
                            rvAdapter.notifyItemChanged(position)
                            Toast.makeText(this@MainActivity, "El elemento se elimino con éxito", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@MainActivity, "Error al eliminar el elemento", Toast.LENGTH_LONG).show()
                        }
                        return true
                    }
                    R.id.editar ->{
                        //Tomamos los datos del alumno, en la posición de la lista donde hicieron click
                        val nombre = data[position].nombre.toString()
                        val cuenta = data[position].cuenta.toString()
                        val correo = data[position].correo.toString()
                        val image = data[position].imagen.toString()
                        //En position tengo el indice del elemento en la lista
                        val idAlum: Int = position
                        intento2.putExtra("id", "${(data.indexOf(Alumno("$nombre","$cuenta","$correo","$image"))) + 1}")
                        intento2.putExtra("mensaje","edit")
                        intento2.putExtra("nombre","${nombre}")
                        intento2.putExtra("cuenta","${cuenta}")
                        intento2.putExtra("correo","${correo}")
                        intento2.putExtra("image","${image}")
                        //Pasamos por extras el idAlum para poder saber cual editar de la lista (ArrayList)
                        intento2.putExtra("idA",idAlum)
                        startActivity(intento2)
                        return true
                    }
                    R.id.ver ->{
                        val intento3 = Intent(this@MainActivity, verInfo::class.java)

                        val nombre = data[position].nombre.toString()
                        val cuenta = data[position].cuenta.toString()
                        val correo = data[position].correo.toString()
                        val image = data[position].imagen.toString()

                        intento3.putExtra("nombre","${nombre}")
                        intento3.putExtra("cuenta","${cuenta}")
                        intento3.putExtra("correo","${correo}")
                        intento3.putExtra("image","${image}")

                        startActivity(intento3)
                    }
                }
                return false
            }
        })
        popupMenu.show()

    }
}