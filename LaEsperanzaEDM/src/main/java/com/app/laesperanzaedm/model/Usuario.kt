package com.app.laesperanzaedm.model

import android.os.Parcelable
import java.io.Serializable

class Usuario:Serializable{
    var id:Int?=null
    var nombre:String?=null
    var apellido:String?=null
    var usuario:String?=null
    var contrase:String?=null
    var codGrado:String?=null
    var tipoDeUsuarioId:Int?=null
}