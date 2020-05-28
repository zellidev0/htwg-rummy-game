package rummy.controller

import model.DeskInterface
import model.deskComp.deskBaseImpl.TileInterface
import play.api.libs.json.JsObject
import rummy.util.{AnswerState, ControllerState, UndoManager}

import scala.collection.immutable.SortedSet

trait ControllerInterface {
  val answer: AnswerState.Value
  val state: ControllerState.Value
  val desk: DeskInterface
  val undoMgr: UndoManager

  def moveTile(thisTile: TileInterface, toThisTile: TileInterface): ControllerInterface
  def layDownTile(tile: TileInterface): ControllerInterface
  def currentPlayerName: String
  def switchToNextPlayer(): ControllerInterface
  def nameInputFinished(): ControllerInterface
  def userFinishedPlay(): ControllerInterface
  def storeFile(): ControllerInterface
  def redo(): ControllerInterface
  def undo(): ControllerInterface
  def createDesk(amount: Int): ControllerInterface
  def toJson: JsObject
  def loadFile(): ControllerInterface
  def addPlayerAndInit(newName: String, max: Int): ControllerInterface
  def viewOfTable: Set[SortedSet[TileInterface]]
  def viewOfBoard: SortedSet[TileInterface]
}
