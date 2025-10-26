# MySQLdbのインポート
import MySQLdb

# データベースへの接続とカーソルの生成
connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='*******',  # password
    db='tele2021db')
cursor = connection.cursor()

cursor.execute("SELECT * FROM gis")
rows = cursor.fetchall()
for row in rows:
  print (row)

# 接続を閉じる
connection.close()
