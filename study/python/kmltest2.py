import MySQLdb
from simplekml import Kml
import time
import keyword
# データベースへの接続
connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='*******',
    db='tele2021db')
# カーソルの生成
cursor = connection.cursor()

# インスタンス生成
kml = Kml()
linestring = kml.newlinestring()

# idの初期値
id = 0

for i in range(50):
  # デバッグ用
  print(str(i) + "回目の試行")
  # データ取得
  cursor.execute("SELECT id,lat,lon FROM gis ORDER BY id DESC LIMIT 1")
  #タプルにすべて入れる
  rows = cursor.fetchall()
  # 1行ずつ追加
  for row in rows:
    prev_id = id
    print("prev_id is ",prev_id)
    id,lat,lon = row
    print("id is ",id)
    if(prev_id != id):
      linestring.coords.addcoordinates([(lat,lon)])
      # デバッグ用
      print(row)

  # kml生成
  kml.save('line.kml')
  time.sleep(1)

# MySQLの接続を閉じる
connection.close()

