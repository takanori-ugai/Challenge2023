all: data/merged.ttl

expand-all: data/expand.ttl data/expand_motivation.ttl

data/expand_motivation.ttl : data/AddMotivation.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/expand.ttl data/AddMotivation.ttl > data/expand_motivation.ttl

data/AddMotivation.ttl : data/expand.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.AddMotivationKt data/expand.ttl > data/AddMotivation.ttl

clustering1:
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.ClusteringKt data/SpeckledBand.ttl data/expand-vector.bin

clustering0:
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.ClusteringKt data/ACaseOfIdentity.ttl data/expand-vector.bin

data/expand.ttl: data/merged.ttl data/ExpandSituation.ttl data/AddWords.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/merged.ttl data/ExpandSituation.ttl data/AddWords.ttl > data/expand.ttl

data/ExpandSituation.ttl: data/merged.ttl data/merged-vector.bin
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.ExpandSituationKt data/merged.ttl data/merged-vector.bin | tail -n +3 > data/ExpandSituation.ttl

data/AddWords.ttl: data/merged.ttl data/merged-vector.bin
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.AddWordsKt data/merged.ttl data/merged-vector.bin | tail -n +3 > data/AddWords.ttl

data/merged.ttl: data/SpeckledBand-merged.ttl data/ACaseOfIdentity-merged.ttl data/CrookedMan-merged.ttl data/DancingMen-merged.ttl data/DevilsFoot-merged.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/SpeckledBand.ttl data/SpeckledBand-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl data/ACaseOfIdentity.ttl data/ACaseOfIdentity-RelatedWords.ttl data/CrookedMan.ttl data/CrookedMan-RelatedWords.ttl data/DancingMen.ttl data/DancingMen-RelatedWords.ttl data/DevilsFoot.ttl data/DevilsFoot-RelatedWords.ttl > data/merged.ttl

all-bin: data/merged-vector.bin data/SpeckledBand-vector.bin data/ACaseOfIdentity-vector.bin data/CrookedMan-vector.bin data/DancingMen-vector.bin data/DevilsFoot-vector.bin

expand-bin: data/expand-vector.bin data/expand_motivation-vector.bin

data/expand_motivation-vector.bin: data/expand_motivation.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/expand_motivation-vector.bin data/expand_motivation.ttl >> expand_motivation-learn.log

data/expand-vector.bin: data/expand.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/expand-vector.bin data/expand.ttl >> expand-learn.log

data/merged-vector.bin: data/merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/merged-vector.bin data/merged.ttl >> merged-learn.log

# SpeckledBand

data/SpeckledBand-vector.bin: data/SpeckledBand-merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/SpeckledBand-vector.bin data/SpeckledBand-merged.ttl >> SpeckledBand-learn.log

data/SpeckledBand-merged.ttl: data/SpeckledBand.ttl data/SpeckledBand-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl
	/export/home1/ugai/apache-jena-3.13.0/bin/rdfcat -out n3 data/SpeckledBand.ttl data/SpeckledBand-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl > data/SpeckledBand-merged.ttl

data/SpeckledBand-RelatedWords.ttl: data/SpeckledBand.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.OpenNLPKt data/SpeckledBand.ttl > data/SpeckledBand-RelatedWords.ttl

# ACaseOfIdentity

data/ACaseOfIdentity-vector.bin: data/ACaseOfIdentity-merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/ACaseOfIdentity-vector.bin data/ACaseOfIdentity-merged.ttl >> ACaseOfIdentity-learn.log

data/ACaseOfIdentity-merged.ttl: data/ACaseOfIdentity.ttl data/ACaseOfIdentity-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/ACaseOfIdentity.ttl data/ACaseOfIdentity-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl > data/ACaseOfIdentity-merged.ttl

data/ACaseOfIdentity-RelatedWords.ttl: data/ACaseOfIdentity.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.OpenNLPKt data/ACaseOfIdentity.ttl > data/ACaseOfIdentity-RelatedWords.ttl

# CrookedMan

data/CrookedMan-vector.bin: data/CrookedMan-merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/CrookedMan-vector.bin data/CrookedMan-merged.ttl >> CrookedMan-learn.log

data/CrookedMan-merged.ttl: data/CrookedMan.ttl data/CrookedMan-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/CrookedMan.ttl data/CrookedMan-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl > data/CrookedMan-merged.ttl

data/CrookedMan-RelatedWords.ttl: data/CrookedMan.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.OpenNLPKt data/CrookedMan.ttl > data/CrookedMan-RelatedWords.ttl

# DancingMen

data/DancingMen-vector.bin: data/DancingMen-merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/DancingMen-vector.bin data/DancingMen-merged.ttl >> DancingMen-learn.log

data/DancingMen-merged.ttl: data/DancingMen.ttl data/DancingMen-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/DancingMen.ttl data/DancingMen-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl > data/DancingMen-merged.ttl

data/DancingMen-RelatedWords.ttl: data/DancingMen.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.OpenNLPKt data/DancingMen.ttl > data/DancingMen-RelatedWords.ttl

# DevilsFoot

data/DevilsFoot-vector.bin: data/DevilsFoot-merged.ttl
	java -jar ../transe/build/libs/transe-all.jar  -s data/DevilsFoot-vector.bin data/DevilsFoot-merged.ttl >> DevilsFoot-learn.log

data/DevilsFoot-merged.ttl: data/DevilsFoot.ttl data/DevilsFoot-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl
	apache-jena-3.13.0/bin/rdfcat -out n3 data/DevilsFoot.ttl data/DevilsFoot-RelatedWords.ttl data/MeanWordnet.ttl data/MotivationWordnet.ttl dict/wordnet31.ttl > data/DevilsFoot-merged.ttl

data/DevilsFoot-RelatedWords.ttl: data/DevilsFoot.ttl
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.OpenNLPKt data/DevilsFoot.ttl > data/DevilsFoot-RelatedWords.ttl

#
data/MeanWordnet.ttl: MeanWords.txt
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.MotivationWordnetKt MeanWords.txt | sed -e s/motivation_of_murder/way_of_mean/g -e s/motivation/mean/g > data/MeanWordnet.ttl

MeanWords.txt:
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.MeanKt | tail -n +2 > MeanWords.txt

data/MotivationWordnet.ttl: MotivationWords.txt
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.MotivationWordnetKt > data/MotivationWordnet.ttl

MotivationWords.txt:
	java -cp build/libs/challenge2019-all.jar com.fujitsu.labs.challenge2019.MotivationKt | tail -n +2 > MotivationWords.txt
