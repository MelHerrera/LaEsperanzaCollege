package com.app.laesperanzacollege

data class Intro(var name:String, var des:String?, var foto:Int?)
{
    companion object
    {
        var intros=  listOf(
            Intro(
                "Administra",
                "Una app para administrar tus estudiantes, crear cuestionarios y guardar formulas utilizadas en cada clase.",
                R.drawable.admi
            ),
            Intro(
                "Crea tus Cuestionarios",
                "Aprende de forma divertida creando tus cuestionarios para que otros lo puedan resolver.",
                R.drawable.crea
            ),
            Intro(
                "Educación",
                "Enseña a través de esta app interactiva, dividida por grados y mira la mejoría a corto plazo.",
                R.drawable.educa
            )
        )
    }
}
