package com.mammothdatallc.spark

import com.mammothdatallc.proto.MyProtocolBuffers
import com.twitter.elephantbird.mapreduce.io.ProtobufWritable
import com.twitter.elephantbird.mapreduce.output.LzoProtobufBlockOutputFormat
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.mapreduce.Job
import org.apache.spark._

object SparkWriteProtoBuf {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("SparkWriteProtoBuf")
    val sc = new SparkContext(sparkConf)

    val job = Job.getInstance()
    val conf = job.getConfiguration
    LzoProtobufBlockOutputFormat.setClassConf(classOf[MyProtocolBuffers.MyProtocolBuffer], conf)

    val protoBuilder = MyProtocolBuffers.MyProtocolBuffer.newBuilder()
    protoBuilder.setGreeting("Hello World!")

    val data = sc.parallelize(List(protoBuilder.build()))

    val outputData = data.map { pb =>
      val protoWritable = ProtobufWritable.newInstance(classOf[MyProtocolBuffers.MyProtocolBuffer])
      protoWritable.set(pb)
      (new LongWritable(0), protoWritable)
    }

    outputData.saveAsNewAPIHadoopFile("/tmp/SparkWriteProtoBuf", classOf[LongWritable],
      classOf[ProtobufWritable[MyProtocolBuffers.MyProtocolBuffer]],
      classOf[LzoProtobufBlockOutputFormat[ProtobufWritable[MyProtocolBuffers.MyProtocolBuffer]]], conf)

    sc.stop()

  }
}