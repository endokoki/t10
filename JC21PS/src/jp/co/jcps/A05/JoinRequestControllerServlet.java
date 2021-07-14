package jp.co.jcps.A05;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.jcps.Common.CommonCheck;
import jp.co.jcps.Common.DBConnection;

/**
 * 部員登録申請画面のコントローラー
 */
@WebServlet("/JoinRequestController")
public class JoinRequestControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public JoinRequestControllerServlet() {
		super();
	}

	/**
	 * GETメソッドでリクエストされた場合の処理
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 共通チェック
		if (!CommonCheck.existSession(request)) {
			// セッションが切れてる場合はログイン画面に遷移
			request.getRequestDispatcher("/Login").forward(request, response);
		}

		// セッションからログイン中のユーザーIDを取得する
		/* TODO: セッションからユーザーIDを取得しなさい。
		 *  ヒント
		 *  セッションには「userId」という名前でログインユーザーIDが格納されている。
		 */
		String userId = (String) request.getSession().getAttribute("userId");

		// SQLを宣言
		String sql = "SELECT * FROM mst_club WHERE club_id NOT IN (SELECT club_id FROM trn_join_request WHERE user_id = ?) AND club_id NOT IN (SELECT club_id FROM trn_club_member WHERE user_id = ?);";

		// SQLに埋め込むパラメータリストを定義
		List<String> paramList = new ArrayList<String>();
		/* TODO: SQLに埋め込む値をparamListに設定しなさい。
		 *  ヒント①
		 *  Listにはaddメソッドで要素を追加することができる。
		 *  ヒント②
		 *  ログインユーザーの情報を使う。
		 */
		paramList.add(userId);



		// DB接続を初期化
		DBConnection db = new DBConnection();

		// 部員登録申請画面に表示するbeanを初期化
		JoinRequestBean bean = new JoinRequestBean();

		try {
			// SQLを実行し結果を取得
			ResultSet rs = db.executeSelectQuery(sql, paramList);
			// beanに画面に出力する情報をセット
			while (rs.next()) {
				bean.addClubIdList(rs.getString("club_id"));
				bean.addClubNameList(rs.getString("club_name"));
				bean.addClubDescriptionList(rs.getString("club_description"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			request.getRequestDispatcher("ERROR/Error.jsp").forward(request, response);
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		// beanをリクエストにセット
		request.setAttribute("bean", bean);


		// 部活情報登録画面を表示
		request.getRequestDispatcher("A05/JoinRequest.jsp").forward(request, response);
	}

}
