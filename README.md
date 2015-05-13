# Overview

This project demonstrates how to read and write [protocol buffers](https://developers.google.com/protocol-buffers/docs/overview) using Twitter's [elephant-bird](https://github.com/twitter/elephant-bird/) 
Library with Spark.

## Protocol Buffer codegen

In order to generate the Java classes, you'll need to install protoc, which on Mac OSX can be done via:
    
    brew install protobuf250
    
You can compile the example *myprotocolbuffers.proto* in this project from the root directory with:

    protoc --proto_path=src/main/resources --java_out=src/main/java src/main/resources/myprotocolbuffers.proto
    
## Hadoop + LZO

The elephant-bird library requires that any of it's LZO code has access to the native LZO library for Hadoop. Installing HDFS is out of scope for this project, but installing these native LZO libraries for the major Hadoop distributions is pretty straightforward. For example, here are the instructions for Hortonworks [here](http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.2.4/HDP_Man_Install_v224/index.html) and [here](http://docs.hortonworks.com/HDPDocuments/Ambari-2.0.0.0/Ambari_Doc_Suite/ADS_v200.html#ref-52d8e014-e1bc-4daa-8bab-904567f2c445).
    
## Spark

    spark-submit --class com.mammothdatallc.spark.SparkWriteProtoBuf --master local target/scala-2.10/spark-protobuf-assembly-1.0.jar --driver-class-path target/scala-2.10/spark-protobuf-assembly-1.0.jar:/usr/lib/hadoop/lib/native/Linux-amd64-64/ --driver-library-path /usr/lib/hadoop/lib/native/Linux-amd64-64/
    spark-submit --class com.mammothdatallc.spark.SparkReadProtoBuf --master local target/scala-2.10/spark-protobuf-assembly-1.0.jar --driver-class-path target/scala-2.10/spark-protobuf-assembly-1.0.jar:/usr/lib/hadoop/lib/native/Linux-amd64-64/ --driver-library-path /usr/lib/hadoop/lib/native/Linux-amd64-64/