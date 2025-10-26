# Module
studyフォルダから有用だと思われるものを引っ張ってきました。版管理は適当です。また、Androidアプリはパッケージを適当にコピってるのでパッケージ名が同じだったりします。ごめん。
## Android(Kotlin,Java)
- **[🐤Kt]** fragmentbundle  
Fragmentの仕組みを掴むアプリです。
- **[🐤Kt]** serialConAndDraw  
シリアル通信をしてグラフを描写するアプリです。
- **[🐤Kt]** USBListener  
シリアル通信についてListenerを実装しました。
- **[🐤Kt]** serialConAndDrawFragment  
グラフをFragmentで実装しました。名前はserialConと書いてありますが、シリアル通信はしません。
- **[🐤Kt]** gisSample1  
GPSを受信して地図を表示し、移動ログをポイント・ラインで表示します。位置情報のPermissionを許可すると使えます。
- **[🐤Kt]** mysqlgnss  
GPSを受信して、MySQLサーバーに転送します。速度等はダミーです。位置情報のPermissionを許可すると使えます。IPアドレスやパスワード等接続情報は要設定です。

## mbed(C)
- **[C]** USBDriver.c  
flightDisplayForS270Malusに仮想データを送信するプログラムです。実機マイコンとの接続をせずにアプリのテストを行うことが出来ます。
4Hzで送信しています。コメントアウト部はランダムな値を生成するコードです。そのままだとインクリメントしていきます。
