from ftplib import FTP

ftp = FTP(
	"stepserver.jp",
	"tbt.bird.cx",
	passwd="*******"  # password
)
with open("point.kml","rb")as f:
	ftp.storbinary("STOR /ssl_html/denso/point.kml",f)  # file
