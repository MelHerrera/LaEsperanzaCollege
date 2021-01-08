package Observers

interface ViewPagerObserver {
    fun paginaSiguiente()
    fun paginaAnterior()
    fun paginaPrimera():Int
    fun estaEnPaginaPrimera():Boolean
}