package de.htwg.se.rummy.controller.component.command

import de.htwg.se.rummy.controller.component.{AnswerState, Controller, ControllerState}
import de.htwg.se.rummy.model.DeskInterface
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.deskImpl.{Board, Player}
import de.htwg.se.rummy.model.deskComp.deskBaseImpl.{Desk, PlayerInterface, TileInterface}
import de.htwg.se.rummy.util.Command

import scala.collection.immutable.SortedSet


class NameCommand(newName: String, max: Int, controller: Controller) extends Command {

  override def undoStep(): Unit = {
    controller.desk = removePlayerFromDesk(controller.desk)
    controller.switchState(AnswerState.REMOVED_PLAYER, ControllerState.INSERTING_NAMES)
  }

  override def redoStep(): Unit =
    doStep()

  override def doStep(): Unit = {
    controller.desk = takeMaxTilesFromBagToPlayersBoard(addPlayerToDesk(controller.desk))
    controller.switchState(AnswerState.ADDED_PLAYER, ControllerState.INSERTING_NAMES)
  }

  /** Adds 12 tiles to the player. Works recursively. */
  def takeMaxTilesFromBagToPlayersBoard(desk: DeskInterface, count: Int = 0): Desk = count match {
    case this.max => takeTileFromBagToPlayer(desk)
    case _ => takeMaxTilesFromBagToPlayersBoard(takeTileFromBagToPlayer(desk), count + 1)
  }

  /** Wrapper to take one tile from the bag and put it on the users board */
  def takeTileFromBagToPlayer(desk: DeskInterface): Desk =
    desk.takeTileFromBagToPlayer(desk.players.find(pl => pl.name == newName).get, desk.getTileFromBag)

  /** Wrapper to add the player to the desk */
  def addPlayerToDesk(desk: DeskInterface): DeskInterface =
    desk addPlayer Player(newName, Board(SortedSet[TileInterface]()), desk.amountOfPlayers == 0)

  /** Wrapper to remove a player from the desk */
  def removePlayerFromDesk(desk: DeskInterface): DeskInterface =
    desk removePlayer desk.players.find(p => p.name == newName).get

}
