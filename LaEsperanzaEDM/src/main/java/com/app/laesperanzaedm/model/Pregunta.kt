package com.app.laesperanzaedm.model

import java.io.Serializable

class Pregunta:Serializable {
    var id:Int?=null
    var descripcion:String?=null
    var opcionDeRespuestaId:Int?=null
    var quizzId:Int?=null
}