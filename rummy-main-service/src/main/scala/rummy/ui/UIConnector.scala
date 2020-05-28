package rummy.ui

import rummy.controller.ControllerInterface
import rummy.util.Observer

object UIConnector {
  private var controllerInterface: ControllerInterface = _
  private var subscribers: Vector[Observer] = Vector()

  def controller: ControllerInterface =
    controllerInterface

  def add(s: Observer): Unit =
    subscribers = subscribers :+ s

  def remove(s: Observer): Unit =
    subscribers = subscribers.filterNot(o => o == s)

  def notifyObservers(controller: ControllerInterface): Unit =
    subscribers.foreach(o => o.updated(controller))

  def updateController(controller: ControllerInterface): Unit = {
    this.controllerInterface = controller
    notifyObservers(controller)
  }

}
