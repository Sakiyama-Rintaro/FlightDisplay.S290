# セットアップ
とりあえず動かしてみましょう  
### もくじ
* [はじめに](https://github.com/sin0111/tbt.denso.21st/blob/master/README.md)
    * 導入 
    * システムアーキテクチャ
    * 用語集
    * 謝辞
* [セットアップ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/set-up.md) `-いまここ`
    * 導入 
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
* [こつ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/tips.md)
    * 活用したサイト・ソフトウェア
    * 通信契約について
    * Android Studio
        * ライブラリの追加
        * Wi-Fiデバッグ
* [おまけ](https://github.com/sin0111/arajin.21st.denso.tbt/blob/master/doc/md/omake.md)気が向いたら書きます。
    * 情報コンセントを作る方法
## ⚠セキュリティ上の注意⚠
S-270ではRaspberry Piをサーバーに使いましたが、**セキュリティ上の懸念や回線の安定性の問題があるため、本番環境では[Azure](https://azure.microsoft.com/ja-jp/pricing/details/virtual-machines/linux/)のようなバーチャルマシンを提供するクラウドサービスを使うと良いでしょう。もちろんVMでもセキュリティ対策は必須ですが。**  
しかしながら、多くの**クラウドサービスは従量課金制なので試行錯誤するにはRaspberry Piのようなローカルマシンのほうが向いているので、テスト端末としてのセットアップをおすすめします。**  
TBTやあなたの財力に余裕があればVM上で動かしても良いでしょう。  
なお、安全のため、**使わないときはLANケーブルを抜くなど、インターネットから切り離すことをおすすめします。**

## Windows上で使ったソフト
1. [Android Studio](https://developer.android.com/studio?hl=ja)（AndroidのIDE。Androidアプリ開発に必須。（ProcessingとかUnityとかを使う方法もあるかも？調べてみて。））
2. [VSCode](https://azure.microsoft.com/ja-jp/products/visual-studio-code/)（エディタ。mbed側のコード書くときとか、軽くコード書くときにに使うよね。お好みのエディタをどうぞ）
3. [IntelliJ IDEA](https://www.jetbrains.com/ja-jp/idea/)（KotlinやJavaのIDE。Android Studio自体これベースっぽい。Kotlin自体の勉強とかテストに使った。学生は登録すると無料で高機能版が使える！）
4. [TeraTerm](https://forest.watch.impress.co.jp/library/software/utf8teraterm/)（ターミナル。mbedの通信に使った。お好みのソフトをどうぞ）
5. [Serial USB Terminal](https://play.google.com/store/apps/details?id=de.kai_morich.serial_usb_terminal&hl=ja&gl=US)（Androidのアプリです。デバイスID見るときや、テストに使える。お好みで）
6. [GitHub Desktop](https://desktop.github.com/) （いまあなたが見てるのがGitHubというものです。GitHub(Git)はプログラムや文章のバージョン管理（ここを変更したというのを逐一管理するということ。共同で開発を容易にしたり、開発の流れを可視化できる）をするツールです。元々、Gitはコマンドを打ち込むものですが、GitHub Desktopではボタンを押すだけなので、コマンドを覚えたりせず、簡単に管理ができます。自分一人でプログラムを書くときはもちろん、みんなで書く時にはとても有用なので、今後は積極的に使っていくととても良いと思います！（今まではGoogleDriveでコードの共有をしていましたが、バージョンの移り変わりや可読性に難がありました。））
7. [Raspberry Pi Imager](https://www.raspberrypi.org/software/) （Raspberry PiのOSを書き込むのに使います。）

※今回は1と7があればとりあえず動くと思います。

# Raspberry Piのセットアップ
## 用意するもの
1. Raspberry Pi本体（3B以降推奨）（4にするなら冷却に配慮したケースとかほしいかも）
2. microSDカード（速いもの、[SunDisk Extreme PRO 128GB](http://www.akibaoo.co.jp/c/130351/)おすすめです。）
3. LANケーブル
4. USBケーブル（microUSBまたはtypeC(機種依存です）)
5. 電源（AnkerのtypeCの強いやつおすすめです）
6. マウス（Raspberry Pi Imagerでsshの設定をしたイメージを書き込めば要らない）
7. キーボード(Raspberry Pi Imagerでsshの設定をしたイメージを書き込めば要らない）
8. モニター(Raspberry Pi Imagerでsshの設定をしたイメージを書き込めば要らない）
9.  HDMIケーブル(Raspberry Pi Imagerでsshの設定をしたイメージを書き込めば要らない）
10. 良質な回線環境

## 手順
### 初期設定
1. WindowsのパソコンでRaspberry Pi Imagerをダウンロードします。
2. microSDをWindowsのPCに接続してツールを用いてOSをインストールします。
3. microSDカードを本体に入れます
4. モニターと本体を接続します。※モニターを先に接続しないと起動しないことがあります。
6. 電源を接続します。※モニターを先に接続しないと起動しないことがあります。
7. 起動します。
8. 表示に従って設定します。（地域を日本するなど）
9. 再起動します。

### 環境構築
1. セキュリティ上大切な設定をする
1. IPアドレスを固定する
2. SSHとVNCを設定する
3. mariaDBを導入する
4. Pythonモジュールの導入  
4-1. mysqlclientの導入する  
4-2. ArcGIS for Pythonの導入する
5. このリポジトリにあるプログラムを動かす。

### 1.セキュリティ上大切な設定
Webサーバーとして稼働させるので、セキュリティには気を付けましょう。割とマジで
[ここを参考にしました](https://qiita.com/c60evaporator/items/ebe9c6e8a445fed859dc)

### 2.IPアドレスの固定
IPを変えるDHCPというものがありますが、サーバーとして使うにはIPアドレスが変わると都合が悪いので、IPアドレスを固定する必要があります。
#### グローバルIPアドレス
IPアドレスとはというのは別途勉強してほしいのですが、ローカルIPアドレスとグローバルIPアドレスがあります。  
グローバルIPアドレスを固定するのにはお金が必要です。接続側はtbt.bird.cxというような決まった名前のところにリクエストすると、可変IPにいい感じにつなげてくれるDDNSというものもありますがそれを使うのも一つですが、今回はとりあえずグローバルIPは固定しないで行きます。（IPアドレスが変わったら、サーバーの設定を変える必要があります）
#### ローカルIPアドレス
RaspberryPiが属するLAN内でIPアドレスを固定するのは簡単です。
[ここを参照にしました](https://mugeek.hatenablog.com/entry/2019/05/27/230256)
なお、Wi-Fiではなく、Ethernetで接続している場合には、3.設定ファイルの編集のwlan0をeth0と読み替えてください。

### 3.SSHの設定
CUIベースの遠隔操作
[ここを参考にしました](https://qiita.com/c60evaporator/items/2384416f1122ae124f50)

### VNCの設定
GUIベースの遠隔操作
[ここを参考にしました](https://note.com/osashimikun/n/n069c84f66a71)  
[ここも参考にしました](https://www.indoorcorgielec.com/resources/raspberry-pi/raspberry-pi-vnc/)  
[VNC Viewer](https://www.realvnc.com/en/connect/download/viewer/)Windows側のソフトはこれを使っています。

### 4.MariaDBの導入
#### 導入方法
[ここを参考にしました](https://gametech.vatchlog.com/2020/09/14/raspi-mariadb-setup/)
#### 使うためのデータベースの設定
```powershell
create table gis(
id int unsigned auto_increment,  
time timestamp default current_timestamp,  
lat double(9,6),  
lon double(9,6),  
rpm float,  
arispd float,  
primary key (id));
```

#### データベースを操作するユーザーの設定
仮にもインターネットから操作出来る（IDとパスワードがわかれば）ので、セキュリティ強化のため、サーバー用に実行できる権限を制限したユーザーを作ることをおすすめします。
ユーザー登録方法はググってください。
自分は下記のユーザーを設定しました。  
* root (全ての権限がついていうrootユーザー）
* arata (一部の権限を付けた一般ユーザー。自分の名前です。）
* server （一部の権限を付けたサーバー用ユーザー）

### 5.Pythonモジュールの導入
#### mysqlclient
[ここを参考にしました](https://qiita.com/xfan/items/f2c88aeb0d3945ed4775)
pipするときにシンプルにsudo pipすると権限でハマります。（ハマりました）
`sudo -H pip3 install mysqlclient`  
これでOKです。
[ここを参考にしました](https://cocoinit23.com/pip-install-permission-denied-sudo-user/)

#### ArcGIS API for Python
##### 導入
```powershell
pip3 install arcgis
pip3 install -U numpy
pip3 install -U pygments
pip3 install arcgis
```

### プログラムの導入
リポジトリにあるプログラム2つをpython3 で実行してください。

## Androidアプリのセットアップ
Andoird Studioを設定したら、リポジトリにあるやつを開いて、ArcGISのAPIKey、MapID並びにデータベースのIPアドレス、ユーザー名、パスワードを自分のもの変更して、ビルド・実行してください。
起動時にIPアドレスを設定できるように変更したいとは思っていま ~~すが。~~ したが。
また、端末にインストール後に位置情報の権限を付与してください。また、位置情報精度をバッテリー節約モードなどではなく、高精度に設定してください。
設定方法は[Android ヘルプ](https://support.google.com/android/answer/3467281?hl=ja#location_accuracy)などを参考にしてください。