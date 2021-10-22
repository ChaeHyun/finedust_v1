package ch.breatheinandout.screen.widgetview

/* This interface makes the class as Observable characteristics in it. */
interface ObservableWidgetView<LISTENER> : WidgetView {
    fun registerListener(listener: LISTENER)
    fun unregisterListener(listener: LISTENER)
}