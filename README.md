ksbysample-webapp-basic
=======================

Spring Boot でログイン画面、検索/一覧画面、登録画面 ( 入力→確認→完了 ) がある Webアプリケーションです。

* 構成要素は、Spring Boot + Spring Web MVC + Thymeleaf + Spring Data JPA + MyBatis3 ( 検索のみ ) + Spring Security です。
* DB は MySQL 5.6 を PC にインストールして使用します。インストール先はインストーラー任せにします。
* DB、テーブルは MySQL のサンプルとしてインストールされる world DB を使用します。
* 実行環境は開発用 ( develop ) と本番用 ( product ) の２種類を想定し、spring.profiles.active に指定された文字列で切り替えられるようにします。また未設定時はエラーになるようにします。
* getter/setter メソッドの作成を省くために lombok をインストールします。
* 画面は Twitter Bootstrap を使用して作成します。
* ログイン画面では Spring Security を使用します。
* 検索/一覧画面は Spring Data JPA 版、MyBatis-Spring 版の２画面作成します。ページネーションも実装します。
* 個人的に検索画面では SQLファイルを使いたいので MyBatis-Spring 版を先に実装します。最後に Spring Data JPA 版 ( Specifications を使用する ) も実装してみます。
* 登録画面は Spring Data JPA版の１種類のみ作成します。Bean Validation による入力チェックも実装してみます。
