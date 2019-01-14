package de.htwg.se.rummy.model.fileIO

import de.htwg.se.rummy.model.DeskInterface

trait FileIOInterface {

  def load: DeskInterface
  def save(desk: DeskInterface): Unit

}
