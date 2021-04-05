import java.io.BufferedReader

import com.hainiu.spark.db.HbaseConnection
import com.hainiu.spark.utils.{FileUtil, Util}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.client.{Result, ResultScanner, Scan, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil}
import org.jsoup.Jsoup

import scala.collection.mutable

object test {
  def main(args: Array[String]): Unit = {
    val reader: BufferedReader = FileUtil.getBufferedReader(new Path("file:///Users/leohe/Desktop/new.html"))
    var html: String = ""
    var htmlT: String = reader.readLine()
    while (htmlT != null) {
      html += htmlT
      htmlT = reader.readLine()
    }

    val doc = Jsoup.parse(html)


    val set = new mutable.HashSet[String]()
    set.add("//div[@class='content_desc']/p")
    val context: String = Util.getContext(doc, set, new mutable.HashSet[String]())
    println(context)

    //    readHtml
  }

  //读取hbase中的html,只适用于已经抽取成功的正文判断是否正确
  def readHtml() = {
    val table: Table = HbaseConnection.getTable("context_extract")

    val s = new Scan()
    s.setStartRow("www.cankaoxiaoxi.com_".getBytes())
    s.setStopRow(Bytes.toBytes("www.cankaoxiaoxi.com_z"))
    s.setCacheBlocks(false)
    s.setCaching(1000)

    val scanner: ResultScanner = table.getScanner(s)

    import scala.collection.convert.wrapAll._
    for (f <- scanner) {
      val result: Result = f.asInstanceOf[Result]
      val cells: Array[Cell] = result.rawCells
      for (c <- cells) {
        println("family:" + new String(CellUtil.cloneFamily(c)) +
          " qualifier:" + new String(CellUtil.cloneQualifier(c)) +
          " value:" + Bytes.toString(CellUtil.cloneValue(c)) +
          " version:" + c.getTimestamp())
      }
    }

  }

}
