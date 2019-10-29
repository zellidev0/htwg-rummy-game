package de.htwg.se.rummy.model.fileIO

import de.htwg.se.rummy.model.deskComp.deskBaseImpl.Desk

trait FileIOInterface {

  def load: Desk
  def save(desk: Desk): Unit

}
