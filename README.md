<div align="center">
    <img src="https://github.com/user-attachments/assets/68660c8d-c4d9-4e0b-9aec-190955e87da9" title="s-290logo" width="450px">
</div>

# S-290フライトディスプレイ備忘録 兼 引き継ぎ資料

## はじめに
初めまして。芝浦工業大学大学院 理工学研究科 電気電子情報学専攻M1の先山倫太郎です。<br>
ここではフライトディスプレイに用いたAndroidアプリ開発についての記録を残してあります。操舵系・計測機器系に関するリポジトリは次のURLにまとめてあるので参考にしてください。<br>
https://github.com/Sakiyama-Rintaro/s-290-Polaris



## 免責事項
実機との接続テストや長時間の稼働、十分なバグ修正が出来ていないので、このまま実機で使ったときの挙動については保証できません。  
表示のバグによってパイロットの命に関わる可能性もありますので、くれぐれも十分なデバッグを行った上でお使いください。

## 導入  
このリポジトリでは
* Androidアプリケーション  
* Webアプリケーション  
* DBサーバー
* Webサーバー
* GIS  

に関することが詰まっています。メインのシステムその他は他の資料を参考にしてください。
なるべく一から書くようにしていますが、割と難しいと思うので、基本情報技術者の本([キタミ式イラストIT塾基本情報技術者](https://gihyo.jp/book/2020/978-4-297-11781-8))で「データベースとは？」「通信はどのように行われているのか？」「開発の手順は？」など基礎を勉強することをお勧めします。IP通信については[マスタリングTCP/IP](https://www.amazon.co.jp/%E3%83%9E%E3%82%B9%E3%82%BF%E3%83%AA%E3%83%B3%E3%82%B0TCP-IP%E2%80%95%E5%85%A5%E9%96%80%E7%B7%A8%E2%80%95-%E7%AC%AC6%E7%89%88-%E4%BA%95%E4%B8%8A-%E7%9B%B4%E4%B9%9F/dp/4274224473)がおすすめです。    
誤りがあったり、適当にコピペしてたりするので、プライベートリポジトリで作っています。（外部には公開しないでください）    
内容に誤りがあるかもしれません。迷ったら自分を信じましょう。誤りを見つけたら、連絡するか編集リクエスト送ってください。

## リポジトリの説明
* flightDisplayForS270Malus `//完成品が入っています`
    * Android `//Androidアプリです`
        * flightDisplayForS270Malus `// パッケージです`
    * Server `//サーバーで動くプログラムです`
* doc `//ドキュメントが入っています`
* [module](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/tree/main/module) `// 単体テストで用いたものが入っています。（studyと重複はあるが、基本有用なものがあるはず。）`
* study `// 勉強に用いたものです。（moduleと重複はあるが、動くのか動かないのかわからないものも入ってる。）`
### もくじ
* [はじめに](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/main/README.md) `-いまここ`
    * 導入 
    * システムアーキテクチャ
    * 用語集
    * 謝辞
* [セットアップ](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/md/set-up.md)
    * S-290のフライトディスプレイをとりあえず再現しよう
        * Windows
        * Android
        * Raspberry Pi
* [プログラムガイド](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/md/guide.md) 
    * 設計思想 
    * Android
        * 設計思想
        * ライブラリ
    * Raspberry Pi
        * 設計思想
        * ライブラリ
* [こつ](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/md/tips.md)
    * 活用したサイト・ソフトウェア
    * 通信契約について
    * Android Studio
        * ライブラリの追加
        * Wi-Fiデバッグ
* [おまけ](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/md/omake.md)気が向いたら書きます。
    * 情報コンセントを作る方法


## 既知のバグ・やり残したこと
[Issues](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/issues)から見られるはずです。

## ⚠セキュリティの警告⚠
[攻撃ログ](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/bf_atk.log)  
これは総当たり攻撃（ブルートフォースアタック）のログです。存在しそうなユーザー名で1秒間に10回弱ログインできないかを試みられています。このログはログインに失敗した（＝攻撃に失敗した）というログですが、インターネットから疎通できるサーバーを設置するとこのような脅威があります。適切なセキュリティ対策を行わないとログインが成功し、乗っ取られて個人情報が取られたり、ウイルスに感染しこのサーバーを踏み台としたサイバー攻撃に使われたりします。踏み台に使われると[不正アクセス禁止法の冤罪をかけられる](https://ja.wikipedia.org/wiki/%E3%83%91%E3%82%BD%E3%82%B3%E3%83%B3%E9%81%A0%E9%9A%94%E6%93%8D%E4%BD%9C%E4%BA%8B%E4%BB%B6)というめんどくさい事になる可能性もあります。インターネットから疎通できるサーバーを使う場合にはセキュリティ対策には十分に気を使いましょう。

## システムアーキテクチャ
まず、機体に搭載されているマイコンは対気速度計や回転数計などからデータを収集します。（この辺の仕組みは別に引き継ぎ資料があるはずです。たぶん）  
収集されたデータはUSBシリアル通信により、Android端末へ送られます。  
Androidアプリでデータを処理し、パイロットのために計器データや地図を表示します。  
そして、同時に地上やボート上にいる設計者などが機体のデータを確認するために、サーバーに位置情報や対気速度や回転数データを送信します。  
送信されたデータはサーバーのMySQLと呼ばれるデータベースにインポートされ、PythonスクリプトによKMLとcsv形式で出力されArcGISがそれらのファイルを参照することにより、Web上で地図やグラフの形で確認することが出来ます。
![全体の構成](https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/img/fddesign.jpg)
### Androidアプリ
<img src="https://github.com/TeamBirdmanTrial/FlightDisplay.arajin.S270/blob/master/doc/img/Androidimage.jpg" title="android" width="250px">
グラフはいくらでも追加できるような仕組みで実装しているので、速度や高度を想定していますが、回転数等も取得出来るのであれば、表示するようにできます。  

[動作イメージ](https://www.youtube.com/embed/85NR1H4mioA)


### サーバー
#### DB
<img src="https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/img/Sqlimage.png" title="sql" width="250px">
このようにDBに記録されていきます。


#### 転送
<img src="https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/img/Transimage.png" title="transfer" width="250px">
DBから抽出してArcGIS Onlineへ転送します

### Webアプリ

<img src="https://github.com/Sakiyama-Rintaro/FlightDisplay.S290/blob/master/doc/img/Webimage.png" title="web" width="250px">
数秒に一度更新されます。
自前のWebサイトでコードを書けば1秒間隔とかでも更新することが出来ます。

### Kotlin
Androidアプリ開発で用いることのできる言語には主に、JavaとKotlinがあります。（他の言語でもできなくはないけど）  
KotlinはJavaより新しい言語で、Androidが公式サポートをして、有名になりました。最近のアプリ開発ではKotlinを使うのがトレンドらしいです。TBTではS-290で初めてKotlinを採用してみました  
モダンなプログラミング言語は書きやすかったり、可読性が高いので好きです。  
このサイトを見るとAndroidアプリ開発におけるKotlinの有用性が何となくわかるかもしれません。https://developer.android.com/kotlin?hl=ja


採用した理由&メリット  
* 新しいものが好きだから
* トレンドだから
* かっこいいから
* おしゃれだから
* 構文がシンプルで書きやすいから
* なんと行末に;要らない！！！
* Javaとも互換性があるから（ある程度は自動変換とかもしてくれる）
* null安全など、予期しないクラッシュを防げるアプリ作りが出来るから
* どうせ引継ぎ資料も無かったし、自分が使いたいやつでやったろうと思ったから

デメリット  
* 新しい言語なので、サンプルや情報が少ない
* Kotlinだけでなく、Javaの知識も少し要るかも


### ちょっとしたコメント
TBTでは、フライトの情報（高度や機速など）を表示するのにAndroid端末を利用しています。  
個人的には、マイコンにディスプレイ（[こういうの](https://www.google.com/search?q=STM32+%E3%83%87%E3%82%A3%E3%82%B9%E3%83%97%E3%83%AC%E3%82%A4&tbm=isch&ved=2ahUKEwjjoriirYTvAhUEdJQKHaISAV4Q2-cCegQIABAA&oq=STM32+%E3%83%87%E3%82%A3%E3%82%B9%E3%83%97%E3%83%AC%E3%82%A4&gs_lcp=CgNpbWcQA1CbHljxHmCVIGgAcAB4AIABTIgBkAGSAQEymAEAoAEBqgELZ3dzLXdpei1pbWfAAQE&sclient=img&ei=VDw3YOOoFYTo0QSipYTwBQ&bih=937&biw=1920#imgrc=xPFi9psR1x7rCM)）を付けたかったのですが、バッテリーや防水性などいろいろ考えると、Androidのほうが優れているのでは無いかと考え、結局Android端末を利用しました。  
何か良い方法が思いつけば、その選択もありだと思います。KotlinやAndroidの勉強をするより、マイコンで使ってるC言語で書けたほうがいいよね。あとは、ラズパイとか使うなら、最近流行りのPythonで書けるしね。  
マイコンとAndroid端末で通信するにはUSBやBluetooth、Wi-Fiなどいろいろあると思いますが、S-290ではUSBの有線での通信を採用しました。  
個人的にはBluetooth5.xによる通信でもいいと思いますが、過去の慣例とまだチップがほとんど存在しないことから見送りました。興味があったら調べてみてください。  
なお、操縦を無線化するのは流石に危険だと思うので止めるべきです。  
機種はXperia XZsを採用しました。自分がXperiaを使っていて、慣れていたからという理由と、どれぐらいの処理性能が必要なのかわからなかったので、高性能な機種を選びましたが、ミドルレンジのスマホでも大丈夫っぽいです。USBシリアル通信に向いていない機種とかもあるかもなので、その辺は調べた方がいいかもしれません。中華系は怪しいかも。

## 用語集
| 用語 | 説明 |
|----|----|
|Arc GIS|今回使った地理情報システムのプラットフォーム全体を指す。|
|Arc GIS Online|ArcGISのオンライン（クラウド）サービス全般を指す。|
|Arc GIS Dashbord|地理情報を遠隔で視覚的に（まあ、ダッシュボードとして）見ることができる。今回のシステムのWebアプリケーション（＝地上側）のこと。|
|Arc GIS Developer|ArcGISの開発者向けのページ。|
|API|難しいことをしなくてもプラットフォーム同士を繋げられるようなやつ。詳しくはググって。|
|GIS|地理情報システム。ただただ地図を表示するだけでなく、コンビニの商圏分析とか統計的・解析的な処理とかもできる。地図表示機能は本当に表面の機能。来年以降は解析機能とか使ったら面白いと思う。可能性は無限大。|
|リアルタイムGIS|従前のGISでは動かないものを対象としてきたが、バスや運送業者のトラックに載せたGPSから輸送管理や統計的処理などに使われたりする。|
|MySQL|データベース（DB)システムの一つ。データを蓄積し、様々なことに用いることができる。会員情報の管理にも使われてたりWebサービスでは当たり前に使われている。SQLのコマンドは意外とシンプル。|
|ポイント|GISの用語。まあ、（緯度経度が紐付いた）点のこと。マクロな視点で見たときの施設などで使われることが多い。詳しくはググって。|
|ライン|GISの用語。まあ、（緯度経度が紐付いた）線のこと。マクロな視点で見たときの河川や鉄道で使われることが多い。詳しくはググって。|
|ポリゴン|GISの用語。まあ、（緯度経度が紐付いた）面のこと。ミクロな視点で見たときの河川とか建物とか海・湖とかで使われることが多い。詳しくはググって。|
|KML|地理情報を扱うフォーマットの一つ。XMLの応用形。緯度経度高度だけでなく、場所の名前やその他のデータ（回転数とか）を付け加えられる。|
|GeoJSON|地理情報を扱うフォーマットの一つ。JSONの応用形。KMLと比べると可読性が高いと思う。その他データを付け加えられるが、ArcGIS Onlineでは付加データを扱うことができない。|
|csv|言わずもしれた表的にデータを扱うフォーマット。「,」が区切りを意味している。Excelで開くと見やすい。付加データを付け加えておけば、ArcGIS Onlineでも処理することができる。通常版のArcGISではラインに変換することができるが、ArcGIS Onlineではポイントとしてのみ扱うことができる。|


## 謝辞
システム理工学部環境システム学科でデータやサイエンスや地図情報に関する研究をされている市川学先生に、ArcGISを用いる上でのシステム構成についてご相談させていただきました。ありがとうございました。

[データ・シミュレーション研究室](https://www.ds.se.shibaura-it.ac.jp/)
