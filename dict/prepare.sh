#! /bin/sh
wget https://downloads.apache.org/opennlp/opennlp-2.1.0/apache-opennlp-2.1.0-bin.tar.gz
tar xf apache-opennlp-2.1.0-bin.tar.gz
wget http://opennlp.sourceforge.net/models-1.5/en-token.bin
wget http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin
# wget https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict
wget https://github.com/richardwilly98/elasticsearch-opennlp-auto-tagging/raw/master/src/main/resources/models/en-lemmatizer.dict
apache-opennlp-2.1.0/bin/opennlp LemmatizerTrainerME -model en-lemmatizer.bin -lang en -data en-lemmatizer.dict -encoding UTF-8
wget https://dl.fbaipublicfiles.com/fasttext/vectors-crawl/cc.en.300.bin.gz
gunzip cc.en.300.bin.gz
gunzip wordnet31.ttl.gz
