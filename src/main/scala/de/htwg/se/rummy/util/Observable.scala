package de.htwg.se.rummy.util

class Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

  protected def notifyObservers(s: String): Unit = subscribers.foreach(o => o.update(s))
}

