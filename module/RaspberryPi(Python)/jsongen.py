import json

data = dict()

data['type'] = 'LineString'
data['coordinates']=[
  [136.256740,35.295200],
  [136.237830, 35.299620]
]

# 辞書オブジェクトをstr型で取得して出力
print(json.dumps(data, ensure_ascii=False, indent=2))

# 辞書オブジェクトをJSONファイルへ出力
with open('point.geojson', mode='wt', encoding='utf-8') as file:
  json.dump(data, file, ensure_ascii=False, indent=2)
