# ナレッジグラフ推論チャレンジ プログラム、データ等の提出物
 - チーム名: 上小田中411
## 提出物のディレクトリ構造
  - src – ナレッジグラフを操作するコマンドのソース
  - build     - ナレッジグラフを操作するコマンドの実行系
  - data - 中間生成物, グラフ埋め込みデータ
  - dict – 単語埋め込みデータ、分かち書き等の辞書
  - gradlew - ソースをコンパイルするコマンド
  - Makefile - 容疑者を検出するスクリプト
  - Form.pdf - 応募フォーム
## 実行方法
  - git clone https://github.com/KGChallenge/Challenge2019.git
  - cd Challenge2019
  - google drive の次の場所から dict.tar.gz と data.tar.gz を入手する
    - https://drive.google.com/drive/folders/1UN8_bUo4qvWUyb-tUS6aq_xP0_3pmxmw?usp=sharing
  - tar xf dict.tar.gz
  - tar xf data.tar.gz
  - make SpeckledBand #「まだらのひも」場合
  - make ACaseOfIdentity
  - make CrookedMan
  - make DancingMen
  - make DevilsFoot
