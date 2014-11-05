package com.lambdalab.thriftdao

import org.scalatest.FunSuite
import com.mongodb.casbah.Imports._

@org.junit.runner.RunWith(classOf[org.scalatest.junit.JUnitRunner])
class QueryIntegrateSuite extends FunSuite {
  // TODO Start mongodb server and perform actual query

  val schema = TestSchemas(SimpleStruct)
  var dao: MongoThriftDao[SimpleStruct, SimpleStruct.type] = null

  val cl =  MongoClient("localhost")  // TODO have a better setup of the test
  var db: MongoDB = null
  try {
    cl.dropDatabase("Test")
    db = cl("Test")
    dao = schema.connect(db)
  } catch {
    case e: Throwable => {
      //      e.printStackTrace()
      printf("DB NOT CONNECTED")
    }
  }

  test("store node") {
    if (db != null) {
      val n = SimpleStruct("x", Some("y"), Some(1))
      dao.store(n)
      assert(dao.findOne(SimpleStruct.StrField -> "x") == Some(n))
    }
  }

  test("update field") {
    if (db != null) {
      val n = SimpleStruct("x")
      dao.store(n)
//      dao.update(List(SimpleStruct.StrField -> "x"), List(SimpleStruct.StrField -> "y"))
      dao.select(SimpleStruct.StrField -> "x").set($(SimpleStruct.StrField) -> "y")
      assert(dao.findOne(SimpleStruct.StrField -> "y") == Some(SimpleStruct("y")))
    }
  }
}
