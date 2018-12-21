package model.fileIO

import model.DeskInterface

trait FileIOInterface {

  def load: DeskInterface
  def save(desk: DeskInterface): Unit

}
