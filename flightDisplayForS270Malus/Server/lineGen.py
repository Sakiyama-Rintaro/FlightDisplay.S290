import MySQLdb
from simplekml import Kml
import time
from ftplib import FTP #ftp転送用のライブラリです

# FTPログイン
ftp = FTP(
	"stepserver.jp",
	"tbt.bird.cx",
	passwd="*****"
)

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

for i in range(7200):
  # デバッグ用
  print(i,"回目の試行")
  connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='******',
    db='tele2021db')
  cursor = connection.cursor()
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
    with open("line.kml","rb")as f:
      ftp.storbinary("STOR /ssl_html/denso/line.kml",f)
  connection.close()

  time.sleep(1)

# MySQLの接続を閉じる
connection.close()


