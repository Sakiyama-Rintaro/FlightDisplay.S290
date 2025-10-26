# プログラムガイド
ここでは、設計思想や仕組みを説明します。
### もくじ
* [はじめに](https://github.com/sin0111/tbt.denso.21st/blob/master/README.md)
    * システムアーキテクチャ
    * 用語集
    * 謝辞
* [セットアップ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/set-up.md)
    * S-270のフライトディスプレイをとりあえず再現しよう
        * Windows
        * Android
        * Raspberry Pi
* [プログラムガイド](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/guide.md) `-いまここ`
    * 導入  
    * 設計思想 
    * Android
        * 設計思想
        * ライブラリ
    * Raspberry Pi
        * 設計思想
        * ライブラリ
* [こつ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/tips.md)
    * 活用したサイト・ソフトウェア
    * 通信契約について
    * Android Studio
        * ライブラリの追加
        * Wi-Fiデバッグ
* [おまけ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/omake.md)気が向いたら書きます。
    * 情報コンセントを作る方法
## 全体の設計思想
**S-270ではRaspberry Piをサーバーに使いましたが、セキュリティ上の懸念や回線の安定性の問題があるため、本番環境では[Azure](https://azure.microsoft.com/ja-jp/pricing/details/virtual-machines/linux/)のようなバーチャルマシンを提供するクラウドサービスを使うと良いでしょう。** Microsoftが提供するAzure以外にもGoogleが提供するGCPやAmazonが提供するAWSなど様々なクラウドサービスがありますが、個人的にはAzureがおすすめです。いずれにしても、MySQLプランなどではなく、自分で仮想マシンを借りて自分で構築するようなプランのほうが安いので、LinuxのVMプランをおすすめします。OSはUbuntu、Red Hat Enterprise Linux、CentOSあたりが良いと思います。
**しかしながら、多くのクラウドサービスは従量課金制なので試行錯誤するにはRaspberry Piのようなローカルのマシンでやったほうが良いので、テスト端末としてセットアップをおすすめします。**  
TBTやあなたの財力に余裕があればVM上で動かしても良いでしょう。

### セキュリティ
[攻撃ログ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/bf_atk.log)
これは総当たり攻撃（ブルートフォースアタック）のログです。それっぽいユーザー名で1秒間に10回弱ログインを試みられています。このログはログインに失敗した＝攻撃に失敗したというログですが、インターネットから疎通できるサーバーを設置するとこのような脅威があります。適切なセキュリティ対策を行わないとログインが成功し、乗っ取られて個人情報が取られたり、ウイルスに感染しこのサーバーを踏み台としたサイバー攻撃に使われたりします。インターネットから疎通できるサーバーを使う場合にはセキュリティ対策には十分に気を使いましょう。

### Fragment
[Fragment](https://developer.android.com/guide/components/fragments?hl=ja)という仕組みを用いて速度や高度のグラフ・マップを実装しています。
<img src="https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/material/fragment/layout3D.png" width="450px">
  
親であるActivity上に子であるFragmentの部品をどのように配置することを設定した上で、個々のFragmentの
layoutを設定します。htmlで言うところのinframeとかに近いかも。  
なぜFragmentを利用したかというと、これらを実装するときにまず考えられるのはMainActiviyに全て記述することですが、コードの可読性の面からとても良くありません。また、MainActiviyに何でもかんでもやらせるのもとても良くありません。そこで、クラス分けをしたりするわけですが、ちょっと遊んでみるとわかりますが、適当なクラスを生成してそこでTextViewとか表示させようとすると画面上に表示する部品を扱うにはUIスレッドから操作してくださいと警告が出るわけです（つまりはビルドは通りません）。（メインスレッドはUIスレッドとも言われてたりしますね。）ここが一般的なJavaやKotlinのプログラムを作るだけではないAndroidの難しさなのですが、このような制約をつけることでメモリ管理上とか色々とメリットがあると耳にしました（この辺は適当に書いてますもしかしたら嘘かも）。そして、Fragmentを用いるというのがクラス分けをしつつViewを操作出来る手法の一つであるわけです。ググるととActivityの肥大化を防ぐ方法として色々な手法が出てきて、「本質的にActivityの肥大化を防いでる訳ではない」と出てくるかと思います。自分もそんな気がします。結論を先に言えと怒られそうですが、まあつまり何が言いたかったかというと、Fragmentを利用した理由はActivityの肥大化を防ぐ他の手法を色々検討するのが面倒くさく、かつまあ悪くはない良い手法であろうと判断したからということです。あざす。
#### Fragmentのマネージメント
```Kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()//トランザクション開始
    fragmentTransaction.add(R.id.map_container, Map())
    fragmentTransaction.add(R.id.spd_container, Speed())
    fragmentTransaction.add(R.id.alt_container, Altitude())
    fragmentTransaction.add(R.id.timer_container, Timer())
    fragmentTransaction.commit()//トランザクション終了
}
```
FragmentはMainActivityから生成することが出来ます。MainActivityのonCreate内でこのようにすると、Activityが生成されたときにFragmentが生成されます。Fragmentについてやり取りするときには[Transaction](https://e-words.jp/w/%E3%83%88%E3%83%A9%E3%83%B3%E3%82%B6%E3%82%AF%E3%82%B7%E3%83%A7%E3%83%B3.html)の形で行わなければなりません。上記のコードでは、MapやSpeedなどのFragmentを追加するトランザクションであり、`begin`でトランザクションを開始、`commit`をすることでトランザクションを終了します。このコードを書くことで初めてグラフや地図が表示されます。
#### Fragmentへの値渡し
```Kotlin
private fun updateGraph(spdValue: Float, altValue: Float) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.spd_container, Speed.createInstance(spdValue))
        fragmentTransaction.replace(R.id.alt_container, Altitude.createInstance(altValue))
        fragmentTransaction.commit()
    }
```
MainActivityにはこのようなコードがあり、これは、グラフを更新するメソッドです。本来このグラフのライブラリには、データを追加して再描写するというメソッドがありますが、グラフのクラスはFragmentを継承しており、普通にインスタンスを生成してメソッドを実行することはできません。   
つまりはこういうことは出来ない。
```Kotlin
val instansu = somethingFragment()
instansu.fragmentNoNakaNoMethod(hikisu)
```
そこで、更新する方法として自分でListenerを作って更新する方法などもありますが、それは面倒くさかったので、マイコンからデータが来るたびにMainActivityからFragmentを`replace`(`delete`削除して`add`生成する＝置換)を行っています。これは正直ベストプラクティスではないと自覚していますが、考えることがとても面倒くさかったのでこの方法にしました。ベストプラクティスでないというのは、この操作はグラフのViewを1から作り直しているということであり、それはCPUやメモリを浪費しているということです。つまりは最近のスマホのマシンパワーがすごいから実現出来ているわけであってマシンパワーしか勝たんということです（？）上の解説と同様に、Transactionを開始して、速度と高度のグラフを再描写させてTransactionを終了していますが、ここで第2引数が違うのがわかるかと思います。ここでは速度等のデータを一緒に送ってFragmentを生成しています。
### まあ、Fragmentは公式リファレンスにたくさん書いてあるので読んでください
- [Fragment(Android developer)](https://developer.android.com/guide/components/fragments?hl=ja)



### 使ったライブラリなど
1. シリアル通信 [usb-serial-for-android](https://github.com/mik3y/usb-serial-for-android)（mavenによる参照でのライブラリ追加できます）
2. グラフ表示 [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)（mavenによる参照でのライブラリ追加できます）
3. 地図表示・テレメトリング [ArcGIS](https://www.esrij.com/products/arcgis-runtime-sdk-for-android/)(mavenによる参照でのライブラリ追加できますが、参照先が通常と異なります）
4. MySQL接続[Exposed](https://github.com/JetBrains/Exposed)　（KotlinのOSM。これインポートすればMySQLに接続できるようになります。JetBrains公式のやつ）

### データの流れ
![DFD](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/DFD.png)

## シリアル通信
  調歩同期方式のシリアル通信です。
いつも正常なデータが来るとも限らないので、誤り検出（伝送に誤りが無いかをチェックする仕組み）や、伝送の正常性を判断するコードを入れた方が良いでしょう。







### mbed側プログラム
#### [USBDriver.c](https://github.com/TeamBirdmanTrial/FlightDisplay.arajin.S270/blob/master/module/mbed(C)/USBDriver.c)の説明です
```C
void dataShape(int spd,int alt){
    int spdPacket[6];//速度データを入れる配列です
    int altPacket[6];//高度データを入れる配列です
    int spdCk;//速度データのチェックサムを入れる変数です
    int altCk;//高度データのチェックサムを入れる変数です
    spdPacket[0] = 'S';//速度データの始まりを示します
    spdPacket[3] = (spd % 10); spd /= 10;//1の位
    spdPacket[2] = (spd % 10); spd /= 10;//10の位
    spdPacket[1] = (spd % 10);//100の位
    spdCk = spdPacket[1] + spdPacket[2] + spdPacket[3];//チェックサムを計算します
    spdPacket[4] = (spdCk / 10);//チェックサム10の位
    spdPacket[5] = (spdCk % 10);//チェックサム1の位

    altPacket[0] = 'A';//同様に高度データの始まりを示します
    altPacket[3] = (alt % 10); alt /= 10;
    altPacket[2] = (alt % 10); alt /= 10;
    altPacket[1] = (alt % 10);
    altCk = altPacket[1] + altPacket[2] + altPacket[3];
    altPacket[4] = (altCk / 10);
    altPacket[5] = (altCk % 10);

    send(spdPacket,altPacket);//送信用の関数を呼びます
}

void send(int spdPacket[],int altPacket[]){
    for(int i = 0;i<6;i++){
        android.putc(spdPacket[i]); //putcで一文字ずつ送ります。
    }
    for(int i = 0;i<6;i++){
        android.putc(altPacket[i]);
    }
}
```
### Android側プログラム
#### データ転送
マイコンから受信したデータは上の図のようにグラフFragmentとテレメトリクラスに対して配信を行います。





### 使ったライブラリ： usb-serial-for-android(ver:3.3.3)
シリアル通信を簡単にするためのライブラリです。    
追加方法は下の[ライブラリの追加方法](https://github.com/sin0111/tbt.denso.21st/blob/master/README.md#%E3%83%A9%E3%82%A4%E3%83%96%E3%83%A9%E3%83%AA%E3%81%AE%E8%BF%BD%E5%8A%A0%E6%96%B9%E6%B3%95)を参照してください。  
下にちょっとしたTips的なことを書いておきますが、もっと詳しいことは公式のGitHubリポジトリや、実際のプログラムを参照してください。     

#### 受信方法1
`read()`メソッドをこのプログラムから能動的に実行することで受信する方法です。
タイミングによっては受信がうまく行かなかないこともありますが、簡単に実装できます。  
次のようにすると、lengthにはデータ受信すると受信したデータ長が、受信していないときは0が代入されます。dataはByteArrayで宣言しておいてください。dataには受信したデータが格納されます。タイムアウト時間はよくわからんけど適当に調整してください。
```Kotlin
var length: Int = port.read([受信データ格納する]data,[タイムアウト時間ms]3)
```

#### 受信方法2
Listenerクラスを実装することで、新しいデータが来た時にonNewDataというメソッド自動的に実行されます。

```Kotlin
override fun onNewData(data: ByteArray) {
            check(data, data.size)//受信データと受信データの桁数をcheck関数に渡します。
}
```
#### データのチェックプログラム 
データが全桁同時に受信されているか、分離されて受信されているかによって分岐します。
```Kotlin
fun check(data: ByteArray, length: Int){
    if (length == 12) {//繋がってきたとき
            detection(data)//12桁のときはそのまま誤り検知をするdetection関数に渡します。
            corrctData.clear()//リセット。（不正データの後に正しいデータが入るとdetection関数のclear()が実行されないため。
        } else if (length < 12) {//分離されてきたときは結合するcorrection関数に渡します。
            correction(length, data)
        }
}
```
#### 補完機能
このライブラリでは受信したデータはByteArrayに格納されますが、12桁のデータを送信しても配列には1回目に7桁、2回目に5桁が格納されるというように分離されてしまうことがあります。最初は、プログラム上の問題かと思いましたが、調歩同期方式のシリアル通信は1文字ごとにスタートビット、ストップビットが送られるものであり、1文字の区切りはわかっても、データの始まりと終わりはわからないため、任意のタイミングで格納が行われるような気がしています。ボーレートの標準は9600bpsが用いられていますが、115200bpsにすると12桁まるまる受信できる回数が増えるようです。
しかし、12桁まるまる受信できないこともたびたびあるので、7桁と5桁に分かれてきた時に別のmutableListに一度代入し結合を行い、12桁になったことを確認できれば、その値の誤りを検証した後に、誤りが認められなければ採用するロジックを採用しました。  
分離されてきたときにはこのように結合を行います。
```Kotlin
fun correction(length: Int, data: ByteArray){
    for (i in 0 until length) {
            corrctData += data[i]//mutableListはこのようにすると元のデータの後ろに新しいデータを結合できます。
        }
        if (corrctData.size == 12) {
            detection(corrctData.toByteArray())//この変数はmutableList<Byte>で宣言されているため、元のByteArrayにキャストし、誤り検知関数に渡します。
            corrctData.clear()//結合したデータを記録するListは空にしておきます。
        }
}
```

#### 誤り検知（チェックサム）
mbedやこのライブラリの機能としてパリティビットの設定があるっぽいですが、動いているのかよくわからないので信頼性を確認するプログラムを自分で実装しました。標準の方法が使えるなら使ったほうがスマートだと思うので調べてみてください。
誤り検出の一つとして、チェックサムという手法があります。誤り検知の中では一番簡単な手法だと思います。
1. mbed側の送信時に、送るデータ（ペイロード部）をすべてを足し合わせ、同時に送信します。　
`例：ペイロードが1234だったらチェックサムは1+2+3+4=10で送信時に123410のように送る`
2. Android側でも受信時に送られてきたデータ（ペイロード部）すべてを足し合わせて、その合計を計算します。　
`例：送られてきたデータが1234だったら同様に10`
3. 送られてきた合計値と受信時に計算した合計値を比較し、一致すればそのデータに誤りが無いとみなし、採用する。一致しなければ、誤りがあると判断し、不採用とする。  
 `例：不採用の場合121210と送られてきた→Android側の計算は1+2+1+2=6であり、10≠6なので誤っていると判断。`
という単純なロジックです。衝突する確率もそこそこあると思いますが、一応これで防げました。他にもやり方はあると思うので、調べてみてください。  
衝突する場合：1212の合計は6,0006の合計も6なので誤って判断してしまう可能性あり。  
  
誤り検知をします。具体的には、識別子とチェックサムの検証を行います。
```Kotlin
private fun detection(data: ByteArray) {
        var spdValue: Float
        var altValue: Float
        var txCkSum: Int
        var rxCkSum: Int
        if (data[0].toChar() == 'S') {//速度データを示します。

            txCkSum = data[4] * 10 + data[5]//ByteAraryの10の位と1の位の要素を2桁のIntに直します。
            rxCkSum = data[1] + data[2] + data[3]//受信側でデータを足します。
            if (txCkSum == rxCkSum) {//送信チェックサムと受信チェックサムを検証します。
                spdValue = data[1].toFloat() * 10 + data[2].toFloat() + data[3].toFloat() / 10 //データを実際の小数付きに変換します。
                if (data[6].toChar() == 'A') {//高度についても同様です。
                    txCkSum = data[10] * 10 + data[11]
                    rxCkSum = data[7] + data[8] + data[9]
                    if (txCkSum == rxCkSum) {
                        altValue =
                            data[7].toFloat() * 10 + data[8].toFloat() + data[9].toFloat() / 10
                        updateGraph(spdValue, altValue)//データをグラフ更新やテレメトリに渡します。
                        //tele.sqlRequest(altValue,spdValue)
                    }
                }
            }
        }
    }
```


## グラフ表示
数値で表示するよりも、グラフィカルに表示した方がパイロットが認識しやすいので、グラフを表示しています。  
自分はライブラリを使いましたが、使わなくてもできるかもしれません。ライブラリを使いたくなければ頑張ってみてください。  
### MPAndroidChart(ver:3.1.0)
追加方法は下の[ライブラリの追加方法](https://github.com/sin0111/tbt.denso.21st/blob/master/README.md#%E3%83%A9%E3%82%A4%E3%83%96%E3%83%A9%E3%83%AA%E3%81%AE%E8%BF%BD%E5%8A%A0%E6%96%B9%E6%B3%95)を参照してください。   
機速、高度等の表示に使った。  
リアルタイム更新は公式にはサポートしていないようですが、できます。
サンプル少なくて難しかった。  
下にちょっとしたTips的なことを書いておきますが、もっと詳しいことは公式のGitHubリポジトリや、実際のプログラムを参照してください。 
デザインの設定については公式のリファレンスやQiitaを参考にしました。
#### リアルタイム描画
リアルタイム更新は公式にはサポートしていませんが、一応それらしきメソッドは存在しており、そのメソッドを実行することでもできます。その場合はListenerを立てて定期的に数値を取りに行く方法が考えられます。また、Fragmentを継承してあれば、乱暴なやり方としてグラフViewごと再描写してしまう方法もあります。今回は乱暴なやり方で実装しました。

#### 色
```Kotlin
var color:String
if (value in 3.0..8.0){
    color = "#00AA90"
}else {
    color = "#CB1B45"
}
```
こんなコードが書かれていますが、値によってグラフの色を変えています。飛行禁止高度とかが定められていたり失速しているのをわかりやすくする目的です。ただし、範囲は適当なので実際の範囲はパイロットや設計者と相談してください。





## 地図表示・テレメトリング
現在位置の把握や、飛行禁止区域の把握に用います。
従前はGoogleマップを使っていたみたいですが、今回は地図表示だけなく地上へのデータ送信を試みたので、ArcGISという地理情報システムの製品を活用してみました。  

### ArcGIS Runtime SDK for Android(ver:100.10) 
地図の表示と自機の位置表示、及び地上への情報送信（テレメトリングとか言うらしい）に使った。  
ArcGISは地理情報システム（Geographic Information System）の一つで、地図に様々な情報を落としこむことで解析や可視化が出来るシステムです。ArcGISは、Esri(エスリまたはエズリ）という会社の製品です。土木工学科の授業でArcGIS Proというデスクトップ版を使って、これ使えるんじゃね？と思って採用しました。   
今回はGISの膨大な機能のうち、ほんの一部分の「可視化する」という機能のみを使っています。解析機能とかも使えたら面白いかもしれないですね！  
GIS自体の詳細はメーカーや国土地理院のサイトを参照してくださいね、  
* [GISとは](https://www.esrij.com/getting-started/what-is-gis/)（esriジャパン）
* [GISとは・・・](https://www.gsi.go.jp/GIS/whatisgis.html)（国土地理院）

#### ArcGISの座標について
移動ログを描画する際に、[polylineBuilder](https://developers.arcgis.com/android/api-reference/reference/com/esri/arcgisruntime/geometry/PolylineBuilder.html)を用いました。
polylineBuilderのインスタンスを生成する際に下のように[空間参照](https://community.esri.com/t5/arcgis-%E9%96%8B%E7%99%BA%E8%80%85%E3%82%B3%E3%83%9F%E3%83%A5%E3%83%8B%E3%83%86%E3%82%A3-documents/gis-%E3%82%A2%E3%83%97%E3%83%AA%E3%81%A8%E7%A9%BA%E9%96%93%E5%8F%82%E7%85%A7/ta-p/904408)について定義をする必要があります。
```Kotlin
val polylineBuilder = PolylineBuilder(SpatialReference.create(4326))
```
- [GIS アプリと空間参照(Esri Community)](https://community.esri.com/t5/arcgis-%E9%96%8B%E7%99%BA%E8%80%85%E3%82%B3%E3%83%9F%E3%83%A5%E3%83%8B%E3%83%86%E3%82%A3-documents/gis-%E3%82%A2%E3%83%97%E3%83%AA%E3%81%A8%E7%A9%BA%E9%96%93%E5%8F%82%E7%85%A7/ta-p/904408)  
　空間参照は上の記事の通り、位置情報を示す座標について定義をするものです。地球を数値で表す際には、まず立体的に考えたときに、地球を真球とするか、回転楕円体とするか、楕円の扁平率はいくつにするか原点や軸はどうするかを決める必要があります。また、平面の地図として扱うには立体のものをどのように平面にするかも決める必要があります。  
　Esri社の製品ではWGS84という測地系（地球の形と座標の定義）についてWeb メルカトル球体補正という投影法を用いて平面の地図として扱う空間参照(WKID:102100)がよく用いれているようで、サンプルコードにもこの102100というIDがよく記載されています。  
　一方で、GPSではWGS84測地系(EPSG:4326)を用いているという点では同じですが、受信データとして得られるのは緯度経度そのものです。（※これらの空間参照はWKIDやEPSGといった一意のIDが付与されていて、上のように引数にIDを入れることでどの空間参照を用いるか定義をすることができます。 ） 

　すなわち、PolylineBuilderによって新しいポイントを追加する際には下のように[addPoint](https://developers.arcgis.com/android/api-reference/reference/com/esri/arcgisruntime/geometry/MultipartBuilder.html#addPoint(double,double))メソッドを実行しますが、最初に空間参照について102100と定義した状態で、引数にGPSより取得した緯度経度（4326）を入れると地図に表示されないという事態になりますので空間参照の設定には注意をしてください。

```Kotlin
polylineBuilder.addPoint(newPointLon,newPointLat)//新しい点の経度と緯度を示しています。
```
また、引数に緯度と経度を逆に入れるというミスも起こしがちです。（特に、Esri社の緯度経度XYの定義は逆になってるような気がしてるのでミスりやすいです。）

土木工学科で勉強したりしますが、興味があれば勉強してみると面白いです。
- [投影法、座標系、測地系、それぞれの違いについて](https://www.gis-py.com/entry/2015/12/30/182723)  
- [絵でわかる地図と測量](https://bookclub.kodansha.co.jp/product?item=0000148853)←芝浦土木の先生が書かれた本です。  
データの更新は他と同様に、Fragmentとして実行すると、ややめんどくさくなります。
方法としては、Listenerを立てて定期的に数値を取りに行く方法と、Fragment内部でGPSデータを取得してしまう方法があります。今回はFragment内部でGPSデータを取得しました。そのため、DBに転送するためのGPSListenerとMapを表示するためのGPSListenerが存在して冗長な構成になっています。（悪い意味でね）

#### MapIDについて
```Kotlin
//Mapセット
val map = ArcGISMap("http://www.arcgis.com/home/item.html?id=ここに入れてね")
```
このようなコードがありますが、ここのidはArcGISOnline上のmapのidを指定します。
そのmapにはなんらかのフィーチャ（飛行禁止場所とか）を入れておいたり、ベースマップの設定（白黒の地図にしたり、OSMを使ったりなど）とかがができるはずです。（試してはない）



## 使ったフレームワーク： Exposed
MySQLだけでなく、今回用いたMariaDB、他にもPostgreSQLなどに対応しているKotlinのフレームワークです。
今回はMySQL系のDBであるMariaDBのDBサーバーに接続するのに用いています。サーバーのIPアドレスとパスワードを指定することで、簡単に接続ができます。  
アドレスは
HTTP接続する際に
`http://`
とするように、
`mysql://`  と指定するとデフォルトのmysqlポート(3306番)で接続を試みるはずです。すなわち、
`mysql://125.198.124.41`  のような形にするることで接続することができます。
可変IPアドレスを用いるのであれば、変更が無いかを確認する必要があります。  
また、セッションを確立するたびに、テーブルを新しく作るようにすると、データの管理性が向上すると思います。（今回は未実装）
また、普通、DBを操作する際にはSQLコマンドを用いますが、それを用いると、Kotlinの良さである実行時エラーが生じにくいという点がなくなってしまいます。なぜならば、KotlinのコンパイラはSQLコマンドのチェックをできないからです。そのため、Kotlinを作ったJetBrain社のこのフレームワークを用いると、SQLコマンドと若干異なるコードを書く必要がありますが、変なコードを書くとコンパイル時エラーが出るため安全性が高まるのです。また、単純にこのフレームワークを用いたほうが考慮事項が減って便利だと思います。

## ストップウォッチ
Timerクラスではストップウォッチを実装しています。
これはググって出てきたやつをいい感じに変えたやつです。
startでタイマー開始、stopでタイマー停止。簡単ですね。
リセットボタンを押すことでこの辺も制御するつもりでしたが、放置してしまいました。

### 仕組み
1. 内臓のセンサやシリアル通信によってデータを取得します。
2. アプリ上の地図に位置等のデータを表示します。
2. 同時にデータをデータベースサーバーに送信します。(インターネットを通すので、モバイルデータ通信の契約が必要になります）
3. ArcGIS Onlineでデータベースを参照します。
4. Web上で位置などのデータを確認することができます。

下にちょっとしたTips的なことを書いておきますが、もっと詳しいことは公式のGitHubリポジトリや、実際のプログラムを参照してください。 

## サーバー周りの全体の構成
ArcGISやサーバー関連の話は少し難しいので詳しく説明をします。
テレメトリーシステムを実現する上で（思いつく限り）以下の方法があります。 *今回は1-B-ⅰの構成を用いました* が、参考としてなぜこの構成を選んだのかわかりやすくするために、他の方法でやる方法も記しておきます。  


ArcGIS Onlineで扱う上でのファイルフォーマットに特徴をまとめておく。

<table>
<tr>
    <th colspan="1">ファイル形式</td>
    <th colspan="4">扱えるデータ</td>
    <th colspan="2">インポート方法</td>
</tr>
<tr>
    <th></td>
    <th>ポイント</th>
    <th>ライン</th>
    <th>ポリゴン</th>
    <th>データ付加</th>
    <th>Web参照</th>
    <th>Python API</th>
</tr>
<tr>
    <th>GeoJSON</th>
    <th>◯</th>
    <th>◯</th>
    <th>◯</th>
    <th>☓</th>
    <th>◯</th>
    <th>◯</th>
</tr>
<tr>
    <th>KML</th>
    <th>◯</th>
    <th>◯</th>
    <th>◯</th>
    <th>☓</th>
    <th>◯</th>
    <th>◯</th>
</tr>
<tr>
    <th>csv</th>
    <th>◯</th>
    <th>☓</th>
    <th>☓</th>
    <th>◯</th>
    <th>△</th>
    <th>◯</th>
</tr>
</table>

※データ付加：その他データを緯度経度に付加させることができる。前述の通り、本来フォーマット形式としてはGeoJSONやKMLもデータの付加に対応してるが、ArcGIS Onlineでは活用することができない。  
※Web参照：Webサーバーにファイルを置いておき、ArcGISに参照させることでデータの更新ができる。△csv：付加データを含まないポイントデータとしてのみ使うのであれば、Webサーバー上にファイルを置くことでデータの更新ができるが、付加データをDashbordに表示させるときにはcsvファイルをフィーチャ サービスとして公開する必要があるため、Python APIを用いる必要がある。  
※Python API：Python向けのAPIをコールすることでデータの更新ができる。

### 1.ArcGISを用いる方法
前提として、今回用いたArcGISには様々なAPIがあり、Android側では、Android SDK/APIを活用していますが、このAPIでは外部から追加されたデータをアプリ上に表示することはできますが、Android側からデータをインポートすることができません。(2021年現在）すなわち、外部から位置や回転数といったデータを確認することが出来ないのです。  
そこで、ArcGISを用いる際には別途中継サーバーにデータを送信し、中継サーバーからAPIを叩いたり、ファイルサーバー上の地理情報ファイルをArcGISに参照させるという手順が必要となります。
しかしながら、ArcGISを用いるとWebアプリケーションを作る必要が無かったり、ArcGISという強力なプラットフォームで将来的にデータの解析に用いることができるというメリットがあるため、採用しました。 しかしながら、ArcGISのプラットフォームに依存すると、データ更新間隔が最短6秒～15秒程度に制限されてしまうことに注意しましょう。
高頻度にデータを更新したい場合は2とかの方法を使うといいでしょう。ArcGISにもOpenLayersのAPIは存在しており、描画画面は自作で、裏の部分はArcGISのAPI使うみたいな方法が一番カスタマイズ性がありつつ利便性があると考えていますが、html/css/jsでWebアプリを作るのはちょっとめんどくさかったので、今回はGUIで出来るArcGIS Dashboardsを使用しています。 
#### A.PythonAPIを用いる方法
[Image]()  
今回ポイントデータの表示で採用しています。
1. AndroidからDBサーバーへモバイルデータ通信で通信キャリアの基地局を通じてインターネットにアクセスし、サーバーにデータを送信します。
2. サーバーではDBシステムが動いており、データはDBへ蓄積されていきます。
3. 同じくサーバーではPythonで書かれたスクリプトが動いており、DBに蓄積されたデータを取り出し、地理情報ファイル(csvフォーマット）を出力します。
4. 同じくそのPythonのプログラムには出力された地理情報ファイルをArcGIS APIによってArcGISのサーバーに送信します
5. ArcGISはそのcsvの情報をもとに描画します。（これは設定した時間に1度更新されます。最短で15秒です）
#### B-ⅰ.APIを用いない＆TBTWebサーバーを使う方法
[Image]()  
今回ラインデータの表示で採用しています。
1. AndroidからDBサーバーへモバイルデータ通信で通信キャリアの基地局を通じてインターネットにアクセスし、サーバーにデータを送信します。
2. サーバーではDBシステムが動いており、データはDBへ蓄積されていきます。
3. 同じくサーバーではPythonで書かれたスクリプトが動いており、DBに蓄積されたデータを取り出し、地理情報ファイル(KMLフォーマット)を出力します。
4. 同じくサーバーではPythonのスクリプトが動いており、FTPでWebサーバー(tbt.bird.cx)に送信します。
5. ArcGISはWebサーバーにあるKMLファイルを参照してそのKMLファイルをもとに描画します。（これは設定した時間に1度更新されます。最短で6秒です。)

### B-ⅱ .APIを用いない&自宅Webサーバーを使う方法
今回用いなかった方法です。
1. AndroidからDBサーバーへモバイルデータ通信で通信キャリアの基地局を通じてインターネットにアクセスし、自宅サーバーにデータを送信します。※なぜ自宅サーバーかというと、似たような仕組みのクラウドサーバーはコスト管理が難しいかったり、高かったりするからです。  
2. 自宅サーバーではMySQLが動いており、データはDBへ蓄積されていきます。
3. 同じく自宅サーバーではPythonで書かれたスクリプトが動いており、DBへ蓄積されたデータを取り出し、地理情報ファイルを出力します。
4. 同じく自宅サーバーではApacheが動いており、その地理情報ファイルをWebページとしてインターネットから参照できるようにします。
5. ArcGISは自宅サーバーにある地理情報ファイルを参照してそのKMLファイルをもとに描画します。（これは設定した時間に1度更新されます。最短で6秒です。)

### 2.OpenLayersを用いる方法
[Image]
今回用いなかった方法です。
1. AndroidからDBサーバーへモバイルデータ通信で通信キャリアの基地局を通じてインターネットにアクセスし、サーバーにデータを送信します。  
2. サーバーではDBシステムが動いており、データはDBへ蓄積されていきます。
4. TBTのWebサーバー上でJavaScriptのスクリプトが動いており、自宅サーバーのDBを参照して描画します。（これは設定した時間に1ど更新されます。恐らく制限はありません。）

### グーグルとかのシステムを使う方法
今回用いなかった方法です。
あんまり詳しくないですがGoogle Earthらへんの仕組みを使うと似たことができるかも。
今回用いなかった方法です。
1. AndroidからDBサーバーへモバイルデータ通信で通信キャリアの基地局を通じてインターネットにアクセスし、クラウドサーバーにデータを送信します。  
2. そのデータはDBに蓄積されます。
4. そのデータをGoogle Earth系のシステムやOpenLayersなどで参照することで描画します

### 今回用いた方法の詳しい説明
この仕組の注意点として、ポイントデータを大量に表示すると重くなってしまうので、ラインデータに変換するか、最新のポイントのみを表示するとという仕組みをとる必要があります。
https://ssl001.kix.ad.jp/tbt.bird.cx/denso/line.kml

## サーバーのお話
### 環境
* メインサーバー: Raspberry Pi 4(2GB) [秋月電子](https://akizukidenshi.com/catalog/g/gM-14839/)
* VPNサーバー: Raspberry Pi 3 B
* ストレージ: SunDisk Extreme PRO 128GB [ここおすすめ](http://www.akibaoo.co.jp/c/130351/)
* 電源: RP-PC136 [Amazon](https://www.amazon.co.jp/gp/product/B08G4GCT2N/ref=ppx_yo_dt_b_asin_title_o01_s00?ie=UTF8&psc=1)
* ケーブル: Ankerのやつ [Amazon](https://www.amazon.co.jp/Anker-PowerLine-USB-C-Galaxy-%E3%80%81MacBook%E3%80%81MateBook%E5%AF%BE%E5%BF%9C/dp/B01GNQXIMG/ref=sr_1_18?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&dchild=1&keywords=Anker+typec&qid=1616479585&sr=8-18)
* OS: RaspberryPiOS（32bitでデスクトップ版なやつ）
* DB: MariaDB(MySQL互換)
* 回線: docomo光1Gbps;イーサネット接続@自宅
* ルーター: WG2600HS [Aterm](https://www.aterm.jp/product/atermstation/product/warpstar/wg2600hs/)
* スイッチングハブ: GS308 [NETGEAR](https://www.jp.netgear.com/home/products/networking/switches/soho-ethernet-switches/gs308.aspx#tab-%E6%8A%80%E8%A1%93%E4%BB%95%E6%A7%98)

### インターネット・プロトコルの話
引き継ぎ資料のどこかでIPv4とIPv6の話をしたかと思いますが、IPv4よりもIPv6を用いたほうが通信速度が速くなり、遅延が少なくなる可能性が高いです。IPについては[マスタリングTCP/IP](https://www.amazon.co.jp/%E3%83%9E%E3%82%B9%E3%82%BF%E3%83%AA%E3%83%B3%E3%82%B0TCP-IP%E2%80%95%E5%85%A5%E9%96%80%E7%B7%A8%E2%80%95-%E7%AC%AC6%E7%89%88-%E4%BA%95%E4%B8%8A-%E7%9B%B4%E4%B9%9F/dp/4274224473)のような本で勉強してください。
インターネットに接続するのに、IPoEという方式と、PPPoEという方式があり、IPoEを用いると一般に速くなります。(モバイル回線でも有用なのかは要検証しかしながら、下の表のように、IPv4ではIPoE方式を用いることができないため、IPv6を用いる必要があります。
<table>
<tr>
    <th colspan="1">プロトコル</td>
    <th colspan="1">PPPoE</td>
    <th colspan="1">IPoE</td>
</tr>
<tr>
    <th>IPv4</td>
    <th>遅い</th>
    <th>存在しない</th>
</tr>
<tr>
    <th>IPv6</td>
    <th>速いこともある</th>
    <th>速い</th>
</tr>
</table>
今回、サーバーのIPアドレス指定の際にIPv4アドレス（下のようなもの）  

`125.198.124.41` 

を用いましたが、IPv6のアドレス（下のようなもの）、  

`2404:f92a:921a:8531:a93b:1f18:a02b:81f`  
を用いると安定性が向上する可能性がある(モバイル回線でも有用なのかは要検証）ので、検討して見てください。
## Raspberrypiのお話
### 使っている機能
* ssh: RaspberryPiをコマンドラインで遠隔操作できます。
* vnc: RaspberryPiをGUIで遠隔操作できます。


### 検索
[チュートリアル](https://community.esri.com/t5/arcgis-%E9%96%8B%E7%99%BA%E8%80%85%E3%82%B3%E3%83%9F%E3%83%A5%E3%83%8B%E3%83%86%E3%82%A3-documents/arcgis-api-for-python-%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E3%81%BF%E3%82%88%E3%81%86-arcgis-online-%E3%81%AE%E3%82%A2%E3%82%A4%E3%83%86%E3%83%A0%E3%82%92%E6%A4%9C%E7%B4%A2%E3%81%97%E3%81%A6%E3%81%BF%E3%82%88%E3%81%86/ta-p/913804)
[構文](https://developers.arcgis.com/rest/users-groups-and-items/search-reference.htm)
[type名](https://developers.arcgis.com/rest/users-groups-and-items/items-and-item-types.htm)

### データの上書き
[上書き方法](https://community.esri.com/t5/arcgis-api-for-python-questions/overwriting-geojson-based-hosted-feature-service/m-p/808579)
[リファレンス](https://developers.arcgis.com/python/api-reference/arcgis.features.managers.html#arcgis.features.managers.FeatureLayerCollectionManager.overwrite)

## Webアプリケーション側の話(ArcGIS Online及びArcGIS Dasboards)
[Webからレイヤーを追加する](https://doc.arcgis.com/ja/arcgis-online/create-maps/add-layers.htm#ESRI_SECTION1_A82A515232CB4672838FEB9FCF8E76D8)  
[更新間隔の設定](https://doc.arcgis.com/ja/arcgis-online/create-maps/set-refresh-interval.htm)  
