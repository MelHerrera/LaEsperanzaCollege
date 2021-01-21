package Observers

import android.view.ActionMode
import com.app.laesperanzaedm.model.Quiz
import java.text.FieldPosition

interface QuizzObserver {

    fun QuizzSaved(quizz: Quiz)
    fun actionModeInit()
    fun quizSelection(pos:Int,selected:Boolean)
}