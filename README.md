# 課題の内容
1. Java言語を用いる
2. TCP通信をどこかで使用する

# 成果物
画像を投稿して閲覧するAndroidアプリケーション

## このアプリができること
- 画像の送信
- ピンチイン・ビンチアウトによる画像の拡大/縮小
- ユーザー名の登録
- 画像の読み込み

## 実装出来なかっこと
- 画像にテキストを乗っける
- タイムラインに情報を付け加える
    - 投稿日時
    - タグなど
- 投稿の削除

## 環境
- フレームワーク:Android studio 3.4.1
- 開発言語:Java
- データベース:MySQL 8.0.16

## サーバー環境構築
1. MySQL 8.0.16をダウンロード
    - Linuxは`brew install mysql`
2. MySQLのバージョンにあったドライバをダウンロード
    - リポジトリには8.0.16用のが入ってる
3. クライアントの接続先を設定      
    - app/src/main/java/com/example/photoapp/Client.javaの`Socket(InetAddress.getByName("サーバーのIP"), PORT);`のIPを適宜変更
4. データベースの設定
```
mysql -u root
create database photo_app;
use photo_app;
CREATE TABLE photos(id INTEGER NOT NULL AUTO_INCREMENT, image_path TEXT NOT NULL, user_name TEXT, PRIMARY KEY(id));
insert into photos values (1, "./images/1.png", "user");
```
5. MySQLとJavaの接続を確認
    - `server/DatabaseConnection.java`を開く
    - pathとuserとpasswordを設定
    - MySQLをインストールしたばかりであれば、下記の設定でOK
```
String databasePath = "jdbc:mysql://localhost:/photo_app";
String user = "root";
String password = "";
```

6. コンパイル(serverの階層)
```
javac -cp mysql-connector-java-8.0.16.jar: DatabaseConnection.java
javac -cp mysql-connector-java-8.0.16.jar: PhotoServer.java
```

7. サーバー起動
`java -cp mysql-connector-java-8.0.16.jar: PhotoServer`
