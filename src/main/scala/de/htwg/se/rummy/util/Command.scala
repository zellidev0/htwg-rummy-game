package de.htwg.se.rummy.util

trait Command {

  def doStep(): Unit
  def undoStep(): Unit
  def redoStep(): Unit

}

