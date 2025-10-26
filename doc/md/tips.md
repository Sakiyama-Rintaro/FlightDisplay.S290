# Tips
ここではプログラムを書く上で面倒が減ることとか、こつを紹介します。

### もくじ
* [はじめに](https://github.com/sin0111/tbt.denso.21st/blob/master/README.md)
    * 導入 
    * システムアーキテクチャ
    * 用語集
    * 謝辞
* [セットアップ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/set-up.md)
    * S-270のフライトディスプレイをとりあえず再現しよう
        * Windows
        * Android
        * Raspberry Pi
* [プログラムガイド](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/guide.md) 
    * 設計思想 
    * Android
        * 設計思想
        * ライブラリ
    * Raspberry Pi
        * 設計思想
        * ライブラリ
* [こつ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/tips.md) `-いまここ`
    * 導入 
    * 活用したサイト・ソフトウェア
    * 通信契約について
    * Android Studio
        * ライブラリの追加
        * Wi-Fiデバッグ
* [おまけ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/omake.md)気が向いたら書きます。
    * 情報コンセントを作る方法

## 活用したサイト

* [Android Developers](https://developer.android.com/docs?hl=ja)（Android公式のドキュメントです。公式のリファレンスを読めるようになっといたほうがいいです）
* [Kotlin](https://kotlinlang.org/docs/reference/)（こちらも、Kotlinの公式ドキュメントです。公式のリファレンスを読めるようになっといたほうがいいです）
* [ArcGIS for Developer 開発リソース集](https://esrijapan.github.io/arcgis-dev-resources/)
* [TryKotlin](https://try.kotlinlang.org/)（KotlinのWeb実行環境です。IDE(IntelliJ IDEA)に突っ込むのがめんどい時、ちょっとしたテストにはこっち使うといいかも）
* [mbed Compiler](https://ide.mbed.com/compiler/)（mbedのWebコンパイラです。VSCodeでも頑張ればコンパイル出来るらしいけど、めんどくさいよね。シリアル通信のコーディングはmbedもセットだからこれも必要です。※メインのマイコンにmbedをまだ使っていれば。(S-270ではmbed LPC1768をメインマイコンに採用していました)）
* [Qiita](https://qiita.com/)（エンジニアのコミュニティーサイトです。あまり信頼しない方がいいですが、そこそこ役に立ちます。）

## 通信契約について
ArcGISを使うにはモバイルデータ通信が必要となります。（いわゆる4G・LTEと呼ばれるもの）  
大手携帯電話会社の料金プランは高額なため、今回のような数メガのデータ通信には格安SIMやIoT機器向けのプランを選ぶとよいでしょう。  
一例を載せておきます。  
* [nuroモバイル](https://mobile.nuro.jp/)
* [ロケットモバイル](https://rokemoba.com/)
* [HISモバイル](https://his-mobile.com/)
* [IIJmio](https://www.iijmio.jp/)
* [NifMo](https://nifmo.nifty.com/)
* [BIGLOBEモバイル](https://join.biglobe.ne.jp/mobile/)
## 端末について
500MB程度はメモリを消費するようなので、Xperia XZs以降など3GB程度のメモリはほしいかもしれないですね。


## AndroidStudio関連
### ライブラリの追加方法
ライブラリというものを追加することで、難しいことが簡単にできるようになります。ライブラリの作者には感謝です。  
ただ、自分は最初そのライブラリの追加方法も全然わからなかったので、書いておきます。  
バージョンアップで仕様が変わってたら調べてください。  
1. `build.gradle(Project)`内の`repositories`に`maven { url 'https://jitpack.io' }`を追加する。
 ↓こんな感じになると思います。


2.  File->Project Structure->+->Library Dependency->ライブラリの名前を入力して（バージョンの指定も忘れずに）OKを押す。

※ArcGISは異なるリポジトリを参照する必要があります。
参照先は以下です。(2020年現在）  
`https://esri.jfrog.io/artifactory/arcgis`  
すなわち、下記のようにすれば良いです。  
`maven { url 'https://esri.jfrog.io/artifactory/arcgis' }`  


### Wi-Fiデバッグについて
USBシリアル通信のデバッグをするには無線でのデバッグが必要になります。（USBポートはマイコンとの通信で埋まっているので）  
ややめんどくさいのでやり方を記しておきます。  
ちなみに学内Wi-FiでもWi-Fiデバッグ出来たので部室とかでやるときにも使えます。(2020年現在)
- [Wi-Fi 経由でデバイスに接続する（Android 11 以降）](https://developer.android.com/studio/command-line/adb?hl=ja#connect-to-a-device-over-wi-fi-android-11+)
- [Wi-Fi 経由でデバイスに接続する（Android 10 以前）](https://developer.android.com/studio/command-line/adb?hl=ja#wireless)
- [qiita](https://qiita.com/fukasawah/items/3f5b24819fac24c6686a)  

方法は3つあります。方法2＞方法1-B＞方法1-Aの順でおすすめです。
<table>
<tr>
    <th colspan="1">プロトコル</td>
    <th colspan="1">方法1-A</td>
    <th colspan="1">方法1-B</td>
    <th colspan="1">方法2 </td>
</tr>
<tr>
    <th>セキュリティ</td>
    <th>ポートを変えることで良くできる</th>
    <th>あまり良くない</th>
    <th>良い</th>
</tr>
<tr>
    <th>利便性</td>
    <th>悪い</th>
    <th>良い</th>
    <th>まあ良い</th>
</tr>
</table>



#### 方法1-A （コマンド手打ち）
#### 手順
以下の操作をWi-Fiデバッグするたびに行ってください。
1. AndroidStudioを起動し、PCとスマホをUSBケーブルで接続する
2. 下のコマンドをコマンドプロンプトやAndroid Studio上のターミナルに入力して実行する。
```powershell
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe tcpip 5555　
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe connect 192.168.10.なんとかかんとか:5555
```
4. 接続がうまくいったことを確認してケーブルを外す。
5. AndroidStudioで対象の端末を選択してデバッグ。
6. 接続を解除するコマンドを打ち込む。
```powershell
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe usb
```
#### 方法1-B （batファイル作成）
#### 手順
以下のコマンドをメモ帳に書いて拡張子を.batにしてください。
```powershell
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe tcpip 5555　
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe connect 192.168.10.なんとかかんとか:5555
```

以下の操作をWi-Fiデバッグするたびに行ってください。
1. AndroidStudioを起動し、PCとスマホをUSBケーブルで接続する
2. 作成したbatファイルを実行
4. 接続がうまくいったことを確認してケーブルを外す。
5. AndroidStudioで対象の端末を選択してデバッグ。
6. 接続を解除するコマンドを打ち込む。
```powershell
%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe usb
```

方法1解説  
**//1行目**  
5555番ポートで通信すると定義しています。ファイアウォールに穴を開けています。5555番は有名なポートなので、セキュリティを強化するには他のポートを指定した方がいいかもしれません。  
(フリーWi-Fiでデバッグする時は特に危険かも。）

**//2行目**  
端末のIPアドレスを指定しています。端末から確認してください。  
家のWi-Fiだと`192.168.10.100`みたいな形式だと思う。IPアドレスは動的なものですが、家のWi-Fiでは固定できると思うのですることをお勧めします。  
大学は`131.なんとかかんとか`みたいな形式だった気がする。これは**たぶん**固定できないので、毎回確認してください。  
`:5555`の部分は1行目で指定したポート番号。（1行目で5555以外を選んでいれば書き換えてください）  
デバッグ作業終わるときはこれで解除する。(解除する必要があるかはわからん。セキュリティ的にした方がいい気がする。しらんけど）  
* [端末のIPアドレスを確認するには](https://novlog.me/android/confirm-ip-address/)  
* [端末のIPアドレスを固定するには](https://www.buffalo.jp/support/faq/detail/15994.html)  
固定するアドレスは他の機器と重複しなさそうなものを選んだほうが良いでしょう。
192.168.10.なんとかかんとかというLAN内にいるときは192.168.10.150あたりなら50台ぐらい電子機器がない限り多分安全です。
固定方法は上記の端末から操作する方法のほかに、ルーターの設定画面から固定する方法もあります。（詳しくは調べてみてください）ルーターから設定する方法なら衝突することもないことや、複数台固定すべき端末があるときに管理が楽になるというメリットがあります。（慣れないとめんどくさいけどね。）

#### 方法2 (Android11以降限定)
Android11以降だともっと簡単さとセキュリティが両立できます。
やりかたは[こちら](https://zenn.dev/ik11235/articles/android-wireless-debug)から
方法1-Bと違い、ポートが毎回変わるようで、セキュリティ的には良いですが、`connect`コマンドを毎回打つ必要がありそうです。  
その際に`%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe connect 192.168.10.100:12482`のように打つのはめんどくさいので、パスを通しておくと良いでしょう。
環境変数の設定（パスを通す）方法は[サイト](https://akira-watson.com/android/path-environment.html)を見てください。
`PATH`に`%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools`のように設定すれば大丈夫だと思います。
パスを通しておくと、`connect 192.168.10.100:12482`で接続ができます。



市川先生によるとデータサイエンティスト目線では使えるかわからなくてもデータを保持しておくことで今後何かに用いることができるかもとの助言をいただきました。
