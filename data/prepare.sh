#! bin/sh
wget https://github.com/KnowledgeGraphJapan/KGRC-RDF/archive/refs/heads/ikgrc2023.zip
unzip ikgrc2023.zip
mv KGRC-RDF-ikgrc2023/* .
rm ikgrc2023.zip
rm -r KGRC-RDF-ikgrc2023