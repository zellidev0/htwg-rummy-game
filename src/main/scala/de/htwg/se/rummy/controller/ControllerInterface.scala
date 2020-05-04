package de.htwg.se.rummy.controller

import de.htwg.se.rummy.controller.component.{AnswerState, ControllerState}
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{PlayerInterface, TileInterface}
import de.htwg.se.rummy.util.Observable
import play.api.libs.json.JsObject

import scala.collection.immutable.SortedSet

trait ControllerInterface extends Observable {

  protected var desk: DeskInterface
  protected var answerState: AnswerState.Value = AnswerState.NONE
  protected var controllerState: ControllerState.Value = ControllerState.MENU
  var userPutTileDown = 0

  def userFinishedPlay(): Unit

  def moveTile(thisTile: TileInterface, toThis: TileInterface): Unit

  def layDownTile(tile: TileInterface): Unit

  def addPlayerAndInit(newName: String, max: Int): Unit

  def createDesk(amount: Int): Unit

  def hasLessThan4Players: Boolean

  def switchToNextPlayer(): Unit

  def nameInputFinished(): Unit

  def getAmountOfPlayers: Int

  def setsOnDeskAreCorrect: Boolean

  def removeTileFromSet(tile: TileInterface): Unit

  def undo(): Unit

  def redo(): Unit

  def storeFile(): Unit

  def loadFile(): Unit


  def getCurrentPlayer: PlayerInterface

  def getPreviousPlayer: PlayerInterface

  def getNextPlayer: PlayerInterface


  def currentAnswerState: AnswerState.Value

  def currentControllerState: ControllerState.Value


  def viewOfBoard: SortedSet[TileInterface]

  def viewOfTable: Set[SortedSet[TileInterface]]

  def switchState(answerState: AnswerState.Value, c: ControllerState.Value): Unit

  def toJson: JsObject

  def getPlayerByName(name: String): Option[PlayerInterface]
}
