from arcgis.gis import GIS
from arcgis.features import FeatureLayerCollection
import MySQLdb
import csv
import time as sleep #timeという変数を作ってしまったのでas sleepしてあります

arcgis_username = '******' #自分のユーザー名を入れてください
arcgis_password = '******' #自分のパスワードを入れてください
arcgis_user = GIS("http://"+arcgis_username+".maps.arcgis.com/",arcgis_username,arcgis_password)
print("auth completed")

# データベースへの接続 //これループの中にも書いてあるのでこれを2回書く必要はない気がします。ループの最初とかに入れると多分動きます。
connection = MySQLdb.connect(
    host='localhost', #他のサーバーにDBが入っている場合はそのIPアドレスを指定してください。
    user='server', #自分のユーザー名を入れてください
    passwd='******', #自分のパスワードを入れてください
    db='tele2021db') #自分のデータベース名を入れてください
# カーソルの生成
cursor = connection.cursor()
# 初期化

prev_id = 0
for i in range(7200):
    
    cursor.execute("SELECT id,time,lat,lon,airspd,rpm FROM gis ORDER BY id DESC LIMIT 1") #gisというテーブルからそれぞれの列を最新の1レコード取得しています
    row = list(cursor.fetchone())
    id,time,lat,lon,airspd,rpm = row[0],row[1],row[2],row[3],row[4],row[5] #SELECTした結果をそれぞれ代入します
    print(row) #結果を表示します
    if(prev_id!=id): #IDが変化しているとき＝新しいレコードが追加されているとき実行します
        with open('point.csv','a',newline="")as f: #csvに書き込みます
            writer = csv.writer(f)
            writer.writerow([id,time,lat,lon,airspd,rpm])

        searched_featureservice = arcgis_user.content.search(query="title: point AND type:Feature Service")[0] #ArcGISOnline上の「point」という名前のFeatureServiceを検索し、検索クエリの一番最初のものを取得します。
        print("upload start")
        csvfile = 'point.csv' #csvファイル名を代入してあります。
        overwrite_result = FeatureLayerCollection.fromitem(searched_featureservice).manager.overwrite(csvfile) #csvファイルをArcGISOnlineに上書きでアップします
        print(overwrite_result) #アップロードに成功したかを表示します。
        print("end") #終了です
        prev_id = id #idを記録しておきます
    else: 
        sleep.sleep(1) 
    connection.close()
    connection = MySQLdb.connect(
        host='localhost',
        user='server',
        passwd='*****',
        db='tele2021db')
    cursor = connection.cursor()
    
    

