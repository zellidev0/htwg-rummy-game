object UIConnector {
  var controller: ControllerInterface = null
  var subscribers: Vector[Observer]   = Vector()
  def contr: ControllerInterface =
    controller
  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

  def updateController(controller: ControllerInterface): Unit = {
    this.controller = controller
    notifyObservers(controller)
  }

  def notifyObservers(controller: ControllerInterface): Unit = subscribers.foreach(o => o.updated(controller))

}
