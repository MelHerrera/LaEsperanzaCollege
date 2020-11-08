package Observers

import com.app.laesperanzaedm.model.Quiz
import java.text.FieldPosition

interface QuizzObserver {

    fun QuizzSaved(quizz: Quiz)
}