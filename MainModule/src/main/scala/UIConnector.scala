object UIConnector {
  var controller: ControllerInterface = _
  var subscribers: Vector[Observer]   = Vector()

  def contr: ControllerInterface                             = controller
  def add(s: Observer): Unit                                 = subscribers = subscribers :+ s
  def remove(s: Observer): Unit                              = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers(controller: ControllerInterface): Unit = subscribers.foreach(o => o.updated(controller))
  def updateController(controller: ControllerInterface): Unit = {
    this.controller = controller
    notifyObservers(controller)
  }

}
