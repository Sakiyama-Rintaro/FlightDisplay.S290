import MySQLdb #MySQL
import json #json

data = dict()
data['type'] = 'LineString'
data['coordinates']=[
   [136.256740,35.295200]
]
# データベースへの接続とカーソルの生成
connection = MySQLdb.connect(
    host='localhost',
    user='server',
    passwd='********',
    db='tele2021db')
cursor = connection.cursor()

cursor.execute("SELECT lat,lon FROM gis")
rows = cursor.fetchall()
for row in rows:
  data['coordinates'] = [row]
  #str_list = list(row)
  #print(row)
  #data['coordinates'] = row
print("thisis")
print(row) 






# data['coordinates']=[
#   [136.256740,35.295200],
#   [136.237830, 35.299620]
# ]

# 辞書オブジェクトをstr型で取得して出力
print(json.dumps(data, ensure_ascii=False, indent=2))

# 辞書オブジェクトをJSONファイルへ出力
# with open('point.geojson', mode='wt', encoding='utf-8') as file:
#   json.dump(data, file, ensure_ascii=False, indent=2)

# MySQLの接続を閉じる
connection.close()