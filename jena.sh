#! /bin/sh
JENA_HOME=`pwd`/apache-jena-3.13.0
export JENA_HOME
PATH=$JENA_HOME/bin:$PATH
$JENA_HOME/bin/sparql $*
