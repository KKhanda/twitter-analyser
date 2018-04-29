# Scala based Twitter analyzer
This twitter analyzer is able to determine 
20 topics clusters grouping them by most popular terms. 
[Latent Dirichlet Allocation](https://en.wikipedia.org/wiki/Latent_Dirichlet_allocation)
 was used as a model.
 
## Prerequisites
* Install [Cassandra](http://cassandra.apache.org)
* Launch Cassanda 
  * Open terminal
  * Type ```cassandra -f```
 
## How to run
Twits are going to be collected for one hour.
This option could be changed for streamingContext in TwitterRunnner.scala.
 
* ```git clone https://github.com/KKhanda/twitter-analyser.git```
* ```cd twitter-analyzer```
* ```sbt run``` 

## Importing Cassandra dump
I have provided dump with about 11 thousand twits in repository.
If you want to use it, you should upload dump into Cassandra. 
Here is the command, which should be executed from ```cqlsh``` 
when you are in project folder:
* ```COPY twits.message FROM './data/twits-data.csv' WITH DELIMITER = ',' AND QUOTE = '"' AND NULL = '';```
