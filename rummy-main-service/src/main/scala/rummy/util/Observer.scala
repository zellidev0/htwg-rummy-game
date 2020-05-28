package rummy.util

import rummy.controller.ControllerInterface

trait Observer {
  def updated(controller: ControllerInterface)
}
