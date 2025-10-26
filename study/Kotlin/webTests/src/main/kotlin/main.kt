fun main(args: Array<String>) {
    val templateNo: Int = readLine()!!.toInt()
    val templateMsg: List<String> = readLine()!!.split(" ")
    var templateRepNo: Int = -1
    for (i in 0 until templateNo) {
        if (templateMsg[i].contains("#")) {
            templateRepNo = i
        }
    }
//↑テンプレート認識終
    if (templateRepNo != -1) {  //置換あり
        val clientNo: Int = readLine()!!.toInt()
        var clientInstList = ArrayList<Clients>()
        for (i in 0 until clientNo) {  //取引先の数だけインスタンス生成
            var clientInst = Clients()
            clientInstList.add(clientInst)
            clientInstList[i].Clients()
        }

        //出力部
        for (i in 0 until clientNo) {
            if (clientInstList[i].infoMsgMap[templateMsg[templateRepNo]] != null) {//一致したとき
                for (j in 0 until templateRepNo) {  //置換する言葉の前まで出力
                    print("${templateMsg[j]} ")
                }

                print("${clientInstList[i].infoMsgMap[templateMsg[templateRepNo]]}")  //置換する言葉を出力

                if (templateMsg.size != templateRepNo + 1) {  //置換する言葉の後ろがあれば出力
                    print(" ")
                    for (j in templateNo - 1 until templateMsg.size) {
                        print("${templateMsg[j]} ")
                    }
                }
                println()
            } else {  //置換不可
                println("Error: Lack of data")
            }
        }

    } else {//置換なし
        for (i in 0 until templateNo) {
            println(templateMsg[i])
        }
    }
}

class Clients() {
    //取引先データ取得
    var infoNo: Int = readLine()!!.toInt()
    var infoMsgMap = mutableMapOf<String, String>()
    fun Clients() {
        for (i in 0 until infoNo) {
            val infoMsg: List<String> = readLine()!!.split(" ")
            infoMsgMap.put(infoMsg[0], infoMsg[1])  //Map登録
        }
    }
    //↑情報の数だけforを回す。
}
