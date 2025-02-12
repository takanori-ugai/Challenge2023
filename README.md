# International Knowledge Graph Resoning Challenge Program and Data

## Structure
  - src – Sources
  - build     - executables (jars)
- data - intermediate products, graph-embedded data
- dict - word-embedded data, dictionaries such as motivations, means
- gradlew - command to compile source
- Makefile - script for suspect detection
- Form.pdf - Application form

## How to execute

### requirements
 - JDK >= 17
### praparation
- git clone https://github.com/takanori-ugai/Challenge2023.git
- cd Challenge2023
 - ./gradlew build
 - cd dict ; sh prepare.sh ; cd ..
 - cd data ; sh prepare.sh ; cd ..
 - set JAVA_OPTS="-Xms1g -Xmx32g"
 - make
### execution
  - make SpeckledBand # in the case of SpeckledBand
  - make ACaseOfIdentity
  - make CrookedMan
  - make DancingMen
  - make DevilsFoot
  - make AbbeyGrange
  - make ResidentPatient
  - make SilverBlaze

### with link prediction
  - make all-bin
- make SpeckledBand # in the case of SpeckledBand
- make ACaseOfIdentity
- make CrookedMan
- make DancingMen
- make DevilsFoot
- make AbbeyGrange
- make ResidentPatient
- make SilverBlaze

### with Conceptnet
  - modify the Makefile to use Conceptnet1 and Conceptnet2 instead of Wordnet. See the source codes.