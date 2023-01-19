wget https://www.apache.org/dist/opennlp/opennlp-1.9.1/apache-opennlp-1.9.1-bin.tar.gz
tar xf apache-opennlp-1.9.1-bin.tar.gz
wget http://opennlp.sourceforge.net/models-1.5/en-token.bin
wget http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin
# wget https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict
wget https://github.com/richardwilly98/elasticsearch-opennlp-auto-tagging/raw/master/src/main/resources/models/en-lemmatizer.dict
apache-opennlp-1.9.1/bin/opennlp LemmatizerTrainerME -model en-lemmatizer.bin -lang en -data en-lemmatizer.dict -encoding UTF-8
wget https://dl.fbaipublicfiles.com/fasttext/vectors-crawl/cc.en.300.bin.gz
gunzip cc.en.300.bin.gz
wget https://www.w3.org/2006/03/wn/wn20/download/wn20full.zip
unzip wn30full.zip
