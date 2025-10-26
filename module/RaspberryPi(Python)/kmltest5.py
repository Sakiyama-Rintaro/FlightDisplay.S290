import MySQLdb
from simplekml import Kml
import time
from arcgis.gis import GIS
from arcgis.features import FeatureLayerCollection

arcgis_username = 'sin0111'
arcgis_password = '**********'
arcgis_user = GIS("http://"+arcgis_username+".maps.arcgis.com/",arcgis_username,arcgis_password)
print("auth completed")

# データベースへの接続
connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='********',
    db='tele2021db')
# カーソルの生成
cursor = connection.cursor()

# インスタンス生成
kml = Kml()
linestring = kml.newlinestring()

# 初期化
cursor.execute("SELECT id,lat,lon FROM gis LIMIT 1")
row = list(cursor.fetchone())
print(row)
id,lat,lon = row[0],row[1],row[2]
linestring.coords.addcoordinates([(lon,lat)])
kml.save('line.kml')

for i in range(500):
  # デバッグ用
  print(i,"回目の試行")

  # データ取得
  cursor.execute("SELECT id,lat,lon FROM gis ORDER BY id DESC LIMIT 1")
  #タプルにすべて入れる
  row = list(cursor.fetchone())
  print(row)
  # 1行ずつ追加
  # 前回レコードのidを記録  
  prev_id = id
  # 今回レコードを分割代入
  id,lat,lon = row[0],row[1],row[2]
  # 前回idと今回idを比較して変更があれば処理を行う
  if(prev_id < id):
    linestring.coords.addcoordinates([(lon,lat)])
    # デバッグ用
    print(row)
    # kml生成
    kml.save('line.kml')
    # ftp転送
    print("upload start")
    file = 'line.kml'
    Item = arcgis_user.content.add({'type':'KML','overwrite':'True'}, file)
    Item.publish()
    print("end")
  connection.close()
  connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='k4niahcg',
    db='tele2021db')
  cursor = connection.cursor()
  time.sleep(1)

# MySQLの接続を閉じる
connection.close()



