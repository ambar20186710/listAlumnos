package com.example.listalumnos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperAlumno (context: Context) : SQLiteOpenHelper(context, DB_name, null, DB_version){
    companion object{
        private var DB_version = 1
        private val DB_name = "dbalumnos.db"
        private val nomTabla = "alumnos"
        private val keyid   = "id"
        private val cuenta = "nocuenta"
        private val nom = "nombre"
        private val correo = "email"
        private val img = "imagen"
    }
    val sqlCreate: String = "CREATE TABLE $nomTabla ($keyid INTEGER PRIMARY KEY, $nom TEXT, $cuenta TEXT, $correo TEXT, $img TEXT)"



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $nomTabla")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}