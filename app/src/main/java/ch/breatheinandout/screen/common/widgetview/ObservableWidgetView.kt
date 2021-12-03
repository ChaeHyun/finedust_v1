package ch.breatheinandout.screen.common.widgetview

/* This interface makes the class as Observable characteristics in it. */
interface ObservableWidgetView<LISTENER> : WidgetView {
    fun registerListener(listener: LISTENER)
    fun unregisterListener(listener: LISTENER)
}