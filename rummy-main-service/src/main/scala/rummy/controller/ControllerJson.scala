package rummy.controller

import model.fileIO.FileIOJson
import play.api.libs.json._
import rummy.controller.impl.Controller
import rummy.util.{AnswerState, ControllerState}

object ControllerJson {
  private val fileIo = new FileIOJson()

  def controllerToJson(controller: ControllerInterface): JsObject = {
    val desk = fileIo.deskToJson(controller.desk).\("desk").get
    Json.obj(
      "desk" -> desk,
      "answer" -> controller.answer,
      "state" -> controller.state,
    )
  } //todo add undomanager

  def jsonToController(json: JsValue): ControllerInterface = {
    val desk = fileIo.jsonToDesk((json \ "desk").get).get
    val answer = (json \ "answer").as[String]
    val state = (json \ "state").as[String]
    Controller(desk, AnswerState.from(answer), ControllerState.from(state))
  } //todo add undomanager

}