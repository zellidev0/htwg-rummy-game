package rummy

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RummySimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://127.0.0.1:9001")
		.inferHtmlResources()
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("PostmanRuntime/7.25.0")

	val headers_0 = Map(
		"Accept" -> "*/*",
		"Postman-Token" -> "ab22abac-de03-4352-b888-81cc3b568ea4")

	val headers_1 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "en-gb",
		"Proxy-Connection" -> "keep-alive",
		"User-Agent" -> "com.apple.trustd/2.0")

	val headers_2 = Map(
		"A-IM" -> "x-bm,gzip",
		"Accept-Encoding" -> "gzip, deflate",
		"Proxy-Connection" -> "keep-alive",
		"User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36")

	val headers_3 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "821ca418-c57b-454f-93cd-c1a67d13ee40")

	val headers_4 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "9f5d9a91-cfee-4ba6-ba23-f7caffb427ef")

	val headers_8 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "64cb481f-5404-4088-8f42-276b99497491")

	val headers_9 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "5b1cb69a-1038-42f7-bcd3-e12296e0f560")

	val headers_10 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "0487c077-f4f9-4220-ad58-656e82314990")

	val headers_11 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "05e76514-676a-4613-8a2c-ef67ccc220e4")

	val headers_12 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "e321d18f-40d9-47db-8da9-ffddc601184f")

	val headers_13 = Map(
		"Accept" -> "*/*",
		"Content-Type" -> "application/json",
		"Postman-Token" -> "6ac54eeb-ac22-487e-8ecd-ba686bbc3401")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"
    val uri2 = "127.0.0.1"
    val uri3 = "http://ocsp.apple.com"

	val scn = scenario("RecordedSimulation")
		.exec(http("request_0")
			.post("http://" + uri2 + ":9000/api/createDesk")
			.headers(headers_0))
		.pause(4)
		.exec(http("request_1")
			.get(uri3 + "/ocsp03-wwdr04/ME4wTKADAgEAMEUwQzBBMAkGBSsOAwIaBQAEFADrDMz0cWy6RiOj1S%2BY1D32MKkdBBSIJxcJqbYYYIvs67r2R1nFUlSjtwIIDutXh%2BeeCY0%3D")
			.headers(headers_1))
		.pause(22)
		.exec(http("request_2")
			.get(uri1 + "?osname=mac&channel=stable&milestone=83")
			.headers(headers_2))
		.pause(5)
		.exec(http("request_3")
			.post("http://" + uri2 + ":9000/api/addPlayer")
			.headers(headers_3)
			.body(RawFileBody("rummy/recordedsimulation/0003_request.json")))
		.pause(14)
		.exec(http("request_4")
			.post("http://" + uri2 + ":9000/api/addPlayer")
			.headers(headers_4)
			.body(RawFileBody("rummy/recordedsimulation/0004_request.json")))
		.pause(18)
		.exec(http("request_5")
			.get(uri3 + "/ocsp03-applerootcag3/MFYwVKADAgEAME0wSzBJMAkGBSsOAwIaBQAEFDQ9%2BWuiBh%2FOOBXswAMX5FRQRzCbBBS7sN6hWDOImqSKmd6%2Bveuv2sskqwIQVvuD1Cv%2FjcM3mSO1Wq5uvQ%3D%3D")
			.headers(headers_1)
			.resources(http("request_6")
			.get(uri3 + "/ocsp03-aaica5g101/MFYwVKADAgEAME0wSzBJMAkGBSsOAwIaBQAEFBT37KkWkFo69DN2RUfQaakllk%2FsBBTZF%2F5LZ5A4S5L0287VV4AUC489yQIQGj18DlTl3ZNPZTCmLDsBaQ%3D%3D")
			.headers(headers_1),
            http("request_7")
			.get(uri3 + "/ocsp03-aaica5g101/MFYwVKADAgEAME0wSzBJMAkGBSsOAwIaBQAEFBT37KkWkFo69DN2RUfQaakllk%2FsBBTZF%2F5LZ5A4S5L0287VV4AUC489yQIQIV3BGA0TnAJYQ5LfsL4OiQ%3D%3D")
			.headers(headers_1)))
		.pause(3)
		.exec(http("request_8")
			.post("http://" + uri2 + ":9000/api/addPlayer")
			.headers(headers_8)
			.body(RawFileBody("rummy/recordedsimulation/0008_request.json")))
		.pause(35)
		.exec(http("request_9")
			.post("/players/save")
			.headers(headers_9)
			.body(RawFileBody("rummy/recordedsimulation/0009_request.json")))
		.pause(5)
		.exec(http("request_10")
			.post("/players/save")
			.headers(headers_10)
			.body(RawFileBody("rummy/recordedsimulation/0010_request.json")))
		.pause(5)
		.exec(http("request_11")
			.post("/players/load")
			.headers(headers_11)
			.body(RawFileBody("rummy/recordedsimulation/0011_request.json")))
		.pause(5)
		.exec(http("request_12")
			.post("/game/save")
			.headers(headers_12)
			.body(RawFileBody("rummy/recordedsimulation/0012_request.json")))
		.pause(5)
		.exec(http("request_13")
			.post("/game/load")
			.headers(headers_13)
			.body(RawFileBody("rummy/recordedsimulation/0013_request.json")))

	setUp(scn.inject(atOnceUsers(50))).protocols(httpProtocol)
}