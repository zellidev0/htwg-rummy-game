package de.htwg.se.rummy.controller

import de.htwg.se.rummy.controller.component.ContState
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import de.htwg.se.rummy.util.Observable

import scala.collection.SortedSet

trait ControllerInterface extends Observable {

  var desk: DeskInterface
  var cState: ContState.Value
  var userPutTileDown: Int

  def userFinishedPlay(): Unit
  def moveTile(tile1: String, tile2: String): Unit
  def layDownTile(tile: String): Unit
  def currentP: PlayerInterface
  def swState(c: ContState.Value): Unit
  def previousP: PlayerInterface
  def nextP: PlayerInterface
  def addPlayerAndInit(newName: String, max: Int): Unit
  def hasLessThan4Players: Boolean
  def createDesk(amount: Int): Unit
  def switchToNextPlayer(): Unit
  def nameInputFinished(): Unit
  def getTileSet: Set[SortedSet[TileInterface]]
  def getAmountOfPlayers: Int
  def setsOnDeskAreCorrect: Boolean
  def removeTileFromSet(tile: TileInterface): Unit
  def undo(): Unit
  def redo(): Unit
  def viewOfBoard: SortedSet[TileInterface]
  def storeFile(): Unit
  def loadFile(): Unit


}
